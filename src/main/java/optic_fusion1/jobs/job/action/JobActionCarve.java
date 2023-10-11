package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

public class JobActionCarve extends AbstractJobAction {

    public JobActionCarve(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCarve(PlayerInteractEvent event) {
        getJobWorkManager().executeCarveWork(event);
    }

}
