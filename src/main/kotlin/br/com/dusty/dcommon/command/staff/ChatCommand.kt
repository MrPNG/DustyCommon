package br.com.dusty.dcommon.command.staff

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.listener.mechanics.AsyncPlayerChatListener
import br.com.dusty.dcommon.util.text.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ChatCommand: PlayerCustomCommand(EnumRank.MODPLUS, "chat") {

	val USAGE = Text.NEGATIVE_PREFIX + "Uso: /chat ".basic() + "<clear>/<restrict>".negative() + " [rank]".basic()

	val NO_RANK_FOUND = Text.NEGATIVE_PREFIX + "Não".negative() + " há um rank com o nome \"".basic() + "%s".negative() + "\"!".basic()
	val CHAT_CLEARED = Text.NEUTRAL_PREFIX + "O ".basic() + "chat".neutral() + " foi ".basic() + "limpo".neutral() + "!".basic()
	val CHAT_ALL = Text.POSITIVE_PREFIX + "O chat agora está ".basic() + "liberado".positive() + " para ".basic() + "todos".positive() + " os jogadores!".basic()
	val CHAT_SOME = Text.NEGATIVE_PREFIX + "O chat agora está ".basic() + "restrito".negative() + " apenas a ".basic() + "%s".negative() + " e acima!".basic()
	val CHAT_CLEAR_MESSAGE = buildString { for (i in 1 .. 128) append(" \n") }

	val COMPLETIONS = arrayListOf("clear", "restrict")

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = Gamers.gamer(sender)

		if (args.isEmpty()) sender.sendMessage(USAGE)
		else when (args[0].toLowerCase()) {
			"clear"    -> {
				val rank: EnumRank = if (args.size > 1) EnumRank[args[1]] else gamer.rank

				if (rank == EnumRank.NONE) {
					sender.sendMessage(NO_RANK_FOUND.format(args[0]))
					return false
				}

				Gamers.gamers().forEach { if (it.rank.isLowerThanOrEquals(rank)) it.player.sendMessage(CHAT_CLEAR_MESSAGE) }

				sender.sendMessage(CHAT_CLEARED)
			}
			"restrict" -> {
				val rank: EnumRank = when {
					args.size > 1                                   -> EnumRank[args[1]]
					AsyncPlayerChatListener.rank == EnumRank.NORMAL -> gamer.rank
					else                                            -> EnumRank.NORMAL
				}

				if (rank == EnumRank.NONE) {
					sender.sendMessage(NO_RANK_FOUND.format(args[0]))
					return false
				}

				AsyncPlayerChatListener.rank = rank

				if (rank == EnumRank.NORMAL) Bukkit.broadcastMessage(CHAT_ALL)
				else Bukkit.broadcastMessage(CHAT_SOME.format(rank.toString()))
			}
		}

		return false
	}

	override fun tabComplete(sender: Player, alias: String, args: Array<String>) = when {
		args.size == 1 -> COMPLETIONS.filter { it.startsWith(args[0], true) }.toMutableList()
		args.size == 2 -> EnumRank.values().filter { it.name.startsWith(args[1], true) }.map { it.name.toLowerCase() }.toMutableList()
		else           -> arrayListOf()
	}
}