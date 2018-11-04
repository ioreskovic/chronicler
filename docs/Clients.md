# Clients

## HTTP
There are 3 HTTP clients for InfluxDB under `chronicler` project.
Clients share the same API, but use different backends under the hood.
All of them delegate work to smaller clients, as separate jars:

- IO client (with suffix `io`)
- Management client (with suffix `management`)


### *IO Client*
If you need to make IO related operations like read/write - choose IO client.
To create it call apply method on `InfluxIO` object:
```
val ioClient = InfluxIO(...) // constructor parameters may wary depends on backend
```

### *Management Client*
If you need to perform some  management operations like: create user/table/database/etc - choose Management client.
To create it call apply method on `InfluxMng` object:
```
val managementClient = InfluxMng(...) // constructor parameters may wary depends on backend
```

Client creation may require additional parameters, depending on selected backend.

### Backends

#### akka-http backend

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

#### url-http backend

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

#### async-http backend

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

For more information you can always ask questions on [gitter](https://gitter.im/chronicler-scala/Lobby) and check out some examples.
