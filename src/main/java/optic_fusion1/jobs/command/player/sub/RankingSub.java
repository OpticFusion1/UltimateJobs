package optic_fusion1.jobs.command.player.sub;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.gui.UpdateTypes;
import optic_fusion1.jobs.job.Job;
import optic_fusion1.jobs.job.JobsPlayer;
import java.util.UUID;
import optic_fusion1.jobs.command.player.sub.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankingSub extends SubCommand {

    public RankingSub(UltimateJobs plugin) {
        super(plugin);
    }

    @Override
    public String getName(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Ranking.Usage");
    }

    @Override
    public String getDescription(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Ranking.Description");
    }

    @Override
    public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
        final Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        if (args.length == 1) {

            if (plugin.getLocalFileManager().getRankingGlobalConfig().getBoolean("EnabledGlobalRanking")) {
                plugin.getGUIAddonManager().createGlobalRankingGUI(player, UpdateTypes.OPEN);

            } else {
                plugin.getAPI().playSound("COMMAND_USAGE", player);
                player.sendMessage(jb.getLanguage().getMessage("command_usage").replaceAll("<usage>",
                        getUsage(UUID)));
            }

        } else if (args.length == 2) {

            if (plugin.getLocalFileManager().getRankingPerJobConfig().getBoolean("EnabledRankingPerJob")) {

                String notreal = args[1].toUpperCase();

                if (!plugin.getAPI().checkIfJobIsReal(notreal.toUpperCase(), player)) {
                    plugin.getAPI().playSound("COMMAND_JOB_NOT_FOUND", player);
                    return;
                }

                Job real = plugin.getAPI().checkIfJobIsRealWithResult(notreal.toUpperCase(), player);

                plugin.getGUIAddonManager().createJobRankingGUI(player, UpdateTypes.OPEN, real);

            } else {
                plugin.getAPI().playSound("COMMAND_USAGE", player);
                player.sendMessage(jb.getLanguage().getMessage("command_usage").replaceAll("<usage>",
                        getUsage(UUID)));
            }

        } else {
            plugin.getAPI().playSound("COMMAND_USAGE", player);
            player.sendMessage(jb.getLanguage().getMessage("command_usage").replaceAll("<usage>",
                    getUsage(UUID)));
        }
    }

    @Override
    public String formatTab() {
        return "command ranking jobs_listed";
    }

    @Override
    public int getTabLength() {
        return 1;
    }

    @Override
    public boolean isEnabled() {
        return plugin.getLocalFileManager().getCMDSettings().getBoolean("Commands.Ranking.Enabled");
    }

    @Override
    public String getUsage(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Ranking.UsageMessage");
    }

}
