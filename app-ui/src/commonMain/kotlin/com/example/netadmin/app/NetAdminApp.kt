package com.example.netadmin.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.netadmin.app.theme.NetAdminTheme
import com.example.netadmin.core.engine.DiscoveryEngine
import com.example.netadmin.core.model.Device
import com.example.netadmin.core.model.Network
import com.example.netadmin.discovery.android.AndroidDiscoveryProvider
import com.example.netadmin.discovery.desktop.DesktopDiscoveryProvider
import com.example.netadmin.storage.InMemoryDeviceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NetAdminApp(isDesktop: Boolean) {
  val repo = remember { InMemoryDeviceRepository() }
  val providers = remember(isDesktop) {
    if (isDesktop) listOf(DesktopDiscoveryProvider())
    else listOf(AndroidDiscoveryProvider())
  }
  val engine = remember(isDesktop) {
    DiscoveryEngine(providers = providers, repo = repo, ioDispatcher = Dispatchers.Default)
  }

  val devices by repo.observeDevices().collectAsState(initial = emptyList())
  val events by repo.observeEvents().collectAsState(initial = emptyList())

  var tab by remember { mutableStateOf(0) }
  val scope = rememberCoroutineScope()
  var scanning by remember { mutableStateOf(false) }

  NetAdminTheme {
    Scaffold(
      topBar = {
        TopAppBar(
          title = { Text("NetDiscoveryAdmin") },
          actions = {
            Button(
              onClick = {
                if (scanning) return@Button
                scanning = true
                scope.launch {
                  // MVP: hardcoded CIDR. Replace with interface detection (ticket).
                  engine.scan(Network(cidr = "192.168.1.0/24"))
                  scanning = false
                }
              }
            ) { Text(if (scanning) "Scanning…" else "Scan now") }
            Spacer(Modifier.width(12.dp))
          }
        )
      }
    ) { padding ->
      Column(Modifier.fillMaxSize().padding(padding)) {
        TabRow(selectedTabIndex = tab) {
          Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Dashboard") })
          Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Devices") })
          Tab(selected = tab == 2, onClick = { tab = 2 }, text = { Text("Activity") })
          Tab(selected = tab == 3, onClick = { tab = 3 }, text = { Text("Style Parity") })
        }
        when (tab) {
          0 -> Dashboard(devices)
          1 -> DevicesList(devices)
          2 -> ActivityList(events.map { "${it.type}: ${it.message}" })
          3 -> StyleParityScreen()
        }
      }
    }
  }
}

@Composable
private fun Dashboard(devices: List<Device>) {
  Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    ElevatedCard(Modifier.fillMaxWidth()) {
      Column(Modifier.padding(16.dp)) {
        Text("Connected devices", style = MaterialTheme.typography.titleMedium)
        Text("${devices.count { isOnline(it) }} online • ${devices.size} total", style = MaterialTheme.typography.bodyMedium)
      }
    }
    ElevatedCard(Modifier.fillMaxWidth()) {
      Column(Modifier.padding(16.dp)) {
        Text("MVP notes", style = MaterialTheme.typography.titleMedium)
        Text("• Replace hardcoded CIDR with interface detection.", style = MaterialTheme.typography.bodyMedium)
        Text("• Add router integrations for accurate client lists + blocking.", style = MaterialTheme.typography.bodyMedium)
      }
    }
  }
}

@Composable
private fun DevicesList(devices: List<Device>) {
  LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
    items(devices) { d ->
      ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp)) {
          Text(d.displayName, style = MaterialTheme.typography.titleMedium)
          val meta = listOfNotNull(
            d.vendor,
            d.lastKnownIp?.let { "IP $it" },
            d.mac?.let { "MAC $it" }
          ).joinToString(" • ")
          if (meta.isNotBlank()) Text(meta, style = MaterialTheme.typography.bodyMedium)
          Text(if (isOnline(d)) "Online" else "Offline", style = MaterialTheme.typography.labelMedium)
        }
      }
    }
  }
}

@Composable
private fun ActivityList(lines: List<String>) {
  LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
    items(lines.take(200)) { line ->
      Text(line, style = MaterialTheme.typography.bodySmall)
      Divider()
    }
  }
}

private fun isOnline(d: Device): Boolean =
  (System.currentTimeMillis() - d.lastSeenEpochMs) < 2 * 60 * 1000
