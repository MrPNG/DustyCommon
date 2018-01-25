package br.com.dusty.dcommon.command.staff

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumMode
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.negative
import org.bukkit.entity.Player

object VisInvisCommand: PlayerCustomCommand(EnumRank.MOD, "vis", "invis") {

	val NOT_ON_ADMIN_MODE = Text.NEGATIVE_PREFIX + "Você ".basic() + "não".negative() + " está no modo ".basic() + "ADMIN".negative() + "!".basic()
	val NO_RANK_FOUND = Text.NEGATIVE_PREFIX + "Não".negative() + " há um rank com o nome \"".basic() + "%s".negative() + "\"!".basic()
	val INVISIBILITY_LIMIT = Text.NEGATIVE_PREFIX + "Você ".basic() + "somente".negative() + " pode ficar ".basic() + "invisível".negative() + " para jogadores que tenham um rank ".basic() + "menor".negative() + " do que o seu!".basic()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = Gamers.gamer(sender)

		if (gamer.mode != EnumMode.ADMIN) sender.sendMessage(NOT_ON_ADMIN_MODE)
		else when (alias) {
			"vis"   -> when (args.size) {
				0 -> gamer.visibleTo = EnumRank.DEFAULT
				1 -> {
					val rank = EnumRank[args[0]]

					when {
						rank == EnumRank.NONE         -> sender.sendMessage(NO_RANK_FOUND.format(args[0]))
						rank.isHigherThan(gamer.rank) -> sender.sendMessage(INVISIBILITY_LIMIT)
						else                          -> gamer.visibleTo = rank
					}
				}
			}
			"invis" -> when (args.size) {
				0 -> gamer.visibleTo = gamer.rank
				1 -> {
					val rank = EnumRank[args[0]]

					when {
						rank == EnumRank.NONE                 -> sender.sendMessage(NO_RANK_FOUND.format(args[0]))
						rank.isHigherThanOrEquals(gamer.rank) -> sender.sendMessage(INVISIBILITY_LIMIT)
						else                                  -> gamer.visibleTo = if (rank.hasNext()) rank.next() else EnumRank.ADMIN
					}
				}
			}
		}

		return false
	}

	override fun tabComplete(sender: Player, alias: String, args: Array<String>) = EnumRank.values().filter {
		it.isLowerThanOrEquals(Gamers.gamer(sender).rank) && it.name.startsWith(args[0], true)
	}.map { it.name.toLowerCase() }.toMutableList()
}