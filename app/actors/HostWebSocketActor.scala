package actors

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import akka.event.Logging
import entities.{Register, Unregister}
import play.api.Logger
import play.api.libs.json.Json

object HostWebSocketActor{

  def props(out: ActorRef)  = Props(new HostWebSocketActor(out))
}

class HostWebSocketActor(out: ActorRef) extends Actor {

  val log = Logging(context.system, this)

  val actorSelection: ActorSelection =
    context.actorSelection("akka://ActorSystem/user/hostActor")

  def receive: PartialFunction[Any, Unit] = {
    case "init" =>
      Logger.info("HostWebSocketActor :: Init connection Host")
      actorSelection ! Register(self)
      out ! Json.stringify(Json.obj("message" -> "HostWebSocketActor"))
    case None =>
      Logger.info("There are no questions")
      out ! Json.stringify(Json.obj("error" -> "no questions"))
  }

  override def postStop(): Unit = {
    actorSelection ! Unregister(self)
  }
}