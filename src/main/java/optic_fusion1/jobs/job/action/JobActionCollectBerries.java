package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerHarvestBlockEvent;

public class JobActionCollectBerries extends AbstractJobAction {

    public JobActionCollectBerries(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHarvestBlock(PlayerHarvestBlockEvent event) {
        if (event.isCancelled() || event.getHarvestedBlock() == null) {
            return;
        }
        String id = event.getHarvestedBlock().getType().toString();
        getJobWorkManager().executeBerrysEvent(event.getPlayer(), id, event.getHarvestedBlock());
    }

}
