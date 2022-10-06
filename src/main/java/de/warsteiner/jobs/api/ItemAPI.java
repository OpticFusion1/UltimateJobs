package de.warsteiner.jobs.api;

/**
 * Creating Items
*/

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.objects.GUIType;

public class ItemAPI {

	private UltimateJobs plugin = UltimateJobs.getPlugin();
	
	public HashMap<String, ItemStack> listeditems = new HashMap<String, ItemStack>();

	@SuppressWarnings("deprecation")
	public ItemStack createItem(Player p, String item) {
		ItemStack i = null;
		if (plugin.getPluginManager().isInstalled("ItemsAdder")) {

			if (plugin.getItemsAdderManager().checkIfItemStackExist(item) != null) {
				return plugin.getItemsAdderManager().checkIfItemStackExist(item);
			}

		}

		if (item.contains(";")) {
			String[] split = item.split(";");
			if (split[0].toLowerCase().equalsIgnoreCase("url")) {
				
				if(listeditems.containsKey(split[1])) {
					i = listeditems.get(split[1]);
				} else {
					i = plugin.getSkullCreatorAPI().itemFromUrl(split[1]);
				}
			} else if (split[0].toLowerCase().equalsIgnoreCase("uuid")) {
				
				if(listeditems.containsKey(split[1])) {
					i = listeditems.get(split[1]);
				} else {
					i = plugin.getSkullCreatorAPI().itemFromUuid(split[1]);
				}
				 
			} else if (split[0].toLowerCase().equalsIgnoreCase("name")) {
				String name = split[1].replaceAll("<name>", p.getName());
				if(listeditems.containsKey(name)) {
					i = listeditems.get(name);
				} else {
					i = plugin.getSkullCreatorAPI().itemFromName(name);
				}
				 
			} else if (split[0].toLowerCase().equalsIgnoreCase("base64")) {
				
				if(listeditems.containsKey(split[1])) {
					i = listeditems.get(split[1]);
				} else {
					i = plugin.getSkullCreatorAPI().itemFromBase64(split[1]);
				}
				 
			}
		 
		} else {
			if (Material.valueOf(item.toUpperCase()) == null) {
				Bukkit.getConsoleSender()
						.sendMessage("§4§lFailed to create Item with itemmaterial: " + item.toUpperCase());
				return new ItemStack(Material.BARRIER, 1);
			}
			i = new ItemStack(Material.valueOf(item.toUpperCase()), 1);
		}

		return i;
	}

	@SuppressWarnings("deprecation")
	public ItemStack createItem(String p, String item) {
		ItemStack i = null;
		if (plugin.getPluginManager().isInstalled("ItemsAdder")) {

			if (plugin.getItemsAdderManager().checkIfItemStackExist(item) != null) {
				return plugin.getItemsAdderManager().checkIfItemStackExist(item);
			}

		}

		if (item.contains(";")) {
			String[] split = item.split(";");
			if (split[0].toLowerCase().equalsIgnoreCase("url")) {
				
				if(listeditems.containsKey(split[1])) {
					i = listeditems.get(split[1]);
				} else {
					i = plugin.getSkullCreatorAPI().itemFromUrl(split[1]);
				}
			} else if (split[0].toLowerCase().equalsIgnoreCase("uuid")) {
				
				if(listeditems.containsKey(split[1])) {
					i = listeditems.get(split[1]);
				} else {
					i = plugin.getSkullCreatorAPI().itemFromUuid(split[1]);
				}
				 
			} else if (split[0].toLowerCase().equalsIgnoreCase("name")) {
				String name = split[1].replaceAll("<name>", p);
				if(listeditems.containsKey(name)) {
					i = listeditems.get(name);
				} else {
					i = plugin.getSkullCreatorAPI().itemFromName(name);
				}
				 
			} else if (split[0].toLowerCase().equalsIgnoreCase("base64")) {
				
				if(listeditems.containsKey(split[1])) {
					i = listeditems.get(split[1]);
				} else {
					i = plugin.getSkullCreatorAPI().itemFromBase64(split[1]);
				}
				 
			}
		 
		}  else {
			if (Material.valueOf(item.toUpperCase()) == null) {
				Bukkit.getConsoleSender()
						.sendMessage("§4§lFailed to create Item with itemmaterial: " + item.toUpperCase());
				return new ItemStack(Material.BARRIER, 1);
			}
			i = new ItemStack(Material.valueOf(item.toUpperCase()), 1);
		}

		return i;
	}

}
