package de.warsteiner.jobs.api.plugins;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceHolderManager extends PlaceholderExpansion {

	private UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public @NotNull String getIdentifier() {
		return "jobs";
	}

	@Override
	public @NotNull String getAuthor() {
		return "Warsteiner37";
	}

	@Override
	public @NotNull String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public String onRequest(OfflinePlayer player, String pr) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());

		if (jb != null) {
			String UUID = "" + player.getUniqueId();
			Collection<String> jobs = jb.getCurrentJobs();
			Collection<String> jobs_own = jb.getOwnJobs();
			if (pr.contains("job_topranking_global")) {
				String[] split = pr.split("_");
				 
				int rank = Integer.valueOf(split[3]);
  
				String dis = null;
				
				if(plugin.getPlayerAPI().getPlaceOfGlobalPlayer(rank).equalsIgnoreCase("Unknown")) {
					dis = "Unknown";
				} else {
					dis = plugin.getPlayerAPI().getDisplayByUUID(plugin.getPlayerAPI().getPlaceOfGlobalPlayer(rank));
				}
				
				return dis;

			}  else if (pr.contains("job_topranking_level")) {
				String[] split = pr.split("_");
				String internal = split[3];
				
				int rank = Integer.valueOf(split[4]);

				Job job = plugin.getJobCache().get(internal.toUpperCase());
				 
				String dis = null;
				
				if(plugin.getPlayerAPI().getPlaceOfLevelsJob(job, rank).equalsIgnoreCase("Unknown")) {
					dis = "Unknown";
				} else {
					dis =plugin.getPlayerAPI().getDisplayByUUID(plugin.getPlayerAPI().getPlaceOfLevelsJob(job, rank));
				}
				
				return dis;
				
			}  else	if (pr.contains("job_topranking_blocks")) {
				String[] split = pr.split("_");
				String internal = split[3];
				
				int rank = Integer.valueOf(split[4]);

				Job job = plugin.getJobCache().get(internal.toUpperCase());
				 
				String dis = null;
				
				if(plugin.getPlayerAPI().getPlaceOfBlocksJob(job, rank).equalsIgnoreCase("Unknown")) {
					dis = "Unknown";
				} else {
					dis = plugin.getPlayerAPI().getDisplayByUUID(plugin.getPlayerAPI().getPlaceOfBlocksJob(job, rank));
				}
				
				return dis;

			}  else if (pr.contains("job_topranking_earnings")) {
				String[] split = pr.split("_");
				String internal = split[3];
				
				int rank = Integer.valueOf(split[4]);

				Job job = plugin.getJobCache().get(internal.toUpperCase());
			 
				String dis = null;
				
				if(plugin.getPlayerAPI().getPlaceOfEarningsJob(job, rank).equalsIgnoreCase("Unknown")) {
					dis = "Unknown";
				} else {
					dis = plugin.getPlayerAPI().getDisplayByUUID(plugin.getPlayerAPI().getPlaceOfEarningsJob(job, rank));
				}
				
				return dis;

			}  else if (pr.contains("job_ranking_global")) {
				
				return plugin.getPlayerAPI().getRankOfGlobalPlayer(""+UUID);

			} else if (pr.contains("job_ranking_level")) {
				String[] split = pr.split("_");
				String internal = split[3];

				Job job = plugin.getJobCache().get(internal.toUpperCase());
				 
				return plugin.getPlayerAPI().getRankOfLevelsJob(job, UUID);

			} else if (pr.contains("job_ranking_blocks")) {
				String[] split = pr.split("_");
				String internal = split[3];

				Job job = plugin.getJobCache().get(internal.toUpperCase());
				 
				return plugin.getPlayerAPI().getRankOfBlocksJob(job, UUID);

			} else if (pr.contains("job_ranking_earnings")) {
				String[] split = pr.split("_");
				String internal = split[3];

				Job job = plugin.getJobCache().get(internal.toUpperCase());
			 
				return plugin.getPlayerAPI().getRankOfEarningsJob(job, UUID);

			} else if (pr.contains("job_current_name")) {
				String[] split = pr.split("_");
				String internal = split[3];

				if (jobs.size() != 0) {

					if (jobs.contains(internal.toUpperCase())) {

						Job job = plugin.getJobCache().get(internal.toUpperCase());
						return job.getDisplay(UUID);

					} else {
						return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_job");
					}
				} else {
					return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_job");
				}
			} else if (pr.contains("job_own_contains")) {
				String[] split = pr.split("_");
				String internal = split[3];

				if (jobs_own.size() != 0) {

					if (jobs_own.contains(internal.toUpperCase())) {

						return "true";

					} else {
						return "false";
					}
				} else {
					return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_job");
				}
			} else if (pr.contains("job_current_contains")) {
				String[] split = pr.split("_");
				String internal = split[3];

				if (jobs.size() != 0) {

					if (jobs.contains(internal.toUpperCase())) {

						return "true";

					} else {
						return "false";
					}
				} else {
					return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_job");
				}
			} else if (pr.contains("job_current_online")) {
				String[] split = pr.split("_");
				String internal = split[3];

				if (jobs.size() != 0) {

					if (jobs.contains(internal.toUpperCase())) {

						Job job = plugin.getJobCache().get(internal.toUpperCase());
						return "" + plugin.getPlayerAPI().getOnlinePlayersInJob(job).size();

					} else {
						return "0";
					}
				} else {
					return "0";
				}
			} else if (pr.contains("job_current_level")) {
				String[] split = pr.split("_");
				String internal = split[3];

				if (jobs.size() != 0) {

					if (jobs.contains(internal.toUpperCase())) {

						return "" + jb.getStatsOf(internal.toUpperCase()).getLevel();
					} else {
						return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_level");
					}
				} else {
					return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_level");
				}
			} else if (pr.contains("job_current_exp")) {
				String[] split = pr.split("_");
				String internal = split[3];
				if (jobs.size() != 0) {
					if (jobs.contains(internal.toUpperCase())) {

						return "" + jb.getStatsOf(internal.toUpperCase()).getExp();
					} else {
						return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_exp");
					}
				} else {
					return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_exp");
				}
			} else if (pr.contains("job_current_levelname")) {
				String[] split = pr.split("_");
				String internal = split[3];

				if (jobs.size() != 0) {

					if (jobs.contains(internal.toUpperCase())) {
						Job job = plugin.getJobCache().get(internal.toUpperCase());
						int lvl = jb.getStatsOf(internal.toUpperCase()).getLevel();
						return job.getLevelDisplay(lvl, UUID);
					} else {
						return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_levelname");
					}
				} else {
					return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_levelname");
				}
			}

		}

		return null;
	}
}
