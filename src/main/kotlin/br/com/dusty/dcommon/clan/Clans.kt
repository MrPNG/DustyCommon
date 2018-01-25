package br.com.dusty.dcommon.clan

import br.com.dusty.dcommon.Config
import br.com.dusty.dcommon.event.ClanInstantiateEvent
import org.bukkit.Bukkit
import java.util.*

object Clans {

	var clanClass: Class<out Clan> = SimpleClan::class.java
	var primitiveClanClass: Class<out PrimitiveClan> = SimplePrimitiveClan::class.java

	val CLAN_BY_UUID = linkedMapOf<UUID, Clan>()
	val PRIMITIVE_CLAN_BY_UUID = HashMap<UUID, PrimitiveClan>()

	fun clan(uuid: UUID): Clan {
		var clan = CLAN_BY_UUID[uuid]

		if (clan == null) {
			clan = clanClass.getDeclaredConstructor(PrimitiveClan::class.java).newInstance(PRIMITIVE_CLAN_BY_UUID[uuid])

			Bukkit.getPluginManager().callEvent(ClanInstantiateEvent(clan))

			CLAN_BY_UUID[uuid] = clan
		}

		return clan!!
	}

	fun clans() = CLAN_BY_UUID.values

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
