package pl.mkubala.akkaPersistenceFsm

import akka.actor.Props
import akka.persistence.fsm.PersistentFSM

import pl.mkubala.akkaPersistenceFsm.MyCommand._
import pl.mkubala.akkaPersistenceFsm.MyEvent._
import pl.mkubala.akkaPersistenceFsm.MyState._

import scala.reflect.ClassTag

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
      val state =
        if (stateData >= -1) goto(Positive) applying Incremented
        else stay applying Incremented

      log.info(s"Increment - outside. Value = $stateData")
      state andThen { newStateData =>
        log.info(s"Increment - inside of andThen. Value = $newStateData")
      }
    case Event(Add(n), _) =>
      if (stateData + n >= 0) goto(Positive) applying Added(n)
      else stay applying Added(n)
    case Event(Subtract(n), stateData) =>
      val state =
        if (n > stateData) goto(Negative) applying Subtracted(n)
        else stay applying Subtracted(n)

      log.info(s"Subtract - outside. Value = $stateData")
      state andThen { newStateData =>
        log.info(s"Subtract - inside of andThen. Value = $newStateData")
      }
  }

  when(Sleeping) {
    case Event(GoSleep, _) =>
      stay
    case Event(_, stateData) =>
      stash()
      val nextState = if (stateData >= 0) Positive else Negative
      goto(nextState) andThen { _ =>
        unstashAll()
      }
  }

  whenUnhandled {
    case Event(GoSleep, _) =>
      log.info("Sleep - outside")
      goto(Sleeping) andThen { _ =>
        log.info("This will never be logged :(")
      } andThen { _ =>
        log.info("Sleep - inside of andThen")
      }
    case Event(Get, counterValue) =>
      log.info("Get - outside")
      stay replying counterValue andThen { _ =>
        log.info("Get - inside of andThen")
      }
  }

}

object PersistentFsmActor {
  def props: Props = Props(new PersistentFsmActor)
}
