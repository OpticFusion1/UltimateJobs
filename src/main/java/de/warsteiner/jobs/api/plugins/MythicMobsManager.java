package de.warsteiner.jobs.api.plugins;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.objects.jobs.Job;
import de.warsteiner.jobs.utils.objects.jobs.JobAction;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;

public class MythicMobsManager {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public void executeWork(MythicMobDeathEvent event) {
		String type = "" + event.getMobType().getInternalName();
		if (event.getKiller() instanceof Player) {
			UUID UUID = ((Player) event.getKiller()).getUniqueId();
			if (plugin.getJobWorkManager().getJobOnWork("" + UUID, JobAction.MMKILL, "" + type) != null) {

				Job job = plugin.getJobWorkManager().getJobOnWork("" + UUID, JobAction.MMKILL, "" + type);

				plugin.getJobWorkManager().finalWork(type, UUID, JobAction.MMKILL, "mmkill-action", 1, null,
						event.getEntity(), true, false, true, job);
				return;
			}
		}
	}

	public boolean existMob(String id) {

		return MythicBukkit.inst().getMobManager().getMythicMob(id) != null;

	}

}
