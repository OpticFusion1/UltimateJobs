package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class JobActionItemPickUp extends AbstractJobAction {

    public JobActionItemPickUp(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getItem() == null || !(event.getEntity() instanceof Player player)
                || event.isCancelled() || event.getItem().hasMetadata("dropped-by-player")) {
            return;
        }

        String type = event.getItem().getItemStack().getType().toString().toUpperCase();
        getJobWorkManager().executePickUpWork(player, type);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnDrop(PlayerDropItemEvent event) {
        event.getItemDrop().setMetadata("dropped-by-player", 
                new FixedMetadataValue(getUltimateJobs(), "dropped-by-player"));
    }
}
