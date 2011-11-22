package models

import java.util._
import javax.persistence._
import play.db.jpa._

import _root_.utils._

/**
 * Represents a UserRole.
 */
@Entity
@Table(name="user_roles")
class UserRole extends Model {
  var name: String = null
  @ManyToOne
  var user: User = null
  
}

/**
 * Singleton object of <code>models.UserRole</code> that provides the following.
 * <ol>
 * <li>Serves as a DAO for UserRole. </li>
 * <li>Provides a builder method for creating objects of UserRole.</li>
 * <li>Enables creation of an instance by binding posted values in HTTP request</li>
 * </ol> 
 */
object UserRole extends QueryOn[UserRole] with HttpBinder[UserRole] {

  def build(name: String, user: User): UserRole = {
    val userRole = new UserRole
    userRole.name = name
    userRole.user = user
    
    return userRole
  }
}