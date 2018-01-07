package br.com.dusty.dcommon.listener.mechanics

import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.GamerRegistry
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPickupItemEvent

object PlayerPickupItemListener: Listener {

	@EventHandler
	fun onEntityPickupItem(event: PlayerPickupItemEvent) {
		val player = event.player
		val gamer = GamerRegistry.gamer(player)

		if (gamer.visibleTo != EnumRank.DEFAULT) event.isCancelled = true
	}
}