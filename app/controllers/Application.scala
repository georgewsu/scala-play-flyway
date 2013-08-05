package controllers

import anorm.SQL
import play.api.Play.current
import play.api.db.DB
import play.api.mvc.Action
import play.api.mvc.Controller

object Application extends Controller {

  def index = Action {
    val columns = DB.withConnection { implicit c =>
      SQL("show columns from widget")().map(row => (row[String]("COLUMNS.COLUMN_NAME"), row[String]("TYPE"))).toList
    }
    Ok(views.html.index(columns))
  }

}