package optic_fusion1.jobs.job.action.mythocmobs;

import optic_fusion1.jobs.UltimateJobs;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import optic_fusion1.jobs.job.action.AbstractJobAction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class JobActionMMKill extends AbstractJobAction {

    public JobActionMMKill(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent(MythicMobDeathEvent event) {
        if (event.getEntity() == null || event.getKiller() == null) {
            return;
        }
        getUltimateJobs().getMythicMobsManager().executeWork(event);
    }
}
