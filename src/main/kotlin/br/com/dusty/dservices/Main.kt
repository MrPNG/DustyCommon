package br.com.dusty.dservices

import br.com.dusty.dservices.services.shutdown.ShutdownService
import br.com.dusty.dservices.services.update.UpdaterService
import br.com.dusty.dservices.util.MessageUtils
import br.com.dusty.dservices.util.NTPUtils
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

class Main : JavaPlugin() {

    init {
        INSTANCE = this
    }

    override fun onLoad() {
        var updated = false

        try {
            updated = UpdaterService.updatePlugins()
        } catch (e: IOException) {
            LOGGER.log(Level.SEVERE, MessageUtils.PREFIX + "Couldn't update plugins: " + e.message)
        }

        if (updated)
            Bukkit.shutdown()
    }

    override fun onEnable() {
        var t: Long = 0

        try {
            t = NTPUtils.time
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (t != 0L)
            ShutdownService.scheduleShutdown()
    }

    override fun onDisable() {

    }

    companion object {

        val LOGGER = Bukkit.getLogger()

        lateinit var INSTANCE: Main
    }
}
