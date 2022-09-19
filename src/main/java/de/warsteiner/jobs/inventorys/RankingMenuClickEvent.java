package de.warsteiner.jobs.inventorys;
 
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.objects.JobsPlayer; 

public class RankingMenuClickEvent implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if (e.getClickedInventory() == null) {
			return;
		}
		if (e.getCurrentItem() == null) {
			return;
		}

		if (e.getView().getTitle() == null) {
			return;
		}

		if (e.getCurrentItem().getItemMeta() == null) {
			return;
		}

		if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
			return;
		}
		
		if(!plugin.getPlayerAPI().existInCacheByUUID(""+e.getWhoClicked().getUniqueId())) {
			e.getWhoClicked().sendMessage("§cError while executing the Jobs ClickEvent. (Player not found)");
			return;
		}

		FileConfiguration cfg = plugin.getFileManager().getRankingGlobalConfig();
		FileConfiguration cfg2 = plugin.getFileManager().getRankingPerJobConfig();

		Player p = (Player) e.getWhoClicked();
		 
		String display = plugin.getPluginManager()
				.toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String title = plugin.getPluginManager().toHex(e.getView().getTitle().replaceAll("&", "§"));
		
		JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(p.getUniqueId());
	 
		if (plugin.getGUIOpenManager().isGlobalRankingMenu(p, title) != null) {
		 
			plugin.getClickManager().executeCustomItem(null, display, p, "Global_Custom", cfg, null);
			
			 e.setCancelled(true);
			
		}  else if(plugin.getGUIOpenManager().isRankingJobMenu(p, title) != null) {
			
			Job j = plugin.getGUIOpenManager().isRankingJobMenu(p, title);

			plugin.getClickManager().executeCustomItem(j, display, p, "PerJobRanking_Custom", cfg2, null);
			
			if (cfg2.getBoolean("Categories.EnabledCategorieItem")) {
				
				int slot = cfg2.getInt("Categories.CatSlot");
				
				if(e.getSlot() == slot) {
					
					String current = plugin.getPlayerDataAPI().getSettingData(""+p.getUniqueId(), "RANKING");
					
					String next = null;
					
					List<String> d = cfg2.getStringList("Categories.List");
					
					for (int i = 0; i != d.size(); i++) {
						
						String cr = d.get(i).toUpperCase();
						
						if(cr.equalsIgnoreCase(current.toUpperCase())) {
							int now = i;
							
							int nx = now + 1;
							 
							if(nx <= d.size()-1) {
								next = d.get(nx);
							} else {
								next = d.get(0);
							}
							
						}
						
					}
					
					if(next != null) {
						plugin.getPlayerDataAPI().updateSettingData(""+p.getUniqueId(), "RANKING", next.toUpperCase());
					 
						plugin.getAPI().playSound("UPDATED_RANKING_JOB", p);
						
						plugin.getGUIAddonManager().updateJobRankingGUI(p, title, jb, j);
					}
					
				}
				
			}
			
			e.setCancelled(true);
		}
	}
}