package de.warsteiner.jobs.utils.objects.jobs;

import de.warsteiner.jobs.utils.objects.multipliers.MultiplierType;
import de.warsteiner.jobs.utils.objects.multipliers.MultiplierWeight;

public class JobsMultiplier {
	
	private String by;
	private String name;
	
	private MultiplierType type;
	
	private String hasJob;
	
	private String until;
	
	private MultiplierWeight weight;
	private double val;

	public JobsMultiplier(String name, String by_plugin, MultiplierType type_of, String until, MultiplierWeight weight, double value, String job) {
		this.name = name;
		this.by = by_plugin;
		this.type = type_of;
		this.until = until;
		this.weight = weight;
		this.val = value;
		this.hasJob = job;
	}
	
	public String getName() {
		return name;
	}
	
	public String getJob() {
		return hasJob;
	}
	
	public boolean hasJob() {
		return hasJob != null;
	}
	
	public double getValue() {
		return val;
	}
	
	public MultiplierWeight getWeight() {
		return weight;
	}
	
	public String getUntil() {
		return until;
	}
	
	public MultiplierType getType() {
		return type;
	}
	
	public String getByPlugin() {
		return by;
	}
	
}
