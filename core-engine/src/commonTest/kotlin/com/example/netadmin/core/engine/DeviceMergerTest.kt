package com.example.netadmin.core.engine

import com.example.netadmin.core.model.Device
import com.example.netadmin.core.model.Sighting
import kotlin.test.Test
import kotlin.test.assertEquals

class DeviceMergerTest {
  @Test
  fun macMatchMerges() {
    val existing = listOf(
      Device(
        deviceId = "dev1",
        displayName = "A",
        mac = "AA:BB:CC:DD:EE:FF",
        vendor = "Test",
        firstSeenEpochMs = 0,
        lastSeenEpochMs = 0,
        lastKnownIp = "192.168.1.2"
      )
    )
    val sightings = listOf(
      Sighting(
        ip = "192.168.1.99",
        mac = "aa:bb:cc:dd:ee:ff",
        hostName = "host",
        vendor = null,
        services = emptyList(),
        seenAtEpochMs = 1
      )
    )
    val merged = DeviceMerger.merge(existing, sightings, 2)
    assertEquals(1, merged.size)
    assertEquals("192.168.1.99", merged[0].lastKnownIp)
  }
}
