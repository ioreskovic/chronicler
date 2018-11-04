package com.github.fsanaulla.chronicler.examples.write

import java.time.Instant

import com.github.fsanaulla.chronicler.core.model._
import com.github.fsanaulla.chronicler.urlhttp.io.InfluxIO
import com.github.fsanaulla.chronicler.urlhttp.io.api.Database

object PointWrite extends App {
  val influxConfig = InfluxConfig(
    "localhost",
    8086,
    Some(InfluxCredentials(username = "admin", password = "admin")),
    false)

  val influxIO = InfluxIO(influxConfig)

  val db = influxIO.database("chroniclerTests")

  def generatePoint(time: Instant): Point = Point(
    "pointMeasurement",
    List(InfluxTag("tagKey1", "tagValue1"), InfluxTag("tagKey2", "tagValue2")),
    List(IntField("foo", 42), DoubleField("bar", 13.0)),
    time.toEpochMilli() * 1000000
  )

  def generatePoints(time: Instant)(n: Int): Seq[Point] = (0 until n).map(s => generatePoint(time.plusSeconds(s)))

  def writePointSingle(p: Point)(db: Database) = db.writePoint(p)
  def writePointsBulk(ps: Seq[Point])(db: Database) = db.bulkWritePoints(ps)

  writePointSingle(generatePoint(Instant.now()))(db).foreach(println)
  writePointsBulk(generatePoints(Instant.now())(10))(db).foreach(println)
}
