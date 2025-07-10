package models

//import slick.jdbc.PostgresProfile.api._
//import slick.jdbc.PostgresProfile
//import slick.dbio.DBIO
//import slick.basic.DatabaseConfig

import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}

import slick.jdbc.MySQLProfile
import slick.jdbc.MySQLProfile.api._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

class TodoRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[MySQLProfile] {


  val todos = TableQuery[TodoTable]

  def create(todo: TodoModel): Future[Int] = {
    db.run(todos += todo)
  }

  def getAll: Future[Seq[TodoModel]] = {
    db.run(todos.result)
  }

  def getById(id: Int): Future[Option[TodoModel]] = {
    db.run(todos.filter(_.id === id).result.headOption)
  }

  def update(id: Int, todo: TodoModel): Future[Int] = {
    db.run(todos.filter(_.id === id).update(todo))
  }

  def delete(id: Int): Future[Int] = {
    db.run(todos.filter(_.id === id).delete)
  }
}
