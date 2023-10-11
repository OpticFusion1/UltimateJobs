package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;

public class JobActionVillagerTrade extends AbstractJobAction {

    public JobActionVillagerTrade(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        if (event.isCancelled() || event.getInventory().getType() != InventoryType.MERCHANT) {
            return;
        }
        MerchantInventory merchantInventory = (MerchantInventory) event.getInventory();
        ItemStack itemStack = merchantInventory.getItem(2);
        ItemStack result = merchantInventory.getSelectedRecipe().getResult();
        if (event.getSlot() != 2 || itemStack == null
                || result == null || itemStack.getType() != result.getType()) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int amount = result.getAmount();
        getJobWorkManager().executeVilBuyTrade(result.getType().name(), player.getUniqueId(), amount);
    }

}
