package pl.mkubala.akkaPersistenceFsm

import akka.actor.ActorSystem
import akka.pattern._
import akka.util.Timeout
import pl.mkubala.akkaPersistenceFsm.MyCommand._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object Main {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("MySystem")
    implicit val ec: ExecutionContext = system.dispatcher
    implicit val timeout: Timeout = Timeout(5.seconds)

    val actor = system.actorOf(PersistentFsmActor.props, "Counter")

    actor ! Increment
    actor ! Add(2)
    actor ! Subtract(4)
    actor ! Subtract(1)
    actor ! GoSleep
    actor ! Increment
    actor ! Increment
    actor ! GoSleep

    val cntFut = (actor ? Get).mapTo[Int]
    cntFut.onComplete {
      case Success(cnt) =>
        system.log.info(s"Count = $cnt")
        system.terminate()
        System.exit(0)
      case Failure(_) =>
        system.terminate()
        System.exit(1)
    }
  }

}
