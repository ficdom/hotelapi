package hotelapi.data

import scala.io.Source

object HotelData {
  private val HOTEL_DATA_FILENAME = "hoteldb.csv"
  private val hotels = Source.fromResource(HOTEL_DATA_FILENAME)
    .getLines
    .drop(1)
    .toList
    .map(parseCsvLine)
  private val hotelsByCity = hotels.groupBy(_.city)

  def getHotelsByCity(city: String): Option[List[Hotel]] =
    hotelsByCity.get(city.toLowerCase.capitalize)

  private def parseCsvLine(line: String) = line.split(',') match {
    case Array(city, hotelId, roomType, price) =>
      Hotel(city, hotelId.toInt, roomType, price.toDouble)
  }
}
