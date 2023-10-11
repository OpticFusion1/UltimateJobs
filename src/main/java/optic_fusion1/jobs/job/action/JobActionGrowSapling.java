package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.api.BlockAPI;
import java.util.UUID;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.StructureGrowEvent;

public class JobActionGrowSapling extends AbstractJobAction {

    public JobActionGrowSapling(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGrow(StructureGrowEvent event) {
        if (event.isCancelled()) {
            return;
        }
        TreeType type = event.getSpecies();
        Block block = event.getLocation().getBlock();
        if (block == null) {
            return;
        }
        BlockAPI blockAPI = getUltimateJobs().getBlockAPI();
        if (blockAPI.getPlacedBy(block) == null) {
            return;
        }
        UUID player = blockAPI.getPlacedBy(block);
        getJobWorkManager().executeSaplingGrowAction(type.name(), player, block);
        blockAPI.removeBlock(block);
    }

}
