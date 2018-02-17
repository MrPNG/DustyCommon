package br.com.dusty.dcommon.listener.mechanics

import br.com.dusty.dcommon.gamer.EnumMode
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.world.Worlds
import com.sk89q.worldguard.protection.flags.DefaultFlag
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

object EntityDamageByEntityListener: Listener {

	@EventHandler
	fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
		if (!event.isCancelled && event.entity is Player && event.damager is Player) {
			val player = event.entity as Player
			val damagerPlayer = event.damager as Player

			val gamer = Gamers.gamer(player)
			val damager = Gamers.gamer(damagerPlayer)

			if (damager.isInvincible() || damager.mode == EnumMode.SPECTATE || !player.canSee(damagerPlayer) || Worlds.REGION_MANAGER!!.getApplicableRegions(damagerPlayer.location).allows(DefaultFlag.INVINCIBILITY)) event.isCancelled = true
			else {
				if (gamer.combatTag < 10000L) gamer.combatTag = 10000L
				if (damager.combatTag < 10000L) damager.combatTag = 10000L

				gamer.combatPartner = damager
				damager.combatPartner = gamer
			}
		}
	}
}
