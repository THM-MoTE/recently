package de.thm.recent

class MRecent[A](private var underlying:Recent[A]) extends Recent[A] {
	override type Self = this.type
	override def recentValues: Seq[RecentValue[A]] = underlying.recentValues
	override def setValue(value:RecentValue[A]): Self = {
		underlying = underlying.setValue(value)
		this
	}
	override def updatePriority(value:RecentValue[A]): Self = {
		underlying = underlying.updatePriority(value)
		this
	}
	override def updatePriority(a:A, defaultPriority:Int)(fn: RecentValue[A] => Int): Self = {
		underlying = underlying.updatePriority(a, defaultPriority)(fn)
		this
	}
}

object MRecent {
	def apply[A](recent:Recent[A]):Recent[A] = new MRecent(recent)
	def fromList[A](xs:List[A]):Recent[A] = MRecent(Recent.fromList(xs))
}