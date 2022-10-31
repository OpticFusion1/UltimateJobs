package de.warsteiner.jobs.jobs;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener; 

import de.warsteiner.jobs.UltimateJobs;
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;

public class JobAction_IA_Break  implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(CustomBlockBreakEvent event) { 
		
		if (event.isCancelled()) {
			if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}
	 
		Player p = event.getPlayer();
	
		String id  = event.getNamespacedID();
		
		Block block = event.getBlock();
		
		plugin.getItemsAdderManager().executeBlockBreakWork(p, block, id);
		
	}
}
