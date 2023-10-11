package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.loot.LootTable;

public class JobActionFindATreasure extends AbstractJobAction {

    public JobActionFindATreasure(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOpenChest(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || clickedBlock.getType() != Material.CHEST) {
            return;
        }
        Chest chest = (Chest) clickedBlock.getState();
        LootTable lootTable = chest.getLootTable();
        if (lootTable == null) {
            return;
        }
        String type = lootTable.toString().replaceAll("minecraft:chests/", "    ").replaceAll(" ", "");
        getJobWorkManager().executeTreasureEvent(type, player);
    }

}
