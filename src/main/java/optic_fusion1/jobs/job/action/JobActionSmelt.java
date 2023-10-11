package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class JobActionSmelt extends AbstractJobAction {

    public JobActionSmelt(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSmelt(InventoryClickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Inventory inventory = event.getInventory();
        ItemStack itemStack = inventory.getItem(2);
        if (inventory.getType() != InventoryType.FURNACE
                || event.getSlot() != 2
                || itemStack == null) {
            return;
        }
        Material material = event.getCursor().getType();
        if (!material.equals(Material.AIR)) {
            return;
        }
        int amount = itemStack.getAmount();
        getJobWorkManager().executeSmelt(itemStack.getType().toString(),
                event.getWhoClicked().getUniqueId(), amount);
    }

}
