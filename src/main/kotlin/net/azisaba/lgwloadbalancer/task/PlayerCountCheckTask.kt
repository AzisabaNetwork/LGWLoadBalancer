package net.azisaba.lgwloadbalancer.task

import com.google.common.io.ByteStreams
import net.azisaba.lgwloadbalancer.LGWLoadBalancer
import org.bukkit.scheduler.BukkitRunnable

class PlayerCountCheckTask(
    private val plugin: LGWLoadBalancer,
) : BukkitRunnable() {
    override fun run() {
        for (serverName in plugin.config.servers) {
            val dataOutput = ByteStreams.newDataOutput()
            dataOutput.writeUTF("PlayerCount")
            dataOutput.writeUTF(serverName)
            plugin.server.sendPluginMessage(plugin, LGWLoadBalancer.BUNGEE_CORD, dataOutput.toByteArray())
        }
    }
}
