package com.example.netadmin.core.model

import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Serializable
data class Network(
  val cidr: String,
  val gatewayIp: String? = null,
  val interfaceName: String? = null,
)

@Serializable
data class Sighting(
  val ip: String,
  val mac: String? = null,
  val hostName: String? = null,
  val vendor: String? = null,
  val services: List<String> = emptyList(),
  val seenAtEpochMs: Long,
)

@Serializable
enum class TrustLevel { TRUSTED, UNKNOWN, SUSPICIOUS, BLOCKED }

@Serializable
data class Device(
  val deviceId: String,
  val displayName: String,
  val mac: String? = null,
  val vendor: String? = null,
  val trust: TrustLevel = TrustLevel.UNKNOWN,
  val firstSeenEpochMs: Long,
  val lastSeenEpochMs: Long,
  val lastKnownIp: String? = null,
)

@Serializable
enum class EventType {
  SCAN_STARTED,
  DEVICE_SEEN,
  DEVICE_UPDATED,
  SCAN_FINISHED,
  POLICY_APPLIED,
  ERROR
}

@Serializable
data class Event(
  val eventId: String,
  val type: EventType,
  val epochMs: Long,
  val severity: Int = 1,
  val message: String,
  val payloadJson: String? = null
)

@Serializable
data class ScanProfile(
  val maxConcurrentProbes: Int = 128,
  val connectTimeoutMs: Int = 350,
  val pingTimeoutMs: Int = 350,
  val quietMode: Boolean = true,
  val newDeviceApprovalGrace: Duration = 15.minutes,
)
