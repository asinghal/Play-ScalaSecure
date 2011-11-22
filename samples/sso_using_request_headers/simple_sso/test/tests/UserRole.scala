import java.util._

import play._
import play.test._

import org.scalatest._
import org.scalatest.junit._
import org.scalatest.matchers._

import models._

class UserRoleTests extends UnitFlatSpec with ShouldMatchers {

  it should "create a userRole" in {
    UserRole.deleteAll

    var user = null
  

    // Create a new userRole
    UserRole.build("test", user).save

    (UserRole.count) should be(1)

    var fetchedUserRoles = UserRole.find("byName", "test").fetch()

    (fetchedUserRoles.size) should be(1)

    val firstUserRole = fetchedUserRoles(0)

    (firstUserRole) should not be (null)
    (firstUserRole.name) should be ("test")
    
  }
}