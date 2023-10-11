package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class JobActionKillByBow extends AbstractJobAction {

    public JobActionKillByBow(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event) {
        EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();
        if (!(damageEvent instanceof EntityDamageByEntityEvent damageByEventEvent)) {
            return;
        }
        Entity entity = damageByEventEvent.getDamager();
        if (!(entity instanceof Arrow arrow) || damageEvent.isCancelled()
                || !(arrow.getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player) arrow.getShooter();
        EntityType type = event.getEntity().getType();
        getJobWorkManager().executeKillByBowEvent(player, type.toString(), event.getEntity());
    }

}
