package de.thm.recent

import spray.json._

class RecentTest extends RcTest with DefaultJsonProtocol {
	"Recent" should "use a predefined list" in {
		val lst = (0 until 20).map(RecentValue(_, 1)).toList
		val rList = new RecentList(lst)
		rList.recentValues should have size lst.size
		rList.recentValues shouldBe lst
		rList.recentValuesByPriority shouldBe lst
		rList.recentElements shouldBe lst.map(v => v.value)
	}

	it should "use a predefined list without priority" in {
		val lst = (0 until 20).toList
		val rc = Recent.fromList(lst)
		rc.recentValues shouldBe lst.map(RecentValue(_,1))
		rc.recentElements shouldBe lst
		rc.recentElementsByPriority shouldBe lst
	}

	it should "use a predefined list with priority" in {
		val lst = (0 until 20).toList.zip( (20 until 0 by -1).toList )
		val rc = Recent.fromPriorityList(lst)
		rc.recentElements shouldBe lst.map(_._1)
		rc.recentElementsByPriority shouldBe lst.map(_._1).reverse
 }

 it should "create a JSON representation" in {
	 val lst = (0 until 20).toList.zip( (20 until 0 by -1).toList )
	 val rc = Recent.fromPriorityList(lst)
	 println(rc.toJson)
 }
}
