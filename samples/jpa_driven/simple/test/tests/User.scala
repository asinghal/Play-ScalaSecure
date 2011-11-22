import java.util._

import play._
import play.test._

import org.scalatest._
import org.scalatest.junit._
import org.scalatest.matchers._

import models._

class UserTests extends UnitFlatSpec with ShouldMatchers {

  it should "create a user" in {
    User.deleteAll

    var dateOfBirth = null
  

    // Create a new user
    User.build(dateOfBirth, "test", "test", "test", "role").save

    (User.count) should be(1)

    var fetchedUsers = User.find("byUsername", "test").fetch()

    (fetchedUsers.size) should be(1)

    val firstUser = fetchedUsers(0)

    (firstUser) should not be (null)
    (firstUser.username) should be ("test")
    (firstUser.name) should be ("test")
    (firstUser.password) should be ("test")
    (firstUser.role) should be ("role")
  }
}