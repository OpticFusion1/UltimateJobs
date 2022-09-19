package de.warsteiner.jobs.api;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.manager.PluginManager;
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

		int next = level;

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

	public void levelPlayerUP(PlayerLevelJobEvent event) {
		Player player = event.getPlayer();
		FileConfiguration config = UltimateJobs.getPlugin().getFileManager().getConfig();

		JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());

		String me = jb.getLanguage().getStringFromLanguage(player.getUniqueId(), "Levels.BoardCastMessage");
		UltimateJobs.getPlugin().getAPI().playSound("LEVEL_UP", player);

		Job job = event.getJob();
		String name = player.getName();

		if (config.getBoolean("Levels.FireWork")) {
			UltimateJobs.getPlugin().getAPI().spawnFireworks(player.getLocation());
		}
		if (config.getBoolean("Levels.BroadCastLevelUps")) {
			Bukkit.broadcastMessage(plugin.getPluginManager().toHex(me).replaceAll("<level>", "" + event.getNewLevel())
					.replaceAll("<job>", job.getDisplay("" + player.getUniqueId())).replaceAll("<name>", name)
					.replaceAll("&", "§"));
		}
	}

	@SuppressWarnings("deprecation")
	public void check(Player player, Job job, JobsPlayer pl, String block) {
		new BukkitRunnable() {

			@Override
			public void run() {

				UUID UUID = player.getUniqueId();

				PluginManager api = UltimateJobs.getPlugin().getPluginManager();
				FileConfiguration cfg = plugin.getFileManager().getConfig();
				String prefix = pl.getLanguage().getPrefix(UUID);

				int old_level = pl.getStatsOf(job.getConfigID()).getLevel();
				int new_level = old_level + 1;

				if (old_level >= job.getCountOfLevels()) {
					return;
				}

				if (!canLevelMore("" + UUID, job, new_level)) {

					if (canlevelUp(job, pl)) {

						if (!canLevelMore("" + UUID, job, new_level)) {

							if (canlevelUp(job, pl)) {

								// rewards

								plugin.getPlayerAPI().updateLevelOf("" + UUID, job, new_level);
								plugin.getPlayerAPI().updateExp("" + UUID, job, 0);

								new BukkitRunnable() {
									public void run() {
										PlayerLevelJobEvent e = new PlayerLevelJobEvent(player, pl, job, new_level);

										levelPlayerUP(e);
									}
								}.runTaskLater(plugin, 1);

								if (job.isVaultOnLevel(new_level)) {

									double money = job.getVaultOnLevel(new_level);

									boolean ys = true;

									if (plugin.getFileManager().getConfig().getString("PayMentMode").toUpperCase()
											.equalsIgnoreCase("STORED")) {
										if (plugin.getFileManager().getConfig()
												.getBoolean("CountLevelsRewardAsSalary")) {
											ys = false;
											double old = plugin.getPlayerAPI().getSalary(pl.getUUIDAsString());

											plugin.getPlayerAPI().updateSalary(pl.getUUIDAsString(), old + money);
										}
									}

									if (ys) {

										UltimateJobs.getPlugin().getEco().depositPlayer(player, money);
									}
								}

								String level_name = job.getLevelDisplay(new_level, "" + UUID);

								if (cfg.getBoolean("Levels.Enable_Title")) {
									String title_1 = api.toHex(pl.getLanguage()
											.getStringFromLanguage(UUID, "Levels.Ttitle_1")
											.replaceAll("<job>", job.getDisplay("" + UUID))
											.replaceAll("<prefix>", prefix).replaceAll("<level_name>", level_name)
											.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));

									String title_2 = api.toHex(pl.getLanguage()
											.getStringFromLanguage(UUID, "Levels.Ttitle_2")
											.replaceAll("<job>", job.getDisplay("" + UUID))
											.replaceAll("<prefix>", prefix).replaceAll("<level_name>", level_name)
											.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));
									player.sendTitle(title_1, title_2);
								}

								if (cfg.getBoolean("Levels.Enable_Message")) {
									String message = api.toHex(pl.getLanguage()
											.getStringFromLanguage(UUID, "Levels.Message")
											.replaceAll("<job>", job.getDisplay("" + UUID))
											.replaceAll("<prefix>", prefix).replaceAll("<level_name>", level_name)
											.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));
									player.sendMessage(message);
								}

								if (cfg.getBoolean("Levels.Enabled_Actionbar")) {
									String message = api.toHex(pl.getLanguage()
											.getStringFromLanguage(UUID, "Levels.Actionbar")
											.replaceAll("<job>", job.getDisplay("" + UUID))
											.replaceAll("<prefix>", prefix).replaceAll("<level_name>", level_name)
											.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));
									player.sendMessage(message);
								}
							}
						}
					}
				}
			}
		}.runTaskAsynchronously(plugin);
	}

}
