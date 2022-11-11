package de.warsteiner.jobs.jobs;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.metadata.FixedMetadataValue;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionItemPickUp  implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPick(PlayerPickupItemEvent event) { 
 
		if(event.getItem() == null) {
			return;
		}
		
		if(event.getPlayer() == null) {
			return;
		}
		
		if (event.isCancelled()) {
			if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {

				event.setCancelled(true);
			}
			return;
		}
	 
		if (event.getItem().hasMetadata("dropped-by-player")) {
			return;
		}
	  
		String type = event.getItem().getItemStack().getType().toString().toUpperCase();
 
		plugin.getJobWorkManager().executePickUpWork(event.getPlayer(), type);
		 
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnDrop(PlayerDropItemEvent e) {
		e.getItemDrop().setMetadata("dropped-by-player",  new FixedMetadataValue(UltimateJobs.getPlugin(), "dropped-by-player"));
	}
	
	
}