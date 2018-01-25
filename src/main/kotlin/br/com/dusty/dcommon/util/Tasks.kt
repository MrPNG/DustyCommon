package br.com.dusty.dcommon.util

import br.com.dusty.dcommon.Main
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

object Tasks {

	fun sync(runnable: Runnable) = Bukkit.getScheduler().runTask(Main.INSTANCE, runnable)

	fun sync(runnable: Runnable, delay: Long) = Bukkit.getScheduler().runTaskLater(Main.INSTANCE, runnable, delay)

	fun sync(runnable: Runnable, delay: Long, period: Long) = Bukkit.getScheduler().runTaskTimer(Main.INSTANCE, runnable, delay, period)

	fun async(runnable: Runnable) = Bukkit.getScheduler().runTaskAsynchronously(Main.INSTANCE, runnable)

	fun async(runnable: Runnable, delay: Long) = Bukkit.getScheduler().runTaskLaterAsynchronously(Main.INSTANCE, runnable, delay)

	fun async(runnable: Runnable, delay: Long, period: Long) = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.INSTANCE, runnable, delay, period)

	fun sync(runnable: BukkitRunnable) = runnable.runTask(Main.INSTANCE)

	fun sync(runnable: BukkitRunnable, delay: Long) = runnable.runTaskLater(Main.INSTANCE, delay)

	fun sync(runnable: BukkitRunnable, delay: Long, period: Long) = runnable.runTaskTimer(Main.INSTANCE, delay, period)

	fun async(runnable: BukkitRunnable) = runnable.runTaskAsynchronously(Main.INSTANCE)

	fun async(runnable: BukkitRunnable, delay: Long) = runnable.runTaskLaterAsynchronously(Main.INSTANCE, delay)

	fun async(runnable: BukkitRunnable, delay: Long, period: Long) = runnable.runTaskTimerAsynchronously(Main.INSTANCE, delay, period)
}
