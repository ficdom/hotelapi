package hotelapi.ratelimit

import scala.collection.concurrent.TrieMap

class TTLCache {
  def put(key: String, ttl: Long): this.type = {
    keys += key -> (System.currentTimeMillis() + ttl)
    refresh()
    this
  }

  def contains(key: String): Boolean = {
    refresh()
    keys.contains(key)
  }

  private val keys = TrieMap[String, Long]()

  private def refresh() = keys.retain((key, expirationTime) => expirationTime > System.currentTimeMillis())
}
