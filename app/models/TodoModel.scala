package models
import play.api.libs.json._

case class TodoModel(id: Int, description: String, isDone: Boolean)

object TodoModel {
  implicit val todoFormat: OFormat[TodoModel] = Json.format[TodoModel]
}
