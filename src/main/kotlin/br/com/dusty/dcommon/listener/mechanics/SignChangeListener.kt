package br.com.dusty.dcommon.listener.mechanics

import br.com.dusty.dcommon.gamer.EnumMode
import br.com.dusty.dcommon.gamer.Gamers
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent

object SignChangeListener: Listener {

	@EventHandler
	fun onSignChange(event: SignChangeEvent) {
		val player = event.player
		val gamer = Gamers.gamer(player)

		if (gamer.mode == EnumMode.ADMIN) for (i in 0 .. 3) event.setLine(i, event.getLine(i).replace("&", "§"))
	}
}
