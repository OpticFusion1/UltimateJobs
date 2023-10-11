package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class JobActionShear extends AbstractJobAction {

    public JobActionShear(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent(PlayerShearEntityEvent event) {
        getJobWorkManager().executeShearWork(event);
    }

}
