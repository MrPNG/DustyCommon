package br.com.dusty.dbuilds.update;

import br.com.dusty.dbuilds.Main;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class PluginUpdater {
	
	public static boolean updatePlugins() throws IOException {
		File rootDir = Bukkit.getWorldContainer();
		
		File buildsDir = new File(rootDir, "builds");
		if(!buildsDir.exists())
			buildsDir.mkdirs();
		
		List<File> buildFiles = new ArrayList<>();
		for(File f : buildsDir.listFiles()){
			if(!f.isDirectory() && f.getName().endsWith(".jar"))
				buildFiles.add(f);
		}
		
		File pluginsDir = new File(rootDir, "plugins");
		
		List<File> pluginFiles = new ArrayList<>();
		for(File f : pluginsDir.listFiles()){
			if(!f.isDirectory() && f.getName().endsWith(".jar"))
				pluginFiles.add(f);
		}
		
		boolean updated = false;
		
		pluginFiles:
		for(File pluginFile : pluginFiles){
			for(File buildFile : buildFiles){
				if(pluginFile.getName().equals(buildFile.getName())){
					if(!checkHash(pluginFile, buildFile)){
						Main.LOGGER.info("[DustyBuilds] Found an update for plugin file \"" + pluginFile.getName() + "\", updating...");
						
						FileUtils.forceDelete(pluginFile);
						FileUtils.moveFile(buildFile, pluginFile);
						
						updated = true;
						Main.LOGGER.info("[DustyBuilds] Updated plugin file \"" + pluginFile.getName() + "\"");
					}else{
						FileUtils.forceDelete(buildFile);
						
						Main.LOGGER.info("[DustyBuilds] No updates found for plugin file \"" + pluginFile.getName() + "\"");
					}
					continue pluginFiles;
				}
			}
		}
		
		return updated;
	}
	
	private static boolean checkHash(File pluginFile, File buildFile) throws IOException {
		byte[] pluginHash = DigestUtils.md5(new FileInputStream(pluginFile));
		byte[] buildHash = DigestUtils.md5(new FileInputStream(buildFile));
		
		return Arrays.equals(pluginHash, buildHash);
	}
}
