package br.com.dusty.dcommon.command.staff

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.GamerRegistry
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.negative
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ProtocolCommand: PlayerCustomCommand(EnumRank.ADMIN, "protocols") {

	val PLAYER_NOT_FOUND = Text.NEGATIVE_PREFIX + "Não".negative() + " há um jogador online com o nome ".basic() + "%s".negative() + "!".basic()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.isEmpty()) {
			val message = StringBuilder(Text.NEUTRAL_PREFIX)

			for ((index, it) in GamerRegistry.onlineGamers().sortedBy { it.protocolVersion }.withIndex()) {
				val protocolVersion = it.protocolVersion

				message.append((if (index == 0) "Protocols: " else ", ").basic() + it.player.name + " (" + (if (protocolVersion.min == protocolVersion.max) protocolVersion.min else (protocolVersion.min.toString() + "-" + protocolVersion.max)) + ": " + protocolVersion.toString() + ")".basic())
			}

			sender.sendMessage(message.toString())
		} else {
			val player = Bukkit.getPlayerExact(args[0])

			if (player == null) sender.sendMessage(PLAYER_NOT_FOUND.format(args[0]))
			else {
				val protocolVersion = GamerRegistry.gamer(player).protocolVersion

				sender.sendMessage(Text.NEUTRAL_PREFIX + "Protocol: ".basic() + player.name + " (" + (if (protocolVersion.min == protocolVersion.max) protocolVersion.min else (protocolVersion.min.toString() + "-" + protocolVersion.max)) + ": " + protocolVersion.toString() + ")".basic())
			}
		}

		return false
	}

	override fun tabComplete(sender: Player, alias: String, args: Array<String>) = Bukkit.getOnlinePlayers().filter {
		sender.canSee(it) && it.name.startsWith(args[0], true)
	}.map { it.name }.toMutableList()
}
