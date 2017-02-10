package de.thm.recent

import spray.json._

object JsProtocol extends DefaultJsonProtocol {
	implicit def recVFormat[A : JsonFormat] = jsonFormat2(RecentValue.apply[A])
}
