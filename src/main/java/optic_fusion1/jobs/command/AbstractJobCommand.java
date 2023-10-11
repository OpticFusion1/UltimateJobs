package optic_fusion1.jobs.command;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.command.CommandExecutor;

public abstract class AbstractJobCommand implements CommandExecutor {

    public UltimateJobs plugin;

    public AbstractJobCommand(UltimateJobs plugin) {
        this.plugin = plugin;
    }

}
