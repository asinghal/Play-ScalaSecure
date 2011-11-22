package jobs

import play.jobs._
import play.test._
import models._

@OnApplicationStart
class Bootstrap extends Job {

  override def doJob {
    if (User.count == 0) {
      Fixtures.loadModels("user.yml");
    }
  }
}