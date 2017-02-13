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

case class RecentList[A](lst:Seq[RecentValue[A]])
	extends Recent[A] {
	override type Self = RecentList[A]
	override def recentValues: Seq[RecentValue[A]] = lst
	override def setValue(value:RecentValue[A]): Self = {
		val newList = lst.find(_.value == value.value) match {
			case Some(oldVal) =>
				value +: lst.filter(_ != oldVal)
			case None => value +: lst
		}
		RecentList(newList)
	}

	override def updatePriority(value:RecentValue[A]): Self = {
		lst
			.find(_.value == value.value)
			.map { oldVal =>
				val newList = oldVal.copy(priority = value.priority) +: lst.filter(_ != oldVal)
				RecentList(newList)
			}
			.getOrElse(this)
	}

	override def updatePriority(a:A, defaultPriority:Int)(fn: RecentValue[A] => Int): Self = {
		val newPriority = lst.find(_.value == a).map(fn).getOrElse(defaultPriority)
		val rc = RecentValue(a, newPriority)
		setValue(rc)
	}
}
