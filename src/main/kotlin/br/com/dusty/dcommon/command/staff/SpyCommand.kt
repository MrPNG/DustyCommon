package br.com.dusty.dcommon.command.staff

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.GamerRegistry
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.negative
import br.com.dusty.dcommon.util.text.positive
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object SpyCommand: PlayerCustomCommand(EnumRank.ADMIN, "spy") {

	val USAGE = Text.NEGATIVE_PREFIX + "Uso: /spy ".basic() + "<jogador>".negative()

	val PLAYER_NOT_FOUND = Text.NEGATIVE_PREFIX + "Não".negative() + " há um jogador online com o nome ".basic() + "%s".negative() + "!".basic()
	val SPYING = Text.POSITIVE_PREFIX + "Agora você está ".basic() + "espionando".positive() + " as conversas privadas do jogador ".basic() + "%s".positive() + "!".basic()
	val NOT_SPYING = Text.NEGATIVE_PREFIX + "Agora você ".basic() + "não".negative() + " está mais ".basic() + "espionando".negative() + " as conversas privadas do jogador ".basic() + "%s".negative() + "!".basic()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.isEmpty()) sender.sendMessage(USAGE)
		else {
			val player = Bukkit.getPlayerExact(args[0])

			if (player == null) sender.sendMessage(PLAYER_NOT_FOUND.format(args[0]))
			else {
				val gamer = GamerRegistry.gamer(player)

				if (gamer.chatSpies.contains(sender)) {
					sender.sendMessage(NOT_SPYING.format(player.name))

					gamer.chatSpies.remove(sender)
				} else {
					sender.sendMessage(SPYING.format(player.name))

					gamer.chatSpies.add(sender)
				}
			}
		}

		return false
	}

	override fun tabComplete(sender: Player, alias: String, args: Array<String>) = Bukkit.getOnlinePlayers().filter {
		sender.canSee(it) && it.name.startsWith(args[0], true)
	}.map { it.name }.toMutableList()
}
