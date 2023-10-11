package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class JobActionAdvancement extends AbstractJobAction {

    public JobActionAdvancement(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {
        getJobWorkManager().executeAchWork(event);
    }

}
