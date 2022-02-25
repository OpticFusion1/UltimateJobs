package de.warsteiner.jobs.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection; 
import org.bukkit.configuration.file.FileConfiguration; 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer; 

public class YMLPlayerManager {
 
	private UltimateJobs plugin = UltimateJobs.getPlugin();
	  
	public void updatePoints(String UUID, double value) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		cfg.set("Player."+UUID+".Points", value);
		save(cfg, file);
	}

	public void updateLevel(String UUID, int value, String job) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		cfg.set("Jobs."+UUID+"."+job+".Level", value);
		save(cfg, file);
	}

	public void updateMax(String UUID, int value) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		cfg.set("Player."+UUID+".Max", value);
		save(cfg, file);
	}

	public void savePlayer(JobsPlayer pl, String UUID) { 
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		Collection<String> current = pl.getCurrentJobs();
		Collection<String> owned = pl.getOwnJobs();
		int max = pl.getMaxJobs();
		double points = pl.getPoints();
		
		cfg.set("Player."+UUID+".Points", points);
		cfg.set("Player."+UUID+".Date", UltimateJobs.getPlugin().getAPI().getDate());
		cfg.set("Player."+UUID+".Max", max); 
  
		ArrayList<String> newowned = new ArrayList<String>();
		
		for (String job : owned) {
			int level = pl.getLevelOf(job);
			double exp = pl.getExpOf(job);
			int broken = pl.getBrokenOf(job);
			String date = pl.getDateOfJob(job);
			
			cfg.set("Jobs."+UUID+"."+job+".Level", level);
			cfg.set("Jobs."+UUID+"."+job+".Date", date);
			cfg.set("Jobs."+UUID+"."+job+".Broken", broken);
			cfg.set("Jobs."+UUID+"."+job+".Exp", exp);

			newowned.add(job); 
		}
 
		cfg.set("Player."+UUID+".Owned", newowned);
		cfg.set("Player."+UUID+".Current", current);
		
		save(cfg, file);
 
	}

	public void createJobData(String UUID, String job) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		String date = plugin.getAPI().getDate();
		
		cfg.set("Jobs."+UUID+"."+job+".Date", date);
		cfg.set("Jobs."+UUID+"."+job+".Level", 1);
		cfg.set("Jobs."+UUID+"."+job+".Exp", 0);
		cfg.set("Jobs."+UUID+"."+job+".Broken", 0);
		save(cfg, file);
	}

	public boolean ExistJobData(String UUID, String job) { 
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		 
		return cfg.getString("Jobs."+UUID+"."+job+".Date") != null;
	}

	public int getLevelOf(String UUID, String job) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		 
		return cfg.getInt("Jobs."+UUID+"."+job+".Level");
	}

	public double getExpOf(String UUID, String job) { 
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		 
		return cfg.getDouble("Jobs."+UUID+"."+job+".Exp");
	}

	public int getBrokenOf(String UUID, String job) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		 
		return cfg.getInt("Jobs."+UUID+"."+job+".Broken");
	}

	public String getDateOf(String UUID, String job) { 
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		 
		return cfg.getString("Jobs."+UUID+"."+job+".Date");
	}

	public ArrayList<String> getOwnedJobs(String UUID) { 
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		 
		return (ArrayList<String>) cfg.getStringList("Player."+UUID+".Owned");
	}
	 
	
	public ArrayList<String> getCurrentJobs(String UUID) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		 
		return (ArrayList<String>) cfg.getStringList("Player."+UUID+".Current");
	}

	public double getPoints(String UUID) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		
		return cfg.getDouble("Player."+UUID+".Points");
	}

	public int getMaxJobs(String UUID) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		
		return cfg.getInt("Player."+UUID+".Max");
	}

	public void createPlayer(String UUID, String name) {
		String date = UltimateJobs.getPlugin().getAPI().getDate();
		createPlayerDetails(UUID, date); 
	}

	public void createPlayerDetails(String UUID, String date) {
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		File file = plugin.getPlayerDataFile().getfile();
		int max = UltimateJobs.getPlugin().getMainConfig().getConfig().getInt("MaxDefaultJobs"); 
		ArrayList<String> list = new ArrayList<String>();
		cfg.set("Player."+UUID+".Points", 0);
		cfg.set("Player."+UUID+".Max", max);
		cfg.set("Player."+UUID+".Date", date);
		cfg.set("Player."+UUID+".Owned", list);
		cfg.set("Player."+UUID+".Current", list);
		save(cfg, file);
	}

	public boolean ExistPlayer(String UUID) { 
		FileConfiguration cfg = plugin.getPlayerDataFile().get();  
		return cfg.getString("Player."+UUID+".Date") != null;
	}

	
	public void save(FileConfiguration cfg, File file) {
		try {
			cfg.save(file);
		} catch (IOException e) {
			plugin.getLogger().warning("§4§lFalied to save Data-File!");
			e.printStackTrace();
		}
	}
	
}
