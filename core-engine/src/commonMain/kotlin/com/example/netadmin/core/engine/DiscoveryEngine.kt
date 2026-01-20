package com.example.netadmin.core.engine

import com.example.netadmin.core.model.Event
import com.example.netadmin.core.model.EventType
import com.example.netadmin.core.model.Network
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlin.random.Random

class DiscoveryEngine(
  private val providers: List<DiscoveryProvider>,
  private val repo: DeviceRepository,
  private val ioDispatcher: CoroutineDispatcher,
) {
  suspend fun scan(network: Network) = withContext(ioDispatcher) {
    repo.addEvents(listOf(event(EventType.SCAN_STARTED, "Scan started", payloadCidr(network.cidr))))

    val sightings = coroutineScope {
      providers.map { p ->
        async {
          runCatching { p.discover(network) }.getOrElse { e ->
            repo.addEvents(listOf(event(EventType.ERROR, "Provider ${p.name} failed", payloadError(e.message), 3)))
            emptyList()
          }
        }
      }.awaitAll().flatten()
    }

    val existing = (repo as? SnapshotDeviceSource)?.snapshotDevices().orEmpty()
    val merged = DeviceMerger.merge(existing, sightings, System.currentTimeMillis())
    repo.upsertDevices(merged)

    repo.addEvents(listOf(event(EventType.SCAN_FINISHED, "Scan finished", payloadSummary(providers.size, sightings.size))))
  }

  private fun event(type: EventType, msg: String, payload: String? = null, severity: Int = 1): Event =
    Event(
      eventId = "evt_${Random.nextLong().toString(16)}",
      type = type,
      epochMs = System.currentTimeMillis(),
      severity = severity,
      message = msg,
      payloadJson = payload
    )

  private fun payloadCidr(cidr: String) = "{"cidr":"$cidr"}"
  private fun payloadSummary(providers: Int, sightings: Int) = "{"providers":$providers,"sightings":$sightings}"
  private fun payloadError(msg: String?) = "{"error":"${(msg ?: "unknown").replace(""","'")}"}"
}

interface SnapshotDeviceSource {
  suspend fun snapshotDevices(): List<com.example.netadmin.core.model.Device>
}
