import com.googlecode.flyway.core.Flyway

import play.api.Application
import play.api.GlobalSettings
import play.api.Logger
import play.api.Play
import play.api.Play.current
import play.api.db.DB

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application onStart starting")

    // run database migrations
    runDatabaseMigrations("default")

    Logger.info("Application onStart finished")
  }

  def runDatabaseMigrations(dbName: String) = {
    Logger.info("Database migrations starting")
    val migrationFilesLocation = s"db/migration/${dbName}"
    Play.current.resource(migrationFilesLocation) match {
      case Some(r) => {
        Logger.info(s"Directory for migration files found: ${migrationFilesLocation}")
        
        val flyway = new Flyway
        flyway.setDataSource(DB.getDataSource(dbName))
        flyway.setLocations(migrationFilesLocation)
        flyway.setInitOnMigrate(true)
        flyway.migrate()
      }
      case None => {
        Logger.warn(s"Directory for migration files not found: ${migrationFilesLocation}")
      }
    }
    Logger.info("Database migrations finished")
  }

}
