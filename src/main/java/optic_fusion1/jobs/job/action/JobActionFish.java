package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;

public class JobActionFish extends AbstractJobAction {

    public JobActionFish(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFish(PlayerFishEvent event) {
        if (event.getCaught() == null || event.isCancelled()) {
            return;
        }
        getJobWorkManager().executeFishWork(event.getCaught(), event.getPlayer());
    }

}
