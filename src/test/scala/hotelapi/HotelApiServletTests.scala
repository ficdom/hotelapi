package hotelapi

import org.scalatest.FunSuiteLike
import org.scalatra.test.scalatest.ScalatraSuite

class HotelApiServletTests extends ScalatraSuite with FunSuiteLike {
  addServlet(classOf[HotelApiServlet], "/*")

  private val bangkokHotel1 = """{"hotelId":1,"roomType":"Deluxe","price":1000.0}"""
  private val bangkokHotel2 = """{"hotelId":6,"roomType":"Superior","price":2000.0}"""
  private val bangkokHotel3 = """{"hotelId":8,"roomType":"Superior","price":2400.0}"""
  private val amsterdamHotel = """{"hotelId":2,"roomType":"Superior","price":2000.0}"""

  test("Call /hotels endpoint without the api key") {
    get("/hotels/Bangkok") {
      status should equal(403)
      body should equal("")
    }
  }

  test("Retrieve hotels without sorting") {
    get("/hotels/Bangkok?key=abc") {
      status should equal(200)
      body should equal(s"[$bangkokHotel2,$bangkokHotel1,$bangkokHotel3]")
    }
  }

  test("Retrieve hotels sorted ascending") {
    get("/hotels/Bangkok?key=abcd&sort=asc") {
      status should equal(200)
      body should equal(s"[$bangkokHotel1,$bangkokHotel2,$bangkokHotel3]")
    }
  }

  test("Retrieve hotels sorted descending") {
    get("/hotels/Bangkok?key=abcde&sort=desc") {
      status should equal(200)
      body should equal(s"[$bangkokHotel3,$bangkokHotel2,$bangkokHotel1]")
    }
  }

  test("Retrieve hotels from a city nonexistent in DB") {
    get("/hotels/Gdansk?key=123") {
      status should equal(200)
      body should equal("")
    }
  }

  test("Retrieve hotels repeatedly") {
    get("/hotels/Amsterdam?key=1234") {
      status should equal(200)
      body should equal(s"[$amsterdamHotel]")
    }
    get("/hotels/Amsterdam?key=1234") {
      status should equal(403)
      body should equal("")
    }
    get("/hotels/Amsterdam?key=1234") {
      status should equal(403)
      body should equal("")
    }
  }

  test("Retrieve hotels repeatedly for a key with a high rate limit") {
    get("/hotels/Amsterdam?key=54fea") {
      status should equal(200)
      body should equal(s"[$amsterdamHotel]")
    }
    get("/hotels/Amsterdam?key=54fea") {
      status should equal(200)
      body should equal(s"[$amsterdamHotel]")
    }
    get("/hotels/Amsterdam?key=54fea") {
      status should equal(200)
      body should equal(s"[$amsterdamHotel]")
    }
  }

  test("Retrieve hotels even when the city name is weirdly cased") {
    get("/hotels/amSTeRdaM?key=54fea") {
      status should equal(200)
      body should equal(s"[$amsterdamHotel]")
    }
  }
}