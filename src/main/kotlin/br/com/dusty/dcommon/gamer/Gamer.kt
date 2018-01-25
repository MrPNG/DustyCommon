package br.com.dusty.dcommon.gamer

import br.com.dusty.dcommon.clan.Clan
import br.com.dusty.dcommon.store.EnumAdvantage
import br.com.dusty.dcommon.util.protocol.EnumProtocolVersion
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

interface Gamer {

	var player: Player

	var protocolVersion: EnumProtocolVersion

	var isPremium: Boolean

	var isAuthenticated: Boolean

	val advantages: ArrayList<EnumAdvantage>

	var displayName: String

	var rank: EnumRank

	var tag: EnumRank

	var visibleTo: EnumRank

	var mode: EnumMode

	var clan: Clan?

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

	fun refreshTag()

	fun updateNameAboveHead(otherGamers: Collection<Gamer>)

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

	fun kill(killed: Gamer)

	fun clear()

	fun fly(fly: Boolean)

	fun fromPrimitiveGamer(primitiveGamer: PrimitiveGamer?): Gamer

	fun toPrimitiveGamer(): PrimitiveGamer
}