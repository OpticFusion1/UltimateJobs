package de.warsteiner.jobs.jobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener; 
import org.bukkit.event.player.PlayerInteractEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionCarve  implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCarve(PlayerInteractEvent event) { 
		plugin.getJobWorkManager().executeCarveWork(event);
	}
}