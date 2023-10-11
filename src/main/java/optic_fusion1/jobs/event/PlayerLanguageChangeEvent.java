package optic_fusion1.jobs.event;

import optic_fusion1.jobs.util.Language;
import optic_fusion1.jobs.job.JobsPlayer;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLanguageChangeEvent extends Event {

    private static HandlerList list = new HandlerList();

    public UUID id;
    public JobsPlayer pl;
    public Player player;

    public Language OldLanguage;
    public Language NewLanguage;

    public PlayerLanguageChangeEvent(Player player, JobsPlayer pl, UUID ID, Language old, Language newl) {

        this.player = player;
        this.id = ID;
        this.pl = pl;
        this.OldLanguage = old;
        this.NewLanguage = newl;

        Bukkit.getPluginManager().callEvent(this);
    }

    public Language getNewLanguage() {
        return NewLanguage;
    }

    public Language getOldLanguage() {
        return OldLanguage;
    }

    public Player getPlayer() {
        return player;
    }

    public JobsPlayer getJobsPlayer() {
        return pl;
    }

    public UUID getID() {
        return id;
    }

    public HandlerList getHandlers() {
        return list;
    }

    public static HandlerList getHandlerList() {
        return list;
    }
}
