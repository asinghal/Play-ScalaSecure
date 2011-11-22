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
import java.util._

import play._
import play.test._

import org.scalatest._
import org.scalatest.junit._
import org.scalatest.matchers._

import _root_.utils._

class ConverterTest extends UnitFlatSpec with ShouldMatchers {

	it should "convert string to int" in {
		val clazz = classOf[Int]
		var value = Converter.convert(clazz.getName, clazz, "1")
		(value)should be(1)
		value = Converter.convert(clazz.getName, clazz, null)
		(value)should be(0)
	}
	
	it should "convert string to long" in {
		val clazz = classOf[Long]
		var value = Converter.convert(clazz.getName, clazz, "1")
		(value)should be(1)
		value = Converter.convert(clazz.getName, clazz, null)
		(value)should be(0)
	}
	
	it should "convert string to string" in {
		val clazz = classOf[String]
		var value: String = Converter.convert(clazz.getName, clazz, "abc").asInstanceOf[String]
		(value)should be("abc")
		value = Converter.convert(clazz.getName, clazz, null).asInstanceOf[String]
		(value)should be(null)
	}
	
	it should "convert string to double" in {
		val clazz = classOf[Double]
		var value = Converter.convert(clazz.getName, clazz, "1")
		(value)should be(1)
		value = Converter.convert(clazz.getName, clazz, null)
		(value)should be(0)
	}
	
	it should "convert string to char" in {
		val clazz = classOf[Char]
		var value = Converter.convert(clazz.getName, clazz, "a")
		(value)should be('a') 
		value = Converter.convert(clazz.getName, clazz, null)
		(value)should be(' ') 
	}
	
	it should "convert string to boolean" in {
		val clazz = classOf[Boolean]
		var value:Boolean = Converter.convert(clazz.getName, clazz, "true").asInstanceOf[Boolean]
		(value)should be(true)
		
		value = Converter.convert(clazz.getName, clazz, null).asInstanceOf[Boolean]
		(value)should be(false) 
	}
	
	it should "convert string to float" in {
		val clazz = classOf[Float]
		var value = Converter.convert(clazz.getName, clazz, "1")
		(value)should be(1)
		value = Converter.convert(clazz.getName, clazz, null)
		(value)should be(0)
	}
	
	it should "convert string to byte" in {
		val clazz = classOf[Byte]
		var value = Converter.convert(clazz.getName, clazz, "1")
		(value)should be(1)
		value = Converter.convert(clazz.getName, clazz, null)
		(value)should be(0)
	}
	
	it should "convert string to short" in {
		val clazz = classOf[Short]
		var value = Converter.convert(clazz.getName, clazz, "1")
		(value)should be(1)
		value = Converter.convert(clazz.getName, clazz, null)
		(value)should be(0)
	}
	
	it should "convert string to bigdecimal" in {
		val clazz = classOf[BigDecimal]
		var value = Converter.convert(clazz.getName, clazz, "1")
		(value)should be(1)
		value = Converter.convert(clazz.getName, clazz, null)
		(value)should be(0)
	}
	
	it should "convert string to bigint" in {
		val clazz = classOf[BigInt]
		var value = Converter.convert(clazz.getName, clazz, "1")
		(value)should be(1)
		value = Converter.convert(clazz.getName, clazz, null)
		(value)should be(0)
	}
}