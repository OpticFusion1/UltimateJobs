package optic_fusion1.jobs.api.plugins;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.job.Job;
import optic_fusion1.jobs.job.JobType;
import dev.lone.itemsadder.api.CustomStack;
import java.util.UUID;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemsAdderManager {

    private UltimateJobs plugin;

    public ItemsAdderManager(UltimateJobs plugin) {
        this.plugin = plugin;
    }

    public ItemStack checkIfItemStackExist(String ID) {
        CustomStack stack = CustomStack.getInstance(ID);
        if (stack != null) {
            ItemStack itemStack = stack.getItemStack();

            return itemStack;
        }
        return null;

    }

    public void executeKillWork(Player player, Entity e, String id) {

        UUID UUID = player.getUniqueId();

        if (plugin.getJobWorkManager().getJobOnWork("" + UUID, JobType.IA_KILL, "" + id) != null) {

            Job job = plugin.getJobWorkManager().getJobOnWork("" + UUID, JobType.IA_KILL, "" + id);

            plugin.getJobWorkManager().finalWork("" + id, UUID, JobType.IA_KILL, "ia-kill-action", 1, null, e, true, false, true,
                    job);

            return;
        }
    }

    public void executeBlockBreakWork(Player player, Block block, String id) {

        if (block.hasMetadata("placed-by-player")) {
            return;
        }

        UUID UUID = player.getUniqueId();

        if (plugin.getJobWorkManager().getJobOnWork("" + UUID, JobType.IA_BREAK, "" + id) != null) {

            Job job = plugin.getJobWorkManager().getJobOnWork("" + UUID, JobType.IA_BREAK, "" + id);

            plugin.getJobWorkManager().finalWork("" + id, UUID, JobType.IA_BREAK, "ia-break-action", 1, block, null, true, true, false,
                    job);

            return;
        }
    }

}
