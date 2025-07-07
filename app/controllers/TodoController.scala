package controllers

import javax.inject._
import play.api.mvc._
import scala.collection.mutable

import models.TodoModel


@Singleton
class TodoController @Inject()(cc:ControllerComponents) extends AbstractController(cc){
  val ItemList: mutable.Map[Int, TodoModel] = mutable.Map()

  def getItems = Action {
    Ok(ItemList.toString())
  }

  def getItem(id: Int) = Action {
    ItemList.get(id) match {
      case Some(item) => Ok(item.toString())
      case None => NotFound(s"Item with $id not found")
    }
  }

  def createItem = Action(parse.json) { request =>
    val name = (request.body \ "name").as[String]
    val description = (request.body \ "description").as[String]
    var id = 1
    while(ItemList.contains((id))){
      id+=1
    }
    ItemList.put(id, Map("name" -> name, "description" -> description))
    Created(s"Item is created with id: $id")
  }

  def editItem(id: Int) = Action(parse.json) { request =>
    val name = (request.body \ "name").as[String]
    val description = (request.body \ "description").as[String]
    if (ItemList.contains(id)){
      ItemList.put(id, Map("name" -> name, "description" -> description))
      Ok(s"Item with id: $id has been updated")
    } else {
      NotFound(s"Item with $id not found")
    }
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
