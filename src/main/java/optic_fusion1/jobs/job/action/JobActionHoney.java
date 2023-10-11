package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class JobActionHoney extends AbstractJobAction {

    public JobActionHoney(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExtractHoney(PlayerInteractEvent event) {
        Action action = event.getAction();
        Block clickedBlock = event.getClickedBlock();
        if (action != Action.RIGHT_CLICK_BLOCK || clickedBlock == null) {
            return;
        }
        Material material = clickedBlock.getType();
        if (material != Material.BEEHIVE && material != Material.BEE_NEST) {
            return;
        }
        BlockData blockData = clickedBlock.getBlockData();
        Beehive beehive = (Beehive) blockData;
        if (beehive.getHoneyLevel() != beehive.getMaximumHoneyLevel()) {
            // TODO: Check if this is needed still
//            if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {
//                event.setCancelled(true);
//            }
            return;
        }
        getJobWorkManager().executeHoneyAction(event);
    }
}
