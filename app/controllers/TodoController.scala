package controllers

import javax.inject._
import play.api.mvc._
import scala.collection.mutable
import play.api.libs.json._

import models.TodoModel
import models.TodoCreate


@Singleton
class TodoController @Inject()(cc:ControllerComponents) extends AbstractController(cc){
  val ItemList: mutable.Map[Int, TodoModel] = mutable.Map()

  def getItems = Action {
    Ok(Json.toJson(ItemList.values.toSeq))
  }

  def getItem(id: Int) = Action {
    ItemList.get(id) match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound(s"Item with $id not found")
    }
  }

  def createItem = Action(parse.json) { request =>
    request.body.validate[TodoCreate].fold(
      errors => BadRequest(Json.obj("message" -> "invalid JSON", "errors" -> errors.toString())),
      todoCreate => {
        val id = if (ItemList.isEmpty) 1 else ItemList.keys.max + 1
        val newTodo = TodoModel(id, todoCreate.description, todoCreate.isDone)
        ItemList.put(id, newTodo)
        Created(Json.obj("message" -> "Item Created", "id" -> id))
      }
    )
  }


  def editItem(id: Int) = Action(parse.json) { request =>
    request.body.validate[TodoCreate].fold(
      errors => {
        println(Json)
        BadRequest(Json.obj("message" -> "Invalid JSON", "errors" -> errors.toString()))
      },
      updateTodo => {
        if (ItemList.contains(id)){
          val changed = TodoModel(id, updateTodo.description, updateTodo.isDone)
          ItemList.put(id, changed)
          Ok(Json.obj("message"->"Changes are Successfull"))
        } else {
          NotFound(Json.obj("message"->"Object is not found"))
        }
      }
    )
  }

  def deleteItem(id: Int) = Action {
    if (ItemList.contains(id)) {
      ItemList.remove((id))
      Ok(s"Deleted item with id: $id")
    } else {
      NotFound("Item not found")
    }
  }


}
