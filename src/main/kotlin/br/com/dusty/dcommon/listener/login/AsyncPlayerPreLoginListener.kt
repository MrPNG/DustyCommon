package br.com.dusty.dcommon.listener.login

import br.com.dusty.dcommon.clan.ClanRegistry
import br.com.dusty.dcommon.gamer.GamerRegistry
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

		val primitiveGamer = GamerRegistry.primitiveGamer(WebAPI.loadProfile(uuid), uuid)

		if (primitiveGamer == null) event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, KICK_NO_PROFILE)
		else {
			val clan = UUID.fromString(primitiveGamer.clan)

			if (primitiveGamer.clan != "" && clan !in ClanRegistry.PRIMITIVE_CLAN_BY_UUID) {
				val primitiveClan = ClanRegistry.primitiveClan(WebAPI.loadClan(clan))

				if (primitiveClan == null) event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, KICK_NO_PROFILE)
				else ClanRegistry.PRIMITIVE_CLAN_BY_UUID.put(clan, primitiveClan)
			}
		}
	}
}
