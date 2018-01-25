package br.com.dusty.dcommon.scoreboard

import br.com.dusty.dcommon.gamer.Gamer
import br.com.dusty.dcommon.util.protocol.EnumProtocolVersion
import org.bukkit.Bukkit
import org.bukkit.scoreboard.DisplaySlot

class SimpleScoreboard: Scoreboard {

	override var labels = arrayOf<String>()

	override var labelsOld = arrayOf<String>()

	override fun create(gamer: Gamer) {
		val player = gamer.player

		val scoreboard = Bukkit.getScoreboardManager().newScoreboard
		player.scoreboard = scoreboard

		val objective = scoreboard.registerNewObjective(player.name, "dummy")
		objective.displayName = player.displayName
		objective.displaySlot = DisplaySlot.SIDEBAR
	}

	override fun update(gamer: Gamer) {
		val player = gamer.player

		clear(gamer)

		val objective = player.scoreboard?.getObjective(player.name) ?: return

		val labels = if (gamer.protocolVersion.isGreaterThanOrEquals(EnumProtocolVersion.RELEASE_1_8)) labels else labelsOld

		val values = arrayOf<String>()

		labels.indices.forEach { objective.getScore(labels[it] + values[it])?.run { score = labels.size - it } }
	}

	override fun update(gamers: Collection<Gamer>) {
		gamers.forEach({ update(it) })
	}

	override fun clear(gamer: Gamer) {
		val scoreboard = gamer.player.scoreboard ?: return

		scoreboard.entries.forEach({ scoreboard.resetScores(it) })
	}
}
