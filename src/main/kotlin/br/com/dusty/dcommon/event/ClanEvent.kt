package br.com.dusty.dcommon.event

import br.com.dusty.dcommon.clan.Clan
import br.com.dusty.dcommon.gamer.Gamer
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

open class ClanEvent(val clan: Clan): Event() {

	private val handlers = HandlerList()

	override fun getHandlers(): HandlerList {
		return handlers
	}

	fun getHandlerList(): HandlerList {
		return handlers
	}
}