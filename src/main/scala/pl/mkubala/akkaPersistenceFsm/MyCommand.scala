package pl.mkubala.akkaPersistenceFsm

sealed trait MyCommand

object MyCommand {

  case object Get extends MyCommand

  case object Increment extends MyCommand

  case class Add(n: Int) extends MyCommand {
    require(n >= 0, "number must be >= 0")
  }

  case class Subtract(n: Int) extends MyCommand {
    require(n >= 0, "number must be >= 0")
  }

  case object GoSleep extends MyCommand

}
