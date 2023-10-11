package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class JobActionKillMob extends AbstractJobAction {

    public JobActionKillMob(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) {
            return;
        }
        LivingEntity entity = event.getEntity();
        if (entity.hasMetadata("spawned-by-spawner") && !getUltimateJobs().getLocalFileManager()
                .getConfig().getBoolean("CanEarnMoneyFromSpawnerMobs")) {
            return;
        }

        getJobWorkManager().executeKillWork(entity, killer);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent(CreatureSpawnEvent event) {
        if (event.getSpawnReason().equals(org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER)) {
            event.getEntity().setMetadata("spawned-by-spawner",
                    new FixedMetadataValue(getUltimateJobs(), "spawned-by-spawner"));
        }
    }

}
