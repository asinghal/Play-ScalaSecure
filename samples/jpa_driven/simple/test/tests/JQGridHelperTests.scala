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
import play._
import play.test._

import org.scalatest._
import org.scalatest.junit._
import org.scalatest.matchers._

import _root_.utils._

class JQGridHelperTests extends UnitFlatSpec with ShouldMatchers {

  class JQGridTestObject(num: Int, text: String) {
  }

  class JQGridTestObject2(id: Int, num: Int, text: String) {
  }

  it should "generate a valid JSON without IDs" in {
    val clazz = classOf[Int]
    var objects: List[JQGridTestObject] = List()
    objects = objects ::: List(new JQGridTestObject(10, "test1")) :::
      List(new JQGridTestObject(11, "test2")) :::
      List(new JQGridTestObject(12, "test3"))
    val json = JQGridHelper.toJson(1, 10, 30, objects)
    (json) should not be (null)
    (json) should include("\"page\":1")
    (json) should include("\"records\":30")
    (json) should include("\"total\":3")
    (json) should include("\"rows\":")
    (json) should include("\"id\":1")
  }
  
  it should "generate a valid JSON with custom IDs" in {
    val clazz = classOf[Int]
    var objects: List[JQGridTestObject2] = List()
    objects = objects ::: List(new JQGridTestObject2(1, 10, "test1")) :::
      List(new JQGridTestObject2(2, 11, "test2")) :::
      List(new JQGridTestObject2(3, 12, null))
    val json = JQGridHelper.toJson(1, 10, 30, objects)
    (json) should not be (null)
    (json) should include("\"page\":1")
    (json) should include("\"records\":30")
    (json) should include("\"total\":3")
    (json) should include("\"rows\":")
    (json) should include("\"id\":1")
  }
}