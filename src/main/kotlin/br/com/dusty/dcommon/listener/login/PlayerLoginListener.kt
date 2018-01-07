package br.com.dusty.dcommon.listener.login

import br.com.dusty.dcommon.Config
import br.com.dusty.dcommon.clan.ClanRegistry
import br.com.dusty.dcommon.gamer.EnumMode
import br.com.dusty.dcommon.gamer.EnumRank
import br.com.dusty.dcommon.gamer.GamerRegistry
import br.com.dusty.dcommon.store.EnumAdvantage
import br.com.dusty.dcommon.util.text.TextColor
import br.com.dusty.dcommon.util.text.basic
import br.com.dusty.dcommon.util.text.color
import br.com.dusty.dcommon.util.text.negative
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import java.util.*

object PlayerLoginListener: Listener {

	private val KICK_NOT_READY = "O servidor ainda não está aberto!\n\nVolte em alguns instantes...".negative()
	private val KICK_FULL_MESSAGE = "O servidor está cheio!\n\n".negative() + "Compre ".basic() + "PRO".color(TextColor.GOLD) + " ou um ".basic() + "Slot Reservado".color(TextColor.GOLD) + " no site ".basic() + "loja.dusty.com.br".color(
			TextColor.GOLD) + " e entre agora!".basic()

	@EventHandler
	fun onPlayerLogin(event: PlayerLoginEvent) {
		val player = event.player

		if (GamerRegistry.PRIMITIVE_GAMER_BY_UUID[player.uniqueId] == null) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, KICK_NOT_READY)

			return
		}

		val gamer = GamerRegistry.gamer(player)

		if (Config.data.serverStatus != Config.EnumServerStatus.ONLINE && gamer.rank.isLowerThan(EnumRank.MOD)) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, KICK_NOT_READY)

			GamerRegistry.remove(player.uniqueId)

			return
		}

		if (event.result == PlayerLoginEvent.Result.KICK_FULL) {
			if (gamer.rank.isLowerThan(EnumRank.MOD) && !gamer.hasAdvantage(EnumAdvantage.SLOT) && GamerRegistry.onlineGamers().filterNot { it.mode == EnumMode.ADMIN }.size >= Config.data.slots) {
				event.disallow(PlayerLoginEvent.Result.KICK_FULL, KICK_FULL_MESSAGE)

				GamerRegistry.remove(player.uniqueId)

				return
			} else {
				event.allow()
			}
		}

		if (gamer.primitiveGamer.clan != "") ClanRegistry.clan(UUID.fromString(gamer.primitiveGamer.clan)).run {
			if (player.uniqueId.toString() in rawMembers()) {
				gamer.clan = this

				onlineMembers.add(gamer)

				if (primitiveClan.leader == player.uniqueId.toString()) leader = gamer
			} else gamer.primitiveGamer.clan = ""
		}
	}
}
