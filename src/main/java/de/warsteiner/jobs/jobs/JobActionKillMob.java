package de.warsteiner.jobs.jobs;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

import de.warsteiner.jobs.UltimateJobs;
import io.lumine.mythic.api.mobs.entities.SpawnReason;

public class JobActionKillMob implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(EntityDeathEvent event) {

		if (event.getEntity().getKiller() == null) {
			return;
		}

		if (!plugin.getLocalFileManager().getConfig().getBoolean("CanEarnMoneyFromSpawnerMobs")) {
			if (event.getEntity().hasMetadata("spawned-by-spawner")) {
				return;
			}
		}

		if (event.getEntity().getKiller() instanceof Player) {
			Player killer = (Player) event.getEntity().getKiller();
			plugin.getJobWorkManager().executeKillWork(event.getEntity(), killer);
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(CreatureSpawnEvent event) {
		if (event.getSpawnReason().equals(org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER)) {
			event.getEntity().setMetadata("spawned-by-spawner",
					new FixedMetadataValue(UltimateJobs.getPlugin(), "spawned-by-spawner"));
		}
	}

}
