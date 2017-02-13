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

class MRecentTest extends RcTest {
	"A mutable RecentList" should "set a value in-place" in {
		val lst = (0 until 10).toList
		val mrecent = new MRecent[Int](Recent.fromList(lst))
		val mrecent2 = mrecent.setValue(RecentValue(1,2))
		mrecent.recentValuesByPriority shouldBe (RecentValue(1, 2) +: (0 +: (2 until 10)).map(RecentValue(_, 1)).toList)
		mrecent2 shouldBe theSameInstanceAs (mrecent)
	}

	it should "update a priority in-place" in {
		val lst = (0 until 10).toList
		val mrecent = new MRecent[Int](Recent.fromList(lst))
		val mrecent2 = mrecent.updatePriority(RecentValue(1,5))
		mrecent.recentValuesByPriority shouldBe (RecentValue(1, 5) +: (0 +: (2 until 10)).map(RecentValue(_, 1)).toList)
		mrecent2 shouldBe theSameInstanceAs (mrecent)
	}
}
