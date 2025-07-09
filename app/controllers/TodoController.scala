package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models._
import models.TodosTable._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TodoController @Inject()(cc: ControllerComponents, todoRepo: TodoRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  def getItems = Action.async {
    todoRepo.getAll.map { todos =>
      Ok(Json.toJson(todos))
    }
  }

  def getItem(id: Int) = Action.async {
    todoRepo.getById(id).map {
      case Some(todo) => Ok(Json.toJson(todo))
      case None => NotFound(Json.obj("message" -> s"The Item with id $id not found"))
    }
  }

  def createItem = Action.async(parse.json) { request =>
    request.body.validate[TodoCreate].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> "Invalid JSON", "errors" -> JsError.toJson(errors)))),
      todoCreate => {
        val newTodo = TodoModel(0, todoCreate.description, todoCreate.isDone)
        todoRepo.create(newTodo).map { id =>
          Created(Json.obj("message" -> s"Object created successfully with id $id"))
        }
      }
    )
  }

  def editItem(id: Int) = Action.async(parse.json) { request =>
    request.body.validate[TodoCreate].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> "Invalid json", "errors" -> JsError.toJson(errors)))),
      todoUpdate => {
        val updateTodo = TodoModel(id, todoUpdate.description, todoUpdate.isDone)
        todoRepo.update(id, updateTodo).map {rowsAffected =>
          if(rowsAffected > 0) {
            Ok(Json.obj("message" -> "Json Updated"))
          } else {
           NotFound(Json.obj("message" -> s"Record with id $id not found"))
          }
        }
      }
    )
  }

  def deleteItem(id: Int) = Action.async { request =>
    todoRepo.delete(id).map { rowsAffected =>
      if (rowsAffected>0) {
        Ok(Json.obj("message"->s"Record with id $id is deleted"))
      } else {
        NotFound(Json.obj("message" -> s"The record with id $id not found"))
      }
    }
  }
}