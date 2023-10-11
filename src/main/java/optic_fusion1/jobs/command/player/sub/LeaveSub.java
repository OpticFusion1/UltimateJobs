package optic_fusion1.jobs.command.player.sub;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.job.JobsPlayer;
import java.util.UUID;
import optic_fusion1.jobs.command.player.sub.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveSub extends SubCommand {

    public LeaveSub(UltimateJobs plugin) {
        super(plugin);
    }

    @Override
    public String getName(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Leave.Usage");
    }

    @Override
    public String getDescription(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Leave.Description");
    }

    @Override
    public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
        final Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        if (args.length == 2) {
            String d = args[1].toUpperCase();

            if (!plugin.getAPI().checkIfJobIsReal(d.toUpperCase(), player)) {
                return;
            }

            String job = plugin.getAPI().checkIfJobIsRealAndGet(d.toUpperCase(), player).getConfigID();

            if (jb.isInJob(job)) {
                plugin.getAPI().playSound("COMMAND_LEAVE_SUCCESS", player);
                jb.remCurrentJob(job);
                player.sendMessage(jb.getLanguage().getMessage("command_leave_message").replaceAll("<job>", plugin.getAPI().checkIfJobIsRealAndGet(d.toUpperCase(), player).getDisplayOfJob("" + UUID)));
            } else {
                player.sendMessage(jb.getLanguage().getMessage("command_leave_already"));
                plugin.getAPI().playSound("COMMAND_LEAVE_ALREADY", player);
            }

        } else {
            plugin.getAPI().playSound("COMMAND_USAGE", player);
            player.sendMessage(
                    jb.getLanguage().getMessage("command_usage").replaceAll("<usage>", getUsage(UUID)));
        }
    }

    @Override
    public String formatTab() {
        return "command leave jobs_in";
    }

    @Override
    public int getTabLength() {
        return 2;
    }

    @Override
    public boolean isEnabled() {
        return plugin.getLocalFileManager().getCMDSettings().getBoolean("Commands.Leave.Enabled");
    }

    @Override
    public String getUsage(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Leave.UsageMessage");
    }

}
