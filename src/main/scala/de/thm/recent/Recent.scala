/**
 * Copyright 2017 Nicola Justus <nicola.justus@mni.thm.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.thm.recent

import spray.json._
import JsProtocol._
import java.io.InputStream
import java.nio.charset.StandardCharsets

trait Recent[A] {
	type Self <: Recent[A]

	def recentValues: Seq[RecentValue[A]]

	def recentValuesByPriority: Seq[RecentValue[A]] =
		recentValues.sortWith( (a,b) => a.priority > b.priority)

	def recentElements: Seq[A] =
		recentValues.map(v => v.value)

	def recentElementsByPriority: Seq[A] =
		recentValuesByPriority.map(v => v.value)

	def toJson(implicit format:JsonFormat[A]): String = {
		recentValuesByPriority.toJson.prettyPrint
	}

	def setValue(value:RecentValue[A]): Self
	def updatePriority(value:RecentValue[A]): Self
	def updatePriority(a:A, defaultPriority:Int)(fn: RecentValue[A] => Int): Self
	def incrementPriority(a:A): Self =
		updatePriority(a, 1) { oldVal => oldVal.priority+1 }
}

object Recent {
	type PriorityItem[A] = (A, Int)

	def fromList[A](xs:Seq[A]): Recent[A] =
		new RecentList(xs.map(a => RecentValue(a, 1)))

	def fromPriorityList[A](xs:Seq[PriorityItem[A]]): Recent[A] = {
		val recentValues = xs.map {
			case (elem, priority) => RecentValue(elem, priority)
		}
		new RecentList(recentValues)
	}

	def fromJson[A : JsonFormat](json:String): Recent[A] = {
		val values = json.parseJson.convertTo[List[RecentValue[A]]]
		new RecentList(values)
	}

	def fromInputStream[A : JsonFormat](is:InputStream): Recent[A] = {
		val buffSrc = scala.io.Source.fromInputStream(is)(scala.io.Codec(StandardCharsets.UTF_8))
		fromJson(buffSrc.mkString)
	}
}
