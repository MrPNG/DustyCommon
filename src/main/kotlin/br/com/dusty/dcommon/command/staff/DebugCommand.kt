package br.com.dusty.dcommon.command.staff

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumRank
import org.bukkit.entity.Player

object DebugCommand: PlayerCustomCommand(EnumRank.ADMIN, "debug") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {

		return false
	}
}
