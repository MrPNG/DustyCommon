package br.com.dusty.dservices.services.shutdown

import br.com.dusty.dservices.Main
import br.com.dusty.dservices.util.Messages
import br.com.dusty.dservices.util.NTPs
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*
import java.util.logging.Level

object ShutdownService {

	lateinit var shutdownTask: BukkitTask

	fun scheduleShutdown() {
		val time = NTPs.time

		val calendar = GregorianCalendar()
		calendar.time = Date(time)
		calendar.add(Calendar.DAY_OF_MONTH, 1)
		calendar.set(Calendar.HOUR_OF_DAY, 4)
		calendar.set(Calendar.MINUTE, 0)
		calendar.set(Calendar.SECOND, 0)
		calendar.set(Calendar.MILLISECOND, 0)

		val remainingTime = calendar.timeInMillis - time
		val ticks = Math.floorDiv(remainingTime, 50)

		Main.LOGGER.log(Level.INFO, Messages.PREFIX + "Ticks until shutdown: " + ticks)

		shutdownTask = object: BukkitRunnable() {
			override fun run() {
				Bukkit.shutdown()
			}
		}.runTaskLater(Main.INSTANCE, ticks)
	}
}
