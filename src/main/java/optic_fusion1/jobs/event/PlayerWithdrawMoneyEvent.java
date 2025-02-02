package optic_fusion1.jobs.event;

import optic_fusion1.jobs.job.JobsPlayer;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerWithdrawMoneyEvent extends Event implements Cancellable {

    private static HandlerList list = new HandlerList();

    public UUID id;
    public JobsPlayer pl;
    public Player player;
    boolean cancelled = false;

    public PlayerWithdrawMoneyEvent(Player player, JobsPlayer plt) {
        this.pl = plt;
        this.player = player;
        Bukkit.getPluginManager().callEvent(this);
    }

    public HandlerList getHandlers() {
        return list;
    }

    public JobsPlayer getJobsPlayer() {
        return pl;
    }

    public Player getPlayer() {
        return player;
    }

    public HandlerList getHandlerList() {
        return list;
    }

    @Override
    public boolean isCancelled() {

        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;

    }
}
