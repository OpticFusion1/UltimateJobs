package de.warsteiner.jobs.api;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.manager.PluginManager;
import de.warsteiner.jobs.utils.cevents.PlayerLevelJobEvent;
import de.warsteiner.jobs.utils.objects.PluginColor;
import de.warsteiner.jobs.utils.objects.jobs.Job;
import de.warsteiner.jobs.utils.objects.jobs.JobLevel;
import de.warsteiner.jobs.utils.objects.jobs.JobsPlayer;

/**
 * Class for the Job Levels
 */

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

		if (event.getNewLevel() != null) {
			Player player = event.getPlayer();
			FileConfiguration config = UltimateJobs.getPlugin().getLocalFileManager().getConfig();

			JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());

			UltimateJobs.getPlugin().getAPI().playSound("LEVEL_UP", player);

			Job job = event.getJob();
			String name = player.getName();

			if (config.getBoolean("Levels.FireWork")) {
				UltimateJobs.getPlugin().getAPI().spawnFireworks(player.getLocation());
			}

			if (job.hasCustomBroadCastMessageForAll(jb.getUUIDAsString())) {
				String me = job.getCustomBroadCastMessageForAll(jb.getUUIDAsString());

				Bukkit.broadcastMessage(plugin.getPluginManager().toHex(me)
						.replaceAll("<level>", "" + event.getNewLevel().getLevel())
						.replaceAll("<level_rank>",
								job.getLevelRankDisplay(event.getNewLevel().getLevel(), "" + player.getUniqueId()))
						.replaceAll("<job>", job.getDisplayOfJob("" + player.getUniqueId())).replaceAll("<name>", name)
						.replaceAll("&", "§"));
			} else if (job.hasCustomBroadCastMessageForLevel(event.getNewLevel().getLevel(), jb.getUUIDAsString())) {

				String me = job.getCustomBroadCastMessageForLevel(event.getNewLevel().getLevel(), jb.getUUIDAsString());

				Bukkit.broadcastMessage(plugin.getPluginManager().toHex(me)
						.replaceAll("<level>", "" + event.getNewLevel().getLevel())
						.replaceAll("<level_rank>",
								job.getLevelRankDisplay(event.getNewLevel().getLevel(), "" + player.getUniqueId()))
						.replaceAll("<job>", job.getDisplayOfJob("" + player.getUniqueId())).replaceAll("<name>", name)
						.replaceAll("&", "§"));
			} else if (config.getBoolean("Levels.BroadCastLevelUps")) {
				String me = jb.getLanguage().getMessage("Levels.BoardCastMessage");

				Bukkit.broadcastMessage(plugin.getPluginManager().toHex(me)
						.replaceAll("<level>", "" + event.getNewLevel().getLevel())
						.replaceAll("<level_rank>",
								job.getLevelRankDisplay(event.getNewLevel().getLevel(), "" + player.getUniqueId()))
						.replaceAll("<job>", job.getDisplayOfJob("" + player.getUniqueId())).replaceAll("<name>", name)
						.replaceAll("&", "§"));
			}

			if (event.getNewLevel().hasSong()) {

				if (plugin.getPluginManager().isInstalled("NoteBlockAPI")) {

					String path = event.getNewLevel().getSongPath();

					plugin.getNoteBlockManager().playSong(player, job, path);
				}
			}
		}

	}

	@SuppressWarnings("deprecation")
	public void check(Player player, Job job, JobsPlayer pl, String block) {
		new BukkitRunnable() {

			@Override
			public void run() {

				UUID UUID = player.getUniqueId();

				PluginManager api = UltimateJobs.getPlugin().getPluginManager();
				FileConfiguration cfg = plugin.getLocalFileManager().getConfig();
				String prefix = pl.getLanguage().getPrefix();

				int old_level = pl.getStatsOf(job.getConfigID()).getLevel();
				int new_level = old_level + 1;

				if (old_level >= job.getLevels().size()) {
					return;
				}

				if (!canLevelMore("" + UUID, job, new_level)) {

					if (canlevelUp(job, pl)) {

						if (!canLevelMore("" + UUID, job, new_level)) {

							if (canlevelUp(job, pl)) {

								// rewards

								plugin.getPlayerAPI().updateLevelOf("" + UUID, job, new_level);
								plugin.getPlayerAPI().updateExp("" + UUID, job, 0);

								JobLevel tp = job.getLevels().get(new_level);

								new BukkitRunnable() {
									public void run() {
										PlayerLevelJobEvent e = new PlayerLevelJobEvent(player, pl, job, tp);

										levelPlayerUP(e);
									}
								}.runTaskLater(plugin, 1);

								if (job.isVaultOnLevel(new_level)) {

									double money = job.getVaultOnLevel(new_level);

									boolean ys = true;

									if (plugin.getLocalFileManager().getConfig().getString("PayMentMode").toUpperCase()
											.equalsIgnoreCase("STORED")) {
										if (plugin.getLocalFileManager().getConfig()
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
									String title_1 = api.toHex(pl.getLanguage().getMessage("Levels.Ttitle_1")
											.replaceAll("<job>", job.getDisplayOfJob("" + UUID))
											.replaceAll("<prefix>", prefix).replaceAll("<level_name>", level_name)
											.replaceAll("<level_rank>", job.getLevelRankDisplay(new_level, "" + UUID))
											.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));

									String title_2 = api.toHex(pl.getLanguage().getMessage("Levels.Ttitle_2")
											.replaceAll("<job>", job.getDisplayOfJob("" + UUID))
											.replaceAll("<level_rank>", job.getLevelRankDisplay(new_level, "" + UUID))
											.replaceAll("<prefix>", prefix).replaceAll("<level_name>", level_name)
											.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));
									player.sendTitle(title_1, title_2);
								}

								if (cfg.getBoolean("Levels.Enable_Message")) {
									String message = api.toHex(pl.getLanguage().getMessage("Levels.Message")
											.replaceAll("<job>", job.getDisplayOfJob("" + UUID))
											.replaceAll("<level_rank>", job.getLevelRankDisplay(new_level, "" + UUID))
											.replaceAll("<prefix>", prefix).replaceAll("<level_name>", level_name)
											.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));
									player.sendMessage(message);
								}

								if (cfg.getBoolean("Levels.Enabled_Actionbar")) {
									String message = api.toHex(pl.getLanguage().getMessage("Levels.Actionbar")
											.replaceAll("<level_rank>", job.getLevelRankDisplay(new_level, "" + UUID))
											.replaceAll("<job>", job.getDisplayOfJob("" + UUID))
											.replaceAll("<prefix>", prefix).replaceAll("<level_name>", level_name)
											.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));
									player.sendMessage(message);
								}
							}
						}
					}
				}
				cancel();
			}
		}.runTaskAsynchronously(plugin);
	}

}
