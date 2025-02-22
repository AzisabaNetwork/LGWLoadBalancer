package net.azisaba.lgwloadbalancer.listener

import com.google.common.io.ByteStreams
import net.azisaba.lgwloadbalancer.LGWLoadBalancer
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener

class BungeeMessageListener(
    private val plugin: LGWLoadBalancer,
) : PluginMessageListener {
    override fun onPluginMessageReceived(
        channel: String,
        player: Player,
        message: ByteArray?,
    ) {
        if (channel != LGWLoadBalancer.BUNGEE_CORD) {
            return
        }
        if (message == null) {
            error("message was null!")
        }

        if (plugin.debugMode) {
            plugin.logger.info("Request raw string: ${String(message)}")
        }
        val dataInput = ByteStreams.newDataInput(message)
        val dataType = dataInput.readUTF()
//        plugin.logger.info("We got event! dataType: $dataType")
        when (dataType) {
            "PlayerCount" -> {
                val serverName = dataInput.readUTF()
                val playerCount = dataInput.readInt()
                plugin.playerCountMap[serverName] = playerCount
                if (plugin.debugMode) {
                    plugin.logger.info("Updated count! server: $serverName, count: $playerCount")
                }
            }
        }
    }
}
