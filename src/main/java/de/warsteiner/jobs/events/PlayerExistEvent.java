package de.warsteiner.jobs.events;
 
import java.util.UUID;

import org.bukkit.Bukkit; 
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.PlayerAPI; 
import de.warsteiner.jobs.utils.JsonMessage;
import de.warsteiner.jobs.utils.objects.jobs.JobsPlayer;

public class PlayerExistEvent implements Listener {

	private UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event) {
 
		new BukkitRunnable() {

			@Override
			public void run() { 
				Player player = event.getPlayer();

				UUID UUID = player.getUniqueId();

				String name = player.getName().toLowerCase();
  
				plugin.getPlayerAPI().checkAndLoadIntoOnlineCache(player, name, player.getName() , ""+UUID);
				 
				plugin.getLocationAPI().setLocation(player.getLocation(), "LastLoc." + UUID);
				
				cancel();
			}

		}.runTaskAsynchronously(plugin);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent event) {

		new BukkitRunnable() {

			@Override
			public void run() {

				Player player = event.getPlayer();
				UUID UUID = player.getUniqueId();
				
				String name = player.getName().toLowerCase();

				plugin.getPlayerAPI().saveAndLoadIntoOfflineCache(player, name, ""+UUID);
				
				plugin.getLocationAPI().setLocation(player.getLocation(), "LastLoc." + UUID);
				
				cancel();
			}

		}.runTaskAsynchronously(plugin);
	}

}