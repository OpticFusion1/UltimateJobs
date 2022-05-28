package de.warsteiner.jobs.jobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionFish implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	 
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(PlayerFishEvent event) {
		plugin.getJobWorkManager().executeFishWork(event);
	}

}
