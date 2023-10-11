package optic_fusion1.jobs.listener;

import optic_fusion1.jobs.event.PlayerFinishedWorkEvent;
import optic_fusion1.jobs.event.PlayerLevelJobEvent;
import optic_fusion1.jobs.job.Job;
import optic_fusion1.jobs.job.JobType;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerRewardCommandEvent implements Listener {

    @EventHandler
    public void onReward(PlayerFinishedWorkEvent event) {
        Player player = event.getPlayer();
        String id = event.getID();
        Job job = event.getJob();
        JobType ac = event.getActionUsed();

        if (job.hasCommandsOfBlock(id, ac)) {
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

            List<String> commands = job.getCommandsOfBlock(id, ac);

            for (String command : commands) {
                Bukkit.dispatchCommand(console, command.replaceAll("<name>", player.getName()));
            }
        }
    }

    @EventHandler
    public void onReward(PlayerLevelJobEvent event) {
        Player player = event.getPlayer();
        Job job = event.getJob();
        Integer level = event.getJobsPlayer().getStatsOf(job.getConfigID()).getLevel();
        if (job.hasLevelCommands(level)) {
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

            List<String> commands = job.getLevelCommands(level);

            for (String command : commands) {
                Bukkit.dispatchCommand(console, command.replaceAll("<name>", player.getName()));
            }
        }
    }

}
