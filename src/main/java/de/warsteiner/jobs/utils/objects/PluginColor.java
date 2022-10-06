package de.warsteiner.jobs.utils.objects;

public enum PluginColor {
	 
	INFO("§a[INFO] ", "§a"),
	WARNING("§6[WARNING] ", "§6"),
	ERROR("§c[ERROR] ", "§c"),
	FAILED("§4§l[FAILED] ","§4§l");
	
	public String color;
	public String prefix;
	
	PluginColor(String prefix, String color) {
		this.color = color;
		this.prefix = prefix;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public String getPrefix() {
		return this.prefix;
	}

}
