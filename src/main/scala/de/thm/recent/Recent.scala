package de.thm.recent

import spray.json._

trait Recent[A] {
	this: DefaultJsonProtocol =>

	def recentValues: Seq[RecentValue[A]]

	def recentValuesByPriority: Seq[RecentValue[A]] =
		recentValues.sortWith( (a,b) => a.priority > b.priority)

	def recentElements: Seq[A] =
		recentValues.map(v => v.value)

	def recentElementsByPriority: Seq[A] =
		recentValuesByPriority.map(v => v.value)

	def toJson(implicit format:JsonFormat[A]): String = {
		implicit val recVFormat = jsonFormat2(RecentValue.apply[A])
		recentValuesByPriority.toJson.prettyPrint
	}
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
}
