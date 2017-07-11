package br.com.dusty.dservices;

import br.com.dusty.dservices.services.shutdown.ShutdownService;
import br.com.dusty.dservices.services.update.UpdaterService;
import br.com.dusty.dservices.util.MessageUtils;
import br.com.dusty.dservices.util.NTPUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
	
	public static final Logger LOGGER = Bukkit.getLogger();
	
	public static Main INSTANCE;
	
	public Main() {
		INSTANCE = this;
	}
	
	@Override
	public void onLoad() {
		boolean updated = false;
		
		try{
			updated = UpdaterService.updatePlugins();
		}catch(IOException e){
			LOGGER.log(Level.SEVERE, MessageUtils.PREFIX + "Couldn't update plugins: " + e.getMessage());
		}
		
		if(updated)
			Bukkit.shutdown();
	}
	
	@Override
	public void onEnable() {
		long t = 0;
		
		try{
			t = NTPUtils.getTime();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		if(t != 0)
			ShutdownService.scheduleShutdown();
	}
	
	@Override
	public void onDisable() {
	
	}
	
	public class Response {
		
		String query, mensagem;
		
		@Override
		public String toString() {
			return "Response{" + "query='" + query + '\'' + ", mensagem='" + mensagem + '\'' + '}';
		}
	}
}
