package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class JobActionEat extends AbstractJobAction {

    public JobActionEat(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            getJobWorkManager().executeEatAction(event);
        }
    }

}
