package br.com.dusty.dcommon.command.override

import br.com.dusty.dcommon.command.CustomCommand
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.text.negative
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

object StopCommand: CustomCommand(EnumRank.ADMIN, "stop") {

	private val KICK_SHUTDOWN = "O servidor está reiniciando!\n\nVolte em alguns segundos...".negative()

	override fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
		if (testPermission(sender)) {
			val gamers = Gamers.gamers().toTypedArray()
//			val clans = Clans.onlineClans().toTypedArray()

			gamers.forEach { gamer -> gamer.player.kickPlayer(KICK_SHUTDOWN) }

//			WebAPI.saveProfiles(*gamers)
//			WebAPI.saveClans(*clans)

			Bukkit.shutdown()
		}

		return false
	}
}
