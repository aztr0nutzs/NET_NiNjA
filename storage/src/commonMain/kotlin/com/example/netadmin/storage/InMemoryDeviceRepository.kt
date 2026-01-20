package com.example.netadmin.storage

import com.example.netadmin.core.engine.DeviceRepository
import com.example.netadmin.core.engine.SnapshotDeviceSource
import com.example.netadmin.core.model.Device
import com.example.netadmin.core.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class InMemoryDeviceRepository : DeviceRepository, SnapshotDeviceSource {
  private val mutex = Mutex()
  private val devices = MutableStateFlow<List<Device>>(emptyList())
  private val events = MutableStateFlow<List<Event>>(emptyList())

  override suspend fun upsertDevices(devices: List<Device>) {
    mutex.withLock { this.devices.value = devices.sortedByDescending { it.lastSeenEpochMs } }
  }

  override suspend fun addEvents(events: List<Event>) {
    mutex.withLock {
      val merged = (events + this.events.value).sortedByDescending { it.epochMs }.take(500)
      this.events.value = merged
    }
  }

  override fun observeDevices(): Flow<List<Device>> = devices.asStateFlow()
  override fun observeEvents(limit: Int): Flow<List<Event>> = events.asStateFlow()

  override suspend fun snapshotDevices(): List<Device> = mutex.withLock { devices.value }
}
