package de.warsteiner.jobs.jobs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.loot.LootTables;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionFindATreasure implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onOpen(PlayerInteractEvent e) {
		if (e.isCancelled()) {
			if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {
				e.setCancelled(true);
			}
			return;
		}

		Player p = e.getPlayer();
		if (e.getClickedBlock() == null) {
			return;
		}
		if (e.getClickedBlock().getType() != Material.CHEST) {
			return;
		}
		Chest c = (Chest) e.getClickedBlock().getState();
		if (c.getLootTable() == null) {
			return;
		}
	 
		String rep = c.getLootTable().toString().replaceAll("minecraft:chests/", "    ").replaceAll(" ", "");
		
		plugin.getJobWorkManager().executeTreasureEvent(rep.toUpperCase(), p);

	}
}
