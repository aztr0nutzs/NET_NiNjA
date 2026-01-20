package com.example.netadmin.discovery.android

import com.example.netadmin.core.engine.DiscoveryProvider
import com.example.netadmin.core.model.Network
import com.example.netadmin.core.model.Sighting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress

class AndroidDiscoveryProvider(
  private val pingTimeoutMs: Int = 350,
) : DiscoveryProvider {
  override val name: String = "android-ping"

  override suspend fun discover(network: Network): List<Sighting> = withContext(Dispatchers.IO) {
    val (base, prefix) = parseCidr(network.cidr)
    if (prefix != 24) return@withContext emptyList()

    val now = System.currentTimeMillis()
    val out = ArrayList<Sighting>(64)

    for (host in 1..254) {
      val ip = "$base.$host"
      runCatching {
        val addr = InetAddress.getByName(ip)
        if (addr.isReachable(pingTimeoutMs)) {
          out.add(Sighting(ip, mac = null, hostName = null, vendor = null, services = emptyList(), seenAtEpochMs = now))
        }
      }
    }
    out
  }

  private fun parseCidr(cidr: String): Pair<String, Int> {
    val parts = cidr.split("/")
    val ip = parts[0]
    val prefix = parts.getOrNull(1)?.toIntOrNull() ?: 24
    val base = ip.split(".").take(3).joinToString(".")
    return base to prefix
  }
}
