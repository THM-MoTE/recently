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

import java.nio.file.{Path, Paths}

import spray.json._

object JsProtocol extends DefaultJsonProtocol {
	implicit def recVFormat[A : JsonFormat] = jsonFormat2(RecentValue.apply[A])
	implicit val pathFormat = new RootJsonFormat[Path]() {
		override def write(path: Path): JsValue = JsString(path.toString)

		override def read(json: JsValue): Path = json match {
			case JsString(str)=> Paths.get(str)
			case _ => deserializationError("String-encoded path expected")
		}
	}
}
