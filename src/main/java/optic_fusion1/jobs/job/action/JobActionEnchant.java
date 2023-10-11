package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class JobActionEnchant extends AbstractJobAction {

    public JobActionEnchant(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnchantItem(EnchantItemEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getEnchanter();
        int amount = 0;
        for (Map.Entry entry : event.getEnchantsToAdd().entrySet()) {
            Enchantment enchantment = (Enchantment) entry.getKey();
            amount += (Integer) entry.getValue();
            if (amount > 0) {
                String id = enchantment.getKey().getKey().toUpperCase() + "_" + amount;
                getJobWorkManager().executeEnchant(id, player.getUniqueId());
            }
        }
    }

}
