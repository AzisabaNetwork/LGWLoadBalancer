package net.azisaba.lgwloadbalancer.task

import com.google.common.io.ByteStreams
import net.azisaba.lgwloadbalancer.LGWLoadBalancer
import org.bukkit.scheduler.BukkitRunnable

class PlayerCountCheckTask(
    private val plugin: LGWLoadBalancer,
) : BukkitRunnable() {
    override fun run() {
        if (plugin.debugMode) plugin.logger.info("Requesting player count data...")
        for (serverName in plugin.config.servers) {
            val dataOutput = ByteStreams.newDataOutput()
            dataOutput.writeUTF("PlayerCount")
            dataOutput.writeUTF(serverName)
            plugin.server.sendPluginMessage(plugin, LGWLoadBalancer.BUNGEE_CORD, dataOutput.toByteArray())
            if (plugin.debugMode) {
                plugin.logger.info("Requested to $serverName")
            }
        }
        if (plugin.debugMode) plugin.logger.info("Player count data requested!")
    }
}
