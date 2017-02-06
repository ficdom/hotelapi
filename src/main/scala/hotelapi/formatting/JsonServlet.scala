package hotelapi.formatting

import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

trait JsonServlet extends ScalatraServlet with JacksonJsonSupport {
  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

}
