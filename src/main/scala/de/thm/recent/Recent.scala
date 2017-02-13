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
