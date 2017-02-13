package de.thm.recent

import spray.json._

class RecentTest extends RcTest with DefaultJsonProtocol {

	def asRecentValue[A](tuple:(A, Int)) = RecentValue(tuple._1, tuple._2)

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
		val lst = (0 until 20).toList.zip( (0 until 20).toList )
		val rc = Recent.fromPriorityList(lst)
		rc.recentElements shouldBe lst.map(_._1)
		rc.recentElementsByPriority shouldBe lst.map(_._1).reverse
 }

 it should "convert to JSON representation" in {
	 val lst = (0 until 4).toList.zip( (4 until 0 by -1).toList )
	 val rc = Recent.fromPriorityList(lst)

	 val expJson = """[{
			|  "value": 0,
			|  "priority": 4
			|}, {
			|  "value": 1,
			|  "priority": 3
			|}, {
			|  "value": 2,
			|  "priority": 2
			|}, {
			|  "value": 3,
			|  "priority": 1
			|}]""".stripMargin
	 rc.toJson shouldBe expJson
 }

 it should "convert from JSON representation" in {
	 val lst = (0 until 4).toList.zip( (4 until 0 by -1).toList )
	 val rc = Recent.fromPriorityList(lst)
	 val json = rc.toJson
	 Recent.fromJson[Int](json) shouldBe rc
 }

 it should "convert from an InputStream containing JSON representation" in {
	 val lst = List("akame" -> 5, "kurome" -> 3, "maggy" -> 2)
	 val stream = classOf[RecentTest].getClassLoader.getResourceAsStream("recent.json")
	 val recent = Recent.fromInputStream[String](stream)
	 recent shouldBe Recent.fromPriorityList(lst)
 }

 it should "add a new value" in {
	 val lst = List("akame" -> 5, "kurome" -> 3, "maggy" -> 2)
	 val recent = Recent.fromPriorityList(lst)
 	 val newRecent = recent.setValue(RecentValue[String]("naruto", 6))
	 newRecent.recentElementsByPriority shouldBe "naruto" :: lst.map(_._1)
 }

 it should "update an existing value" in {
	 val lst = List("akame" -> 5, "kurome" -> 3, "maggy" -> 2)
	 val recent = Recent.fromPriorityList(lst)
 	 val newRecent = recent.setValue(RecentValue[String]("maggy", 8))
	 newRecent.recentValues should have size lst.size
	 newRecent.recentValues shouldBe List("maggy" -> 8, "akame" -> 5, "kurome" -> 3).map(asRecentValue)
 }

 it should "update priority of existent values" in {
	 val lst = List("akame" -> 5, "kurome" -> 3, "maggy" -> 2)
	 val recent = Recent.fromPriorityList(lst)
	 val newRecent = recent.updatePriority(RecentValue("kurome", 6))
	 newRecent.recentValues should have size lst.size
	 newRecent.recentValues shouldBe List("kurome" -> 6, "akame" -> 5, "maggy" -> 2).map(asRecentValue)
 }

 it should "not update priority of non-existent values" in {
	 val lst = List("akame" -> 5, "kurome" -> 3, "maggy" -> 2)
	 val recent = Recent.fromPriorityList(lst)
	 val newVal = RecentValue("naruto", 3)
	 val newRecent = recent.updatePriority(newVal)
	 newRecent.recentValues should have size lst.size
	 newRecent shouldBe recent
 }
}
