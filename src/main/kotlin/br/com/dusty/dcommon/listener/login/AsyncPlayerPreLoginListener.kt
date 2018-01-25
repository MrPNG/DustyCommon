package br.com.dusty.dcommon.listener.login

import br.com.dusty.dcommon.clan.Clans
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.web.WebAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import java.util.*

object AsyncPlayerPreLoginListener: Listener {

	private val KICK_NO_PROFILE = "Não foi possível carregar seu perfil!\n\nVolte em alguns instantes...".basic()

	@EventHandler
	fun onAsyncPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
		val uuid = event.uniqueId

		Gamers.UUID_BY_ADDRESS[event.address] = uuid

		if (uuid.toString()[14] == '4') {
			val primitiveGamer = Gamers.primitiveGamer(WebAPI.loadProfile(uuid), uuid)

			if (primitiveGamer == null) event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, KICK_NO_PROFILE)
			else {
				Gamers.PRIMITIVE_GAMER_BY_UUID[uuid] = primitiveGamer

				if (primitiveGamer.clan != "") {
					val clan = UUID.fromString(primitiveGamer.clan)

					if (clan !in Clans.PRIMITIVE_CLAN_BY_UUID) {
						val primitiveClan = Clans.primitiveClan(WebAPI.loadClan(clan))

						if (primitiveClan == null) event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, KICK_NO_PROFILE)
						else Clans.PRIMITIVE_CLAN_BY_UUID[clan] = primitiveClan
					}
				}
			}
		}
	}
}
