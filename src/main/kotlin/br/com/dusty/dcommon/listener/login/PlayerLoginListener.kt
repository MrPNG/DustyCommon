package br.com.dusty.dcommon.listener.login

import br.com.dusty.dcommon.Config
import br.com.dusty.dcommon.gamer.EnumMode
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.store.EnumAdvantage
import br.com.dusty.dcommon.util.protocol.Protocols
import br.com.dusty.dcommon.util.text.TextColor
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.color
import br.com.dusty.dcommon.util.text.negative
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent

object PlayerLoginListener: Listener {

	private val KICK_NOT_READY = "O servidor ainda não está aberto!\n\nVolte em alguns instantes...".negative()
	private val KICK_FULL_MESSAGE = "O servidor está cheio!\n\n".negative() + "Compre ".basic() + "PRO".color(TextColor.GOLD) + " ou um ".basic() + "Slot Reservado".color(TextColor.GOLD) + " no site ".basic() + "loja.dusty.com.br".color(
			TextColor.GOLD) + " e entre agora!".basic()

	@EventHandler
	fun onPlayerLogin(event: PlayerLoginEvent) {
		val uuid = Gamers.UUID_BY_ADDRESS.remove(event.address)

		if (uuid == null) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, KICK_NOT_READY)

			return
		}

		when {
			uuid.toString()[14] == '4'                        -> {
				val player = event.player

				Protocols.changeUuid(player, uuid)

				val primitiveGamer = Gamers.PRIMITIVE_GAMER_BY_UUID[player.uniqueId]

				if (primitiveGamer == null) {
					event.disallow(PlayerLoginEvent.Result.KICK_OTHER, KICK_NOT_READY)

					return
				}

				val gamer = Gamers.gamer(player).apply {
					isPremium = true
					isAuthenticated = true
				}

				if (Config.data.serverStatus != Config.EnumServerStatus.ONLINE && gamer.rank.isLowerThan(EnumRank.MOD)) {
					event.disallow(PlayerLoginEvent.Result.KICK_OTHER, KICK_NOT_READY)

					Gamers.remove(player.uniqueId)

					return
				}

				if (event.result == PlayerLoginEvent.Result.KICK_FULL) {
					if (gamer.rank.isLowerThan(EnumRank.MOD) && !gamer.hasAdvantage(EnumAdvantage.SLOT) && Gamers.gamers().filterNot { it.mode == EnumMode.ADMIN }.size >= Config.data.slots) {
						event.disallow(PlayerLoginEvent.Result.KICK_FULL, KICK_FULL_MESSAGE)

						Gamers.remove(player.uniqueId)

						return
					} else event.allow()
				}
			}
			event.result == PlayerLoginEvent.Result.KICK_FULL -> event.allow()
		}
	}
}
