package de.warsteiner.jobs.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.PlayerDataFile;

public class BlockAPI {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	private HashMap<Block, UUID> blocks = new HashMap<Block, UUID>();
	
	public void removeBlock(Block block) {
		block.removeMetadata("placed-by-player", plugin);
		blocks.remove(block);
	}

	public void addBlock(Block block, UUID uuid) {
		blocks.put(block, uuid);
	}
 
	public UUID getPlacedBy(Block block) {

		List<MetadataValue> values = block.getMetadata("placed-by-player");
		if (!values.isEmpty()) {

			for (MetadataValue value : values) {
				String val = value.value().toString();

				return UUID.fromString(val.toString());

			}

		}
		return null;

	}

	public void loadBlocks() {

		PlayerDataFile file = plugin.getBlockData();
		FileConfiguration cfg = file.get();

		List<String> listed = cfg.getStringList("Blocks");

		listed.forEach((block) -> {

			String[] split = block.split(";");

			String type = split[0];
			String x = split[1];
			String y = split[2];
			String z = split[3];
			String world = split[4];
			String player = split[5];
			UUID id = UUID.fromString(player.toString());

			if (Bukkit.getWorld(world) != null) {
				Location location = new Location(Bukkit.getWorld(world), Double.valueOf(x), Double.valueOf(y),
						Double.valueOf(z));

				if (location.getBlock().getType().equals(Material.valueOf(type))) {
					Block real = location.getBlock();

					real.setMetadata("placed-by-player", new FixedMetadataValue(UltimateJobs.getPlugin(), id));

					blocks.put(real, id);
				}
			}

		});

	}

	public void saveBlocks() {
		PlayerDataFile file = plugin.getBlockData();
		FileConfiguration cfg = file.get();

		List<String> listed = cfg.getStringList("Blocks");

		blocks.forEach((block, id) -> {

			if (block.hasMetadata("placed-by-player")) {

				Location location = block.getLocation();

				double x = location.getX();
				double y = location.getY();
				double z = location.getZ();
				String world = location.getWorld().getName();

				String g = block.getType().toString() + ";" + x + ";" + y + ";" + z + ";" + world + ";" + id;

				listed.add(g);
			}
		});

		cfg.set("Blocks", listed);

		file.save();
	}

}
