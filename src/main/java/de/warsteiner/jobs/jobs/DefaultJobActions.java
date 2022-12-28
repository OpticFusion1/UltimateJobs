package de.warsteiner.jobs.jobs;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import de.warsteiner.jobs.UltimateJobs;

public class DefaultJobActions implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExtendMove(BlockPistonExtendEvent e) {

		if (e.isCancelled()) {
			e.setCancelled(true);
			return;
		}

		if (e.getBlocks() == null) {
			return;
		}

		List<Block> b = e.getBlocks();

		UUID by = plugin.getBlockAPI().getPlacedBy(e.getBlock());

		if (by != null) {
			for (Block block : b) {

				plugin.getBlockAPI().addBlock(block, by);
				block.setMetadata("placed-by-player", new FixedMetadataValue(UltimateJobs.getPlugin(), by));

			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRetractMove(BlockPistonRetractEvent e) {

		if (e.isCancelled()) {
			e.setCancelled(true);
			return;
		}

		if (e.getBlocks() == null) {
			return;
		}

		List<Block> b = e.getBlocks();

		UUID by = plugin.getBlockAPI().getPlacedBy(e.getBlock());

		if (by != null) {
			for (Block block : b) {

				plugin.getBlockAPI().addBlock(block, by);
				block.setMetadata("placed-by-player", new FixedMetadataValue(UltimateJobs.getPlugin(), by));

			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent e) {

		if (e.isCancelled()) {
			e.setCancelled(true);
			return;
		}

		e.getBlock().setMetadata("placed-by-player",
				new FixedMetadataValue(UltimateJobs.getPlugin(), e.getPlayer().getUniqueId()));
		plugin.getBlockAPI().addBlock(e.getBlock(), e.getPlayer().getUniqueId());
	}
}
