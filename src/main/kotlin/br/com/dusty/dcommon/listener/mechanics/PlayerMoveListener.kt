package br.com.dusty.dcommon.listener.mechanics

import br.com.dusty.dcommon.Main
import br.com.dusty.dcommon.gamer.EnumMode
import br.com.dusty.dcommon.gamer.GamerRegistry
import br.com.dusty.dcommon.util.isWalk
import br.com.dusty.dcommon.util.world.Worlds
import com.sk89q.worldguard.protection.flags.DefaultFlag
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import kotlin.math.sign

object PlayerMoveListener: Listener {

	@EventHandler
	fun onPlayerMove(event: PlayerMoveEvent) {
		val player = event.player
		val gamer = GamerRegistry.gamer(player)

		val from = event.from
		val to = event.to

		if (gamer.mode != EnumMode.ADMIN) {
			when {
				gamer.isFrozen() && event.isWalk() -> event.to = Location(from.world, from.x, to.y, from.z, to.yaw, to.pitch)
				toInvincibleLocation(from, to)     -> {
					val velocity = player.velocity.clone()

					val signX = velocity.x.sign
					val signY = velocity.y.sign
					val signZ = velocity.z.sign

					velocity.x = -2.0 * (if (signX != 0.0) signX else if (Main.RANDOM.nextBoolean()) 1.0 else -1.0)
					velocity.y = -2.0 * signY
					velocity.z = -2.0 * (if (signZ != 0.0) signZ else if (Main.RANDOM.nextBoolean()) 1.0 else -1.0)

					player.velocity = velocity
				}
			}
		}
	}

	fun fromInvincibleLocation(from: Location,
	                           to: Location) = Worlds.REGION_MANAGER!!.getApplicableRegions(from).allows(DefaultFlag.INVINCIBILITY) && !Worlds.REGION_MANAGER!!.getApplicableRegions(to).allows(
			DefaultFlag.INVINCIBILITY)

	fun toInvincibleLocation(from: Location,
	                         to: Location) = !Worlds.REGION_MANAGER!!.getApplicableRegions(from).allows(DefaultFlag.INVINCIBILITY) && Worlds.REGION_MANAGER!!.getApplicableRegions(to).allows(
			DefaultFlag.INVINCIBILITY)
}
