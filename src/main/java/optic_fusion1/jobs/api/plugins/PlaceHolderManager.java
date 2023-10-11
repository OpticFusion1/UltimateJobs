package optic_fusion1.jobs.api.plugins;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.job.Job;
import optic_fusion1.jobs.job.JobsPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceHolderManager extends PlaceholderExpansion {

    private UltimateJobs plugin;

    public PlaceHolderManager(UltimateJobs plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull
    String getIdentifier() {
        return "jobs";
    }

    @Override
    public @NotNull
    String getAuthor() {
        return "Warsteiner37";
    }

    @Override
    public @NotNull
    String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String pr) {
        JobsPlayer j = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());

        String UUID = "" + player.getUniqueId();

        if (pr.equalsIgnoreCase("job_player_points")) {

            if (j != null) {
                return plugin.getAPI().Format(plugin.getPlayerAPI().getPoints(UUID));
            }

            return "0.0";

        } else if (pr.equalsIgnoreCase("job_player_own")) {

            String dis = " ";

            if (j != null) {
                if (j.getOwnJobs().size() == 0) {
                    dis = j.getLanguage().getMessage("placeholder_no_job");
                } else {
                    dis = plugin.getJobCache().get(j.getOwnJobs().get(0)).getDisplayOfJob(j.getUUIDAsString());
                }
            }

            return dis;

        } else if (pr.equalsIgnoreCase("job_player_current")) {

            String dis = " ";
            if (j != null) {
                if (j.getCurrentJobs().size() == 0) {
                    dis = j.getLanguage().getMessage("placeholder_no_job");
                } else {
                    dis = plugin.getJobCache().get(j.getCurrentJobs().get(0)).getDisplayOfJob(j.getUUIDAsString());
                }
            }
            return dis;

        } else if (pr.contains("job_topranking_global")) {
            String[] split = pr.split("_");

            int rank = Integer.valueOf(split[3]);

            String dis = " ";

            if (plugin.getPlayerAPI().getPlaceOfGlobalPlayer(rank).equalsIgnoreCase("Unknown")) {
                dis = "Unknown";
            } else {
                dis = plugin.getPlayerAPI().getDisplayByUUID(plugin.getPlayerAPI().getPlaceOfGlobalPlayer(rank));
            }

            return dis;

        } else if (pr.contains("job_topranking_level")) {
            String[] split = pr.split("_");
            String internal = split[3];

            int rank = Integer.valueOf(split[4]);

            Job job = plugin.getJobCache().get(internal.toUpperCase());

            String dis = " ";

            if (plugin.getPlayerAPI().getPlaceOfLevelsJob(job, rank).equalsIgnoreCase("Unknown")) {
                dis = "Unknown";
            } else {
                dis = plugin.getPlayerAPI().getDisplayByUUID(plugin.getPlayerAPI().getPlaceOfLevelsJob(job, rank));
            }

            return dis;

        } else if (pr.contains("job_topranking_blocks")) {
            String[] split = pr.split("_");
            String internal = split[3];

            int rank = Integer.valueOf(split[4]);

            Job job = plugin.getJobCache().get(internal.toUpperCase());

            String dis = " ";

            if (plugin.getPlayerAPI().getPlaceOfBlocksJob(job, rank).equalsIgnoreCase("Unknown")) {
                dis = "Unknown";
            } else {
                dis = plugin.getPlayerAPI().getDisplayByUUID(plugin.getPlayerAPI().getPlaceOfBlocksJob(job, rank));
            }

            return dis;

        } else if (pr.contains("job_topranking_earnings")) {
            String[] split = pr.split("_");
            String internal = split[3];

            int rank = Integer.valueOf(split[4]);

            Job job = plugin.getJobCache().get(internal.toUpperCase());

            String dis = " ";

            if (plugin.getPlayerAPI().getPlaceOfEarningsJob(job, rank).equalsIgnoreCase("Unknown")) {
                dis = "Unknown";
            } else {
                dis = plugin.getPlayerAPI().getDisplayByUUID(plugin.getPlayerAPI().getPlaceOfEarningsJob(job, rank));
            }

            return dis;

        } else if (pr.contains("job_ranking_global")) {

            return plugin.getPlayerAPI().getRankOfGlobalPlayer("" + UUID);

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

        } else if (pr.contains("job_current_levelrank")) {
            String[] split = pr.split("_");
            String internal = split[3];
            if (j != null) {
                if (j.getCurrentJobs().size() != 0) {

                    if (j.getCurrentJobs().contains(internal.toUpperCase())) {

                        Job job = plugin.getJobCache().get(internal.toUpperCase());
                        return job.getLevelRankDisplay(j.getStatsOf(internal).getLevel(), UUID);

                    } else {
                        return "Unknown";
                    }
                } else {
                    return j.getLanguage().getMessage("placeholder_no_job");
                }
            }
            return " ";
        } else if (pr.contains("job_current_name")) {
            String[] split = pr.split("_");
            String internal = split[3];
            if (j != null) {
                if (j.getCurrentJobs().size() != 0) {

                    if (j.getCurrentJobs().contains(internal.toUpperCase())) {

                        Job job = plugin.getJobCache().get(internal.toUpperCase());
                        return job.getDisplayOfJob(UUID);

                    } else {
                        return j.getLanguage().getMessage("placeholder_no_job");
                    }
                } else {
                    return j.getLanguage().getMessage("placeholder_no_job");
                }
            }
            return " ";
        } else if (pr.contains("job_own_contains")) {
            String[] split = pr.split("_");
            String internal = split[3];
            if (j != null) {
                if (j.getOwnJobs().size() != 0) {

                    if (j.getOwnJobs().contains(internal.toUpperCase())) {

                        return "true";

                    } else {
                        return "false";
                    }
                } else {
                    return "false";
                }
            }
            return "false";
        } else if (pr.contains("job_current_contains")) {
            String[] split = pr.split("_");
            String internal = split[3];
            if (j != null) {
                if (j.getCurrentJobs().size() != 0) {

                    if (j.getCurrentJobs().contains(internal.toUpperCase())) {

                        return "true";

                    } else {
                        return "false";
                    }
                } else {
                    return "false";
                }
            }
            return "false";
        } else if (pr.contains("job_current_level")) {
            String[] split = pr.split("_");
            String internal = split[3];
            if (j != null) {
                if (j.getCurrentJobs().size() != 0) {

                    if (j.getCurrentJobs().contains(internal.toUpperCase())) {

                        return "" + j.getStatsOf(internal.toUpperCase()).getLevel();
                    } else {
                        return j.getLanguage().getMessage("placeholder_no_level");
                    }
                } else {
                    return j.getLanguage().getMessage("placeholder_no_level");
                }
            }
            return " ";
        } else if (pr.contains("job_current_exp")) {
            String[] split = pr.split("_");
            String internal = split[3];
            if (j != null) {
                if (j.getCurrentJobs().size() != 0) {
                    if (j.getCurrentJobs().contains(internal.toUpperCase())) {

                        return "" + j.getStatsOf(internal.toUpperCase()).getExp();
                    } else {
                        return j.getLanguage().getMessage("placeholder_no_exp");
                    }
                } else {
                    return j.getLanguage().getMessage("placeholder_no_exp");
                }
            }
            return " ";
        } else if (pr.contains("job_current_levelname")) {
            String[] split = pr.split("_");
            String internal = split[3];
            if (j != null) {
                if (j.getCurrentJobs().size() != 0) {

                    if (j.getCurrentJobs().contains(internal.toUpperCase())) {
                        Job job = plugin.getJobCache().get(internal.toUpperCase());
                        int lvl = j.getStatsOf(internal.toUpperCase()).getLevel();
                        return job.getLevelDisplay(lvl, UUID);
                    } else {
                        return j.getLanguage().getMessage("placeholder_no_levelname");
                    }
                } else {
                    return j.getLanguage().getMessage("placeholder_no_levelname");
                }
            }
            return " ";

        }

        return null;
    }
}
