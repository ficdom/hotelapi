package hotelapi.ratelimit

import hotelapi.utils.Logging
import org.scalatra.ScalatraServlet

trait RateLimitedServlet extends ScalatraServlet with RateLimit with Logging {
  before() {
    params.get("key") match {
      case None =>
        log.trace("Request doesn't contain API Key. Rejecting.")
        halt(403)
      case Some(key) => if (rateLimitExceeded(key)) halt(403)
    }
  }

}
