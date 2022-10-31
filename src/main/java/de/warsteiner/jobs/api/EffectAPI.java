package de.warsteiner.jobs.api;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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
	
}
