package com.example.netadmin.core.engine

import com.example.netadmin.core.model.Device
import com.example.netadmin.core.model.Sighting
import com.example.netadmin.core.model.TrustLevel
import kotlin.random.Random

object DeviceMerger {

  fun merge(existing: List<Device>, sightings: List<Sighting>, nowMs: Long): List<Device> {
    val byMac = existing.filter { it.mac != null }.associateBy { it.mac!!.lowercase() }.toMutableMap()
    val byIp = existing.filter { it.lastKnownIp != null }.associateBy { it.lastKnownIp!! }.toMutableMap()
    val results = existing.associateBy { it.deviceId }.toMutableMap()

    for (s in sightings) {
      val macKey = s.mac?.lowercase()
      val candidate: Device? = when {
        macKey != null && byMac.containsKey(macKey) -> byMac[macKey]
        byIp.containsKey(s.ip) -> byIp[s.ip]
        else -> null
      }

      if (candidate == null) {
        val deviceId = newId("dev")
        val name = s.hostName ?: s.vendor ?: "Unknown device"
        val dev = Device(
          deviceId = deviceId,
          displayName = name,
          mac = s.mac,
          vendor = s.vendor,
          trust = TrustLevel.UNKNOWN,
          firstSeenEpochMs = nowMs,
          lastSeenEpochMs = nowMs,
          lastKnownIp = s.ip
        )
        results[deviceId] = dev
        if (macKey != null) byMac[macKey] = dev
        byIp[s.ip] = dev
      } else {
        val updated = candidate.copy(
          displayName = candidate.displayName.ifBlank { s.hostName ?: candidate.vendor ?: "Unknown device" },
          vendor = candidate.vendor ?: s.vendor,
          mac = candidate.mac ?: s.mac,
          lastKnownIp = s.ip,
          lastSeenEpochMs = nowMs
        )
        results[updated.deviceId] = updated
        if (updated.mac != null) byMac[updated.mac!!.lowercase()] = updated
        byIp[s.ip] = updated
      }
    }
    return results.values.toList()
  }

  private fun newId(prefix: String): String = "${prefix}_${Random.nextLong().toString(16)}"
}
