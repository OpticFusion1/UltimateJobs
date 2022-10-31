package de.warsteiner.jobs.manager;
 
import java.io.BufferedReader;
import java.io.File; 
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader; 
import java.net.URL;
import java.net.URLConnection; 
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList; 

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
 
public class WebManager {
	
	public boolean canUpdate = false;
	public String newVersion = null;
	
	public ArrayList<String> added = new ArrayList<String>();
	public ArrayList<String> updated = new ArrayList<String>();
	public ArrayList<String> removed = new ArrayList<String>();
	
	public void checkVersion() {
		try {

			URLConnection connection = new URL("https://apiv3.war-projects.com/ultimatejobs/version.txt").openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			connection.connect();

			BufferedReader r = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				sb.append(line);
			}
			String version = sb.toString();

			String ver = UltimateJobs.getPlugin().getDescription().getVersion();
			
			if (!version.equalsIgnoreCase(ver)) {
				canUpdate = true;
				newVersion = version;
				 
				Bukkit.getConsoleSender().sendMessage("§c§lThere was a Update found for UltimateJobs! https://www.spigotmc.org/resources/ultimatejobs-player-jobs.99978/");
			} else {
				Bukkit.getConsoleSender().sendMessage("§a§lNo Update for UltimateJobs found!");
			}
			r.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("§cFailed to send Web Request to Web-Server!");
		}
	}
 
 
	public void checkVersionWithPlayer(Player player) {
		try {
			 
			URLConnection connection = new URL("https://apiv3.war-projects.com/ultimatejobs/version.txt").openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			connection.connect();

			BufferedReader r = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				sb.append(line);
			}
			String version = sb.toString();

			String ver = UltimateJobs.getPlugin().getDescription().getVersion();
			
			if (!version.equalsIgnoreCase(ver)) {
				canUpdate = true;
				newVersion = version;
				 
				player.sendMessage(AdminCommand.prefix+"§4There was a new Update Found! https://www.spigotmc.org/resources/ultimatejobs-player-jobs.99978/"); 
			} else {
				player.sendMessage(AdminCommand.prefix+"§aNo Update Found!");
			}
  
		} catch (IOException ex) {
			ex.printStackTrace();
			player.sendMessage(AdminCommand.prefix+"§4Failed to contact Webserver...");
		}
	}
 
}
