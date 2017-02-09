package hotelapi.utils

import hotelapi.data.Hotel
import org.json4s.jackson.Serialization.write
import org.json4s.{DefaultFormats, FieldSerializer, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

trait JsonServlet extends ScalatraServlet with JacksonJsonSupport {
  private val hotelSerializer = FieldSerializer[Hotel](FieldSerializer.ignore("city"))
  protected implicit val jsonFormats: Formats = DefaultFormats + hotelSerializer

  before() {
    contentType = formats("json")
  }

  def toJson[A <: AnyRef](a: A): String = write(a)

}
