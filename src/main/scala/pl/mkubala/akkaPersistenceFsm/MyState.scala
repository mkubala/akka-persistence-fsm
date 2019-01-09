package pl.mkubala.akkaPersistenceFsm

import akka.persistence.fsm.PersistentFSM.FSMState

sealed trait MyState extends FSMState

object MyState {

  case object Positive extends MyState {
    override def identifier: String = "Positive"
  }

  case object Negative extends MyState {
    override def identifier: String = "Negative"
  }

  case object Sleeping extends MyState {
    override def identifier: String = "Sleeping"
  }

}
