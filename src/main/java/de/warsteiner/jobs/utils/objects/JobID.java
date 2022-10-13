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
	 
	public JobID(String id, String real, List<String> commandlist, int chance, double reward, double exp, double points, String icon, String modeldata) {
		this.id = id;
		this.commands = commandlist;
		this.chance = chance;
		this.exp = exp;
		this.reward = reward;
		this.points = points;
		this.real = real;
		this.icon = icon;
		this.modeldata = modeldata;
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
