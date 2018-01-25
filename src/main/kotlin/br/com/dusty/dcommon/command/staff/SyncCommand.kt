package br.com.dusty.dcommon.command.staff

import br.com.dusty.dcommon.command.PlayerCustomCommand
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.Tasks
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.negative
import br.com.dusty.dcommon.util.text.positive
import br.com.dusty.dcommon.util.web.WebAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object SyncCommand: PlayerCustomCommand(EnumRank.ADMIN, "sync") {

	val PLAYER_NOT_FOUND = Text.NEGATIVE_PREFIX + "Não".negative() + " há um jogador online com o nome ".basic() + "%s".negative() + "!".basic()
	val ALL_PROFILES_SYNC = Text.POSITIVE_PREFIX + "Os ".basic() + "perfis".positive() + " de ".basic() + "todos".positive() + " os jogadores serão ".basic() + "sincronizados".positive() + "!".basic()
	val SINGLE_PROFILE_SYNC = Text.POSITIVE_PREFIX + "O ".basic() + "perfil".positive() + " do jogador ".basic() + "%s".positive() + " será ".basic() + "sincronizado".positive() + "!".basic()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.isEmpty()) {
			Tasks.async(Runnable { Bukkit.getLogger().info("Web API: " + WebAPI.saveProfiles(*Gamers.gamers().toTypedArray())) })

			sender.sendMessage(ALL_PROFILES_SYNC)
		} else {
			val player = Bukkit.getPlayerExact(args[0])

			if (player == null) sender.sendMessage(PLAYER_NOT_FOUND.format(args[0]))
			else {
				val gamer = Gamers.gamer(player)

				Tasks.async(Runnable { Bukkit.getLogger().info("Web API: " + WebAPI.saveProfiles(gamer)) })

				sender.sendMessage(SINGLE_PROFILE_SYNC.format(player.name))
			}
		}

		return false
	}

	override fun tabComplete(sender: Player, alias: String, args: Array<String>) = Bukkit.getOnlinePlayers().filter {
		sender.canSee(it) && it.name.startsWith(args[0], true)
	}.map { it.name }.toMutableList()
}
