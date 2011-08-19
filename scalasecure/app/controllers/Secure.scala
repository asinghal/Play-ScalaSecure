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
package controllers

import play._
import play.mvc._
import play.libs._
import play.utils._

import java.lang.reflect._

import models._
import secure._

/**
 * This trait provides controllers an ability to enforce security on their methods. For this purpose,
 * <ol>
 * <li>The controller must have this trait.</li>
 * <li>All methods that do not need to be secure must be annotated with @NonSecure</li>
 * <li>All methods that need special permissions must be annotated with @Authorize</li>
 * <li>All permissions that must be applied to each method of a controller (except those
 * marked with @NonSecure) must be defined in @Authorize annotated on the controller object.</li>
 * </ol>
 * @author Aishwarya Singhal
 */
trait Secure {
  self: Controller =>

  /**
   * This method is called before invocation of each method in implementing
   * controllers. It is responsible for authenticating and authorizing a
   * request before the requested method could be executed.
   */
  @Before
  def checkSecurity = {
    // lets first check if this method needs security.
    var annotation = getActionAnnotation(classOf[NonSecure]).getOrElse(null)
    // if the annotation is not applied, proceed towards checking for permissions
    if (annotation == null) {

      session("username") match {
        case Some(username) =>
          try {
            authorizeAction(username) { getActionAnnotation(_) }

            authorizeAction(username) { getControllerInheritedAnnotation(_) }

            Continue
          } catch {
            case e: UnauthorizedAccessException =>
              Redirect("/unauthorized")
          }
        case None =>
          flash.put("url", request.url)
          Action(Authentication.login)
      }
    } else {
      Continue
    }
  }

  /**
   * Authorizes based on the permissions defined with the provided annotation details.
   *
   * @param username
   * @param annotation
   */
  def authorize(username: String, annotation: Authorize) = {
    for (role <- annotation.values) {
      val hasRole = Security.authorize(username, role)
      if (!hasRole) {
        Security.onAuthorizationFailure(role)
      }
    }
  }

  /**
   * Retrieve annotation for the action method
   * @param clazz The annotation class
   * @return Annotation object or null if not found
   */
  def getActionAnnotation[T <: java.lang.annotation.Annotation](clazz: Class[T]): Option[T] = {
    val m = ActionInvoker.getActionMethod(Http.Request.current().action)(1).asInstanceOf[Method];
    var annotation: Option[T] = None

    if (m.isAnnotationPresent(clazz)) {
      annotation = Some(m.getAnnotation(clazz))
    }

    annotation
  }

  /**
   * Retrieve annotation for the controller class
   * @param clazz The annotation class
   * @return Annotation object or null if not found
   */
  def getControllerInheritedAnnotation[T <: java.lang.annotation.Annotation](clazz: Class[T]): Option[T] = {
    var c = getControllerClass();
    while (!c.equals(classOf[Object])) {
      if (c.isAnnotationPresent(clazz)) {
        return Some(c.getAnnotation(clazz));
      }
      c = c.getSuperclass().asInstanceOf[Class[_ <: Controller]];
    }
    return None;
  }

  /**
   * Retrieve the controller class.
   *
   * @return controller class as recorded in the request.
   */
  def getControllerClass(): Class[_ <: Controller] = {
    return Http.Request.current().controllerClass;
  }

  /**
   * Checks if the logged in user is authorized to execute the requested method.
   * @param username
   * @param action
   */
  private def authorizeAction(username: String)(action: (Class[Authorize]) => Option[Authorize]) = {
    var annotation = action(classOf[Authorize]).getOrElse(null)
    if (annotation != null) {
      authorize(username, annotation)
    }
  }
}