package br.com.dusty.dcommon.gamer

import br.com.dusty.dcommon.store.EnumAdvantage
import br.com.dusty.dcommon.util.protocol.EnumProtocolVersion
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

interface Gamer {

	val player: Player

	var displayName: String

	var protocolVersion: EnumProtocolVersion

	val advantages: ArrayList<EnumAdvantage>

	var rank: EnumRank

	var tag: EnumRank

	var visibleTo: EnumRank

	var mode: EnumMode

	var chat: EnumChat

	var invincibility: Long

	var noFall: Long

	var freeze: Long

	var chatPartner: Gamer?

	var chatSpies: ArrayList<Player>

	var combatTask: BukkitTask?

	var combatPartner: Gamer?

	var combatTag: Long

	fun hasAdvantage(advantage: EnumAdvantage): Boolean

	fun shouldSee(gamer: Gamer): Boolean

	fun hidePlayers()

	fun hideFromPlayers()

	fun isInvincible(): Boolean

	fun removeInvincibility()

	fun isNoFall(): Boolean

	fun removeNoFall()

	fun isFrozen(): Boolean

	fun removeFreeze()

	fun isCombatTagged(): Boolean

	fun removeCombatTag(announce: Boolean)

	fun canInteract(): Boolean

	fun canInteract(gamer: Gamer): Boolean

	fun kill(gamer: Gamer)

	fun clear()

	fun fly(fly: Boolean)

	companion object {

		var clazz: Class<out Gamer> = SimpleGamer::class.java
	}
}