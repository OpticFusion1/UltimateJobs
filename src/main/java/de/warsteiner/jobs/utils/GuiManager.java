package de.warsteiner.jobs.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.warsteiner.jobs.UltimateJobs;

public class GuiManager {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public Inventory createGui(int size, String name) {
		final Inventory inv = Bukkit.createInventory(null, size * 9,
				plugin.getJobAPI().toHex(name).replaceAll("&", "�"));
		return inv;
	}
	
	public void createMainGUIOfJobs(Player player) {
		
		YamlConfiguration config = plugin.getMainConfig().getConfig();
		
		String name = config.getString("Main_Name");
		int size = config.getInt("Main_Size");
		
		Inventory inv = createGui(size, name);
		
		player.openInventory(inv);
		
		setPlaceHolders(player, inv, config.getStringList("Main_Place"), name);
		setCustomitems(player, inv, config, "Main_Custom.", config.getStringList("Main_Custom.List"), name);
		setMainInventoryJobItems(inv, player, name);
	}
	 
	public void setMainInventoryJobItems(Inventory inv, Player player, String name) {
		YamlConfiguration config = plugin.getMainConfig().getConfig();
		
		String title = player.getOpenInventory().getTitle();
		JobAPI api = plugin.getJobAPI();
		String UUID = ""+player.getUniqueId();
		String need = plugin.getJobAPI().toHex(name).replaceAll("&", "�");
		if(title.equalsIgnoreCase(need)) {
			
			ArrayList<File> jobs = plugin.getLoadedJobs();
			
			for(File job : jobs) {
				
				String display = api.toHex(api.getDisplay(job).replaceAll("&", "�"));
				int slot = api.getSlot(job);
				List<String> lore = api.getLore(job);
				String mat = api.getMaterial(job);
				int price = api.getPrice(job);
				String id = api.getID(job);
				 
				ItemStack item = createItemStack(player, mat);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(display.replaceAll("&", "�"));
				
				List<String> see;
				
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				
				if(plugin.getPlayerAPI().ownJob(UUID, need)) {
					
					if(plugin.getPlayerAPI().isInJob(UUID, id)) {
						meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
					 
						see = config.getStringList("Jobs.Lore.In"); 
					} else {
						see = config.getStringList("Jobs.Lore.Bought"); 
					}
					
				} else {
					see =config.getStringList("Jobs.Lore.Price"); 
				} 
				List<String> filore = new ArrayList<String>() ;
				for(String l : lore) {
					filore.add(plugin.getJobAPI().toHex(l).replaceAll("&", "�"));
				} 
				for(String l : see) {
					filore.add(plugin.getJobAPI().toHex(l).replaceAll("<price>", ""+price).replaceAll("&", "�"));
				} 
				meta.setLore(filore); 
				
				item.setItemMeta(meta);
				
				inv.setItem(slot, item); 
				
			} 
		}
	}
	 
 
	public void setCustomitems(Player player, Inventory inv, YamlConfiguration config, String prefix, List<String> list, String name) {
		String title = player.getOpenInventory().getTitle();
		String need = plugin.getJobAPI().toHex(name).replaceAll("&", "�");
		if(title.equalsIgnoreCase(need)) {
			for (String pl : list) { 
				if(config.contains(prefix + pl + ".Display")) {
					String display = config.getString(prefix + pl + ".Display");
					String mat = config.getString( prefix + pl + ".Material").toUpperCase();
					int slot = config.getInt(prefix + pl + ".Slot");
				  
					ItemStack item = createItemStack(player, mat);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(display.replaceAll("&", "�"));
				 
					if(config.contains(prefix + pl + ".Lore")) {
						List<String> lore = config.getStringList( prefix + pl + ".Lore");
						List<String> filore = new ArrayList<String>() ;
						for(String l : lore) {
							filore.add(plugin.getJobAPI().toHex(l).replaceAll("&", "�"));
						}
						meta.setLore(filore); 
					} 
					item.setItemMeta(meta);
		 
					inv.setItem(slot, item); 
				} else {
					plugin.getLogger().warning("�c�lMissing Element in "+ need + " �4�lCustom Item: �b�l"+pl);
				}
			}
		}	
	}
	
	public void setPlaceHolders(Player player, Inventory inv, List<String> list, String name) {
		String title = player.getOpenInventory().getTitle();
		String need = plugin.getJobAPI().toHex(name).replaceAll("&", "�");
		if(title.equalsIgnoreCase(need)) {
			for (String pl : list) {
				String[] t = pl.split(":");
	
				String mat = t[0].toUpperCase();
				int slot = Integer.valueOf(t[1]).intValue();
				String display = t[2];
	
				ItemStack item = createItemStack(player, mat);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(display.replaceAll("&", "�"));
				item.setItemMeta(meta);
	 
				inv.setItem(slot, item); 
			}
		}	
	}
	

	public ItemStack createItemStack(Player p, String item) { 
		ItemStack i; 
		if (Material.getMaterial(item.toUpperCase()) == null) {
			i = generateSkull(item.replaceAll("<skull>", p.getName()));
		} else {
			i = new ItemStack(Material.valueOf(item.toUpperCase()), 1);
		}
		return i;
	}

	public ItemStack generateSkull(String owner) {
		ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
		skullMeta.setOwner(owner);
		itemStack.setItemMeta((ItemMeta) skullMeta);
		return itemStack;
	}
	
}
