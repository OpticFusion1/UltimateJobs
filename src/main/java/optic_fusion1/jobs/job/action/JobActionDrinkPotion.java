package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;

public class JobActionDrinkPotion extends AbstractJobAction {

    public JobActionDrinkPotion(UltimateJobs ultimateJobs) {
        super(ultimateJobs);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPotionDrink(PlayerItemConsumeEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item.getType() != Material.POTION) {
            return;
        }
        PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
        PotionData potionData = potionMeta.getBasePotionData();
        int level = potionData.isUpgraded() ? 2 : 1;
        String named = potionData.getType().name().toUpperCase();
        String id = named + "_" + level;
        getJobWorkManager().executePotionDrink(id, player.getUniqueId());
    }

}
