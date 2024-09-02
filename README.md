# webdav-gateway

August 2024: This project is deprecated meanwhile because the latest nextcloud 
photo app releases added features like a map. The show case server is no longer
running.

A simple read only gateway/bridge for mapping a webdav interface (eg. Nextcloud) to an easy to use REST like HTTP endpoint. This might than be used for web clients for easy fetching webdav data. 

Providing a CORS proxy was the initial and main reason for this gateway.

https://github.com/thomass171/web-photo-album is a web photo album that uses this gateway.

# Building

Just a

```
mvn clean install
```

# Local Testing

```
sh bin/startServer.sh
```

In a browser enter

```
localhost:8019
```
