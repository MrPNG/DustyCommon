package br.com.dusty.dcommon.clan

import br.com.dusty.dcommon.Config
import java.util.*

object ClanRegistry {

	var clanClass: Class<out Clan> = SimpleClan::class.java
	var primitiveClanClass: Class<out PrimitiveClan> = SimplePrimitiveClan::class.java

	val CLAN_BY_UUID = linkedMapOf<UUID, Clan>()
	val PRIMITIVE_CLAN_BY_UUID = HashMap<UUID, PrimitiveClan>()

	fun clan(uuid: UUID): Clan {
		var clan = CLAN_BY_UUID[uuid]

		if (clan == null) {
			clan = clanClass.getDeclaredConstructor(PrimitiveClan::class.java).newInstance(PRIMITIVE_CLAN_BY_UUID[uuid])

			CLAN_BY_UUID.put(uuid, clan)
		}

		return clan!!
	}

	fun onlineClans() = CLAN_BY_UUID.values

	fun primitiveClan(json: String?) = when (json) {
		null             -> null
		"{\"status\":2}" -> null
		else             -> Config.GSON.fromJson(json, primitiveClanClass)
	}

	fun remove(uuid: UUID): Clan? {
		PRIMITIVE_CLAN_BY_UUID.remove(uuid)

		return CLAN_BY_UUID.remove(uuid)
	}
}
