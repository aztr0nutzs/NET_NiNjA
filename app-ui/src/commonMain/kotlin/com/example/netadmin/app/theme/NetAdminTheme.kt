package com.example.netadmin.app.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun NetAdminTheme(content: @Composable () -> Unit) {
  // MVP: token wiring is a follow-up ticket; defaults align to design/tokens.json.
  val scheme = darkColorScheme(
    primary = Color(0xFF7C3AED),
    background = Color(0xFF0B0D10),
    surface = Color(0xFF11151B),
    onSurface = Color(0xFFE6EDF3),
    onBackground = Color(0xFFE6EDF3),
  )
  MaterialTheme(colorScheme = scheme, content = content)
}
