package br.com.dusty.dcommon.command.staff

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumMode
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.Gamers
import org.bukkit.entity.Player

object AdminCommand: PlayerCustomCommand(EnumRank.MOD, "admin") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = Gamers.gamer(sender)

		gamer.mode = if (gamer.mode == EnumMode.ADMIN) EnumMode.PLAY else EnumMode.ADMIN

		return false
	}
}
