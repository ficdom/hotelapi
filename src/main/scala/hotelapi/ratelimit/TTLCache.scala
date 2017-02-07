package hotelapi.ratelimit

import scala.collection.concurrent.TrieMap

class TTLCache {
  private val keys = TrieMap[String, Long]()

  def put(key: String, ttl: Long): this.type = {
    keys += key -> (System.currentTimeMillis() + ttl)
    refresh()
    this
  }

  private def refresh() = keys.retain((key, expirationTime) => expirationTime > System.currentTimeMillis())

  def contains(key: String): Boolean = {
    refresh()
    keys.contains(key)
  }
}
