package de.warsteiner.jobs.jobs;
 
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import de.warsteiner.jobs.UltimateJobs; 
import dev.lone.itemsadder.api.Events.CustomEntityDeathEvent;

public class JobAction_IA_Kill implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(CustomEntityDeathEvent event) { 
		 
		if(event.getEntity() == null) {
			return;
		}
		
		Player p = (Player)event.getKiller();
	
		String id  = event.getNamespacedID();
	 
		Entity ent = event.getEntity();
		
		plugin.getItemsAdderManager().executeKillWork(p, ent, id);
		
	}
}