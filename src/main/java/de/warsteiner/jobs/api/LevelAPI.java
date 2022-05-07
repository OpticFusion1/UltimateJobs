package de.warsteiner.jobs.api;
 
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.jobs.UltimateJobs; 
import de.warsteiner.jobs.utils.cevents.PlayerLevelJobEvent;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class LevelAPI {

	private UltimateJobs plugin; 

	public LevelAPI(UltimateJobs plugin) { 
		this.plugin = plugin;
	}

	public double getJobNeedExp(Job job, JobsPlayer pl) {

		int next = pl.getStatsOf(job.getConfigID()).getLevel() + 1;

		return job.getExpOfLevel(next);
	}

	public double getJobNeedExpWithOutPlayer(Job job, int level) {

		int next = level + 1;

		return job.getExpOfLevel(next);
	}

	public boolean canLevelMore(String uuid, Job job, int level) {
		if (job.getLevelDisplay(level, uuid) != null) {
			return false;
		}
		return true;
	}

	public boolean canlevelUp(Job Job, JobsPlayer pl) {
		double need = getJobNeedExp(Job, pl);

		double exp = pl.getStatsOf(Job.getConfigID()).getExp();

		if (exp == need || exp >= need) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public void check(Player player, Job job, JobsPlayer pl, String block) {
		plugin.getExecutor().execute(() -> {
			UUID UUID =  player.getUniqueId();

			PluginAPI api = SimpleAPI.getInstance().getAPI();
			FileConfiguration cfg = plugin.getFileManager().getConfig();
			String prefix = pl.getLanguage().getPrefix(UUID);
			
			int old_level = pl.getStatsOf(job.getConfigID()).getLevel();
			int new_level = old_level + 1;
			
			if(old_level >= job.getCountOfLevels()) {
				return;
			}

			if (!canLevelMore(""+UUID, job, new_level)) {

				if (canlevelUp(job, pl)) {

					//rewards
				 
					 pl.getStatsOf(job.getConfigID()).updateLevel(new_level);
					 pl.getStatsOf(job.getConfigID()).updateExp(0);
				 
					new BukkitRunnable() {
						public void run() {
							new PlayerLevelJobEvent(player, pl, job, new_level);
						}
					}.runTaskLater(plugin, 1);
			 
					if(job.isVaultOnLevel(new_level)) {
						double money = job.getVaultOnLevel(new_level); 
						UltimateJobs.getPlugin().getEco().depositPlayer(player, money);
					}
				
					String level_name = job.getLevelDisplay(new_level, ""+UUID);

					if (cfg.getBoolean("Levels.Enable_Title")) {
						String title_1 = api.toHex(pl.getLanguage().getStringFromLanguage(UUID, "Levels.Ttitle_1")
								.replaceAll("<prefix>", prefix).replaceAll("<level_name>", level_name)
								.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));

						String title_2 = api.toHex(pl.getLanguage().getStringFromLanguage(UUID,"Levels.Ttitle_2")
								.replaceAll("<prefix>", prefix).replaceAll("<level_name>", level_name)
								.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));
						player.sendTitle(title_1, title_2);
					}

					if (cfg.getBoolean("Levels.Enable_Message")) {
						String message = api.toHex(pl.getLanguage().getStringFromLanguage(UUID,"Levels.Message")
								.replaceAll("<prefix>", prefix).replaceAll("<level_name>", level_name)
								.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));
						player.sendMessage(message);
					}

					if (cfg.getBoolean("Levels.Enabled_Actionbar")) {
						String message = api.toHex(pl.getLanguage().getStringFromLanguage(UUID,"Levels.Actionbar")
								.replaceAll("<prefix>", prefix).replaceAll("<level_name>", level_name)
								.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));
						player.sendMessage(message);
					} 

				}
			}

		});
	}

}
