# Writing data

Chronicler supports 3 levels at which you can write data
1. Native line protocol
2. Generic point
3. Your own domain model

On top of that, data can be written element by element or in bulk.

## Native line protocol
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

[line protocol]: https://docs.influxdata.com/influxdb/v1.6/write_protocols/line_protocol_reference/

## Native Point
