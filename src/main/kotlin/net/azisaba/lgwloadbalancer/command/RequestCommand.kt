package net.azisaba.lgwloadbalancer.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import com.google.common.io.ByteStreams
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

@CommandAlias("pkreq")
class RequestCommand(
    private val plugin: Plugin,
) : BaseCommand() {
    fun register(commandManager: PaperCommandManager) {
        commandManager.registerCommand(this)
    }

    @Default
    fun default(sender: CommandSender) {
        sender.sendMessage("Just send some packets.")
    }

    @Subcommand("playercount")
    fun getPlayerCount(
        player: Player,
        serverName: String,
    ) {
        player.sendMessage("Sending...")
        val dataOutput = ByteStreams.newDataOutput()
//        dataOutput.writeUTF("Subchannel")
//        dataOutput.writeUTF(LGWLoadBalancer.CHANNEL_ID)
        dataOutput.writeUTF("PlayerCount")
        dataOutput.writeUTF(serverName)
        player.sendPluginMessage(plugin, "BungeeCord", dataOutput.toByteArray())
        player.sendMessage("Sent!")
    }
}
