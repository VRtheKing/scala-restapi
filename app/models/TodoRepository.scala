package models

import javax.inject._
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.PostgresProfile
import slick.dbio.DBIO
import slick.basic.DatabaseConfig
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TodoRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[PostgresProfile] {

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
