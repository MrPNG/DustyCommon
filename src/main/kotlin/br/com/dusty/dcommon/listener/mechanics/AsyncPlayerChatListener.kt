package br.com.dusty.dcommon.listener.mechanics

import br.com.dusty.dcommon.gamer.EnumChat.*
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.GamerRegistry
import br.com.dusty.dcommon.util.text.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

object AsyncPlayerChatListener: Listener {

	val STAFF_CHAT_PREFIX_POSITIVE = "[".basic() + "Staff Chat".positive() + "] ".basic()
	val STAFF_CHAT_PREFIX_NEGATIVE = "[".basic() + "Staff Chat".negative() + "] ".basic()

	val CLAN_CHAT_PREFIX_POSITIVE = "[".basic() + "Clan Chat".positive() + "] ".basic()
	val CLAN_CHAT_PREFIX_NEGATIVE = "[".basic() + "Clan Chat".negative() + "] ".basic()

	val RESTRICTED = Text.NEGATIVE_PREFIX + "O chat está ".basic() + "restrito".negative() + " apenas a ".basic() + "%s".negative() + " e acima!".basic()

	var rank = EnumRank.DEFAULT

	@EventHandler
	fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
		val player = event.player
		val gamer = GamerRegistry.gamer(player)

		when (gamer.chat) {
			NORMAL -> {
				if (gamer.rank.isLowerThan(rank)) {
					event.isCancelled = true

					player.sendMessage(RESTRICTED.format(rank.toString()))
				}

				event.format = "%s" + ":".basic() + " %s".styles(TextStyle.RESET)
			}
			CLAN   -> {
				event.recipients.clear()

				val messagePositive = CLAN_CHAT_PREFIX_POSITIVE + player.displayName + ": ".basic() + event.message.neutral()
				val messageNegative = CLAN_CHAT_PREFIX_NEGATIVE + player.displayName + ": ".basic() + event.message.negative()

				GamerRegistry.onlineGamers().filter { it.clan == gamer.clan }.forEach { it.player.sendMessage(if (it.chat == CLAN) messagePositive else messageNegative) }
			}
			STAFF  -> {
				event.recipients.clear()

				val messagePositive = STAFF_CHAT_PREFIX_POSITIVE + gamer.rank.format(player.name) + ": ".basic() + event.message.neutral()
				val messageNegative = STAFF_CHAT_PREFIX_NEGATIVE + gamer.rank.format(player.name) + ": ".basic() + event.message.negative()

				GamerRegistry.onlineGamers().filter { it.rank.isHigherThanOrEquals(EnumRank.MOD) }.forEach { it.player.sendMessage(if (it.chat == STAFF) messagePositive else messageNegative) }
			}
		}
	}
}
