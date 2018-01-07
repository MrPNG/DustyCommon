package br.com.dusty.dcommon.gamer

import br.com.dusty.dcommon.Config
import org.bukkit.entity.Player
import java.util.*

object GamerRegistry {

	var gamerClass: Class<out Gamer> = SimpleGamer::class.java
	var primitiveGamerClass: Class<out PrimitiveGamer> = SimplePrimitiveGamer::class.java

	val GAMER_BY_UUID = hashMapOf<UUID, Gamer>()
	val PRIMITIVE_GAMER_BY_UUID = hashMapOf<UUID, PrimitiveGamer>()

	fun gamer(player: Player): Gamer {
		val uuid = player.uniqueId

		var gamer = GAMER_BY_UUID[uuid]

		if (gamer == null) {
			gamer = gamerClass.getDeclaredConstructor(Player::class.java, PrimitiveGamer::class.java).newInstance(player, PRIMITIVE_GAMER_BY_UUID[uuid])

			GAMER_BY_UUID.put(uuid, gamer)
		}

		return gamer!!
	}

	fun onlineGamers() = GAMER_BY_UUID.values

	fun emptyPrimitiveGamer(uuid: UUID) = primitiveGamerClass.newInstance().apply {
		this.uuid = uuid.toString()

		PRIMITIVE_GAMER_BY_UUID.put(uuid, this)
	}

	fun primitiveGamer(json: String?, uuid: UUID) = when (json) {
		null             -> null
		"{\"status\":2}" -> emptyPrimitiveGamer(uuid)
		else             -> Config.GSON.fromJson(json, PrimitiveGamer::class.java)
	}

	fun remove(uuid: UUID): Gamer? {
		PRIMITIVE_GAMER_BY_UUID.remove(uuid)

		return GAMER_BY_UUID.remove(uuid)
	}
}
