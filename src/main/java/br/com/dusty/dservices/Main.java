package br.com.dusty.dservices;

import br.com.dusty.dservices.services.shutdown.ShutdownService;
import br.com.dusty.dservices.services.update.UpdaterService;
import br.com.dusty.dservices.util.NTPUtils;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
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
			LOGGER.log(Level.SEVERE, "[DustyBuilds] Couldn't update plugins: " + e.getMessage());
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
		
		HttpClient httpClient = new DefaultHttpClient();
		
		try{
			HttpGet httpGet = new HttpGet("http://ianszot.duckdns.org/testes/merda.php?query=MrPingu_");
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			
			String s = EntityUtils.toString(httpEntity);
			LOGGER.log(Level.WARNING, s);
			
			Gson gson = new Gson();
			Response response = gson.fromJson(s, Response.class);
			
			LOGGER.log(Level.WARNING, response.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			HttpPost httpPost = new HttpPost("http://ianszot.duckdns.org/testes/post.php");
			
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("query", "MrPingu_"));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			
			String s = EntityUtils.toString(httpEntity);
			LOGGER.log(Level.WARNING, s);
			
			Gson gson = new Gson();
			Response response = gson.fromJson(s, Response.class);
			
			LOGGER.log(Level.WARNING, response.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
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
