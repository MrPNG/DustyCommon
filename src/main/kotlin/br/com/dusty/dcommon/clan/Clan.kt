package br.com.dusty.dcommon.clan

import br.com.dusty.dcommon.gamer.Gamer
import java.util.*

interface Clan {

	var primitiveClan: PrimitiveClan

	var uuid: UUID

	var name: String

	var tag: String

	var leader: Gamer?

	var onlineMembers: MutableCollection<Gamer>

	fun rawMembers(): Array<String>

	fun add(gamer: Gamer)

	fun add(uuid: UUID)

	fun remove(gamer: Gamer)

	fun remove(uuid: UUID)
}