package br.com.dusty.dcommon.listener.login

import br.com.dusty.dcommon.gamer.EnumMode
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.Gamers
import br.com.dusty.dcommon.util.Tasks
import br.com.dusty.dcommon.util.protocol.EnumProtocolVersion
import br.com.dusty.dcommon.util.protocol.EnumProtocolVersion.UNKNOWN
import br.com.dusty.dcommon.util.protocol.Protocols
import br.com.dusty.dcommon.util.stdlib.clearFormatting
import br.com.dusty.dcommon.util.text.*
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinListener: Listener {

	val COULDNT_CHECK_VERSION = "Não foi possível identificar a versão de seu jogo!\n\nVerifique se você está usando mods que alterem sua versão...".negative()
	val DIDNT_AUTHENTICATE = "Você demorou muito para fazer login no servidor!\n\nUse o comando /registrar caso ainda não tenha uma conta...".negative()

	val WELCOME = Text.SPECIAL_PREFIX + "Bem vindo ao ".basic() + "Dusty".special() + "!\n \n"
	val LOGIN_OR_REGISTER = Text.POSITIVE_PREFIX + "Faça login usando o comando ".basic() + "/login".positive() + " ou registre-se com o comando ".basic() + "/registrar".positive() + "!".basic()

	private val JOIN_MESSAGE_PREFIX = "[".basic() + "+".positive() + "] ".basic()

	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		val player = event.player
		val gamer = Gamers.gamer(player)

		if (gamer.protocolVersion == UNKNOWN) {
			val protocolVersion = EnumProtocolVersion[Protocols.VIA_VERSION_API.getPlayerVersion(player)]

			if (protocolVersion == UNKNOWN) player.kickPlayer(COULDNT_CHECK_VERSION)
			else gamer.protocolVersion = protocolVersion
		}

		player.sendMessage(WELCOME)

		if (gamer.isAuthenticated) {
			if (gamer.rank.isHigherThanOrEquals(EnumRank.MOD)) event.joinMessage = null
			else event.joinMessage = JOIN_MESSAGE_PREFIX + player.displayName.clearFormatting()

			Tasks.sync(Runnable {
				gamer.run {
					if (rank.isHigherThanOrEquals(EnumRank.MOD)) mode = EnumMode.ADMIN

					hidePlayers()
					hideFromPlayers()

					tag = rank

					Gamers.gamers().forEach { it.updateNameAboveHead(arrayListOf(gamer)) }
				}
			})
		} else {
			event.joinMessage = null

			player.run {
				gameMode = GameMode.ADVENTURE

				health = player.maxHealth
				saturation = 0F
				foodLevel = 20
				exp = 0F
				level = 0
				fireTicks = 0

				inventory.clear()

				activePotionEffects.forEach { removePotionEffect(it.type) }

				sendMessage(LOGIN_OR_REGISTER)
			}

			Tasks.sync(Runnable { if (!gamer.isAuthenticated) player.kickPlayer(DIDNT_AUTHENTICATE) }, 20L * 60L)
		}
	}
}
