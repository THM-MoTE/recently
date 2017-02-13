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
