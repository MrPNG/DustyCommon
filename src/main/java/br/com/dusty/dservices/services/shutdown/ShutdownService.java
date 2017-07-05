package br.com.dusty.dservices.services.shutdown;

import br.com.dusty.dservices.Main;
import br.com.dusty.dservices.util.NTPUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;

public class ShutdownService {
	
	private static BukkitTask shutdownTask = null;
	
	public static BukkitTask scheduleShutdown() {
		long t = 0;
		try{
			t = NTPUtils.getTime();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date(t));
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 4);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		long delta = calendar.getTimeInMillis() - t;
		long ticks = Math.floorDiv(delta, 50);
		
		Main.LOGGER.log(Level.INFO, "[DustyBuilds] Ticks till shutdown: " + ticks);
		
		return shutdownTask = new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.shutdown();
			}
		}.runTaskLater(Main.INSTANCE, ticks);
	}
}
