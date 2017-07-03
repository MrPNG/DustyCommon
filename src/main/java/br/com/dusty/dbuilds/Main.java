package br.com.dusty.dbuilds;

import br.com.dusty.dbuilds.update.PluginUpdater;
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
			updated = PluginUpdater.updatePlugins();
		}catch(IOException e){
			LOGGER.log(Level.SEVERE, "[DustyBuilds] Couldn't update plugins: " + e.getMessage());
		}
		if(updated)
			Bukkit.shutdown();
	}
	
	@Override
	public void onEnable() {
		
	}
	
	@Override
	public void onDisable() {
	
	}
}
