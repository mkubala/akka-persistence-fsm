package pl.mkubala.akkaPersistenceFsm

import akka.persistence.fsm.PersistentFSM
import akka.persistence.fsm.PersistentFSM.FSMState

import scala.reflect.ClassTag

sealed trait MyState extends FSMState
case object Positive extends MyState {
  override def identifier: String = "Positive"
}
case object Negative extends MyState {
  override def identifier: String = "Negative"
}

sealed trait MyCommand
case object Increment extends MyCommand
case class Add(n: Int) extends MyCommand {
  require(n >= 0, "number must be >= 0")
}
case class Subtract(n: Int) extends MyCommand {
  require(n >= 0, "number must be >= 0")
}

sealed trait MyEvent
case object Incremented extends MyEvent
case class Added(n: Int) extends MyEvent {
  require(n >= 0, "number must be >= 0")
}
case class Subtracted(n: Int) extends MyEvent {
  require(n >= 0, "number must be >= 0")
}

class PersistentFsmActor(implicit val domainEventClassTag: ClassTag[MyEvent])
  extends PersistentFSM[MyState, Int, MyEvent] {

  override def persistenceId: String = "counter-1"

  override def applyEvent(domainEvent: MyEvent, currentData: Int): Int = domainEvent match {
    case Incremented => currentData + 1
    case Added(n) => currentData + n
    case Subtracted(n) => currentData - n
  }

  startWith(Positive, 0)

  when(Positive) {
    case Event(Increment, _) =>
      stay applying Incremented
    case Event(Add(n), _) =>
      stay applying Added(n)
    case Event(Subtract(n), stateData) =>
      if (n > stateData) goto(Negative) applying Subtracted(n)
      else stay applying Subtracted(n)
  }

  when(Negative) {
    case Event(Increment, stateData) =>
      if (stateData >= -1) goto(Positive) applying Incremented
      else stay applying Incremented
    case Event(Add(n), _) =>
      if (stateData + n >= 0) goto(Positive) applying Added(n)
      else stay applying Added(n)
    case Event(Subtract(n), stateData) =>
      if (n > stateData) goto(Negative) applying Subtracted(n)
      else stay applying Subtracted(n)
  }

}
