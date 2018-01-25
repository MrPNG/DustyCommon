package br.com.dusty.dcommon.command.override

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.Players
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.negative
import org.bukkit.entity.Player

object TellCommand: PlayerCustomCommand(EnumRank.DEFAULT, "tell", "msg", "w", "r") {

	val USAGE_TELL = Text.NEGATIVE_PREFIX + "Uso: /%s ".basic() + "<jogador> <mensagem>".negative()
	val USAGE_RESPONSE = Text.NEGATIVE_PREFIX + "Uso: /r ".basic() + "<mensagem>".negative()

	val NO_PLAYER_FOUND = Text.NEGATIVE_PREFIX + "Não".negative() + " há um jogador online com o nome \"".basic() + "%s".negative() + "\"!".negative()
	val TELL_SENT = "[%s -> %s] %s".basic()
	val TELL_RECEIVED = "[%s <- %s] %s".basic()
	val SPY_TELL = "[".basic() + "Tell Spy".negative() + "] (%s -> %s) %s"
	val NOT_IN_A_CONVERSATION = Text.NEGATIVE_PREFIX + "Você ".basic() + "não".negative() + " está conversando com ".basic() + "ninguém".negative() + "!".basic()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = Gamers.gamer(sender)

		when (alias) {
			"tell", "msg", "w" -> {
				if (args.size < 2) {
					sender.sendMessage(USAGE_TELL.format(alias))
				} else {
					val player = Players[args[0]]

					if (player == null) {
						sender.sendMessage(NO_PLAYER_FOUND.format(args[0]))
					} else {
						val partner = Gamers.gamer(player)

						//TODO: Ignore implementation
						gamer.chatPartner = partner
						partner.chatPartner = gamer

						val message = args.copyOfRange(1, args.size).joinToString(" ")

						sender.sendMessage(TELL_SENT.format(gamer.displayName, partner.displayName, message))
						sender.sendMessage(TELL_RECEIVED.format(partner.displayName, gamer.displayName, message))

						val spyMessage = SPY_TELL.format(gamer.displayName, partner.displayName, message)

						gamer.chatSpies.forEach { it.sendMessage(spyMessage) }
					}
				}
			}
			"r"                -> {
				when {
					args.isEmpty()                                                    -> sender.sendMessage(USAGE_RESPONSE)
					gamer.chatPartner == null || !gamer.chatPartner!!.player.isOnline -> {
						gamer.chatPartner = null

						sender.sendMessage(NOT_IN_A_CONVERSATION)
					}
					else                                                              -> {
						val partner = gamer.chatPartner!!
						val player = partner.player

						val message = args.copyOfRange(0, args.size).joinToString(" ")

						//TODO: Ignore implementation
						sender.sendMessage(TELL_SENT.format(gamer.displayName, partner.displayName, message))
						sender.sendMessage(TELL_RECEIVED.format(partner.displayName, gamer.displayName, message))

						val spyMessage = SPY_TELL.format(gamer.displayName, partner.displayName, message)

						gamer.chatSpies.forEach { it.sendMessage(spyMessage) }
					}
				}
			}
		}

		return false
	}

	override fun tabComplete(sender: Player, alias: String, args: Array<String>) = Gamers.gamers().filter {
		sender.canSee(it.player) && (args.size > 1 || it.displayName.startsWith(args[0], true))
	}.map { it.displayName }.toMutableList()
}
