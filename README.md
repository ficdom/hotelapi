
## Running the service:

``` scala
$ sbt
> jetty:start
```
Service by default listens on port 8080.


## Sample requests:

```
localhost:8080/hotels/Bangkok?key=e5c30b608255f96f
```
Response will contain a JSON body:
```

[
    {
        "hotelId":11,
        "roomType":"Deluxe",
        "price":60.0
    },
    {
        "hotelId":15,
        "roomType":"Deluxe",
        "price":900.0
    },
    {
        "hotelId":1,
        "roomType":"Deluxe",
        "price":1000.0
    },
    {
        "hotelId":6,
        "roomType":"Superior",
        "price":2000.0
    },
    {
        "hotelId":8,
        "roomType":"Superior",
        "price":2400.0
    },
    {
        "hotelId":18,
        "roomType":"Sweet Suite",
        "price":5300.0
    },
    {
        "hotelId":14,
        "roomType":"Sweet Suite",
        "price":25000.0
    }
]
```

Sorting hotels by the price can be done by adding 'sort' param to the path:
```
localhost:8080/hotels/Amsterdam?key=e5c30b608255f96f&sort=asc
localhost:8080/hotels/Amsterdam?key=e5c30b608255f96f&sort=desc
```
Key 'e5c30b608255f96f' is specified in the config file with allowed rate of 20 RPS. API Keys not included in the config are limited to one request per 10 seconds:
```
localhost:8080/hotels/Amsterdam?key=unknownKey
```
Sending above request twice within 10 seconds will result in status 403 being returned on second attempt and 'unknownKey' being suspended for 5 minutes.

For requests without API Key, i.e.:
```
localhost:8080/hotels/Amsterdam
```
response with status 403 is always returned.
## RateLimit:
Solution is based on a simple TTL cache implementation relying on scala.collection.concurrent.TrieMap.

Note that setting allowed rate of 20 RPS for a given key doesn't guarantee that such rate will always be handled. It will be handled only in one case: when requests will be sent at an interval of 0.05 second.