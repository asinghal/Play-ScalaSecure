import play._
import play.test._

import org.scalatest._
import org.scalatest.junit._
import org.scalatest.matchers._

import models._
import secure._

class SecurityTests extends UnitFlatSpec with ShouldMatchers {

  it should "check token holder" in {
    var token: AuthenticatedToken = AuthenticatedTokenHolder.get("test")

    (token) should be(null)

    val roles = List("admin")
    var t = new AuthenticatedToken
    t.roles = roles

    AuthenticatedTokenHolder.set("test", t)

    token = AuthenticatedTokenHolder.get("test")

    (token.roles) should be(roles)

    AuthenticatedTokenHolder.remove("test")
    token = AuthenticatedTokenHolder.get("test")

    (token) should be(null)
  }

  it should "not authenticate" in {
    try {
      Security.authenticate("t", "p")
    } catch {
      case e: AuthenticationFailureException => // expected
    }
  }

  it should "not authorise" in {
    val roles = List("admin")
    var t = new AuthenticatedToken
    t.roles = roles

    AuthenticatedTokenHolder.set("test", t)

    Security.authorize("test", "customer")
  }
}