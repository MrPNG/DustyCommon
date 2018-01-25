package br.com.dusty.dcommon.listener.login

import br.com.dusty.dcommon.util.web.MojangAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import protocolsupport.api.events.PlayerLoginStartEvent

object PlayerLoginStartListener: Listener {

	@EventHandler
	fun onPlayerPlayerLoginStart(event: PlayerLoginStartEvent) {
		if (MojangAPI.profile(event.name) != null) event.isOnlineMode = true
	}
}
