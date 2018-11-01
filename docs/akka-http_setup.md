# Akka-Http setup

As the name implies, using `akka-http` with `chronicler` requires an `ActorSystem` to be present.
```scala
implicit val actorSystem: ActorSystem = ActorSystem("chronicler-examples")
```

Since operations return `Future`, an `ExecutionContext` is also required. We can use ones provided to us by `akka`.
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

Full example can be found in `examples/akka-http` module in `com.github.fsanaulla.chronicler.examples.akka.ClientCreation` class.


[akka-http]: (https://doc.akka.io/docs/akka-http/current/)
