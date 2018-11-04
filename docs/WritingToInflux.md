# Writing data

Chronicler supports 3 levels at which you can write data
1. Native line protocol
2. Generic point
3. Your own domain model

On top of that, data can be written element by element or in bulk.

## [Native line protocol]
While Influx uses it's [line protocol] in HTTP API, it is not recommended to
use it to write your data from your programs this way directly. It relies on
specific string formatting, is not type safe and has peculiarities you would
need to cover yourself for consistent usage. Still, for completeness sake,
here is the example.

Non-type-safe writes require specifying database explicitly
```scala
val db = influxIO.database("chroniclerTests")
```

To generate basic line protocol point, one could do something like this
```scala
type LineProtocolPoint = String

def generateNative(time: Instant): LineProtocolPoint = {
  val epochNanos = time.toEpochMilli * 1000000
  s"nativeMeasurement,tagKey1=tagValue1,tagKey2=tagValue foo=42,bar=13.0 $epochNanos"
}
```

And to write it
```scala
def writeNativeSingle(p: LineProtocolPoint)(db: Database): Try[WriteResult] = {
  db.writeNative(p)
}
```

Native writer also supports bulk writes
```scala
def writeNativeBulk(ps: Seq[LineProtocolPoint])(db: Database): Try[WriteResult] = {
  db.bulkWriteNative(ps)
}
```

## [Generic Point]
If you don't want to construct line protocol string by yourself
(and you really shouldn't), there is an abstraction over it, and it's called
`Point`.

As with native line protocol, point allows you to specify measurement name,
tags, fields and event time.

```scala
def generatePoint(time: Instant): Point = Point(
  "pointMeasurement",
  List(InfluxTag("tagKey1", "tagValue1"), InfluxTag("tagKey2", "tagValue2")),
  List(IntField("foo", 42), DoubleField("bar", 13.0)),
  time.toEpochMilli() * 1000000
)
```

Similar to line protocol, writing requires database to be specified explicitly
```scala
def writePointSingle(p: Point)(db: Database) = db.writePoint(p)
```

Currently the following value types are supported by `chronicler`
* `com.github.fsanaulla.chronicler.core.model.StringField`
* `com.github.fsanaulla.chronicler.core.model.IntField`
* `com.github.fsanaulla.chronicler.core.model.LongField`
* `com.github.fsanaulla.chronicler.core.model.DoubleField`
* `com.github.fsanaulla.chronicler.core.model.BooleanField`
* `com.github.fsanaulla.chronicler.core.model.CharField`
* `com.github.fsanaulla.chronicler.core.model.BigDecimalField`

## [Your own domain model]
Finally, to avoid the fuss of creating strings and/or points manually,
you could use `chronicler` macro support to do that for you.

First step is to annotate your domain entities so `chronicler` knows how
to interpret them
```scala
case class DomainEntity(
    @tag tagKey1: String,
    @tag tagKey2: String,
    @field foo: Int,
    @field bar: Double,
    @timestamp epochNanos: Long
)
```

Second step is to obtain type-safe handle for writing
```scala
val mm = influxIO.measurement[DomainEntity](
  dbName = "chroniclerTests",
  measurementName = "domainMeasurement"
)
```

Third one is to bring writer into scope
```scala
import com.github.fsanaulla.chronicler.macros.Macros._
implicit val domainWriter: InfluxWriter[DomainEntity] = writer[DomainEntity]
```

Enjoy your compile time type-safety
```scala
mm.write(generateEntity(Instant.now())).foreach(println)
mm.bulkWrite(generateEntities(Instant.now())(10)).foreach(println)
```

[line protocol]: https://docs.influxdata.com/influxdb/v1.6/write_protocols/line_protocol_reference/
[Native line protocol]: ../examples/src/main/scala/com/github/fsanaulla/chronicler/examples/write/LineProtocolWrite.scala
[Generic Point]: ../examples/src/main/scala/com/github/fsanaulla/chronicler/examples/write/PointWrite.scala
[Your own domain model]: ../examples/src/main/scala/com/github/fsanaulla/chronicler/examples/write/DomainWrite.scala
