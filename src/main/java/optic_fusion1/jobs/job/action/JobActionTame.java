package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTameEvent;

public class JobActionTame extends AbstractJobAction {

    public JobActionTame(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTame(EntityTameEvent event) {
        AnimalTamer tamer = event.getOwner();
        if (!(tamer instanceof Player)) {
            return;
        }
        getJobWorkManager().executeTameWork(event);
    }

}
