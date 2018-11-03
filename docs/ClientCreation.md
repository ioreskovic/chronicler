# Client creation

## akka-http backend

As the name implies, using `akka-http` with `chronicler` requires an
`ActorSystem` to be present.

```scala
implicit val actorSystem: ActorSystem = ActorSystem("chronicler-examples")
```

Since operations return `Future`, an `ExecutionContext` is also required.
We can use ones provided to us by `akka`.

```scala
import actorSystem.dispatcher
```

Creating Influx client instance requires valid configuration
```scala
val influxConfig = InfluxConfig(
    "localhost",
    8086,
    Some(InfluxCredentials(username = "admin", password = "admin")),
    false
)

val influxMng = InfluxMng(influxConfig)
val influxIo = InfluxIO(influxConfig)
```

When done, don't forget to close the client and terminate the actor system.

Full example can be found in `examples` module in `
com.github.fsanaulla.chronicler.examples.akka.ClientCreation` class.

## url-http backend

This client uses `sttp` library under the hood to talk to InfluxDB in a
synchronous manner. As such, it does not require any special imports
like`akka-http` one.

Creating Influx client instance is the same as in `akka-http` example, the only
difference is that clients reside in `com.github.fsanaulla.chronicler.urlhttp`
package.

```scala
import com.github.fsanaulla.chronicler.urlhttp.management.InfluxMng
```

```scala
val influxConfig = InfluxConfig(
    "localhost",
    8086,
    Some(InfluxCredentials(username = "admin", password = "admin")),
    false
)

val influxMng = InfluxMng(influxConfig)
val influxIo = InfluxIO(influxConfig)
```

Full example can be found in `examples` module in `
com.github.fsanaulla.chronicler.examples.sttp.core.ClientCreation` class.

## async-http backend

As the name implies, this backend communicates with InfluxDB asynchronously,
so just like with `akka-http`, an execution context is required.

For this example, a global one is imported

```scala
import scala.concurrent.ExecutionContext.Implicits.global
```

Again, client creation looks the same

```scala
val influxConfig = InfluxConfig(
    "localhost",
    8086,
    Some(InfluxCredentials(username = "admin", password = "admin")),
    false)

val influxMng = InfluxMng(influxConfig)
val influxIo = InfluxIO(influxConfig)
```

Full example can be found in `examples` module in `
com.github.fsanaulla.chronicler.examples.sttp.async.ClientCreation` class.
