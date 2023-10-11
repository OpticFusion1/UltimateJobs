package optic_fusion1.jobs.command.player.sub;

import optic_fusion1.jobs.UltimateJobs;
import java.util.UUID;
import optic_fusion1.jobs.job.JobsPlayer;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    public UltimateJobs plugin;

    public SubCommand(UltimateJobs plugin) {
        this.plugin = plugin;
    }

    public abstract String getName(UUID UUID);

    public abstract String getDescription(UUID UUID);

    public abstract void perform(CommandSender sender, String[] args, JobsPlayer jobsPlayer);

    public abstract String formatTab();

    public abstract int getTabLength();

    public abstract boolean isEnabled();

    public abstract String getUsage(UUID UUID);
}
