package optic_fusion1.jobs.command.admin.sub;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.command.CommandSender;

public abstract class AdminSubCommand {

    public UltimateJobs plugin;

    public AdminSubCommand(UltimateJobs plugin) {
        this.plugin = plugin;
    }

    public abstract String getName();

    public abstract String getUsage();

    public abstract String getDescription();

    public abstract void perform(CommandSender sender, String[] args);

    public abstract String formatTab();

    public abstract int getTabLength();

    public abstract String getPermission();

    public abstract boolean showOnHelp();
}
