package com.github.fsanaulla.chronicler.examples.write

import java.time.Instant

import com.github.fsanaulla.chronicler.core.model.{
  InfluxConfig,
  InfluxCredentials,
  WriteResult
}
import com.github.fsanaulla.chronicler.urlhttp.io.api.Database
import com.github.fsanaulla.chronicler.urlhttp.io.InfluxIO

import scala.util.Try

object LineProtocolWrite extends App {
  type LineProtocolPoint = String

  val influxConfig = InfluxConfig(
    "localhost",
    8086,
    Some(InfluxCredentials(username = "admin", password = "admin")),
    false)

  val influxIO = InfluxIO(influxConfig)

  val db = influxIO.database("chroniclerTests")

  def generateNative(time: Instant): LineProtocolPoint = {
    val epochNanos = time.toEpochMilli * 1000000
    s"nativeMeasurement,tagKey1=tagValue1,tagKey2=tagValue foo=42,bar=13.0 $epochNanos"
  }

  def generateNativeBulk(time: Instant)(n: Int): Seq[LineProtocolPoint] = {
    (0 until n).map(s => generateNative(time.plusSeconds(s)))
  }

  def writeNativeSingle(p: LineProtocolPoint)(
      db: Database): Try[WriteResult] = {
    db.writeNative(p)
  }

  def writeNativeBulk(ps: Seq[LineProtocolPoint])(db: Database): Try[WriteResult] = {
    db.bulkWriteNative(ps)
  }

  writeNativeSingle(generateNative(Instant.now()))(db).foreach(println)
  writeNativeBulk(generateNativeBulk(Instant.now())(10))(db).foreach(println)
}
