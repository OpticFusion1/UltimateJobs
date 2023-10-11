package optic_fusion1.jobs.event;

import optic_fusion1.jobs.job.Job;
import optic_fusion1.jobs.job.JobType;
import optic_fusion1.jobs.job.JobsPlayer;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerFinishedWorkEvent extends Event {

    private static HandlerList list = new HandlerList();

    public UUID id;
    public JobsPlayer pl;
    public Job job;
    public String ID;
    public Player player;
    public JobType ac;

    public PlayerFinishedWorkEvent(Player player, JobsPlayer plt, Job job, String id, JobType ac) {
        this.pl = plt;
        this.ac = ac;
        this.ID = id;
        this.job = job;
        this.player = player;
        Bukkit.getPluginManager().callEvent(this);
    }

    public JobType getActionUsed() {
        return ac;
    }

    public String getID() {
        return ID;
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
