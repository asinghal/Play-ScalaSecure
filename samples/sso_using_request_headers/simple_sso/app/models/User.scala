package models

import javax.persistence._
import play.db.jpa._

import _root_.utils._

/**
 * Represents a User.
 */
@Entity
@Table(name="users")
class User extends Model {
  var username: String = null
  var name: String = null
  var empId: String = null
  @OneToMany(mappedBy="user")
  var roles:java.util.List[UserRole] = new java.util.ArrayList[UserRole]()
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

  def build(username: String, name: String, empId: String): User = {
    val user = new User
    user.username = username
    user.name = name
    user.empId = empId
    
    return user
  }
}