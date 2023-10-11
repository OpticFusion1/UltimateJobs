package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.Tag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class JobActionStripLog extends AbstractJobAction {

    public JobActionStripLog(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onStripLog(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK
                || !Tag.LOGS.isTagged(event.getClickedBlock().getType())
                || !isAxe(event.getItem())) {
            return;
        }
        getJobWorkManager().executeStripLogWork(event);
    }

    private static boolean isAxe(ItemStack item) {
        if (item == null || item.getAmount() == 0) {
            return false;
        }
        return item.getType().name().endsWith("_AXE");
    }

}
