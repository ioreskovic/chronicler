package com.github.fsanaulla.chronicler.examples.clients

import com.github.fsanaulla.chronicler.core.model.{InfluxConfig, InfluxCredentials}
import com.github.fsanaulla.chronicler.urlhttp.management.InfluxMng

import scala.util.{Failure, Success}

object SttpClientCreation extends App {
  val influxConfig = InfluxConfig(
    "localhost",
    8086,
    Some(InfluxCredentials(username = "admin", password = "admin")),
    false)

  val influxMng = InfluxMng(influxConfig)

  val pong = influxMng.ping

  pong match {
    case Success(result) if result.isSuccess => println(result)
    case Success(result) =>
      println(s"Code: ${result.code}, Reason: ${result.ex}")
    case Failure(exception) => exception.printStackTrace()
  }

  influxMng.close()
}
