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
}
