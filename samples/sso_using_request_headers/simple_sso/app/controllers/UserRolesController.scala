package controllers

import java.util._

import play._
import play.mvc._

import models._

/**
 * Controller class for UserRole. Defines methods for all CRUD operations.
 */
object UserRolesController extends Controller with Secure {

  import views.userRoles._
  import _root_.utils._
  import play.data.validation._

  def index = {
    val showPlainHTML = if (!isEmptyString(params.get("flatHTML"))) params.get("flatHTML").toBoolean else false
    val allUserRoles = if (showPlainHTML) UserRole.find(
      "order by id desc").from(0).fetch(100) else null

    html.index("All UserRoles", allUserRoles)
  }

  def show(id: Long) = {

	UserRole.findById(id).map { userRole =>
      html.show("UserRole " + id, userRole)
    }.getOrElse {
      NotFound("No such UserRole")
    }
  }

  def newValue() = {
     var userRole = new UserRole
     html.newValue("New UserRole", userRole)
  }

  def edit(id: Long) = {
  
    UserRole.findById(id).map { userRole =>
      html.edit("UserRole " + id, userRole)
    }.getOrElse {
      NotFound("No such UserRole")
    }
  }

  def create() = {
        var userRole = new UserRole
        UserRole.bind(params, userRole)
        userRole.user = if (!isEmptyString(params.get("user"))) User.findById(params.get("user").toLong).getOrElse(null) else null
        
        userRole.save
  
        show(userRole.id)
  }

  def update(id: Long) = {
    UserRole.findById(id).map { userRole =>
        UserRole.bind(params, userRole)
        userRole.user = if (!isEmptyString(params.get("user"))) User.findById(params.get("user").toLong).getOrElse(null) else null
        
        userRole.save
        
        show(id)

    }.getOrElse {
        NotFound("No such UserRole")
    }
  }
  
  def delete(id: Long) = {
    UserRole.findById(id).map { userRole =>
        userRole.delete
        
        index

    }.getOrElse {
        NotFound("No such UserRole")
    }
  }

  def grid = {
    val page = if (!isEmptyString(params.get("page"))) params.get("page").toInt else 0
    val rows = if (!isEmptyString(params.get("rows"))) params.get("rows").toInt else 0
    val sidx = if (!isEmptyString(params.get("sidx"))) params.get("sidx") else "id"
    val sord = if (!isEmptyString(params.get("sord"))) params.get("sord") else "desc"
    val filters = if (!isEmptyString(params.get("filters"))) params.get("filters") else ""
    val search = if (!isEmptyString(params.get("_search"))) params.get("_search").toBoolean else false
    var query = ""
    
    if (search) {
      val searchObj = JQGridHelper.fromJson(filters)
      query = searchObj.getQuery
    }

    val allUserRoles = UserRole.find(query +
      "order by " + sidx + " " + sord).from((page - 1) * rows).fetch(rows);

    JQGridHelper.toJson(page, rows, UserRole.count.toInt, allUserRoles)
  }
  
  private def isEmptyString(input : String) : Boolean = {
  		(input == null || "".equals(input)) 
  }
}