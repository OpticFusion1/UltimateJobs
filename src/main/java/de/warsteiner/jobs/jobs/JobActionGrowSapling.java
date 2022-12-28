package de.warsteiner.jobs.jobs;

import java.util.UUID;

import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.metadata.FixedMetadataValue;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionGrowSapling implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGrow(StructureGrowEvent event) {

		if (event.isCancelled()) {
			if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}

		TreeType type = event.getSpecies();

		Block block = event.getLocation().getBlock();

		if (block == null) {
			return;
		}

		if (plugin.getBlockAPI().getPlacedBy(block) != null) {

			UUID player = plugin.getBlockAPI().getPlacedBy(block);

			plugin.getJobWorkManager().executeSaplingGrowAction("" + type, player, block);

			plugin.getBlockAPI().removeBlock(block);
		}

	}

}
