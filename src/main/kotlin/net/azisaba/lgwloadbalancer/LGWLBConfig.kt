package net.azisaba.lgwloadbalancer

import kotlinx.serialization.Serializable

@Serializable
data class LGWLBConfig(
    val version: Int = 1,
    val playerThreshold: Int = 10,
    val servers: List<String> = listOf("ServerA", "ServerB"),
)
