package de.warsteiner.jobs.utils.objects;

import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import de.warsteiner.jobs.UltimateJobs;

public class Item {

	private String internal;
	
	private ItemStack icon;
	
	private ItemType type;
 
	private List<ItemAction> actions;
	
	public Item(String internal, ItemType type, ItemStack icon, List<ItemAction> actions) {
		
		this.internal = internal;
		this.icon = icon;
		this.actions = actions;
		this.type = type;
	}
	
	public ItemType getType() {
		return type;
	}
	
	public List<ItemAction> getActions() {
		return this.actions;
	}

	public ItemStack getItemStack() { 
		return icon;
	}
 
	public String getInternalID() {
		return this.internal;
	}
	
}
