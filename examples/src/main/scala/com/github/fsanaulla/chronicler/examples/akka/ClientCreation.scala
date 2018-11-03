package com.github.fsanaulla.chronicler.examples.akka

import akka.actor.ActorSystem
import com.github.fsanaulla.chronicler.akka.management.InfluxMng
import com.github.fsanaulla.chronicler.core.model.{InfluxConfig, InfluxCredentials}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object ClientCreation extends App {
  implicit val actorSystem
    : ActorSystem = ActorSystem("chronicler-examples") // creates actor system
  import actorSystem.dispatcher // imports default actor system execution context

  val influxConfig = InfluxConfig(
    "localhost",
    8086,
    Some(InfluxCredentials(username = "admin", password = "admin")),
    false)
  val influxMng = InfluxMng(influxConfig) // execution context and actor system are used as implicit parameters

  val pong = for {
    pong <- influxMng.ping
    _ <- Future(influxMng.close())
    _ <- actorSystem.terminate()
  } yield pong

  pong.onComplete {
    case Success(result) if result.isSuccess => println(result)
    case Success(result) =>
      println(s"Code: ${result.code}, Reason: ${result.ex}")
    case Failure(exception) => exception.printStackTrace()
  }
}
