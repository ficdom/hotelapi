package hotelapi

import hotelapi.data.HotelData
import hotelapi.formatting.JsonServlet
import hotelapi.ratelimit.RateLimitedServlet

class HotelApiServlet extends RateLimitedServlet with JsonServlet {

  get("/hotels/:city") {
    HotelData.getHotelsByCity(params("city"))
      .map(hotels => params.get("sort") match {
        case Some("asc"|"ASC") => hotels.sortBy(_.price)
        case Some("desc"|"DESC") => hotels.sortBy(-_.price)
        case _ => hotels
      })
  }
}
