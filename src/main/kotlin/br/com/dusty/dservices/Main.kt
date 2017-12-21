package br.com.dusty.dservices

import br.com.dusty.dservices.services.shutdown.ShutdownService
import br.com.dusty.dservices.services.update.UpdaterService
import br.com.dusty.dservices.util.Messages
import br.com.dusty.dservices.util.NTPs
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import java.util.logging.Level

class Main: JavaPlugin() {

	init {
		INSTANCE = this
	}

	override fun onLoad() {
		try {
			if (UpdaterService.updatePlugins()) Bukkit.shutdown()
		} catch (e: IOException) {
			LOGGER.log(Level.SEVERE, Messages.PREFIX + "Couldn't update plugins: " + e.message)
		}
	}

	override fun onEnable() {
		try {
			if (NTPs.time > 0) ShutdownService.scheduleShutdown()
		} catch (e: IOException) {
			LOGGER.log(Level.SEVERE, Messages.PREFIX + "Couldn't get NTP time: " + e.message)
		}
	}

	override fun onDisable() {

	}

	companion object {

		val LOGGER = Bukkit.getLogger()!!

		lateinit var INSTANCE: Main
	}
}
