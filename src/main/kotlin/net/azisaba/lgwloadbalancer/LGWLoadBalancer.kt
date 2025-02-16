package net.azisaba.lgwloadbalancer

import co.aikar.commands.PaperCommandManager
import net.azisaba.lgwloadbalancer.command.RequestCommand
import net.azisaba.lgwloadbalancer.listener.BungeeMessageListener
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.ConcurrentHashMap

class LGWLoadBalancer : JavaPlugin() {
    lateinit var commandManager: PaperCommandManager

    override fun onEnable() {
        // Plugin startup logic
        server.messenger.registerOutgoingPluginChannel(this, BUNGEE_CORD)
        server.messenger.registerIncomingPluginChannel(this, BUNGEE_CORD, BungeeMessageListener(this, logger))

        commandManager = PaperCommandManager(this)
        RequestCommand(this).register(commandManager)
    }

    override fun onDisable() {
        // Plugin shutdown logic
        server.messenger.unregisterOutgoingPluginChannel(this)
        server.messenger.unregisterIncomingPluginChannel(this)
    }

    companion object {
        val playerCountMap = ConcurrentHashMap<String, Int>()
        val CHANNEL_ID = "lgwloadbalancer"
        val BUNGEE_CORD = "BungeeCord"
    }
}
