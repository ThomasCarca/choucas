package com

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.annotate.AnnotateRegistryActor
import com.hike.HikeRegistryActor
import com.pinpoint.PinpointRegistryActor
import com.box.BoxRegistryActor
import com.elasticSearch.ElasticSearchActor
import com.sentinel.SentinelRegistryActor
import com.queue.QueueRegistryActor
import com.tile.{DownloadRegistryActor, SaveRegistryActor, TileRegistryActor}

object Server extends App with RootRoutes {

  implicit val system: ActorSystem = ActorSystem("choucas-server")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatchers.lookup("my-blocking-dispatcher")

  val annotateRegistryActor: ActorRef = system.actorOf(AnnotateRegistryActor.props, "annotateRegistryActor")
  val hikeRegistryActor: ActorRef = system.actorOf(HikeRegistryActor.props.withDispatcher("my-blocking-dispatcher"), "hikeRegistryActor")
  val pinpointRegistryActor: ActorRef = system.actorOf(PinpointRegistryActor.props, "pinpointRegistryActor")
  val boxRegistryActor: ActorRef = system.actorOf(BoxRegistryActor.props, "boxRegistryActor")
  val sentinelRegistryActor: ActorRef = system.actorOf(SentinelRegistryActor.props.withDispatcher("my-blocking-dispatcher"), "sentinelRegistryActor")
  val tileRegistryActor: ActorRef = system.actorOf(TileRegistryActor.props.withDispatcher("my-blocking-dispatcher"), "tileRegistryActor")
  val downloadRegistryActor: ActorRef = system.actorOf(DownloadRegistryActor.props.withDispatcher("my-blocking-dispatcher"), "downloadRegistryActor")
  val queueRegistryActor: ActorRef = system.actorOf(QueueRegistryActor.props, "queueRegistryActor")
  val saveRegistryActor: ActorRef = system.actorOf(SaveRegistryActor.props.withDispatcher("my-blocking-dispatcher"), "saveRegistryActor")
  val elasticSearchActor: ActorRef = system.actorOf(ElasticSearchActor.props, "elasticSearchActor")

  lazy val routes: Route = rootRoutes

  val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(routes, "localhost", 8080)

  serverBinding.onComplete {
    case Success(bound) =>
      println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
    case Failure(e) =>
      Console.err.println(s"Server could not start!")
      e.printStackTrace()
      system.terminate()
  }

  Await.result(system.whenTerminated, Duration.Inf)
}
