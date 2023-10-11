package optic_fusion1.jobs.api.plugins;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.job.Job;
import optic_fusion1.jobs.job.JobType;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import java.util.UUID;
import org.bukkit.entity.Player;

public class MythicMobsManager {

    private UltimateJobs plugin;

    public MythicMobsManager(UltimateJobs plugin) {
        this.plugin = plugin;
    }

    public void executeWork(MythicMobDeathEvent event) {
        String type = "" + event.getMobType().getInternalName();
        if (event.getKiller() instanceof Player) {
            UUID UUID = ((Player) event.getKiller()).getUniqueId();
            if (plugin.getJobWorkManager().getJobOnWork("" + UUID, JobType.MMKILL, "" + type) != null) {

                Job job = plugin.getJobWorkManager().getJobOnWork("" + UUID, JobType.MMKILL, "" + type);

                plugin.getJobWorkManager().finalWork(type, UUID, JobType.MMKILL, "mmkill-action", 1, null,
                        event.getEntity(), true, false, true, job);
                return;
            }
        }
    }

    public boolean existMob(String id) {

        return MythicBukkit.inst().getMobManager().getMythicMob(id) != null;

    }

}
