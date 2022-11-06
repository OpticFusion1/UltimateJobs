package de.warsteiner.jobs.manager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
 
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.warsteiner.jobs.UltimateJobs;

public class FileManager {
	
	private  FileConfiguration gui;
	private  File gui_file;
	
	private  FileConfiguration cfg;
	private  File cfg_file;
 
	private  FileConfiguration cmd;
	private  File cmd_file;
	
	private  FileConfiguration help;
	private  File help_file;
	
	private  FileConfiguration settings;
	private  File settings_file;
	
	private  FileConfiguration confirm;
	private  File confirm_file;
  
	private  FileConfiguration stats;
	private  File stats_file;
	
	private  FileConfiguration rewards;
	private  File rewards_file;
	
	private  FileConfiguration levels;
	private  File levels_file;
	
	private  FileConfiguration earnings_all;
	private  File earnings_all_file;
	
	private  FileConfiguration earnings_job;

	private  File earnings_job_file;
	
	private  FileConfiguration data;
	private  File data_file;
	
	private  FileConfiguration lang;
	private  File lang_file;
	
	private  FileConfiguration langg;
	private  File langg_file;
	
	private  FileConfiguration with;
	private  File with_file;
	
	private  FileConfiguration withc;
	private  File withc_file;
	
	private  FileConfiguration cleave;
	private  File cleave_file;
	
	private  FileConfiguration utils;
	private  File utils_file;
	
	private  FileConfiguration globalc;
	private  File globalc_file;
	
	private  FileConfiguration jobr;
	private  File jobr_file;
	
	private  FileConfiguration ef;
	private  File ef_file;
	
	private  FileConfiguration q_settings;
	private  File q_settings_file;
	
	private  FileConfiguration q_types;
	private  File q_types_file;
 
	private List<String> defaultlanguages = Arrays.asList("en-US");
	private List<String> defaultlanguages_files = Arrays.asList("guis","jobs","language","messages","dailyquests");
	private List<String> defaultjobs = Arrays.asList("Miner","Lumberjack","FarmGrow","Digger", "Killer","Fishman","Milkman"
			, "Crafter", "Shear","Advancements","Eat","Honey","Tame","MythicMobs","Breed"
			, "Berrys","Saplings","KillBow","FarmBreak","FindTreasure","Smelt","Explore","Enchant","DrinkPotion","VillagerTrade");
	 
	public  boolean generateFiles() {
		createGUIFile();
		createDefaultLanguages();
		createCMDFile();
		createHelpGUIFile();
		createSettingsGUIFile();
		createConfirmGUIFIle();
		createCFGFile();
		createLevelsFile();
		createEarningsJobFile();
		createDataFile();
		createLangFile();
		createLangGUIFile();
		createWithFile();
		createWithConfirmFile();
		createLeaveConfirmFile();
		createUtilsFile();
		createEffectsFile();
 
		//guis
		createguistatsFiles();
		createAddonRewardsFiles(); 
		createEarningsAllFile();
		createGlobalRankingFile();
		createRankingPerJobFile();
		
		createDailyQuestsTypeFile();
		createDailyQuestsSettingFile();
	 
		return true;
	}
 
	public void createDefaultJobs() { 
		for(String b : defaultjobs) {
			File f = new File(UltimateJobs.getPlugin().getDataFolder(), "jobs" + File.separatorChar + b+".yml");
	        if (!f.exists()) {
	        	f.getParentFile().mkdirs();
	        	UltimateJobs.getPlugin().saveResource("jobs" + File.separatorChar + b+".yml", false);
	         }
		}
	}
	
	private List<String> defaultsongs = Arrays.asList("Itsmylife","sovietanthem","thefinalcountdown");
	
	public void createDefaultSongs() { 
		for(String b : defaultsongs) {
			File f = new File(UltimateJobs.getPlugin().getDataFolder(), "addons"+File.separatorChar +"songs" + File.separatorChar + b+".nbs");
	        if (!f.exists()) {
	        	f.getParentFile().mkdirs();
	        	UltimateJobs.getPlugin().saveResource("addons"+File.separatorChar +"songs" + File.separatorChar + b+".nbs", false);
	         }
		}
	}
 
	
	public void createDefaultLanguages() { 
		for(String b : defaultlanguages) {
			
			File folder_1 = new File(UltimateJobs.getPlugin().getDataFolder()+"/lang/", b);

			if (!folder_1.exists()) {
				folder_1.mkdir();
			}
			
			for(String file : defaultlanguages_files) {
				File f = new File(UltimateJobs.getPlugin().getDataFolder()+"/lang/"+b+"/", file+".yml");
		        if (!f.exists()) {
		        	f.getParentFile().mkdirs();
		        	UltimateJobs.getPlugin().saveResource("lang/"+b+"/" + file+".yml", false);
		         }
			}
		} 
	}
 
	/*
	 * file
	 */
	public  FileConfiguration getGUI() {
		return gui;
	}
	
	public  File getGUIFile() {
		return gui_file;
	}
	
	public  FileConfiguration getEffectSettings() {
		return ef;
	}
	
	public  File getEffectFile() {
		return ef_file;
	}
	
 
	public  FileConfiguration getCMDSettings() {
		return cmd;
	}
	
	public  File getCMDFile() {
		return cmd_file;
	}
	
	public  FileConfiguration getHelpSettings() {
		return help;
	}
	
	public  File getHelpFile() {
		return help_file;
	}
	
	public  FileConfiguration getSettings() {
		return settings;
	}
	
	public  File getSettingsFile() {
		return settings_file;
	}
	
	public  FileConfiguration getConfirm() {
		return confirm;
	}
	
	public  File getConfirmFile() {
		return confirm_file;
	}
	
	public  FileConfiguration getConfig() {
		return cfg;
	}
	
	public  File getConfigFile() {
		return cfg_file;
	}
	
	public  FileConfiguration getStatsConfig() {
		return stats;
	}
	
	public  File getStatsFile() {
		return stats_file;
	}
	
	public  FileConfiguration getRewardsConfig() {
		return rewards;
	}
	
	public  File getRewardsFile() {
		return rewards_file;
	}
	
	public  FileConfiguration getLevelGUIConfig() {
		return levels;
	}
	
	public  File getLevelGUIFile() {
		return levels_file;
	}
	
	public  FileConfiguration getEarningsAllConfig() {
		return earnings_all;
	}
	
	public  File getEarningAllFIle() {
		return earnings_all_file;
	}
	
	public  FileConfiguration getEarningsJobConfig() {
		return earnings_job;
	}
	
	public  File getEarningJobFIle() {
		return earnings_job_file;
	}
	

	public  FileConfiguration getDataConfig() {
		return data;
	}
	
	public  File getDataFile() {
		return data_file;
	}
	
	public  FileConfiguration getLanguageConfig() {
		return lang;
	}
	
	public  File getLanguageFile() {
		return lang_file;
	}
	
	public  FileConfiguration getLanguageGUIConfig() {
		return langg;
	}
	
	public  File getLanguageGUIFile() {
		return langg_file;
	}
	
	
	public  FileConfiguration getWithdrawConfig() {
		return with;
	}
	
	public  File getWithdrawFile() {
		return with_file;
	}
	
	
	public  FileConfiguration getWithdrawConfirmConfig() {
		return withc;
	}
	
	public  File getWithdrawConfirmFile() {
		return withc_file;
	}
	
	public  FileConfiguration getLeaveConfirmConfig() {
		return cleave;
	}
	
	public  File getLeaveConfirmFile() {
		return cleave_file;
	}
	
	public  FileConfiguration getUtilsConfig() {
		return utils;
	}
	
	public  File getUtilsFile() {
		return utils_file;
	}
	
	public  FileConfiguration getRankingGlobalConfig() {
		return globalc;
	}
	
	public  File getGlobalRankingFile() {
		return globalc_file;
	}
	
	public  FileConfiguration getRankingPerJobConfig() {
		return jobr;
	}
	
	public  File getRankingPerJobFile() {
		return jobr_file;
	}
	
	public  FileConfiguration getDailyQuestsTypesConfig() {
		return q_types;
	}
	
	public  File getDailyQuestsTypesFile() {
		return q_types_file;
	}
	
	public  FileConfiguration getDailyQuestsSettingsConfig() {
		return q_settings;
	}
	
	public  File getDailyQuestsSettingsFile() {
		return q_settings_file;
	}
 
 public boolean createDailyQuestsSettingFile() {
	 q_settings_file = new File(UltimateJobs.getPlugin().getDataFolder(), "addons"+ File.separatorChar + "dailyquests" + File.separatorChar + "quests_settings.yml");
     if (!q_settings_file.exists()) {
    	 q_settings_file.getParentFile().mkdirs();
     	UltimateJobs.getPlugin().saveResource("addons"+ File.separatorChar + "dailyquests" + File.separatorChar + "quests_settings.yml", true);
     }
     
     q_settings = new YamlConfiguration();
     try {
    	 q_settings.load(q_settings_file);
     } catch (IOException | InvalidConfigurationException e) {
         e.printStackTrace();
         return false;
     }
     return true;
	}
	
	public boolean createDailyQuestsTypeFile() {
		q_types_file = new File(UltimateJobs.getPlugin().getDataFolder(), "addons"+ File.separatorChar + "dailyquests" + File.separatorChar + "quests_types.yml");
        if (!q_types_file.exists()) {
        	q_types_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("addons"+ File.separatorChar + "dailyquests" + File.separatorChar + "quests_types.yml", true);
        }
        
        q_types = new YamlConfiguration();
        try {
        	q_types.load(q_types_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createRankingPerJobFile() {
		jobr_file = new File(UltimateJobs.getPlugin().getDataFolder(), "guis" + File.separatorChar + "ranking_per_job.yml");
        if (!jobr_file.exists()) {
        	jobr_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("guis" + File.separatorChar + "ranking_per_job.yml", true);
        }
        
        jobr = new YamlConfiguration();
        try {
        	jobr.load(jobr_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createGlobalRankingFile() {
		globalc_file = new File(UltimateJobs.getPlugin().getDataFolder(), "guis" + File.separatorChar + "ranking_global.yml");
        if (!globalc_file.exists()) {
        	globalc_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("guis" + File.separatorChar + "ranking_global.yml", true);
        }
        
        globalc = new YamlConfiguration();
        try {
        	globalc.load(globalc_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createUtilsFile() {
		utils_file = new File(UltimateJobs.getPlugin().getDataFolder(), "settings" + File.separatorChar + "utils.yml");
        if (!utils_file.exists()) {
        	utils_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "utils.yml", true);
        }
        
        utils = new YamlConfiguration();
        try {
        	utils.load(utils_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createLeaveConfirmFile() {
		cleave_file = new File(UltimateJobs.getPlugin().getDataFolder(), "guis" + File.separatorChar + "leave_job_confirm_gui.yml");
        if (!cleave_file.exists()) {
        	cleave_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("guis" + File.separatorChar + "leave_job_confirm_gui.yml", true);
        }
        
        cleave = new YamlConfiguration();
        try {
        	cleave.load(cleave_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createWithConfirmFile() {
		withc_file = new File(UltimateJobs.getPlugin().getDataFolder(), "guis" + File.separatorChar + "withdraw_confirm_gui.yml");
        if (!withc_file.exists()) {
        	withc_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("guis" + File.separatorChar + "withdraw_confirm_gui.yml", true);
        }
        
        withc = new YamlConfiguration();
        try {
        	withc.load(withc_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createWithFile() {
		with_file = new File(UltimateJobs.getPlugin().getDataFolder(), "guis" + File.separatorChar + "withdraw_gui.yml");
        if (!with_file.exists()) {
        	with_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("guis" + File.separatorChar + "withdraw_gui.yml", true);
        }
        
        with = new YamlConfiguration();
        try {
        	with.load(with_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createLangGUIFile() {
		langg_file = new File(UltimateJobs.getPlugin().getDataFolder(), "guis" + File.separatorChar + "lang_gui.yml");
        if (!langg_file.exists()) {
        	langg_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("guis" + File.separatorChar + "lang_gui.yml", true);
        }
        
        langg = new YamlConfiguration();
        try {
        	langg.load(langg_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createLangFile() {
		lang_file = new File(UltimateJobs.getPlugin().getDataFolder(), "settings" + File.separatorChar + "languages.yml");
        if (!lang_file.exists()) {
        	lang_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "languages.yml", true);
        }
        
        lang = new YamlConfiguration();
        try {
        	lang.load(lang_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	
	public boolean createDataFile() {
		data_file = new File(UltimateJobs.getPlugin().getDataFolder(), "settings" + File.separatorChar + "data.yml");
        if (!data_file.exists()) {
        	data_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "data.yml", true);
        }
        
        data = new YamlConfiguration();
        try {
        	data.load(data_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createEarningsJobFile() {
		earnings_job_file = new File(UltimateJobs.getPlugin().getDataFolder(), "guis" + File.separatorChar + "earnings_job.yml");
        if (!earnings_job_file.exists()) {
        	earnings_job_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("guis" + File.separatorChar + "earnings_job.yml", true);
        }
        
        earnings_job = new YamlConfiguration();
        try {
        	earnings_job.load(earnings_job_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createEarningsAllFile() {
		earnings_all_file = new File(UltimateJobs.getPlugin().getDataFolder(), "guis" + File.separatorChar + "earnings_all.yml");
        if (!earnings_all_file.exists()) {
        	earnings_all_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("guis" + File.separatorChar + "earnings_all.yml", true);
        }
        
        earnings_all = new YamlConfiguration();
        try {
        	earnings_all.load(earnings_all_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createLevelsFile() {
		levels_file = new File(UltimateJobs.getPlugin().getDataFolder(), "guis" + File.separatorChar + "levelsgui.yml");
        if (!levels_file.exists()) {
        	levels_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("guis" + File.separatorChar + "levelsgui.yml", true);
        }
        
        levels = new YamlConfiguration();
        try {
        	levels.load(levels_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createAddonRewardsFiles() {
		rewards_file = new File(UltimateJobs.getPlugin().getDataFolder(), "guis" + File.separatorChar + "rewards.yml");
        if (!rewards_file.exists()) {
        	rewards_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("guis" + File.separatorChar + "rewards.yml", true);
        }
        
        rewards = new YamlConfiguration();
        try {
        	rewards.load(rewards_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createguistatsFiles() {
		stats_file = new File(UltimateJobs.getPlugin().getDataFolder(), "guis" + File.separatorChar + "stats.yml");
        if (!stats_file.exists()) {
        	stats_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("guis" + File.separatorChar + "stats.yml", true);
        }
        
        stats = new YamlConfiguration();
        try {
        	stats.load(stats_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createCFGFile() {
		cfg_file = new File(UltimateJobs.getPlugin().getDataFolder(), "Config.yml");
        if (!cfg_file.exists()) {
        	cfg_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("Config.yml", false);
         }

        cfg = new YamlConfiguration();
        try {
        	cfg.load(cfg_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createConfirmGUIFIle() {
		confirm_file = new File(UltimateJobs.getPlugin().getDataFolder(), "guis" + File.separatorChar + "confirm_gui.yml");
        if (!confirm_file.exists()) {
        	confirm_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("guis" + File.separatorChar + "confirm_gui.yml", true);
         }

        confirm = new YamlConfiguration();
        try {
        	confirm.load(confirm_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createSettingsGUIFile() {
		settings_file = new File(UltimateJobs.getPlugin().getDataFolder(), "guis" + File.separatorChar + "settings_gui.yml");
        if (!settings_file.exists()) {
        	settings_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("guis" + File.separatorChar + "settings_gui.yml", true);
         }

        settings = new YamlConfiguration();
        try {
        	settings.load(settings_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
 
	public boolean createHelpGUIFile() {
		help_file = new File(UltimateJobs.getPlugin().getDataFolder(), "guis" + File.separatorChar + "help_gui.yml");
        if (!help_file.exists()) {
        	help_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("guis" + File.separatorChar + "help_gui.yml", true);
         }

        help = new YamlConfiguration();
        try {
        	help.load(help_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public  boolean createCMDFile() {
		cmd_file = new File(UltimateJobs.getPlugin().getDataFolder(), "settings" + File.separatorChar + "command.yml");
        if (!cmd_file.exists()) {
        	cmd_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "command.yml", true);
         }

        cmd = new YamlConfiguration();
        try {
        	cmd.load(cmd_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
	
	public  boolean createEffectsFile() {
		ef_file = new File(UltimateJobs.getPlugin().getDataFolder(), "settings" + File.separatorChar + "effects.yml");
        if (!ef_file.exists()) {
        	ef_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "effects.yml", true);
         }

        ef = new YamlConfiguration();
        try {
        	ef.load(ef_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
 
	public  boolean createGUIFile() {
		gui_file = new File(UltimateJobs.getPlugin().getDataFolder(), "guis" + File.separatorChar + "main_gui.yml");
        if (!gui_file.exists()) {
        	gui_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("guis" + File.separatorChar + "main_gui.yml", true);
         }

        gui = new YamlConfiguration();
        try {
        	gui.load(gui_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
  
}
