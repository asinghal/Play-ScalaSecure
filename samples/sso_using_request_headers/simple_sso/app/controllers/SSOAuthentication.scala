package controllers

import play._
import play.mvc._
import play.libs._

import models._
import secure._

object SSOAuthentication extends Controller {

  /**
   * Displays the login page. If a past request for "remember me" has
   * generated a cookie, the user will be automatically logged in. Note that
   * this implementation only puts username into the session which acts as
   * a successful login token to the system.
   */
  def login = {
    var url = flash.get("url")

    try {
      // this is where you should put the code to login (parse request headers?)
      //      Security.authenticate("myauthkey", "")

      session.put("username", "myauthkey");
      flash.keep
      println("successfully authenticated SSO user")

      redirectToOriginalURL()
    } catch {
      case e: AuthenticationFailureException =>
        flash.error("Login failed", "username")
        url = "/login"
        flash.keep
        Redirect(url)
      case e: Throwable =>
        flash.error("Unknown error", "username")
        url = "/login"
        flash.keep
        Redirect(url)
    }
  }

  /**
   * Redirects user to the requested URL post login.
   */
  private def redirectToOriginalURL() = {
    Security.onSuccessfulLogin
    var url = flash.get("url");
    if (url == null) {
      url = "/";
    }
    Redirect(url);
  }
}