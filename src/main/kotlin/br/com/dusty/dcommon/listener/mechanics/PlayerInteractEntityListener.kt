package br.com.dusty.dcommon.listener.mechanics

import br.com.dusty.dcommon.gamer.EnumMode
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.openInventory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent

object PlayerInteractEntityListener: Listener {

	@EventHandler
	fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
		if (event.rightClicked is Player) {
			val rightClicked = event.rightClicked as Player

			val player = event.player
			val gamer = Gamers.gamer(player)

			if (gamer.mode == EnumMode.ADMIN) player.openInventory(rightClicked)
		}
	}
}
