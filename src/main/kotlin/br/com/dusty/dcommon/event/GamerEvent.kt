package br.com.dusty.dcommon.event

import br.com.dusty.dcommon.gamer.Gamer
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

open class GamerEvent(val gamer: Gamer): PlayerEvent(gamer.player) {

	private val handlers = HandlerList()

	override fun getHandlers(): HandlerList {
		return handlers
	}

	fun getHandlerList(): HandlerList {
		return handlers
	}
}