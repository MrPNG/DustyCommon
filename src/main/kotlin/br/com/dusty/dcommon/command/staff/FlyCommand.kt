package br.com.dusty.dcommon.command.staff

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumMode
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.GamerRegistry
import br.com.dusty.dcommon.util.Players
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.negative
import br.com.dusty.dcommon.util.text.positive
import org.bukkit.entity.Player

object FlyCommand: PlayerCustomCommand(EnumRank.MODPLUS, "fly") {

	val NOT_ON_ADMIN_MODE = Text.NEGATIVE_PREFIX + "Você ".basic() + "não".negative() + " está no modo ".basic() + "ADMIN".negative() + "!".basic()
	val YOU_ARE_FLYING = Text.POSITIVE_PREFIX + "Agora você ".basic() + "está".positive() + " voando!".basic()
	val YOU_ARE_NOT_FLYING = Text.NEGATIVE_PREFIX + "Agora você ".basic() + "não".negative() + " está mais voando!".basic()
	val NO_PLAYER_FOUND = Text.NEGATIVE_PREFIX + "Não".negative() + " há um jogador online com o nome \"".basic() + "%s".negative() + "\"!".basic()
	val THEY_ARE_FLYING = Text.POSITIVE_PREFIX + "Agora o jogador ".basic() + "%s".positive() + " está voando!".basic()
	val THEY_ARE_NOT_FLYING = Text.NEGATIVE_PREFIX + "Agora o jogador ".basic() + "%s".negative() + " não está mais voando!".basic()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = GamerRegistry.gamer(sender)

		when {
			gamer.mode != EnumMode.ADMIN -> sender.sendMessage(NOT_ON_ADMIN_MODE)
			args.isEmpty()               -> {
				val fly = !sender.allowFlight

				gamer.fly(fly)

				if (fly) sender.sendMessage(YOU_ARE_FLYING)
				else sender.sendMessage(YOU_ARE_NOT_FLYING)
			}
			else                         -> {
				val player = Players[args[0]]

				if (player == null) {
					sender.sendMessage(NO_PLAYER_FOUND.format(args[0]))
				} else {
					val fly = !player.allowFlight

					val otherGamer = GamerRegistry.gamer(player)

					otherGamer.fly(fly)

					if (fly) sender.sendMessage(THEY_ARE_FLYING.format(otherGamer.displayName))
					else sender.sendMessage(THEY_ARE_NOT_FLYING.format(otherGamer.displayName))
				}
			}
		}

		return false
	}

	override fun tabComplete(sender: Player, alias: String, args: Array<String>) = GamerRegistry.onlineGamers().filter {
		sender.canSee(it.player) && (args.size > 1 || it.displayName.startsWith(args[0], true))
	}.map { it.displayName }.toMutableList()
}
