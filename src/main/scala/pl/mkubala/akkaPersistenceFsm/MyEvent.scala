package pl.mkubala.akkaPersistenceFsm

sealed trait MyEvent

object MyEvent {

  case object Incremented extends MyEvent

  case class Added(n: Int) extends MyEvent

  case class Subtracted(n: Int) extends MyEvent

}
