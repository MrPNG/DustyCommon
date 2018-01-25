package br.com.dusty.dcommon.gamer

import br.com.dusty.dcommon.Config
import br.com.dusty.dcommon.event.GamerInstantiateEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.net.InetAddress
import java.util.*

object Gamers {

	var gamerClass: Class<out Gamer> = SimpleGamer::class.java
	var primitiveGamerClass: Class<out PrimitiveGamer> = SimplePrimitiveGamer::class.java

	val UUID_BY_ADDRESS = hashMapOf<InetAddress, UUID>()
	val GAMER_BY_UUID = hashMapOf<UUID, Gamer>()
	val PRIMITIVE_GAMER_BY_UUID = hashMapOf<UUID, PrimitiveGamer>()

	fun gamer(player: Player): Gamer {
		val uuid = player.uniqueId

		var gamer = GAMER_BY_UUID[uuid]

		if (gamer == null) {
			gamer = gamerClass.getDeclaredConstructor(Player::class.java).newInstance(player)!!

			if (uuid in UUID_BY_ADDRESS.values) gamer = gamer.fromPrimitiveGamer(PRIMITIVE_GAMER_BY_UUID.remove(uuid))

			Bukkit.getPluginManager().callEvent(GamerInstantiateEvent(gamer))

			GAMER_BY_UUID[uuid] = gamer
		}

		return gamer
	}

	fun gamers() = GAMER_BY_UUID.values

	fun onlineGamers() = gamers().filter { it.isAuthenticated && it.mode != EnumMode.ADMIN }.size

	fun emptyPrimitiveGamer(uuid: UUID) = primitiveGamerClass.newInstance().apply { this.uuid = uuid.toString() }

	fun primitiveGamer(json: String?, uuid: UUID) = when (json) {
		null -> null
		"2"  -> emptyPrimitiveGamer(uuid)
		else -> Config.GSON.fromJson(json, primitiveGamerClass)
	}

	fun remove(uuid: UUID): Gamer? {
		PRIMITIVE_GAMER_BY_UUID.remove(uuid)

		return GAMER_BY_UUID.remove(uuid)
	}
}
