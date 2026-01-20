package com.example.netadmin.core.engine

import com.example.netadmin.core.model.Device
import com.example.netadmin.core.model.Event
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
  suspend fun upsertDevices(devices: List<Device>)
  suspend fun addEvents(events: List<Event>)
  fun observeDevices(): Flow<List<Device>>
  fun observeEvents(limit: Int = 200): Flow<List<Event>>
}
