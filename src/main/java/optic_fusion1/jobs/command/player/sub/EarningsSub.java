package optic_fusion1.jobs.command.player.sub;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.gui.UpdateTypes;
import optic_fusion1.jobs.job.Job;
import optic_fusion1.jobs.job.JobsPlayer;
import java.util.UUID;
import optic_fusion1.jobs.command.player.sub.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EarningsSub extends SubCommand {

    public EarningsSub(UltimateJobs plugin) {
        super(plugin);
    }

    @Override
    public String getName(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Earnings.Usage");
    }

    @Override
    public String getDescription(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Earnings.Description");
    }

    @Override
    public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
        final Player player = (Player) sender;
        UUID UUID = player.getUniqueId();

        if (args.length == 2) {
            String notreal = args[1].toUpperCase();

            if (!plugin.getAPI().checkIfJobIsReal(notreal.toUpperCase(), player)) {
                plugin.getAPI().playSound("COMMAND_JOB_NOT_FOUND", player);
                return;
            }

            Job real = plugin.getAPI().checkIfJobIsRealWithResult(notreal.toUpperCase(), player);

            plugin.getGUIAddonManager().createEarningsGUI_Single_Job(player, UpdateTypes.OPEN, real);

            return;
        } else if (args.length == 1) {

            if (plugin.getLocalFileManager().getEarningsAllConfig().getBoolean("Enabled")) {

                plugin.getGUIAddonManager().createEarningsGUI_ALL_Jobs(player, UpdateTypes.OPEN);

            } else {
                plugin.getAPI().playSound("COMMAND_USAGE", player);
                player.sendMessage(
                        jb.getLanguage().getMessage("command_usage").replaceAll("<usage>", getUsage(UUID)));
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
        return "command earnings jobs_listed";
    }

    @Override
    public int getTabLength() {
        return 1;
    }

    @Override
    public boolean isEnabled() {
        return plugin.getLocalFileManager().getCMDSettings().getBoolean("Commands.Earnings.Enabled");
    }

    @Override
    public String getUsage(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Earnings.UsageMessage");
    }

}
