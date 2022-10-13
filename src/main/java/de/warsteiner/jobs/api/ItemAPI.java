package de.warsteiner.jobs.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Creating Items
*/

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.manager.FileManager;
import de.warsteiner.jobs.utils.objects.DataMode;
import de.warsteiner.jobs.utils.objects.Item;
import de.warsteiner.jobs.utils.objects.ItemAction;
import de.warsteiner.jobs.utils.objects.ItemType;
import de.warsteiner.jobs.utils.objects.PluginColor;

public class ItemAPI {

	private UltimateJobs plugin = UltimateJobs.getPlugin();

	public HashMap<String, Item> items = new HashMap<String, Item>();
	public ArrayList<String> fails = new ArrayList<String>();
	
	public HashMap<String, List<Item>> custom_items = new HashMap<String,  List<Item>>();
	
	public HashMap<String, Item> getItems() {
		return this.items;
	}
	
	public Item getItem(String named) {
		return getItems().get(named);
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack getItemStack(String named, String player, String item) {
		
		if(!getItems().containsKey(named)) {
			
			//creating items
			
			ItemStack it = null;
			
			if (plugin.getPluginManager().isInstalled("ItemsAdder")) {

				if (plugin.getItemsAdderManager().checkIfItemStackExist(item) != null) { 
					it =  plugin.getItemsAdderManager().checkIfItemStackExist(item);
				}
			} 
			
			if (item.contains(";")) {
				String[] split = item.split(";");
				if(split[1] != null) {
					String s = split[1];
					if (split[0].toLowerCase().equalsIgnoreCase("url")) { 
						it = plugin.getSkullCreatorAPI().itemFromUrl(s);
					} else if (split[0].toLowerCase().equalsIgnoreCase("uuid")) { 
						it = plugin.getSkullCreatorAPI().itemFromUuid(s);
					} if (split[0].toLowerCase().equalsIgnoreCase("name")) {
						it = plugin.getSkullCreatorAPI().itemFromName(s.replaceAll("<name>", player));
					} if (split[0].toLowerCase().equalsIgnoreCase("base64")) { 
						it = plugin.getSkullCreatorAPI().itemFromBase64(s);
					}
				}
			}  else {

				try {
					Material used = Material.valueOf(item.toUpperCase());
	  
					it = new ItemStack(used, 1);
				} catch (IllegalArgumentException ex) { 
				}
			}
			
			return it;
			
		} else {
			
			//loading item
			
			return getItem(named).getItemStack();
			
		} 
	}

	public void loadItems() {

		items.clear();

		FileManager fm = plugin.getFileManager();

		Bukkit.getConsoleSender().sendMessage(PluginColor.ITEM_RELATED_INFO.getPrefix() + "Checking Plugin Items...");

		fm.getGUI().getStringList("Main_Custom.List").forEach((named) -> {
			checkCustomItems("Main_Custom", named, fm.getGUI());
			 
		});
		
		fm.getWithdrawConfig().getStringList("Withdraw_Custom.List").forEach((named) -> {
			checkCustomItems("Withdraw_Custom", named, fm.getWithdrawConfig());
		});
		
		fm.getWithdrawConfirmConfig().getStringList("ConfirmWithdraw_Custom.List").forEach((named) -> {
			checkCustomItems("ConfirmWithdraw_Custom", named, fm.getWithdrawConfirmConfig());
		});
		
		fm.getStatsConfig().getStringList("Other_Custom.List").forEach((named) -> {
			checkCustomItems("Other_Custom", named, fm.getStatsConfig());
		});
		
		fm.getStatsConfig().getStringList("Self_Custom.List").forEach((named) -> {
			checkCustomItems("Self_Custom", named, fm.getStatsConfig());
		});
		
		fm.getSettings().getStringList("Settings_Custom.List").forEach((named) -> {
			checkCustomItems("Settings_Custom", named, fm.getSettings());
		});
		fm.getRewardsConfig().getStringList("Rewards_Custom.List").forEach((named) -> {
			checkCustomItems("Rewards_Custom", named, fm.getRewardsConfig());
		});
		fm.getRankingPerJobConfig().getStringList("PerJobRanking_Custom.List").forEach((named) -> {
			checkCustomItems("PerJobRanking_Custom", named, fm.getRankingPerJobConfig());
		});
		fm.getRankingGlobalConfig().getStringList("Global_Custom.List").forEach((named) -> {
			checkCustomItems("Global_Custom", named, fm.getRankingGlobalConfig());
		});
		
		fm.getLevelGUIConfig().getStringList("Levels_Custom.List").forEach((named) -> {
			checkCustomItems("Levels_Custom", named, fm.getLevelGUIConfig());
		});
		
		fm.getLeaveConfirmConfig().getStringList("LeaveConfirm_Custom.List").forEach((named) -> {
			checkCustomItems("LeaveConfirm_Custom", named, fm.getLeaveConfirmConfig());
		});
		
		fm.getLanguageGUIConfig().getStringList("Custom.List").forEach((named) -> {
			checkCustomItems("Custom", named, fm.getLanguageGUIConfig());
		});
		
		fm.getHelpSettings().getStringList("Help_Custom.List").forEach((named) -> {
			checkCustomItems("Help_Custom", named, fm.getHelpSettings());
		});
		
		fm.getEarningsJobConfig().getStringList("Job_Earnings_Custom.List").forEach((named) -> {
			checkCustomItems("Job_Earnings_Custom", named, fm.getEarningsJobConfig());
		});
		
		fm.getEarningsAllConfig().getStringList("All_Earnings_Custom.List").forEach((named) -> {
			checkCustomItems("All_Earnings_Custom", named, fm.getEarningsAllConfig());
		});
		
		fm.getConfirm().getStringList("AreYouSureGUI_Custom.List").forEach((named) -> {
			checkCustomItems("Global_Custom", named, fm.getConfirm());
		});
		
		plugin.getJobCache().forEach((id, real) -> {
			
			String cfgid = real.getConfigID();
		 
			String mat = real.getRawIcon();
			
			boolean ignore=false;
			
			ItemType type = null;
			
			ItemStack item = null;
			
			if (plugin.getPluginManager().isInstalled("ItemsAdder")) {

				if (plugin.getItemsAdderManager().checkIfItemStackExist(mat) != null) {
					type = ItemType.ITEMSADDER;
					item =  plugin.getItemsAdderManager().checkIfItemStackExist(mat);
				}
			} 
			
			if (mat.contains(";")) {
				String[] split = mat.split(";");
				if(split[1] != null) {
					String s = split[1];
					if (split[0].toLowerCase().equalsIgnoreCase("url")) {
						type = ItemType.URL;
						item = plugin.getSkullCreatorAPI().itemFromUrl(s);
					} else if (split[0].toLowerCase().equalsIgnoreCase("uuid")) {
						type = ItemType.UUID;
						item = plugin.getSkullCreatorAPI().itemFromUuid(s);
					} if (split[0].toLowerCase().equalsIgnoreCase("name")) {
						type = ItemType.NAME;
						if(s.toLowerCase().equalsIgnoreCase("<name>")) {
							ignore = true;
						}
					} if (split[0].toLowerCase().equalsIgnoreCase("base64")) {
						type = ItemType.BASE64;
						item = plugin.getSkullCreatorAPI().itemFromBase64(s);
					}
				}
			}  else {

				try {
					Material used = Material.valueOf(mat.toUpperCase());
	 
					type = ItemType.NORMAL;
					item = new ItemStack(used, 1);
				} catch (IllegalArgumentException ex) { 
				}
			}
			
			if(!ignore) {
				if(type == null) {
					Bukkit.getConsoleSender().sendMessage(
							PluginColor.ITEM_RELATED_ERROR.getPrefix() + "Failed to create Item "+mat+" for Job "+cfgid+"!");
				} else {
					
					String pr = cfgid+"_Material";
					
					Item it = new Item(pr, type, item, null);
					
					items.put(pr, it);
			 
					Bukkit.getConsoleSender().sendMessage(
							PluginColor.ITEM_RELATED_INFO.getPrefix() + "Created and Saved Item "+mat+" for Job "+cfgid+"!");
				}
			} else {
				Bukkit.getConsoleSender().sendMessage(
						PluginColor.ITEM_RELATED_WARNING.getPrefix() +"Ignoring Item "+mat+" for Job "+cfgid+"...");
			}
			
			
		});

		if(fails.size() == 0) {
			 
			Bukkit.getConsoleSender().sendMessage(
					PluginColor.ITEM_RELATED_INFO.getPrefix() + "Successfully loaded Items!");
		} else {
			Bukkit.getConsoleSender().sendMessage(
					PluginColor.ITEM_RELATED_ERROR.getPrefix() + "Failed to load Items with "+fails.size()+" Issues!");
			plugin.printFailed();
		}
		
	}

	public void checkCustomItems(String prefix, String named, FileConfiguration cfg) {
		Bukkit.getConsoleSender()
				.sendMessage(PluginColor.ITEM_RELATED_INFO.getPrefix() + "Loading Custom Item " + named + "...");

		String actions_path = prefix + "." + named + ".ActionList";

		if (cfg.getStringList(actions_path) == null) {
			Bukkit.getConsoleSender().sendMessage(
					PluginColor.ITEM_RELATED_ERROR.getPrefix() + "Failed to get ActionList for Item " + named + ".");
			fails.add(actions_path);
		}

		List<String> cfgactions = cfg.getStringList(actions_path);

		List<ItemAction> actions = new ArrayList<ItemAction>();

		for (String action : cfgactions) {
			try {
				actions.add(ItemAction.valueOf(action));
			} catch (IllegalArgumentException ex) {
				Bukkit.getConsoleSender().sendMessage(PluginColor.ITEM_RELATED_ERROR.getPrefix()
						+ "Failed to get Real Job Action for " + named + ".");
			}
		}

		String material = prefix + "." + named + ".Material";

		if (!cfg.contains(material)) {
			Bukkit.getConsoleSender().sendMessage(
					PluginColor.ITEM_RELATED_ERROR.getPrefix() + "Failed to get Material for Item " + named + ".");
			fails.add(actions_path);
		}
		
		boolean ignore=false;
		
		ItemType type = null;
		
		ItemStack item = null;
		
		if (plugin.getPluginManager().isInstalled("ItemsAdder")) {

			if (plugin.getItemsAdderManager().checkIfItemStackExist(material) != null) {
				type = ItemType.ITEMSADDER;
				item =  plugin.getItemsAdderManager().checkIfItemStackExist(material);
			}
		} 
		
		if (material.contains(";")) {
			String[] split = material.split(";");
			if(split[1] != null) {
				String s = split[1];
				if (split[0].toLowerCase().equalsIgnoreCase("url")) {
					type = ItemType.URL;
					item = plugin.getSkullCreatorAPI().itemFromUrl(s);
				} else if (split[0].toLowerCase().equalsIgnoreCase("uuid")) {
					type = ItemType.UUID;
					item = plugin.getSkullCreatorAPI().itemFromUuid(s);
				} if (split[0].toLowerCase().equalsIgnoreCase("name")) {
					type = ItemType.NAME;
					if(s.toLowerCase().equalsIgnoreCase("<name>")) {
						ignore = true;
					}
				} if (split[0].toLowerCase().equalsIgnoreCase("base64")) {
					type = ItemType.BASE64;
					item = plugin.getSkullCreatorAPI().itemFromBase64(s);
				}
			}
		}  else {

			try {
				Material used = Material.valueOf(material.toUpperCase());
 
				type = ItemType.NORMAL;
				item = new ItemStack(used, 1);
			} catch (IllegalArgumentException ex) { 
			}
		}
		
		if(!ignore) {
			if(type == null) {
				Bukkit.getConsoleSender().sendMessage(
						PluginColor.ITEM_RELATED_ERROR.getPrefix() + "Failed to create Item "+named+"!");
			} else {
				
				String pr = prefix+"_"+named;
				
				Item it = new Item(pr, type, item, actions);
				
				items.put(pr, it);
				
				List<Item> listed = null;
				
				if(!custom_items.containsKey(prefix)) {
					listed = new ArrayList<Item>();
				} else {
					listed = custom_items.get(prefix);
				}
				
				listed.add(it);
				
				custom_items.put(prefix, listed);
				
				Bukkit.getConsoleSender().sendMessage(
						PluginColor.ITEM_RELATED_INFO.getPrefix() + "Created and Saved Item "+named+"!");
			}
		} else {
			Bukkit.getConsoleSender().sendMessage(
					PluginColor.ITEM_RELATED_WARNING.getPrefix() +"Ignoring Item "+named+"...");
		}
		
		 

	}
 

}
