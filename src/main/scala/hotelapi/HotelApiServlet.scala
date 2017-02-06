package hotelapi

import hotelapi.data.HotelData
import hotelapi.formatting.JsonServlet

class HotelApiServlet extends JsonServlet {

  get("/hotels/:city") {
    HotelData.getHotelsByCity(params("city"))
      .map(hotels => params.get("sort") match {
        case Some("asc") => hotels.sortBy(_.hotelId)
        case Some("desc") => hotels.sortBy(-_.hotelId)
        case _ => hotels
      })
  }
}
