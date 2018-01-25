package br.com.dusty.dcommon.clan

import br.com.dusty.dcommon.gamer.Gamer
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.stdlib.copyAndAdd
import br.com.dusty.dcommon.util.stdlib.copyAndRemove
import org.bukkit.Bukkit
import java.util.*

open class SimpleClan(fromPrimitiveClan: PrimitiveClan): Clan {

	override var primitiveClan = fromPrimitiveClan

	override var uuid = UUID.fromString(primitiveClan.uuid)

	override var name
		get() = primitiveClan.name
		set(value) {
			primitiveClan.name = value
		}

	override var tag
		get() = primitiveClan.tag
		set(value) {
			primitiveClan.tag = value
		}

	override var leader = Bukkit.getPlayer(UUID.fromString(primitiveClan.leader))?.run { Gamers.gamer(this) }
		set(value) {
			field = leader

			primitiveClan.leader = if (value == null) "" else value.player.uniqueId.toString()
		}

	override var onlineMembers = primitiveClan.members.filter { it != "" }.mapNotNull { Bukkit.getPlayer(UUID.fromString(it))?.run { Gamers.gamer(this) } }.toMutableList() as MutableCollection<Gamer>

	override fun rawMembers() = primitiveClan.members

	override fun add(gamer: Gamer) {
		onlineMembers.add(gamer)

		primitiveClan.members = primitiveClan.members.copyAndAdd(gamer.player.uniqueId.toString()).first
	}

	override fun add(uuid: UUID) {
		primitiveClan.members = primitiveClan.members.copyAndAdd(uuid.toString()).first
	}

	override fun remove(gamer: Gamer) {
		onlineMembers.remove(gamer)

		primitiveClan.members = primitiveClan.members.copyAndRemove(gamer.player.uniqueId.toString()).first
	}

	override fun remove(uuid: UUID) {
		primitiveClan.members = primitiveClan.members.copyAndRemove(uuid.toString()).first
	}

	override fun equals(other: Any?) = when {
		this === other                                   -> true
		javaClass != other?.javaClass                    -> false
		uuid.toString() != (other as PrimitiveClan).uuid -> false
		else                                             -> true
	}

	override fun hashCode() = uuid.hashCode()

	override fun toString(): String {
		return "Clan(uuid='$uuid', leader=$leader, onlineMembers=$onlineMembers)"
	}
}
