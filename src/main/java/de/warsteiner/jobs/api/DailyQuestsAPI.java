package de.warsteiner.jobs.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.objects.PluginColor;

public class DailyQuestsAPI {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public void loadQuests() {

		FileConfiguration settings = plugin.getLocalFileManager().getDailyQuestsSettingsConfig();
		FileConfiguration types = plugin.getLocalFileManager().getDailyQuestsTypesConfig();

		File dataFolder = new File("plugins/UltimateJobs/lang/");
		File[] folders = dataFolder.listFiles();

		if (folders != null) {

			Collection<String> fails = new ArrayList<String>();

			for (int i = 0; i < folders.length; i++) {
				String name = folders[i].getName();

				File dataFolder2 = new File("plugins/UltimateJobs/lang/" + name + "/");

				File[] files = dataFolder2.listFiles();

				if (files != null) {

					for (int i2 = 0; i2 < files.length; i2++) {
						String filenamed = files[i2].getName();

						if (filenamed.toLowerCase().equalsIgnoreCase("language.yml")) {
							File filed = files[i2];

							YamlConfiguration cfg = YamlConfiguration.loadConfiguration(filed);

						}
					}
				}

			}
		}

	}

}
