package de.warsteiner.jobs.api;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import de.warsteiner.jobs.UltimateJobs;

public class EffectAPI {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	
	public void playSound(String ty, Player player) {
		FileConfiguration config = plugin.getLocalFileManager().getEffectSettings();
		if (config.contains("Sounds." + ty + ".Sound")) {

			if (Sound.valueOf(config.getString("Sounds." + ty + ".Sound")) == null) {
				Bukkit.getConsoleSender()
						.sendMessage("§cFailed to get Sound from : " + config.getString("Sounds." + ty + ".Sound"));
				return;
			}

			Sound sound = Sound.valueOf(config.getString("Sounds." + ty + ".Sound"));
			double vol = config.getDouble("Sounds." + ty + ".Volume", config.getInt("Sounds." + ty + ".Volume"));
			double pitch = config.getDouble("Sounds." + ty + ".Pitch", config.getInt("Sounds." + ty + ".Pitch"));
			player.playSound(player.getLocation(), sound, (float) vol, (float) pitch);
		}
	}
	

	public void spawnFireworks(Location location) {
		Location loc = location;
		Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		fwm.setPower(1);
		fwm.addEffect(FireworkEffect.builder().withColor(Color.GREEN).flicker(true).build());
		fw.setFireworkMeta(fwm);
		fw.detonate();
		fw.setMetadata("nodamage", (MetadataValue) new FixedMetadataValue((Plugin) plugin, Boolean.valueOf(true)));
		Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		fw2.setFireworkMeta(fwm);
	}

	
}
