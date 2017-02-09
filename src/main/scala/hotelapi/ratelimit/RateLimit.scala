package hotelapi.ratelimit

import com.typesafe.config.ConfigFactory
import hotelapi.utils.Logging

trait RateLimit extends Logging {
  private val config = ConfigFactory.load()
  private val defaultLimit = config.getDouble("defaultRateLimit")
  private val keyLimits = config.getConfig("apiKeysRateLimits")
  private val suspensionDuration = config.getLong("suspensionDurationMS")
  private val activeKeys = new TTLCache()
  private val suspendedKeys = new TTLCache()

  def rateLimitExceeded(key: String): Boolean =
    if (suspendedKeys.contains(key)) {
      log.trace(s"Key $key is suspended. Rejecting the request.")
      true
    } else if (activeKeys.contains(key)) {
      log.trace(s"Suspending the key=$key for $suspensionDuration ms. Rejecting the request.")
      suspendedKeys.put(key, suspensionDuration)
      true
    } else {
      activeKeys.put(key, getTTL(key))
      false
    }

  private def getTTL(key: String) = rateLimitToTTL(getRateLimit(key))

  private def getRateLimit(key: String) =
    if (keyLimits.hasPath(key)) {
      keyLimits.getDouble(key)
    } else {
      defaultLimit
    }

  private def rateLimitToTTL(rateLimit: Double) = (1000 / rateLimit).toLong
}
