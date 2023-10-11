package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import java.util.EnumSet;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class JobActionMilk extends AbstractJobAction {

    private static final EnumSet<EntityType> VALID_MOBS = EnumSet.of(EntityType.COW, EntityType.GOAT);
    
    public JobActionMilk(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMilk(PlayerInteractAtEntityEvent event) {
        Entity clicked = event.getRightClicked();
        if (!VALID_MOBS.contains(clicked.getType())
                || event.getPlayer().getInventory().getItemInMainHand().getType() != Material.BUCKET) {
            return;
        }
        getJobWorkManager().executeMilkWork(event);
    }

}
