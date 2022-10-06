package de.warsteiner.jobs.utils.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JobID {
	
	private String id;
	private String real;
	private List<String> commands;
	private int chance;
	private double reward;
	private double exp;
	private double points;
	private String icon;
	private String modeldata;
	
	private HashMap<String, String> lang_display;
	private HashMap<String, String> lang_rewards_display;
	private HashMap<String, ArrayList<String>> lang_rewards_lore;
	
	public JobID(String id, String real, List<String> commandlist, int chance, double reward, double exp, double points, String icon, String modeldata
			, HashMap<String, String> lang_display, HashMap<String, String> lang_rewards_display,HashMap<String, ArrayList<String>> lang_rewards_lore) {
		this.id = id;
		this.commands = commandlist;
		this.chance = chance;
		this.exp = exp;
		this.reward = reward;
		this.points = points;
		this.real = real;
		this.icon = icon;
		this.modeldata = modeldata;
		this.lang_display = lang_display;
		this.lang_rewards_display = lang_rewards_display;
		this.lang_rewards_lore = lang_rewards_lore;
	}
	
	public ArrayList<String> getRewardsLoreFromlanguage(Language lang) {
		return this.lang_rewards_lore.get(lang.getID());
	}
	
	public String getRewardsDisplayFromlanguage(Language lang) {
		return this.lang_rewards_display.get(lang.getID());
	}
	
	public String getDisplayFromlanguage(Language lang) {
		return this.lang_display.get(lang.getID());
	}
	
	public int getModelData() {
		return Integer.valueOf(this.modeldata);
	}
	
	public boolean hasModelData() {
		return this.modeldata != null;
	}
	
	public String getIcon() {
		return this.icon;
	}

	public String getRealID() {
		return this.real;
	}
	
	public double getPoints() {
		return this.points;
	}
	
	public double getExp() {
		return this.exp;
	}
	
	public double getReward() {
		return this.reward;
	}
	
	public int getChance() {
		return this.chance;
	}
	
	public List<String> getCommands() {
		return this.commands;
	}

	public String getInternalID() {
		return this.id;
	}
	
}
