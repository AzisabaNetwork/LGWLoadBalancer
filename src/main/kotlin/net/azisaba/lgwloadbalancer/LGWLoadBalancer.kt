package net.azisaba.lgwloadbalancer

import com.charleskorn.kaml.Yaml
import net.azisaba.lgwloadbalancer.command.LGWLBCommand
import net.azisaba.lgwloadbalancer.listener.BungeeMessageListener
import net.azisaba.lgwloadbalancer.task.PlayerCountCheckTask
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.concurrent.ConcurrentHashMap

class LGWLoadBalancer : JavaPlugin() {
    var debugMode = false
    val playerCountMap = ConcurrentHashMap<String, Int>()

    lateinit var config: LGWLBConfig
    lateinit var lgwlbCommand: LGWLBCommand
    lateinit var checkTask: PlayerCountCheckTask
    private var initialized = false

    override fun onEnable() {
        dataFolder.mkdirs()

        // if a config file wasn't found
        if (!getConfigFile().exists()) {
            saveDefaultConfig()
            logger.info("Wrote new config file. Please edit it.")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        // get fresh config
        loadConfig()

        server.messenger.registerOutgoingPluginChannel(this, BUNGEE_CORD)
        server.messenger.registerIncomingPluginChannel(this, BUNGEE_CORD, BungeeMessageListener(this))

        lgwlbCommand = LGWLBCommand(this)

        checkTask = PlayerCountCheckTask(this)
        checkTask.runTaskTimerAsynchronously(this, 10, 20 * 5)
        initialized = true
    }

    override fun onDisable() {
        // Plugin shutdown logic
        if (initialized) {
            server.messenger.unregisterOutgoingPluginChannel(this)
            server.messenger.unregisterIncomingPluginChannel(this)
            checkTask.cancel()
        }
        initialized = false
    }

    override fun saveDefaultConfig() {
        saveConfig(LGWLBConfig())
    }

    override fun saveConfig() {
        saveConfig(config)
    }

    private fun saveConfig(config: LGWLBConfig) {
        getConfigFile().writeText(
            Yaml.default.encodeToString(LGWLBConfig.serializer(), config),
        )
    }

    fun updateConfig() {
        reloadConfig()
    }

    private fun loadConfig() {
        config = Yaml.default.decodeFromString(LGWLBConfig.serializer(), getConfigFile().readText())
    }

    companion object {
        val BUNGEE_CORD = "BungeeCord"
    }

    private fun getConfigFile(): File = File(dataFolder, "config.yml")
}
