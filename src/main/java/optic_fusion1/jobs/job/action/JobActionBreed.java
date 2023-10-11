package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityBreedEvent;

public class JobActionBreed extends AbstractJobAction {

    public JobActionBreed(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreed(EntityBreedEvent event) {
        if (event.getBreeder() instanceof Player) {
            getJobWorkManager().executeBreedWork(event);
        }
    }

}
