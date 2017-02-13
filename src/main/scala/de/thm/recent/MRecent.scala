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

class MRecent[A](private var underlying:Recent[A]) extends Recent[A] {
	override type Self = this.type
	override def recentValues: Seq[RecentValue[A]] = underlying.recentValues
	override def setValue(value:RecentValue[A]): Self = {
		underlying = underlying.setValue(value)
		this
	}
	override def updatePriority(value:RecentValue[A]): Self = {
		underlying = underlying.updatePriority(value)
		this
	}
	override def updatePriority(a:A, defaultPriority:Int)(fn: RecentValue[A] => Int): Self = {
		underlying = underlying.updatePriority(a, defaultPriority)(fn)
		this
	}
}

object MRecent {
	def apply[A](recent:Recent[A]):Recent[A] = new MRecent(recent)
	def fromList[A](xs:List[A]):Recent[A] = MRecent(Recent.fromList(xs))
}
