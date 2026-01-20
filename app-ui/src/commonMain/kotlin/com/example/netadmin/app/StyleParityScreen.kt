package com.example.netadmin.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyleParityScreen() {
  Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    Text("Style Parity", style = MaterialTheme.typography.titleLarge)
    Text("Validate components vs your CSS tokens.", style = MaterialTheme.typography.bodyMedium)

    ElevatedCard(Modifier.fillMaxWidth()) {
      Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          Button(onClick = {}) { Text("Primary") }
          OutlinedButton(onClick = {}) { Text("Outlined") }
          TextButton(onClick = {}) { Text("Text") }
          Button(onClick = {}, enabled = false) { Text("Disabled") }
        }

        var text by remember { mutableStateOf("") }
        OutlinedTextField(
          value = text,
          onValueChange = { text = it },
          label = { Text("Input") },
          modifier = Modifier.fillMaxWidth()
        )

        var expanded by remember { mutableStateOf(false) }
        var selected by remember { mutableStateOf("Unknown") }

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
          OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text("Dropdown") },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
          )
          ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listOf("Trusted", "Unknown", "Suspicious", "Blocked").forEach {
              DropdownMenuItem(text = { Text(it) }, onClick = {
                selected = it
                expanded = false
              })
            }
          }
        }

        LinearProgressIndicator(Modifier.fillMaxWidth())
      }
    }
  }
}
