package hotelapi.utils

import grizzled.slf4j.Logger

trait Logging {
  @transient protected lazy val log: Logger = Logger(getClass)
}
