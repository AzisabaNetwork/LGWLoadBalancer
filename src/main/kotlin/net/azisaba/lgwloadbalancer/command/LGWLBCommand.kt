package net.azisaba.lgwloadbalancer.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import com.google.common.io.ByteStreams
import net.azisaba.lgwloadbalancer.LGWLoadBalancer
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("lgwlb")
class LGWLBCommand(
    private val plugin: LGWLoadBalancer,
) : BaseCommand() {
    @Default
    fun default(sender: CommandSender) {
        sender.sendMessage("fmm... this command is invalid.")
    }

    @Subcommand("go")
    @CommandPermission("lgwloadbalancer.cmd.lgwlb.go")
    fun go(player: Player) {
        for (serverName in plugin.config.servers) {
            if ((plugin.playerCountMap[serverName] ?: continue) < plugin.config.playerThreshold) {
                val dataOutput = ByteStreams.newDataOutput()
                dataOutput.writeUTF("Connect")
                dataOutput.writeUTF(serverName)
                player.sendPluginMessage(plugin, LGWLoadBalancer.BUNGEE_CORD, dataOutput.toByteArray())
                break
            }
        }
    }

    @Subcommand("stat")
    @CommandPermission("lgwloadbalancer.cmd.lgwlb.stat")
    fun stat(sender: CommandSender) {
        sender.sendMessage("[LGWLB] ==== Config & Stats ====")
        sender.sendMessage("[LGWLB] Player threshold: ${plugin.config.playerThreshold}")
        for (serverName in plugin.config.servers) {
            sender.sendMessage("[LGWLB] $serverName: ${plugin.playerCountMap[serverName] ?: -1}")
        }
    }

    @Subcommand("debugmode")
    @CommandPermission("lgwloadbalancer.cmd.lgwlb.debugmode")
    fun debugMode(sender: CommandSender) {
        plugin.debugMode = plugin.debugMode.not()
        sender.sendMessage(Component.text("Debug mode: ${plugin.debugMode}"))
    }

    @Subcommand("request")
    @CommandPermission("lgwloadbalancer.cmd.lgwlb.request")
    fun request(
        sender: CommandSender,
        cmd: String,
    ) {
        val l = cmd.split(";")
        val dataOutput = ByteStreams.newDataOutput()
        l.forEach { dataOutput.writeUTF(it) }
        plugin.server.sendPluginMessage(plugin, LGWLoadBalancer.BUNGEE_CORD, dataOutput.toByteArray())
    }
}
