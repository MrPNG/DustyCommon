package br.com.dusty.dcommon.command.staff

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumMode
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.Players
import br.com.dusty.dcommon.util.openInventory
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.negative
import org.bukkit.entity.Player

object InvSeeCommand: PlayerCustomCommand(EnumRank.MOD, "invsee") {

	val USAGE = Text.NEGATIVE_PREFIX + "Uso: /invsee ".basic() + "<nomeDoJogador>".negative()

	val NOT_ON_ADMIN_MODE = Text.NEGATIVE_PREFIX + "Você ".basic() + "não".negative() + " está no modo ".basic() + "ADMIN".negative() + "!".basic()
	val PLAYER_NOT_FOUND = Text.NEGATIVE_PREFIX + "Não".negative() + " há um jogador online com o nome ".basic() + "%s".negative() + "!".basic()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (Gamers.gamer(sender).mode != EnumMode.ADMIN) sender.sendMessage(NOT_ON_ADMIN_MODE)
		else when (args.size) {
			0    -> {
				sender.sendMessage(USAGE)
			}
			else -> {
				val player = Players[args[0]]

				if (player == null) sender.sendMessage(PLAYER_NOT_FOUND.format(args[0]))
				else sender.openInventory(player)
			}
		}

		return false
	}

	override fun tabComplete(sender: Player, alias: String, args: Array<String>) = Gamers.gamers().filter {
		sender.canSee(it.player) && (args.size > 1 || it.displayName.startsWith(args[0], true))
	}.map { it.displayName }.toMutableList()
}