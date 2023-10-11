package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.api.BlockAPI;
import java.util.UUID;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockGrowEvent;

public class JobActionFarmGrow extends AbstractJobAction {

    public JobActionFarmGrow(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockGrow(BlockGrowEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Block block = event.getBlock();
        BlockAPI blockAPI = getUltimateJobs().getBlockAPI();
        if (block == null || blockAPI.getPlacedBy(block) != null) {
            return;
        }
        UUID player = blockAPI.getPlacedBy(block);
        getJobWorkManager().executeFarmGrowWork(block, player);
    }

}
