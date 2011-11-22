package secure

import models.User

class JPASecurity extends Security[User] {
  /**
   * This method is called during the authentication process. This is where you check if
   * the user is allowed to log in into the system. This is the actual authentication process
   * against a third party system (most of the time a DB).
   *
   * @param username
   * @param password
   * @return authenticated token
   * @throws AuthenticationFailureException if the authentication process fails
   */
  override def authenticate(username: String, password: String): User = {
    User.find("byUsernameAndPassword", username, password).first match {
      case Some(u) => u
      case None => throw new AuthenticationFailureException
    }
  }

  /**
   * This method checks that a profile is allowed to view this page/methodbased on roles defined in @Authorize.
   *
   * @param token AuthenticatedToken
   * @param role
   * @return true if you are allowed to execute this controller method.
   */
  override def authorize(user: User, role: String): Boolean = {
    user.role == role
  }

  /**
   * This method is called after a successful authentication.
   * You need to override this method if you with to perform specific actions (eg. Record the time the user signed in)
   */
  override def onSuccessfulLogin = {

  }

  /**
   * This method is called after a successful sign off.
   * You need to override this method if you wish to perform specific actions (eg. Record the time the user signed off)
   */
  override def onSuccessfulLogout = {

  }

  /**
   * This method is called if an authorization check does not succeed. By default it throws a UnauthorizedAccessException
   * @param role
   */
  override def onAuthorizationFailure(role: String) = {
    throw new UnauthorizedAccessException
  }
}