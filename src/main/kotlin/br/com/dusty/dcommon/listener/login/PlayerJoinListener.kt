package br.com.dusty.dcommon.listener.login

import br.com.dusty.dcommon.gamer.EnumMode
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.GamerRegistry
import br.com.dusty.dcommon.util.Tasks
import br.com.dusty.dcommon.util.protocol.EnumProtocolVersion
import br.com.dusty.dcommon.util.protocol.Protocols
import br.com.dusty.dcommon.util.stdlib.clearFormatting
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.negative
import br.com.dusty.dcommon.util.text.positive
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinListener: Listener {

	private val COULDNT_CHECK_VERSION = "Não foi possível identificar a versão de seu jogo!\n\nVerifique se você está usando mods que alterem sua versão...".negative()

	private val JOIN_MESSAGE_PREFIX = "[".basic() + "+".positive() + "] ".basic()

	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		val player = event.player
		val gamer = GamerRegistry.gamer(player)

		val protocolVersion = EnumProtocolVersion[Protocols.VIA_VERSION_API.getPlayerVersion(player)]

		if (protocolVersion == EnumProtocolVersion.UNKNOWN) player.kickPlayer(COULDNT_CHECK_VERSION)
		else gamer.protocolVersion = protocolVersion

		if (gamer.rank.isLowerThan(EnumRank.MOD)) event.joinMessage = JOIN_MESSAGE_PREFIX + player.displayName.clearFormatting()
		else event.joinMessage = null

		Tasks.sync(Runnable {
			gamer.run {
				when {
					rank.isLowerThan(EnumRank.MOD) -> player.gameMode = GameMode.ADVENTURE
					else                           -> mode = EnumMode.ADMIN
				}

				hidePlayers()

				tag = rank

				GamerRegistry.onlineGamers().forEach { it.updateNameAboveHead(arrayListOf(gamer)) }
			}
		})
	}
}
