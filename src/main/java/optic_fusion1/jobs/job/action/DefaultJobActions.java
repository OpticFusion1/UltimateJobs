package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.api.BlockAPI;
import java.util.List;
import java.util.UUID;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class DefaultJobActions extends AbstractJobAction {

    private BlockAPI blockAPI;

    public DefaultJobActions(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
        blockAPI = ultimateJobs.getBlockAPI();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExtendMove(BlockPistonExtendEvent event) {
        if (event.isCancelled() || event.getBlocks() == null) {
            return;
        }
        setMetadataForBlocks(event.getBlocks(), blockAPI.getPlacedBy(event.getBlock()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRetractMove(BlockPistonRetractEvent event) {
        if (event.isCancelled() || event.getBlocks() == null) {
            return;
        }
        setMetadataForBlocks(event.getBlocks(), blockAPI.getPlacedBy(event.getBlock()));
    }

    private void setMetadataForBlocks(List<Block> blocks, UUID uuid) {
        if (uuid == null) {
            return;
        }
        for (Block block : blocks) {
            setBlockMetadata(block, uuid);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        setBlockMetadata(event.getBlock(), event.getPlayer().getUniqueId());
    }

    private void setBlockMetadata(Block block, UUID playerUUID) {
        block.setMetadata("placed-by-player", new FixedMetadataValue(getUltimateJobs(), playerUUID));
        blockAPI.addBlock(block, playerUUID);
    }

}
