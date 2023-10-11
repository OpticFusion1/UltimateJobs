package optic_fusion1.jobs.event;

import optic_fusion1.jobs.job.Job;
import optic_fusion1.jobs.job.JobLevel;
import optic_fusion1.jobs.job.JobsPlayer;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLevelJobEvent extends Event {

    private static HandlerList list = new HandlerList();

    public UUID id;
    public JobsPlayer pl;
    public Job job;
    public Player player;
    public JobLevel level;

    public PlayerLevelJobEvent(Player player, JobsPlayer plt, Job job, JobLevel level) {
        this.pl = plt;
        this.level = level;
        this.job = job;
        this.player = player;
        Bukkit.getPluginManager().callEvent(this);
    }

    public JobLevel getNewLevel() {
        return level;
    }

    public HandlerList getHandlers() {
        return list;
    }

    public Job getJob() {
        return job;
    }

    public JobsPlayer getJobsPlayer() {
        return pl;
    }

    public Player getPlayer() {
        return player;
    }

    public static HandlerList getHandlerList() {
        return list;
    }
}
