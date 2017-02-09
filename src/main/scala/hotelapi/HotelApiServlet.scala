package hotelapi

import hotelapi.data.HotelData
import hotelapi.ratelimit.RateLimitedServlet
import hotelapi.utils.{JsonServlet, Logging}

class HotelApiServlet extends RateLimitedServlet with JsonServlet with Logging {

  get("/hotels/:city") {
    val city = params("city")
    val key = params("key")
    val sort = params.get("sort")

    log.info(s"Handling request on /hotels/$city with an API Key: $key")

    val result = HotelData.getHotelsByCity(city)
      .map(hotels => sort match {
        case Some("asc" | "ASC") => hotels.sortBy(_.price)
        case Some("desc" | "DESC") => hotels.sortBy(-_.price)
        case _ => hotels
      })

    log.info(s"Returning response with body: ${toJson(result)}")

    result
  }

}
