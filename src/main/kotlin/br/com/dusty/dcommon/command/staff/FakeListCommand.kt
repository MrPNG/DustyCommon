package br.com.dusty.dcommon.command.staff

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.neutral
import org.bukkit.entity.Player

object FakeListCommand: PlayerCustomCommand(EnumRank.MOD, "fakelist") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = Gamers.gamer(sender)

		var message = Text.NEUTRAL_PREFIX + "Jogadores usando nicks ".basic() + "falsos".neutral() + ":".basic()

		Gamers.gamers().forEach {
			if (gamer.rank.isHigherThanOrEquals(it.rank) && it.displayName != it.player.name) message += "\n  - ".basic() + it.player.name.neutral() + " » ".basic() + it.displayName.neutral()
		}

		sender.sendMessage(message)

		return false
	}
}
