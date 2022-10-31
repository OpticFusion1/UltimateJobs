package de.warsteiner.jobs.utils.objects;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import de.warsteiner.jobs.UltimateJobs;

public class Language {

	private String name;
	private String id;
	private String icon;
	private String display;
	private int data;

	private HashMap<String, String> dm;
	private HashMap<String, String> gm;
	private HashMap<String, List<String>> gl;
	private HashMap<String, List<String>> dl;
	private HashMap<String, String> js;
	private HashMap<String, List<String>> jl;

	public Language(String name, String id, String icon, String display, int data,
			HashMap<String, String> dm, HashMap<String, List<String>> dl, HashMap<String, String> gm,
			HashMap<String, List<String>> gl, HashMap<String, String> js,
			HashMap<String, List<String>> jl) {
		this.name = name;
		this.id = id;
		this.icon = icon;
		this.display = display;
		this.data = data;

		this.dm = dm;
		this.dl = dl;
		this.gm = gm;
		this.gl = gl;
		this.js = js;
		this.jl = jl;
	}
	
	public String getPrefix() {
		return dm.get("prefix");
	}
	

	public String getGUIMessage(String path) {
		if (!this.gm.containsKey(path)) {
			Bukkit.getConsoleSender().sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix() + "Failed to get String " + path
					+ " from language " + this.name + "!");
			return "Error";
		}
		return UltimateJobs.getPlugin().getPluginManager().toHex(this.gm.get(path).replaceAll("<prefix>", ""+getPrefix()));
	}

	public List<String> getGUIList(String path) {
		if (!this.gl.containsKey(path)) {
			Bukkit.getConsoleSender().sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix() + "Failed to get Stringlist " + path
					+ " from language " + this.name + "!");
			return null;
		}
		return this.gl.get(path);
	}

	public String getJobMessage(String path) {
		if (!this.js.containsKey(path)) {
			Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix() + "Failed to get String " + path
					+ " from language " + this.name + "!");
			return "Error";
		}
		return UltimateJobs.getPlugin().getPluginManager().toHex(this.js.get(path).replaceAll("<prefix>", ""+getPrefix()));
	}
	
	public List<String> getJobList(String path) {
		if (!this.jl.containsKey(path)) {
			Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix() + "Failed to get Stringlist " + path
					+ " from language " + this.name + "!");
			return null;
		}
		return this.jl.get(path);
	}
	
	public HashMap<String, String> getMessageContentOfJobs() {
		return this.js;
	}
	
	public String getMessage(String path) {
		if (!this.dm.containsKey(path)) {
			Bukkit.getConsoleSender().sendMessage(PluginColor.LANG_RELATED_ERROR.getPrefix() + "Failed to get String " + path
					+ " from language " + this.name + "!");
			return "Error";
		}
		return UltimateJobs.getPlugin().getPluginManager().toHex(this.dm.get(path).replaceAll("<prefix>", ""+getPrefix()));
	}

	public List<String> getList(String path) {
		if (!this.dl.containsKey(path)) {
			Bukkit.getConsoleSender().sendMessage(PluginColor.LANG_RELATED_ERROR.getPrefix() + "Failed to get Stringlist " + path
					+ " from language " + this.name + "!");
			return null;
		}
		return this.dl.get(path);
	}

	public int getModelData() {
		return data;
	}

	public String getDisplay() {
		return display;
	}

	public String getIcon() {
		return icon;
	}

	public String getID() {
		return id;
	}
 
	public String getName() {
		return name;
	}

}
