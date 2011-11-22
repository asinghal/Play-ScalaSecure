package utils

import java.lang.reflect.Field
import scala.math.BigDecimal.RoundingMode
import java.math.MathContext

/**
 * <p>Provides a mechanism for generating a JSON string based on the format as understood for JQGrid plugin. A sample JSON is :<br>
 * <pre>
 * {"page":1,
 * 	"records":30,
 * 	"total":3,
 * 	"rows":[
 * 	       {
 * 	       "id":1,"cell":["test","","test string","2006-01-01 00:01:00.0"]
 * 	       }]
 * 	}</pre>
 * </p>
 */
object JQGridHelper {
  import com.google.gson._

  /**
   * Generates the JSON using Google JSON library (Gson).
   */
  def toJson(page: Int, perPage: Int, recordCount: Int, objects: Seq[Any]): String = {
    var id = 0
    val gson = new Gson()

    var rows: List[JQGridRow] = List()

    // for each object, add a row
    for (obj <- objects) {
      id = id + 1
      val ref = obj.asInstanceOf[AnyRef]

      // build cells for this object
      val cells = getCells(ref)

      // extract the id if the object has such a field.
      val field = findField(ref, "id")

      val value = if (field != null) getFieldValue(field, ref, "0").toInt else id

      rows = rows ::: List(JQGridRow.build(value, cells))
    }

    val total: Int = getTotalPages(recordCount, perPage)

    val container = JQGridContainer.build(page, recordCount, total, rows)
    val json = gson.toJson(container)

    json
  }

  def fromJson(json: String) = {
    val gson = new Gson()
    gson.fromJson(json, classOf[JQSearch])
  }

  /**
   * Gets the total number of pages corresponding to the data.
   */
  private def getTotalPages(recordCount: Int, perPage: Int): Int = {
    val totalRecords = BigDecimal(recordCount, MathContext.DECIMAL128)
    val recordsPerPage = BigDecimal(perPage, MathContext.DECIMAL128)
    val totalPages: Int = (totalRecords / recordsPerPage).setScale(0, RoundingMode.UP).toInt

    totalPages
  }

  /**
   * Gets a list of string values as represented by each of the field in a given object.
   */
  private def getCells(obj: AnyRef): Seq[String] = {
    var cells: List[String] = List()
    for (field <- obj.getClass.getDeclaredFields) {
      val value: String = getFieldValue(field, obj, "")
      cells = cells ::: scala.List(value)
    }

    cells
  }

  /**
   * Find a field on an object using java reflection.
   */
  private def findField(obj: AnyRef, name: String): Field = {
    var field: Field = null
    try {
      field = obj.getClass.getDeclaredField(name)
    } catch {
      case e: Exception =>
        try {
          field = obj.getClass.getSuperclass.getDeclaredField(name)
        } catch {
          case e: Exception =>
        }
    }
    field
  }

  /**
   * Gets the values of a field using java reflection.
   */
  private def getFieldValue(field: Field, obj: AnyRef, defaultValue: String): String = {
    val access: Boolean = field.isAccessible

    field.setAccessible(true)
    val value: String = if (field.get(obj) != null) field.get(obj).toString else defaultValue
    field.setAccessible(access)

    value
  }
}

/**
 * Defines a container for carrying information needed by JQGrid.
 */
class JQGridContainer {
  /** current page number */
  var page: Int = 0
  /** Total number of records */
  var records: Int = 0
  /** Total number of pages */
  var total: Int = 0
  /** actual data to be shown on the grid */
  var rows: Array[JQGridRow] = Array()
}

/**
 * Object representation that enables building an instance of JQGridContainer.
 */
object JQGridContainer {
  def build(page: Int, records: Int, total: Int, rows: List[JQGridRow]): JQGridContainer = {
    var container = new JQGridContainer
    container.page = page
    container.records = records
    container.total = total
    container.rows = rows.toArray
    container
  }
}

/**
 * Represents the rows of data for the grid.
 */
class JQGridRow {
  var id: Int = 0
  var cell: Array[String] = Array()
}

/**
 * Object representation that enables building an instance of JQGridRow.
 */
object JQGridRow {
  def build(id: Int, cell: Seq[String]): JQGridRow = {
    val row = new JQGridRow
    row.id = id
    row.cell = cell.toArray
    row
  }
}

class JQSearch {
  var groupOp: String = null
  var rules: Array[JQSearchRule] = Array()

  def getQuery = {
    var query = ""
    for (rule <- rules) {
      val condition = JQSearchOperation.operations(rule.op).replace("__VALUE__", rule.data)
      if (query != "") {
    	  query += groupOp 
      }
      query += ("(" + rule.field + " " + condition + ")")
    }

    query
  }
}

class JQSearchRule {
  var field: String = null
  var op: String = null
  var data: String = ""
}

object JQSearchOperation {
  val operations = Map("eq" -> "= '__VALUE__'",
    "ne" -> "<> '__VALUE__'",
    "bw" -> "LIKE '__VALUE__%'",
    "bn" -> "NOT LIKE '__VALUE__%'",
    "ew" -> "LIKE '__VALUE__%'",
    "en" -> "NOT LIKE '%__VALUE__'",
    "cn" -> "LIKE '%__VALUE__%'",
    "nc" -> "NOT LIKE '%__VALUE__%'",
    "nu" -> "IS NULL",
    "nn" -> "IS NOT NULL",
    "in" -> "IN (__VALUE__)",
    "ni" -> "NOT IN (__VALUE__)")
}