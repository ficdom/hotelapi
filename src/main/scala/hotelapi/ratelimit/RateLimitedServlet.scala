package hotelapi.ratelimit

import org.scalatra.ScalatraServlet

trait RateLimitedServlet extends ScalatraServlet with RateLimit {
  before() {
    params.get("key") match {
      case None => halt(403)
      case Some(key) => if (rateLimitExceeded(key)) halt(403)
    }
  }

}
