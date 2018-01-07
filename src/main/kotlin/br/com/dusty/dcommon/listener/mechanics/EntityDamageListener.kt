package br.com.dusty.dcommon.listener.mechanics

import br.com.dusty.dcommon.gamer.GamerRegistry
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

object EntityDamageListener: Listener {

	@EventHandler
	fun onEntityDamage(event: EntityDamageEvent) {
		if (event.entity is Player) {
			val player = event.entity as Player
			val gamer = GamerRegistry.gamer(player)

			if (gamer.isInvincible()) event.isCancelled = true

			if (event.cause == EntityDamageEvent.DamageCause.FALL && gamer.isNoFall()) {
				gamer.removeNoFall()

				event.isCancelled = true
			}
		}
	}
}
