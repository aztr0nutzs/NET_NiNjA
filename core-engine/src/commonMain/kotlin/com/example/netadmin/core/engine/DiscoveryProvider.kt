package com.example.netadmin.core.engine

import com.example.netadmin.core.model.Network
import com.example.netadmin.core.model.Sighting

interface DiscoveryProvider {
  val name: String
  suspend fun discover(network: Network): List<Sighting>
}
