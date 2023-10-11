package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.CraftItemEvent;

public class JobActionCraft extends AbstractJobAction {

    public JobActionCraft(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemCraft(CraftItemEvent event) {
        getJobWorkManager().executeCraftWork(event);
    }

}
