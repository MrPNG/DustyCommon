package br.com.dusty.dcommon.command.staff

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.GamerRegistry
import br.com.dusty.dcommon.util.Players
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.negative
import br.com.dusty.dcommon.util.text.neutral
import org.bukkit.entity.Player

object IpCheckCommand: PlayerCustomCommand(EnumRank.ADMIN, "ipcheck") {

	val USAGE = Text.NEGATIVE_PREFIX + "Uso: /ipcheck ".basic() + "<nomeDoJogador>".negative()

	val PLAYER_NOT_FOUND = Text.NEGATIVE_PREFIX + "Não".negative() + " há um jogador online com o nome ".basic() + "%s".negative() + "!".basic()
	val PLAYER_IP = Text.NEUTRAL_PREFIX + "O ".basic() + "ip".neutral() + " do jogador ".basic() + "%s".neutral() + " é ".basic() + "%s".basic()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.isEmpty()) sender.sendMessage(USAGE)
		else {
			val player = Players[args[0]]

			if (player == null) sender.sendMessage(PLAYER_NOT_FOUND.format(args[0]))
			else sender.sendMessage(PLAYER_IP.format(GamerRegistry.gamer(player).displayName, player.address.address.hostAddress))
		}

		return false
	}

	override fun tabComplete(sender: Player, alias: String, args: Array<String>) = GamerRegistry.onlineGamers().filter {
		sender.canSee(it.player) && (args.size > 1 || it.displayName.startsWith(args[0], true))
	}.map { it.displayName }.toMutableList()
}
