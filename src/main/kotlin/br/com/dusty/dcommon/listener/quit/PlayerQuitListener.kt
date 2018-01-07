package br.com.dusty.dcommon.listener.quit

import br.com.dusty.dcommon.Config
import br.com.dusty.dcommon.clan.ClanRegistry
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.GamerRegistry
import br.com.dusty.dcommon.util.Tasks
import br.com.dusty.dcommon.util.stdlib.clearFormatting
import br.com.dusty.dcommon.util.text.Text
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.negative
import br.com.dusty.dcommon.util.web.WebAPI
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object PlayerQuitListener: Listener {

	private val QUIT_MESSAGE_PREFIX = "[".basic() + "-".negative() + "] ".basic()

	@EventHandler
	fun onPlayerQuit(event: PlayerQuitEvent) {
		val player = event.player
		val gamer = GamerRegistry.gamer(player)

		GamerRegistry.remove(player.uniqueId)?.run {
			if (isCombatTagged()) {
				Bukkit.broadcastMessage(Text.NEGATIVE_PREFIX + displayName.negative() + " deslogou em ".basic() + "combate".negative() + "!".basic())

				combatPartner?.kill(this)
			}

			if (Config.data.serverStatus != Config.EnumServerStatus.OFFLINE) Tasks.async(Runnable { WebAPI.saveProfiles(gamer) })

			if (clan != null) {
				val clan = clan!!

				clan.onlineMembers.remove(this)

				if (clan.leader == this) clan.leader = null

				if (clan.onlineMembers.isEmpty()) ClanRegistry.remove(clan.uuid)?.run { if (Config.data.serverStatus != Config.EnumServerStatus.OFFLINE) WebAPI.saveClans(clan) }
			}

			if (rank.isLowerThan(EnumRank.MOD)) event.quitMessage = QUIT_MESSAGE_PREFIX + displayName.clearFormatting()
			else event.quitMessage = null
		}
	}
}
