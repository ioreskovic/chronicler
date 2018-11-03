# Clients

## HTTP
There are 3 HTTP clients for InfluxDB under `chronicler` project
Clients share the same API, but use different backends under the hood:

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
For more information you can always ask questions on [gitter](https://gitter.im/chronicler-scala/Lobby) and check out some examples.

- [akka-http setup example](ClientCreation.md#akka-http-backend)
- [async-http setup example](ClientCreation.md#async-http-backend)
- [url-http setup example](ClientCreation.md#url-http-backend)
