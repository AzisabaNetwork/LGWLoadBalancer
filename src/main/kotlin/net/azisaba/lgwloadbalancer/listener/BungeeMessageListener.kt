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

        val dataInput = ByteStreams.newDataInput((message ?: error("message was null!")))
//        plugin.logger.info("Message received!")
        when (dataInput.readUTF()) {
            "PlayerCount" -> {
                val serverName = dataInput.readUTF()
                val playerCount = dataInput.readInt()
                plugin.playerCountMap[serverName] = playerCount
                plugin.logger.info("PlayerCount on $serverName -> $playerCount")
            }
        }
//        plugin.server.broadcast(Component.text("Message Received! ${dataInput.readLine()}"))
    }
}
