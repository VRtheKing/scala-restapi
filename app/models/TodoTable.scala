package models

import slick.jdbc.PostgresProfile.api._
import play.api.libs.json._
import slick.lifted.{ProvenShape, TableQuery, Tag}

import models.TodoModel

object TodoTable {
  implicit val format: OFormat[TodoModel] = Json.format[TodoModel]
}

class TodoTable(tag: Tag) extends Table[TodoModel](tag, "todos") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def description = column[String]("description")
  def isDone = column[Boolean]("is_done")

  def * : ProvenShape[TodoModel] = (id, description, isDone) <> (TodoModel.tupled, TodoModel.unapply)
}

object TodosTable  {
  val todos = TableQuery[TodoTable]
}