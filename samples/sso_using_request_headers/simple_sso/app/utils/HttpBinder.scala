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
 *  Created on: 12th August 2011
 */
package utils

import java.util.Date
import java.util.Calendar

/**
 * Provides a binding mechanism between an entity and HTTP request parameters (form data)
 * captured in Play framework.
 *
 * @author Aishwarya Singhal
 */
trait HttpBinder[T] {

  import play.mvc.Scope._

  def bind(params: Params, bean: AnyRef) = {
    for (field <- bean.getClass.getDeclaredFields) {
      var s: String = params.get(field.getName())
      if ((field.getType == classOf[Date] || field.getType == classOf[Calendar]) && params.get(field.getName() + "_day") != null) {
        s = params.get(field.getName() + "_month") + "/" + params.get(field.getName() + "_day") +
          "/" + params.get(field.getName() + "_year")
      }
      val value = Converter.convert(field.getType.getName, field.getType, s)
      val access: Boolean = field.isAccessible

      field.setAccessible(true)
      field.set(bean, value)
      field.setAccessible(access)
    }
  }
}

object Converter {

  val registry = Map(classOf[Int].getName -> IntConverter, classOf[Long].getName -> LongConverter,
    classOf[Date].getName -> DateConverter, classOf[Boolean].getName -> BooleanConverter,
    classOf[String].getName -> StringConverter, classOf[Double].getName -> DoubleConverter,
    classOf[Float].getName -> FloatConverter, classOf[Char].getName -> CharConverter, classOf[Byte].getName -> ByteConverter,
    classOf[Short].getName -> ShortConverter, classOf[BigDecimal].getName -> BigDecimalConverter, classOf[BigInt].getName -> BigIntConverter,
    classOf[Calendar].getName -> CalendarConverter)

  def convert[T](className: String, clazz: Class[T], s: String): Any = {
    if (className.startsWith("models.")) {
      null
    } else {
      val converter = registry.get(className).getOrElse(throw new RuntimeException("unknown data type " + className))
      if (!isEmpty(s)) {
        converter convert s
      } else {
        converter getDefault
      }
    }
  }

  def isEmpty(s: String): Boolean = {
    s == null || s.trim == ""
  }
}

trait Converter[T] {
  def convert(s: String): T

  def getDefault: T
}

object IntConverter extends Converter[Int] {
  override def convert(s: String): Int = {
    s.toInt
  }

  override def getDefault: Int = {
    0
  }
}

object StringConverter extends Converter[String] {
  override def convert(s: String): String = {
    s
  }

  override def getDefault: String = {
    null
  }
}

object LongConverter extends Converter[Long] {
  override def convert(s: String): Long = {
    s.toLong
  }

  override def getDefault: Long = {
    0
  }
}

object DateConverter extends Converter[Date] {
  import java.text.SimpleDateFormat

  override def convert(s: String): Date = {
    new SimpleDateFormat("MM/dd/yyyy").parse(s);
  }

  override def getDefault: Date = {
    null
  }
}

object BooleanConverter extends Converter[Boolean] {
  override def convert(s: String): Boolean = {
    s.toBoolean
  }

  override def getDefault: Boolean = {
    false
  }
}

object DoubleConverter extends Converter[Double] {
  override def convert(s: String): Double = {
    s.toDouble
  }

  override def getDefault: Double = {
    0
  }
}

object FloatConverter extends Converter[Float] {
  override def convert(s: String): Float = {
    s.toFloat
  }

  override def getDefault: Float = {
    0
  }
}

object CharConverter extends Converter[Char] {
  override def convert(s: String): Char = {
    s.charAt(0)
  }

  override def getDefault: Char = {
    ' '
  }
}

object ByteConverter extends Converter[Byte] {
  override def convert(s: String): Byte = {
    s.toByte
  }

  override def getDefault: Byte = {
    0
  }
}

object ShortConverter extends Converter[Short] {
  override def convert(s: String): Short = {
    s.toShort
  }

  override def getDefault: Short = {
    0
  }
}

object BigDecimalConverter extends Converter[BigDecimal] {
  override def convert(s: String): BigDecimal = {
    BigDecimal(s)
  }

  override def getDefault: BigDecimal = {
    BigDecimal(0)
  }
}

object BigIntConverter extends Converter[BigInt] {
  override def convert(s: String): BigInt = {
    BigInt(s)
  }

  override def getDefault: BigInt = {
    0
  }
}

object CalendarConverter extends Converter[Calendar] {
  import java.text.SimpleDateFormat
  
  override def convert(s: String): Calendar = {
    var date = new SimpleDateFormat("mm/dd/yyyy").parse(s)
    var calendar = Calendar.getInstance
    calendar.setTime(date)

    calendar
  }

  override def getDefault: Calendar = {
    null
  }
}