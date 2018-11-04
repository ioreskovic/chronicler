package com.github.fsanaulla.chronicler.examples.write

import java.time.Instant

import com.github.fsanaulla.chronicler.core.model.{InfluxConfig, InfluxCredentials, InfluxWriter}
import com.github.fsanaulla.chronicler.macros.annotations.{field, tag, timestamp}
import com.github.fsanaulla.chronicler.urlhttp.io.InfluxIO

case class DomainEntity(
    @tag tagKey1: String,
    @tag tagKey2: String,
    @field foo: Int,
    @field bar: Double,
    @timestamp epochNanos: Long
)

object DomainWrite extends App {
  val influxConfig = InfluxConfig(
    "localhost",
    8086,
    Some(InfluxCredentials(username = "admin", password = "admin")),
    false)

  val influxIO = InfluxIO(influxConfig)

  def generateEntity(time: Instant): DomainEntity = DomainEntity(
    "tagValue1",
    "tagValue2",
    42,
    13.0,
    time.toEpochMilli * 1000000
  )

  def generateEntities(time: Instant)(n: Int): Seq[DomainEntity] =
    (0 until n).map(s => generateEntity(time.plusSeconds(s)))

  val mm = influxIO.measurement[DomainEntity](
    dbName = "chroniclerTests",
    measurementName = "domainMeasurement"
  )

  import com.github.fsanaulla.chronicler.macros.Macros._
  implicit val domainWriter: InfluxWriter[DomainEntity] = writer[DomainEntity]

  mm.write(generateEntity(Instant.now())).foreach(println)
  mm.bulkWrite(generateEntities(Instant.now())(10)).foreach(println)
}
