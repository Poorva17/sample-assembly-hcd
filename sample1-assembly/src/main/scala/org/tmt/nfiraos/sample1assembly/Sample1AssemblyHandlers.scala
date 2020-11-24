package org.tmt.nfiraos.sample1assembly

import akka.Done
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.adapter.TypedActorSystemOps
import akka.stream.scaladsl.Source
import akka.stream.{ActorMaterializer, Materializer}
import csw.framework.CurrentStatePublisher
import csw.framework.scaladsl.ComponentHandlers
import csw.messages.TopLevelActorMessage
import csw.messages.commands.{CommandName, CommandResponse, ControlCommand, Setup}
import csw.messages.framework.ComponentInfo
import csw.messages.location.TrackingEvent
import csw.messages.params.generics.KeyType.{IntKey, StringKey}
import csw.messages.params.states.{CurrentState, StateName}
import csw.services.alarm.api.scaladsl.AlarmService
import csw.services.command.CommandResponseManager
import csw.services.event.api.scaladsl.EventService
import csw.services.location.scaladsl.LocationService
import csw.services.logging.scaladsl.LoggerFactory

import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.concurrent.{ExecutionContextExecutor, Future}

/**
 * Domain specific logic should be written in below handlers.
 * This handlers gets invoked when component receives messages/commands from other component/entity.
 * For example, if one component sends Submit(Setup(args)) command to Sample1Hcd,
 * This will be first validated in the supervisor and then forwarded to Component TLA which first invokes validateCommand hook
 * and if validation is successful, then onSubmit hook gets invoked.
 * You can find more information on this here : https://tmtsoftware.github.io/csw-prod/framework.html
 */
class Sample1AssemblyHandlers(
    ctx: ActorContext[TopLevelActorMessage],
    componentInfo: ComponentInfo,
    commandResponseManager: CommandResponseManager,
    currentStatePublisher: CurrentStatePublisher,
    locationService: LocationService,
    eventService: EventService,
    alarmService: AlarmService,
    loggerFactory: LoggerFactory
) extends ComponentHandlers(
      ctx,
      componentInfo,
      commandResponseManager,
      currentStatePublisher,
      locationService,
      eventService,
      alarmService,
      loggerFactory
    ) {

  implicit val ec: ExecutionContextExecutor = ctx.executionContext
  implicit val mat: Materializer            = ActorMaterializer()(ctx.system.toUntyped)

  override def initialize(): Future[Unit] = Future.successful(println("Hello World ************"))

  override def onLocationTrackingEvent(trackingEvent: TrackingEvent): Unit = {}

  override def validateCommand(controlCommand: ControlCommand): CommandResponse = {
    CommandResponse.Accepted(controlCommand.runId)
  }

  override def onSubmit(controlCommand: ControlCommand): Unit = {
    Thread.sleep(2000)
    println("Submit command received by assembly")
  }

  override def onOneway(controlCommand: ControlCommand): Unit = {
    controlCommand match {
      case s @ Setup(_, _, CommandName("filter-move"), _, _)    => delegateToHcd("filter", 1.second, s)
      case s @ Setup(_, _, CommandName("disperser-move"), _, _) => delegateToHcd("disperser", 2500.millis, s)
      case _                                                    => println("OneWay command received by assembly")
    }
  }

  def delegateToHcd(hcdName: String, delay: FiniteDuration, setup: Setup): Future[Done] = {
    val key       = IntKey.make(s"$hcdName-position")
    val nameParam = setup.get(StringKey.make("name"))
    Source
      .tick(0.millis, delay, ())
      .zipWithIndex
      .map {
        case (_, index) =>
          currentStatePublisher.publish(
            CurrentState(
              componentInfo.prefix,
              StateName(hcdName),
              Set(key.set(index.toInt)) ++ nameParam
            )
          )
      }
      .take(20)
      .runForeach(_ => ())
  }

  override def onShutdown(): Future[Unit] = Future {
    println("Shutdown command received by assembly")
  }

  override def onGoOffline(): Unit = {}

  override def onGoOnline(): Unit = {}

}
