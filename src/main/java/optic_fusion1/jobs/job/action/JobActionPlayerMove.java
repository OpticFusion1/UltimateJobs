package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

public class JobActionPlayerMove extends AbstractJobAction {

    public JobActionPlayerMove(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        if (event.isCancelled()) {
            return;
        }
        getJobWorkManager().executeMoveAction(event.getPlayer().getUniqueId(), event);
    }

}
