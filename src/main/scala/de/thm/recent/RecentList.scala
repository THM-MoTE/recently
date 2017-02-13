package de.thm.recent

import spray.json._

case class RecentList[A](lst:Seq[RecentValue[A]])
	extends Recent[A] {
	override type Self = RecentList[A]
	override def recentValues: Seq[RecentValue[A]] = lst
}
