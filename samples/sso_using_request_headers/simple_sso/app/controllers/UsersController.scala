package controllers

import java.util._

import play._
import play.mvc._

import models._

/**
 * Controller class for User. Defines methods for all CRUD operations.
 */
object UsersController extends Controller with Secure{

  import views.users._
  import _root_.utils._
  import play.data.validation._

  def index = {
    val showPlainHTML = if (!isEmptyString(params.get("flatHTML"))) params.get("flatHTML").toBoolean else false
    val allUsers = if (showPlainHTML) User.find(
      "order by id desc").from(0).fetch(100) else null

    html.index("All Users", allUsers)
  }

  def show(id: Long) = {

	User.findById(id).map { user =>
      html.show("User " + id, user)
    }.getOrElse {
      NotFound("No such User")
    }
  }

  def newValue() = {
     var user = new User
     html.newValue("New User", user)
  }

  def edit(id: Long) = {
  
    User.findById(id).map { user =>
      html.edit("User " + id, user)
    }.getOrElse {
      NotFound("No such User")
    }
  }

  def create() = {
        var user = new User
        User.bind(params, user)
        
        user.save
  
        show(user.id)
  }

  def update(id: Long) = {
    User.findById(id).map { user =>
        User.bind(params, user)
        
        user.save
        
        show(id)

    }.getOrElse {
        NotFound("No such User")
    }
  }
  
  def delete(id: Long) = {
    User.findById(id).map { user =>
        user.delete
        
        index

    }.getOrElse {
        NotFound("No such User")
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

    val allUsers = User.find(query +
      "order by " + sidx + " " + sord).from((page - 1) * rows).fetch(rows);

    JQGridHelper.toJson(page, rows, User.count.toInt, allUsers)
  }
  
  private def isEmptyString(input : String) : Boolean = {
  		(input == null || "".equals(input)) 
  }
}