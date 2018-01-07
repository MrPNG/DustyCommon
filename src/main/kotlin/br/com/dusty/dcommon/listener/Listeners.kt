package br.com.dusty.dcommon.listener

import br.com.dusty.dcommon.Main
import br.com.dusty.dcommon.listener.login.AsyncPlayerPreLoginListener
import br.com.dusty.dcommon.listener.login.PlayerJoinListener
import br.com.dusty.dcommon.listener.login.PlayerLoginListener
import br.com.dusty.dcommon.listener.mechanics.*
import br.com.dusty.dcommon.listener.quit.PlayerQuitListener
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import java.util.*

object Listeners {

	/**
	 * [ArrayList] que contém todos os [Listener] a serem/já registrados pelo plugin.
	 */
	private val LISTENERS = hashSetOf<Listener>()

	/**
	 * Registra todos os [Listener] da [ArrayList] LISTENERS.
	 */
	fun registerAll() {
		//Usage: LISTENERS.add(FooListener)

		//Login
		LISTENERS.add(AsyncPlayerPreLoginListener)
		LISTENERS.add(PlayerJoinListener)
		LISTENERS.add(PlayerLoginListener)

		//Mechanics
		LISTENERS.add(AsyncPlayerChatListener)
		LISTENERS.add(EntityDamageListener)
		LISTENERS.add(InventoryClickListener)
		LISTENERS.add(InventoryOpenListener)
		LISTENERS.add(PlayerInteractEntityListener)
		LISTENERS.add(PlayerMoveListener)
		LISTENERS.add(PlayerPickupItemListener)
		LISTENERS.add(WorldLoadListener)

		//Quit
		LISTENERS.add(PlayerQuitListener)

		LISTENERS.forEach { listener -> Bukkit.getPluginManager().registerEvents(listener, Main.INSTANCE) }
	}
}
