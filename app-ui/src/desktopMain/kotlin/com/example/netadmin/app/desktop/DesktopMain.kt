package com.example.netadmin.app.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.netadmin.app.NetAdminApp

fun main() = application {
  Window(onCloseRequest = ::exitApplication, title = "NetDiscoveryAdmin") {
    NetAdminApp(isDesktop = true)
  }
}
