package com.github.fsanaulla.chronicler.examples.clients

import com.github.fsanaulla.chronicler.async.management.InfluxMng
import com.github.fsanaulla.chronicler.core.model.{InfluxConfig, InfluxCredentials}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object AsyncHttpClientCreation extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  val influxConfig = InfluxConfig(
    "localhost",
    8086,
    Some(InfluxCredentials(username = "admin", password = "admin")),
    false)

  val influxMng = InfluxMng(influxConfig)

  val pong = for {
    pong <- influxMng.ping
    _ <- Future(influxMng.close())
  } yield pong

  pong.onComplete {
    case Success(result) if result.isSuccess => println(result)
    case Success(result) =>
      println(s"Code: ${result.code}, Reason: ${result.ex}")
    case Failure(exception) => exception.printStackTrace()
  }
}
