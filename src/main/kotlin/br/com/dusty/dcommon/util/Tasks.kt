package br.com.dusty.dcommon.util

import br.com.dusty.dcommon.Main
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

object Tasks {

	fun sync(runnable: Runnable): BukkitTask = Bukkit.getScheduler().runTask(Main.INSTANCE, runnable)

	fun sync(runnable: Runnable, delay: Long): BukkitTask = Bukkit.getScheduler().runTaskLater(Main.INSTANCE, runnable, delay)

	fun sync(runnable: Runnable, delay: Long, period: Long): BukkitTask = Bukkit.getScheduler().runTaskTimer(Main.INSTANCE, runnable, delay, period)

	fun async(runnable: Runnable): BukkitTask = Bukkit.getScheduler().runTaskAsynchronously(Main.INSTANCE, runnable)

	fun async(runnable: Runnable, delay: Long): BukkitTask = Bukkit.getScheduler().runTaskLaterAsynchronously(Main.INSTANCE, runnable, delay)

	fun async(runnable: Runnable, delay: Long, period: Long): BukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.INSTANCE, runnable, delay, period)

	fun sync(runnable: BukkitRunnable): BukkitTask = runnable.runTask(Main.INSTANCE)

	fun sync(runnable: BukkitRunnable, delay: Long): BukkitTask = runnable.runTaskLater(Main.INSTANCE, delay)

	fun sync(runnable: BukkitRunnable, delay: Long, period: Long): BukkitTask = runnable.runTaskTimer(Main.INSTANCE, delay, period)

	fun async(runnable: BukkitRunnable): BukkitTask = runnable.runTaskAsynchronously(Main.INSTANCE)

	fun async(runnable: BukkitRunnable, delay: Long): BukkitTask = runnable.runTaskLaterAsynchronously(Main.INSTANCE, delay)

	fun async(runnable: BukkitRunnable, delay: Long, period: Long): BukkitTask = runnable.runTaskTimerAsynchronously(Main.INSTANCE, delay, period)
}
