/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Created on: 16th August 2011
 */
package secure

import java.lang.reflect._

import play._
import play.utils._

/**
 * This is the default security realm that should be overridden in a real world application.
 *
 * @author Aishwarya Singhal
 */
class Security[T] {

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
  def authenticate(username: String, password: String): T = {
    throw new AuthenticationFailureException
  }

  /**
   * This method checks that a profile is allowed to view this page/methodbased on roles defined in @Authorize.
   *
   * @param token AuthenticatedToken
   * @param role
   * @return true if you are allowed to execute this controller method.
   */
  def authorize(token: Any, role: String): Boolean = {
    false
  }

  /**
   * This method is called after a successful authentication.
   * You need to override this method if you with to perform specific actions (eg. Record the time the user signed in)
   */
  def onSuccessfulLogin = {

  }

  /**
   * This method is called after a successful sign off.
   * You need to override this method if you wish to perform specific actions (eg. Record the time the user signed off)
   */
  def onSuccessfulLogout = {

  }

  /**
   * This method is called if an authorization check does not succeed. By default it throws a UnauthorizedAccessException
   * @param role
   */
  def onAuthorizationFailure(role: String) = {
    throw new UnauthorizedAccessException
  }
}

object Security {

  /**
   * Authenticates a login attempt.
   * @param username
   * @param password
   * @return Authenticated token
   */
  def authenticate(username: String, password: String): Any = {
    val token = realm.authenticate(username, password)
    AuthenticatedTokenHolder.set(username, token)
    token
  }

  /**
   * Checks if the logged in user is authorized to perform the requested action.
   * @param username
   * @param role
   * @return
   */
  def authorize[T](username: String, role: String): Boolean = {
    val token = AuthenticatedTokenHolder.get(username)
    realm authorize (token, role)
  }

  /**
   * Called upon a successful login.
   */
  def onSuccessfulLogin = {
    realm onSuccessfulLogin
  }

  /**
   * Called upon a successful logout.
   * @param username
   */
  def onSuccessfulLogout(username: String) = {
    realm onSuccessfulLogout;
    AuthenticatedTokenHolder.remove(username)
  }

  /**
   * Called in case of an authorization failure.
   *
   * @param role
   * @return
   */
  def onAuthorizationFailure(role: String) = {
    realm onAuthorizationFailure role
  }

  /**
   * Identifies the security realm to be invoked.
   *
   * @return
   */
  private def realm[T <: Security[_]]: T = {
    var security: Class[_ <: Security[_]] = classOf[Security[_]]
    var classes = Play.classloader.getAssignableClasses(classOf[Security[_]])
    if (classes.size() != 0) {
      security = classes.get(0).asInstanceOf[Class[_ <: Security[_]]];
    }

    security.newInstance.asInstanceOf[T]
  }
}

/**
 * Container for holding the authenticated tokens. It uses Cache in the background.
 *
 * @author Aishwarya Singhal
 */
object AuthenticatedTokenHolder {
  import play.cache.Cache

  /**
   * Remove the token for a given user name.
   *
   * @param username
   */
  def remove(username: String) = {
    Cache.delete(username)
  }

  /**
   * Save the token for a given user name.
   *
   * @param username
   */
  def set(username: String, token: Any) = {
    Cache.set(username, token)
  }

  /**
   * Fetch the token for a given user name.
   *
   * @param username
   */
  def get(username: String): Any = {
    Cache.get(username)
  }
}