package optic_fusion1.jobs.command.player.sub;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.gui.UpdateTypes;
import optic_fusion1.jobs.job.JobsPlayer;
import java.util.UUID;
import optic_fusion1.jobs.command.player.sub.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsSub extends SubCommand {

    public StatsSub(UltimateJobs plugin) {
        super(plugin);
    }

    @Override
    public String getName(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Stats.Usage");
    }

    @Override
    public String getDescription(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Stats.Description");
    }

    @Override
    public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
        final Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        if (args.length == 1) {
            plugin.getGUIAddonManager().createSelfStatsGUI(player, UpdateTypes.OPEN);
            return;
        }
        if (args.length == 2) {
            String pl = args[1].toUpperCase();

            if (plugin.getPlayerAPI().getJobsPlayerByName(pl.toLowerCase()) == null) {
                plugin.getAPI().playSound("COMMAND_PLAYER_NOT_FOUND", player);
                player.sendMessage(jb.getLanguage().getMessage("command_stats_not_found")
                        .replaceAll("<name>", args[1]));
                return;
            } else {
                String uuid = plugin.getPlayerAPI().getJobsPlayerByName(pl.toLowerCase());

                if (uuid.equalsIgnoreCase("" + player.getUniqueId())) {
                    plugin.getGUIAddonManager().createSelfStatsGUI(player, UpdateTypes.OPEN);
                } else {
                    plugin.getGUIAddonManager().createOtherStatsGUI(player, UpdateTypes.OPEN, args[1], uuid);
                }
            }
            return;
        } else {
            plugin.getAPI().playSound("COMMAND_USAGE", player);
            player.sendMessage(
                    jb.getLanguage().getMessage("command_usage").replaceAll("<usage>", getUsage(UUID)));
        }
    }

    @Override
    public String formatTab() {
        return "command stats players_online";
    }

    @Override
    public int getTabLength() {
        return 1;
    }

    @Override
    public boolean isEnabled() {
        return plugin.getLocalFileManager().getCMDSettings().getBoolean("Commands.Stats.Enabled");
    }

    @Override
    public String getUsage(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Stats.UsageMessage");
    }

}
