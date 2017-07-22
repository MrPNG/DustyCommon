package br.com.dusty.dservices.services.shutdown

import br.com.dusty.dservices.Main
import br.com.dusty.dservices.util.MessageUtils
import br.com.dusty.dservices.util.NTPUtils
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

import java.io.IOException
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.logging.Level

object ShutdownService {

    private var shutdownTask: BukkitTask? = null

    fun scheduleShutdown(): BukkitTask {
        val t = NTPUtils.time

        val calendar = GregorianCalendar()
        calendar.time = Date(t)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 4)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val delta = calendar.timeInMillis - t
        val ticks = Math.floorDiv(delta, 50)

        Main.LOGGER.log(Level.INFO, MessageUtils.PREFIX + "Ticks till shutdown: " + ticks)

        shutdownTask = object : BukkitRunnable() {
            override fun run() {
                Bukkit.shutdown()
            }
        }.runTaskLater(Main.INSTANCE, ticks)

        return shutdownTask!!
    }
}
