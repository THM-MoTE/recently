package de.thm.recent

import spray.json._

class RecentList[A](lst:Seq[RecentValue[A]])
	extends Recent[A]
	with DefaultJsonProtocol {
	override def recentValues: Seq[RecentValue[A]] = lst
}
