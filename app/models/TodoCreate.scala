package models

import play.api.libs.json.OFormat
import play.api.libs.json._

case class TodoCreate(description: String, isDone: Boolean)

object TodoCreate {
  implicit val todoFormat: OFormat[TodoCreate] = Json.format[TodoCreate]
}
