package net.azisaba.lgwloadbalancer.listener

import com.google.common.io.ByteStreams
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.messaging.PluginMessageListener
import java.util.logging.Logger

class BungeeMessageListener(
    private val plugin: Plugin,
    val logger: Logger,
) : PluginMessageListener {
    override fun onPluginMessageReceived(
        channel: String,
        player: Player,
        message: ByteArray?,
    ) {
        if (channel != "BungeeCord") {
            return
        }

        val dataInput = ByteStreams.newDataInput((message ?: error("message was null!")))
        logger.info("Message received!")
        when (dataInput.readUTF()) {
            "PlayerCount" -> {
                val serverName = dataInput.readUTF()
                val playerCount = dataInput.readInt()
                plugin.server.broadcast(Component.text("PlayerCount on $serverName -> $playerCount"))
            }
        }
        plugin.server.broadcast(Component.text("Message Received! ${dataInput.readLine()}"))
    }
}
