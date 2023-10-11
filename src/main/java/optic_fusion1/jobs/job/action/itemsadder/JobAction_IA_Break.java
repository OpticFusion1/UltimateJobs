package optic_fusion1.jobs.job.action.itemsadder;

import optic_fusion1.jobs.UltimateJobs;
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import optic_fusion1.jobs.job.action.AbstractJobAction;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class JobAction_IA_Break extends AbstractJobAction {

    public JobAction_IA_Break(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(CustomBlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player p = event.getPlayer();
        String id = event.getNamespacedID();
        Block block = event.getBlock();
        getUltimateJobs().getItemsAdderManager().executeBlockBreakWork(p, block, id);
    }
}
