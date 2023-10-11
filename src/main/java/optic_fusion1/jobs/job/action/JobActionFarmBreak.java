package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class JobActionFarmBreak extends AbstractJobAction {

    public JobActionFarmBreak(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFarm(BlockBreakEvent event) {
        getJobWorkManager().executeFarmWork(event);
    }

}
