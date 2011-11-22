package models

import java.util._
import javax.persistence._
import play.db.jpa._

import _root_.utils._

/**
 * Represents a User.
 */
@Entity
@Table(name="users")
class User extends Model {
  var dateOfBirth: Calendar = null
  var username: String = null
  var name: String = null
  var password: String = null
  var role: String = null
}

/**
 * Singleton object of <code>models.User</code> that provides the following.
 * <ol>
 * <li>Serves as a DAO for User. </li>
 * <li>Provides a builder method for creating objects of User.</li>
 * <li>Enables creation of an instance by binding posted values in HTTP request</li>
 * </ol> 
 */
object User extends QueryOn[User] with HttpBinder[User] {

  def build(dateOfBirth: Calendar, username: String, name: String, password: String, role: String): User = {
    val user = new User
    user.dateOfBirth = dateOfBirth
    user.username = username
    user.name = name
    user.password = password
    user.role = role
    
    return user
  }
}