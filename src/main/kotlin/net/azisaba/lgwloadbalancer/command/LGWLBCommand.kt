package net.azisaba.lgwloadbalancer.command

import com.google.common.io.ByteStreams
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.LiteralArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import net.azisaba.lgwloadbalancer.LGWLoadBalancer
import net.kyori.adventure.text.Component

class LGWLBCommand(
    private val plugin: LGWLoadBalancer,
) {
    val lgwLbGo =
        CommandAPICommand("go")
            .withPermission(PERMISSION_LGWLB_GO)
            .executesPlayer(
                PlayerCommandExecutor { player, _ ->
                    for (serverName in plugin.config.servers) {
                        if ((plugin.playerCountMap[serverName] ?: continue) < plugin.config.playerThreshold) {
                            val dataOutput = ByteStreams.newDataOutput()
                            dataOutput.writeUTF("Connect")
                            dataOutput.writeUTF(serverName)
                            player.sendPluginMessage(plugin, LGWLoadBalancer.BUNGEE_CORD, dataOutput.toByteArray())
                            break
                        }
                    }
                },
            )

    val lgwLbStat =
        CommandAPICommand("stat")
            .withPermission(PERMISSION_LGWLB_STAT)
            .executesPlayer(
                PlayerCommandExecutor { player, _ ->
                    player.sendMessage("[LGWLB] ==== Config & Stats ====")
                    player.sendMessage("[LGWLB] Player threshold: ${plugin.config.playerThreshold}")
                    for (serverName in plugin.config.servers) {
                        player.sendMessage("[LGWLB] $serverName: ${plugin.playerCountMap[serverName] ?: -1}")
                    }
                },
            )

    val lgwLbDebugMode =
        CommandAPICommand("debugmode")
            .withPermission(PERMISSION_LGWLB_DEBUGMODE)
            .executesPlayer(
                PlayerCommandExecutor { player, _ ->
                    plugin.debugMode = plugin.debugMode.not()
                    player.sendMessage(Component.text("Debug mode: ${plugin.debugMode}"))
                },
            )

    val lgwLbRequest =
        CommandAPICommand("request")
            .withPermission(PERMISSION_LGWLB_REQUEST)
            .withArguments(LiteralArgument("commands"))
            .executesPlayer(
                PlayerCommandExecutor { player, args ->
                    val commands = args.get("commands") as String
                    val dataOutput = ByteStreams.newDataOutput()
                    commands.split(";").forEach { dataOutput.writeUTF(it) }
                    plugin.server.sendPluginMessage(plugin, LGWLoadBalancer.BUNGEE_CORD, dataOutput.toByteArray())
                },
            )

    val lgwLb =
        CommandAPICommand("lgwlb")
            .withSubcommands(
                lgwLbGo,
                lgwLbStat,
                lgwLbDebugMode,
                lgwLbRequest,
            )

    init {
        lgwLb.register(plugin)
    }

    companion object {
        const val PERMISSION_PARENT = "lgwloadbalancer.cmd.lgwlb"
        val PERMISSION_LGWLB_GO = CommandPermission.fromString("$PERMISSION_PARENT.go")
        val PERMISSION_LGWLB_STAT = CommandPermission.fromString("$PERMISSION_PARENT.stat")
        val PERMISSION_LGWLB_DEBUGMODE = CommandPermission.fromString("$PERMISSION_PARENT.debugmode")
        val PERMISSION_LGWLB_REQUEST = CommandPermission.fromString("$PERMISSION_PARENT.request")
    }
}
