package optic_fusion1.jobs.job.action.itemsadder;

import optic_fusion1.jobs.UltimateJobs;
import dev.lone.itemsadder.api.Events.CustomEntityDeathEvent;
import optic_fusion1.jobs.job.action.AbstractJobAction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class JobAction_IA_Kill extends AbstractJobAction {

    public JobAction_IA_Kill(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(CustomEntityDeathEvent event) {
        if (event.getEntity() == null) {
            return;
        }
        Player p = (Player) event.getKiller();
        String id = event.getNamespacedID();
        Entity ent = event.getEntity();
        getUltimateJobs().getItemsAdderManager().executeKillWork(p, ent, id);
    }
}
