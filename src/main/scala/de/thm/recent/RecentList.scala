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
