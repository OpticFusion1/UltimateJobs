package de.warsteiner.jobs.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.manager.FileManager;
import de.warsteiner.jobs.utils.objects.Language;
import de.warsteiner.jobs.utils.objects.PluginColor;

/**
 * Class to manage Player Languages
 */

public class LanguageAPI {

	private UltimateJobs plugin = UltimateJobs.getPlugin();

	private HashMap<String, Language> langs = new HashMap<String, Language>();
	private ArrayList<Language> arraylangs = new ArrayList<Language>();

	public ArrayList<Language> getLoadedLanguagesAsArray() {
		return arraylangs;
	}

	public int getLanguagesAmount() {
		return langs.size();
	}

	public HashMap<String, Language> getLanguages() {
		return langs;
	}

	public Language getLanguageFromID(String id) {
		for (int i = 0; i < langs.size(); i++) {
			Language l = arraylangs.get(i);

			if (l.getID().toLowerCase().equalsIgnoreCase(id.toLowerCase())) {
				return l;
			}
		}
		return null;
	}

	public Language getLanguageFromName(String id) {
		for (int i = 0; i < langs.size(); i++) {
			Language l = arraylangs.get(i);

			if (l.getName().toLowerCase().equalsIgnoreCase(id.toLowerCase())) {
				return l;
			}
		}
		return null;
	}

	public Language getLanguage(String id) {
		for (int i = 0; i < langs.size(); i++) {
			Language l = arraylangs.get(i);

			if (l.getName().toLowerCase().equalsIgnoreCase(id.toLowerCase())) {
				return l;
			}
		}
		return null;
	}

	public List<String> known_strings = List.of("prefix", "job_not_found", "noperm", "placeholder_no_job",
			"placeholder_no_level", "placeholder_no_exp", "placeholder_no_levelname", "command_notfound",
			"command_usage", "command_language_NotFound", "command_language_Already", "command_language_Changed",
			"command_join_Joined", "command_join_max", "command_join_already", "command_join_not_own",
			"command_leaveall_message", "command_leaveall_already", "command_leave_message", "command_leave_already",
			"command_limit_other", "command_limit_not_found", "command_limit_self", "command_points_not_found",
			"command_points_self", "command_points_other", "command_stats_not_found", "command_levels_no_data_found",
			"Other.Bought_Job", "Other.Not_Enough_Money", "Other.Full", "Other.Joined", "Other.Left_Job",
			"Other.Leave_All", "Other.Already_Left_All", "Levels.BoardCastMessage", "Levels.Ttitle_1",
			"Levels.Ttitle_2", "Levels.Message", "Levels.Actionbar",

			"Commands.Ranking.Usage", "Commands.Ranking.UsageMessage", "Commands.Ranking.Description",
			"Commands.Withdraw.Usage", "Commands.Withdraw.UsageMessage", "Commands.Withdraw.Description",
			"Commands.Earnings.Usage", "Commands.Earnings.UsageMessage", "Commands.Earnings.Description",
			"Commands.Rewards.Usage", "Commands.Rewards.UsageMessage", "Commands.Rewards.Description",
			"Commands.Stats.Usage", "Commands.Stats.UsageMessage", "Commands.Stats.Description",
			"Commands.Language.Usage", "Commands.Language.UsageMessage", "Commands.Language.Description",
			"Commands.Help.Usage", "Commands.Help.UsageMessage", "Commands.Help.Description", "Commands.Points.Usage",
			"Commands.Points.UsageMessage", "Commands.Points.Description", "Commands.Limit.Usage",
			"Commands.Limit.UsageMessage", "Commands.Limit.Description", "Commands.Leave.Usage",
			"Commands.Leave.UsageMessage", "Commands.Leave.Description", "Commands.LeaveALL.Usage",
			"Commands.LeaveALL.UsageMessage", "Commands.LeaveALL.Description", "Commands.Join.Usage",
			"Commands.Join.UsageMessage", "Commands.Join.Description", "Commands.Levels.Usage",
			"Commands.Levels.UsageMessage", "Commands.Levels.Description");

	public List<String> known_lists = List.of("LanguageChoosenLore", "LanguageCanChoose", "Commands.Help.List");

	public List<String> guis_known_names = List.of("Main_Name", "LeaveConfirm_Name", "LeaveConfirm.Button_NO.Display",
			"LeaveConfirm.Button_YES.Display", "Withdraw_Name", "Withdraw_Custom.CollectButton.CantMessage",
			"Withdraw_Custom.CollectButton.WithdrawMessage", "Withdraw_Custom.CollectButton.Display",
			"Withdraw_Custom.Info.Display", "Withdraw_Custom.NoSalaryToCollect.Display", "ConfirmWithdraw_Name",
			"ConfirmWithdraw.Button_NO.Display", "ConfirmWithdraw.Button_YES.Display", "Name", "Job_Earnings_Name",
			"Job_Earnings_Items.Display", "Job_Earnings_Custom.Previous.Display",
			"Job_Earnings_Custom.Previous.Message_NotFound", "Job_Earnings_Custom.Next.Display",
			"Job_Earnings_Custom.Next.Message_NotFound", "All_Earnings_Name", "All_Earnings_Items.Display",
			"All_Earnings_Custom.Previous.Display", "All_Earnings_Custom.Previous.Message_NotFound",
			"All_Earnings_Custom.Next.Display", "All_Earnings_Custom.Next.Message_NotFound", "Help_Name",
			"AreYouSureGUI_Name", "AreYouSureItems.Button_NO.Display", "AreYouSureItems.Button_YES.Display",
			"Settings_Name", "Rewards_Name", "Rewards_Custom.Previous.Display",
			"Rewards_Custom.Previous.Message_NotFound", "Rewards_Custom.Next.Display",
			"Rewards_Custom.Next.Message_NotFound", "Global_Name", "Global_RankingItems.OwnSkull.Display",
			"PerJobRanking_Name", "RankingCategories.Level.Display", "RankingCategories.Blocks.Display",
			"RankingCategories.Today.Display", "PerJobRanking.OwnSkull.Display", "Levels_Name",
			"Levels_Custom.Previous.Display", "Levels_Custom.Previous.Message_NotFound", "Levels_Custom.Next.Display",
			"Levels_Custom.Next.Message_NotFound", "Self_Name", "Other_Name", "Self_Custom.NotFound.Display",
			"Self_Custom.Skull.Display", "Other_Custom.Skull.Display", "Other_Custom.NotFound.Display",
			"Withdraw_Reset_Salary_Message", "Withdraw_Remove_Percent_Of_Salary_Message");

	public List<String> guis_known_lists = List.of("Main_Job_Items.Lore.Bought", "Main_Job_Items.Lore.In",
			"Main_Job_Items.Lore.Price", "LeaveConfirm.Button_NO.Lore", "LeaveConfirm.Button_YES.Lore",
			"Withdraw_Custom.CollectButton.LoreCantCollect", "Withdraw_Custom.CollectButton.LoreCanCollect",
			"Withdraw_Custom.Info.Lore", "Withdraw_Custom.NoSalaryToCollect.Lore", "ConfirmWithdraw.Button_NO.Lore",
			"ConfirmWithdraw.Button_YES.Lore", "Job_Earnings_Items.Lore", "Job_Earnings_Custom.Previous.Lore",
			"Job_Earnings_Custom.Next.Lore", "All_Earnings_Items.Lore", "All_Earnings_Custom.Previous.Lore",
			"All_Earnings_Custom.Next.Lore", "AreYouSureItems.Button_NO.Lore", "AreYouSureItems.Button_YES.Lore",
			"Rewards_Item_Lores.Reached", "Rewards_Item_Lores.Currently", "Rewards_Item_Lores.Locked",
			"Rewards_Custom.Previous.Lore", "Rewards_Custom.Next.Lore", "Global_RankingItems.OwnSkull.Lore",
			"RankingCategories.Level.Lore", "RankingCategories.Blocks.Lore", "RankingCategories.Today.Lore",
			"PerJobRanking.LoreForRanks.Level", "PerJobRanking.LoreForRanks.Blocks", "PerJobRanking.LoreForRanks.Today",
			"PerJobRanking.OwnSkull.Lore", "Levels_Custom.Previous.Lore", "Levels_Custom.Next.Lore",
			"Self_Custom.NotFound.Lore", "Self_Custom.Skull.Lore", "Other_Custom.NotFound.Lore",
			"Other_Custom.Skull.Lore");

	public void loadLanguages() {

		FileManager fm = plugin.getLocalFileManager();

		File dataFolder = new File("plugins/UltimateJobs/lang/");
		File[] folders = dataFolder.listFiles();

		if (folders != null) {

			for (int i = 0; i < folders.length; i++) {
				String name = folders[i].getName();

				File dataFolder2 = new File("plugins/UltimateJobs/lang/" + name + "/");

				File[] files = dataFolder2.listFiles();
				if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Info")) {
					Bukkit.getConsoleSender().sendMessage(
							PluginColor.LANG_RELATED_INFO.getPrefix() + "Checking Language Folder; " + name + "...");
				}
				Collection<String> fails = new ArrayList<String>();

				HashMap<String, String> gui_languages_strings = new HashMap<String, String>();
				HashMap<String, List<String>> gui_languages_lists = new HashMap<String, List<String>>();
				HashMap<String, String> jobs_languages_strings = new HashMap<String, String>();
				HashMap<String, List<String>> jobs_languages_lists = new HashMap<String, List<String>>();

				HashMap<String, String> languages_strings = new HashMap<String, String>();
				HashMap<String, List<String>> languages_lists = new HashMap<String, List<String>>();

				String lang_named = null;
				String lang_icon = null;
				String lang_display = null;
				int lang_model = 0;

				if (files != null) {

					for (int i2 = 0; i2 < files.length; i2++) {
						String filenamed = files[i2].getName();
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.LANG_RELATED_INFO.getPrefix()
									+ "Checking Language file; " + filenamed + "...");
						}

						File filed = files[i2];

						if (filenamed.toLowerCase().equalsIgnoreCase("language.yml")) {

							YamlConfiguration cfg = YamlConfiguration.loadConfiguration(filed);

							String lang_display_path = "LanguageDisplay";

							lang_model = cfg.getInt("CustomModelData");

							if (!cfg.contains(lang_display_path)) {
								if (plugin.getLocalFileManager().getUtilsConfig()
										.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
									Bukkit.getConsoleSender().sendMessage(
											PluginColor.LANG_RELATED_ERROR.getPrefix() + "Failed to get String "
													+ lang_display_path + " from language " + name + "!");
								}
								fails.add(lang_display_path);
							}

							lang_display = cfg.getString(lang_display_path);

							String lang_named_path = "LanguageName";

							if (!cfg.contains(lang_named_path)) {
								if (plugin.getLocalFileManager().getUtilsConfig()
										.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
									Bukkit.getConsoleSender().sendMessage(
											PluginColor.LANG_RELATED_ERROR.getPrefix() + "Failed to get String "
													+ lang_named_path + " from language " + name + "!");
								}
								fails.add(lang_named_path);
							}

							lang_named = cfg.getString(lang_named_path);

							String lang_icon_path = "LanguageIcon";

							if (!cfg.contains(lang_icon_path)) {
								if (plugin.getLocalFileManager().getUtilsConfig()
										.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
									Bukkit.getConsoleSender().sendMessage(
											PluginColor.LANG_RELATED_ERROR.getPrefix() + "Failed to get String "
													+ lang_icon_path + " from language " + name + "!");
								}
								fails.add(lang_icon_path);
							}

							lang_icon = cfg.getString(lang_icon_path);

						} else if (filenamed.toLowerCase().equalsIgnoreCase("messages.yml")) {

							YamlConfiguration cfg = YamlConfiguration.loadConfiguration(filed);

							known_strings.forEach((string) -> {

								if (!cfg.contains(string)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender().sendMessage(PluginColor.LANG_RELATED_ERROR.getPrefix()
												+ "Failed to get String " + string + " from language " + name + "!");
									}
									fails.add(string);
								}

								languages_strings.put(string, cfg.getString(string));

							});

							known_lists.forEach((string) -> {

								if (cfg.getStringList(string) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.LANG_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + string + " from language "
														+ name + "!");
									}
									fails.add(string);
								}

								languages_lists.put(string, cfg.getStringList(string));

							});

						} else if (filenamed.toLowerCase().equalsIgnoreCase("jobs.yml")) {

							YamlConfiguration cfg = YamlConfiguration.loadConfiguration(filed);

							plugin.getJobCache().forEach((named, type) -> {

								String job_display = "Jobs." + type.getConfigID() + ".Display";

								if (!cfg.contains(job_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
														+ "Failed to get String Display from language " + name + " for "
														+ type.getConfigID() + "!");
									}
									fails.add(job_display);
								}

								jobs_languages_strings.put(job_display, cfg.getString(job_display));

								String job_broadcast_levelup = "Jobs." + type.getConfigID()
										+ ".BroadCastMessageOnLevelUp";

								if (cfg.contains(job_broadcast_levelup)) {
									jobs_languages_strings.put(job_broadcast_levelup,
											cfg.getString(job_broadcast_levelup));
								}

								String job_display_id = "Jobs." + type.getConfigID() + ".DisplayID";

								if (!cfg.contains(job_display_id)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
														+ "Failed to get String DisplayID from language " + name
														+ " for " + type.getConfigID() + "!");
									}
									fails.add(job_display_id);
								}

								jobs_languages_strings.put(job_display_id, cfg.getString(job_display_id));

								if (type.hasMaxEarningsPerDay()) {
									String bossbar = "Jobs." + type.getConfigID() + ".MaxEarningsBossBar";

									if (!cfg.contains(bossbar)) {
										if (plugin.getLocalFileManager().getUtilsConfig()
												.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
											Bukkit.getConsoleSender()
													.sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
															+ "Loading Job " + type.getConfigID()
															+ " without Max Earnings BossBar Message... ");
										}
									}

									jobs_languages_strings.put(bossbar, cfg.getString(bossbar));

									String actionbar = "Jobs." + type.getConfigID() + ".MaxEarningsActionbar";

									if (!cfg.contains(actionbar)) {
										if (plugin.getLocalFileManager().getUtilsConfig()
												.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
											Bukkit.getConsoleSender()
													.sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
															+ "Loading Job " + type.getConfigID()
															+ " without Max Earnings ActionBar Message... ");
										}
									}

									jobs_languages_strings.put(actionbar, cfg.getString(actionbar));

									String message = "Jobs." + type.getConfigID() + ".MaxEarningsMessage";

									if (!cfg.contains(message)) {
										if (plugin.getLocalFileManager().getUtilsConfig()
												.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
											Bukkit.getConsoleSender()
													.sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
															+ "Loading Job " + type.getConfigID()
															+ " without Max Earnings Message... ");
										}
									}

									jobs_languages_strings.put(message, cfg.getString(message));
								}

								String bossbar = "Jobs." + type.getConfigID() + ".BossBar";

								if (!cfg.contains(bossbar)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender().sendMessage(
												PluginColor.JOB_RELATED_WARNING.getPrefix() + "Loading Job "
														+ type.getConfigID() + " without BossBar Message... ");
									}
								}

								jobs_languages_strings.put(bossbar, cfg.getString(bossbar));

								String actionbar = "Jobs." + type.getConfigID() + ".Actionbar";

								if (!cfg.contains(actionbar)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
														+ "Loading Job " + type.getConfigID()
														+ " without ActionBar Message... ");
									}
								}

								jobs_languages_strings.put(actionbar, cfg.getString(actionbar));

								String message = "Jobs." + type.getConfigID() + ".Message";

								if (!cfg.contains(message)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender().sendMessage(
												PluginColor.JOB_RELATED_WARNING.getPrefix() + "Loading Job "
														+ type.getConfigID() + " without Reward Message... ");
									}
								}

								jobs_languages_strings.put(message, cfg.getString(message));

								if (type.getPermission() != null) {

									String path_lore = "Jobs." + type.getConfigID() + ".NoPermLore";
									String path_message = "Jobs." + type.getConfigID() + ".NoPermMessage";

									if (cfg.getStringList(path_lore) == null) {
										if (plugin.getLocalFileManager().getUtilsConfig()
												.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
											Bukkit.getConsoleSender()
													.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
															+ "Failed to get Stringlist NoPermLore from language "
															+ name + " for " + type.getConfigID() + "!");
										}
										fails.add(path_lore);
									}

									jobs_languages_lists.put(path_lore, cfg.getStringList(path_lore));

									if (!cfg.contains(path_message)) {
										if (plugin.getLocalFileManager().getUtilsConfig()
												.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
											Bukkit.getConsoleSender()
													.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
															+ "Failed to get String NoPermMessage from language " + name
															+ " for " + type.getConfigID() + "!");
										}
										fails.add(path_message);
									}

									jobs_languages_strings.put(path_message, cfg.getString(path_message));
								}

								if (type.getOptions().containsKey("CannotLeaveJob")) {

									String path_message = "Jobs." + type.getConfigID() + ".CannotLeaveJobMessage";

									if (!cfg.contains(path_message)) {
										if (plugin.getLocalFileManager().getUtilsConfig()
												.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
											Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_ERROR
													.getPrefix()
													+ "Failed to get String CannotLeaveJobMessage from language " + name
													+ " for " + type.getConfigID() + "!");
										}
										fails.add(path_message);
									}

									jobs_languages_strings.put(path_message, cfg.getString(path_message));
								}

								String job_lore = "Jobs." + type.getConfigID() + ".Lore";

								if (cfg.getStringList(job_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist Lore from language " + name
														+ " for " + type.getConfigID() + "!");
									}
									fails.add(job_lore);
								}

								jobs_languages_lists.put(job_lore, cfg.getStringList(job_lore));

								String job_stats_main = "Jobs." + type.getConfigID() + ".Stats";

								if (cfg.getStringList(job_stats_main) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist Stats from language " + name
														+ " for " + type.getConfigID() + "!");
									}
									fails.add(job_stats_main);
								}

								jobs_languages_lists.put(job_stats_main, cfg.getStringList(job_stats_main));

								String job_stats_display = "Jobs." + type.getConfigID() + ".StatsGUI.Display";

								if (!cfg.contains(job_stats_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
														+ "Failed to get String StatsGUI.Display from language " + name
														+ " for " + type.getConfigID() + "!");
									}
									fails.add(job_stats_display);
								}

								jobs_languages_strings.put(job_stats_display, cfg.getString(job_stats_display));

								String job_stats_self = "Jobs." + type.getConfigID() + ".StatsGUI.Lore.Self";

								if (cfg.getStringList(job_stats_self) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist StatsGUI.Lore.Self from language "
														+ name + " for " + type.getConfigID() + "!");
									}
									fails.add(job_stats_self);
								}

								jobs_languages_lists.put(job_stats_self, cfg.getStringList(job_stats_self));

								String job_stats_other = "Jobs." + type.getConfigID() + ".StatsGUI.Lore.Other";

								if (cfg.getStringList(job_stats_other) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist StatsGUI.Lore.Other from language "
														+ name + " for " + type.getConfigID() + "!");
									}
									fails.add(job_stats_other);
								}

								jobs_languages_lists.put(job_stats_other, cfg.getStringList(job_stats_other));

								type.getLevels().forEach((lvl, ty) -> {

									String job_levels_display = "Jobs." + type.getConfigID() + ".Levels." + lvl
											+ ".Display";

									if (!cfg.contains(job_levels_display)) {
										if (plugin.getLocalFileManager().getUtilsConfig()
												.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
											Bukkit.getConsoleSender()
													.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
															+ "Failed to get String Levels." + lvl
															+ ".Display from language " + name + " for "
															+ type.getConfigID() + "!");
										}
										fails.add(job_levels_display);
									}

									jobs_languages_strings.put(job_levels_display, cfg.getString(job_levels_display));

									String job_levels_lore = "Jobs." + type.getConfigID() + ".Levels." + lvl + ".Lore";

									if (cfg.getStringList(job_levels_lore) == null) {
										if (plugin.getLocalFileManager().getUtilsConfig()
												.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
											Bukkit.getConsoleSender()
													.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
															+ "Failed to get Stringlist Levels." + lvl
															+ ".Lore from language " + name + " for "
															+ type.getConfigID() + "!");
										}
										fails.add(job_levels_lore);
									}

									jobs_languages_lists.put(job_levels_lore, cfg.getStringList(job_levels_lore));

									String job_rank_display = "Jobs." + type.getConfigID() + ".Levels." + lvl + ".Rank";

									if (!cfg.contains(job_rank_display)) {
										if (plugin.getLocalFileManager().getUtilsConfig()
												.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
											Bukkit.getConsoleSender()
													.sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
															+ "Loading Level " + lvl + " without Level Rank for "
															+ type.getConfigID() + "!");
										}
									} else {
										jobs_languages_strings.put(job_rank_display, cfg.getString(job_rank_display));
									}

									String job_level_broascast = "Jobs." + type.getConfigID() + ".Levels." + lvl
											+ ".BroadCastMessageOnLevelUp";

									if (!cfg.contains(job_level_broascast)) {
										if (plugin.getLocalFileManager().getUtilsConfig()
												.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
											Bukkit.getConsoleSender()
													.sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
															+ "Loading Level " + lvl
															+ " without Level Broadcast Message for "
															+ type.getConfigID() + "!");
										}
									} else {
										jobs_languages_strings.put(job_level_broascast,
												cfg.getString(job_level_broascast));
									}

								});

								type.getActionList().forEach((action) -> {

									type.getIDsOf(action).forEach((iid, tyd) -> {
										String internal = tyd.getInternalID();

										String job_id_display = "Jobs." + type.getConfigID() + ".IDS." + internal
												+ ".Display";

										if (!cfg.contains(job_id_display)) {
											if (plugin.getLocalFileManager().getUtilsConfig()
													.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
												Bukkit.getConsoleSender()
														.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
																+ "Failed to get String IDS." + internal
																+ ".Display from language " + name + " for "
																+ type.getConfigID() + "!");
											}
											fails.add(job_id_display);
										}

										jobs_languages_strings.put(job_id_display, cfg.getString(job_id_display));

										String job_id_rewards_display = "Jobs." + type.getConfigID() + ".IDS."
												+ internal + ".Rewards.Display";

										if (!cfg.contains(job_id_rewards_display)) {
											if (plugin.getLocalFileManager().getUtilsConfig()
													.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
												Bukkit.getConsoleSender()
														.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
																+ "Failed to get String IDS." + internal
																+ ".Rewards.Display from language " + name + " for "
																+ type.getConfigID() + "!");
											}
											fails.add(job_id_rewards_display);
										}

										jobs_languages_strings.put(job_id_rewards_display,
												cfg.getString(job_id_rewards_display));

										String job_id_rewards_lore = "Jobs." + type.getConfigID() + ".IDS." + internal
												+ ".Rewards.Lore";

										if (cfg.getStringList(job_id_rewards_lore) == null) {
											if (plugin.getLocalFileManager().getUtilsConfig()
													.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
												Bukkit.getConsoleSender()
														.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
																+ "Failed to get Stringlist IDS." + internal
																+ ".Rewards.Lore from language " + name + " for "
																+ type.getConfigID() + "!");
											}
											fails.add(job_id_rewards_lore);
										}

										jobs_languages_lists.put(job_id_rewards_lore,
												cfg.getStringList(job_id_rewards_lore));

									});

								});

							});

						} else if (filenamed.toLowerCase().equalsIgnoreCase("guis.yml")) {

							YamlConfiguration cfg = YamlConfiguration.loadConfiguration(filed);

							guis_known_names.forEach((string) -> {

								if (!cfg.contains(string)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + string + " from language "
														+ name + "!");
									}
									fails.add(string);
								}

								gui_languages_strings.put(string, cfg.getString(string));

							});

							guis_known_lists.forEach((string) -> {

								if (cfg.getStringList(string) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + string + " from language "
														+ name + "!");
									}
									fails.add(string);
								}

								gui_languages_lists.put(string, cfg.getStringList(string));

							});

							fm.getGUI().getStringList("Main_Custom.List").forEach((named) -> {

								String get_display = "Main_Custom." + named + ".Display";
								String get_lore = "Main_Custom." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getLeaveConfirmConfig().getStringList("LeaveConfirm_Custom.List").forEach((named) -> {

								String get_display = "LeaveConfirm_Custom." + named + ".Display";
								String get_lore = "LeaveConfirm_Custom." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getWithdrawConfig().getStringList("Withdraw_Custom.List").forEach((named) -> {

								String get_display = "Withdraw_Custom." + named + ".Display";
								String get_lore = "Withdraw_Custom." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getWithdrawConfirmConfig().getStringList("ConfirmWithdraw_Custom.List")
									.forEach((named) -> {

										String get_display = "ConfirmWithdraw_Custom." + named + ".Display";
										String get_lore = "ConfirmWithdraw_Custom." + named + ".Lore";

										if (!cfg.contains(get_display)) {
											if (plugin.getLocalFileManager().getUtilsConfig()
													.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
												Bukkit.getConsoleSender()
														.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
																+ "Failed to get Stringlist " + get_display
																+ " from language " + name + "!");
											}
											fails.add(get_display);
										}

										gui_languages_strings.put(get_display, cfg.getString(get_display));

										if (cfg.getStringList(get_lore) == null) {
											if (plugin.getLocalFileManager().getUtilsConfig()
													.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
												Bukkit.getConsoleSender()
														.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix()
																+ "Loading " + named + " from language " + name
																+ " without Lore...");
											}
										} else {
											gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
										}

									});

							fm.getLanguageGUIConfig().getStringList("Custom.List").forEach((named) -> {

								String get_display = "Custom." + named + ".Display";
								String get_lore = "Custom." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getEarningsJobConfig().getStringList("Job_Earnings_Custom.List").forEach((named) -> {

								String get_display = "Job_Earnings_Custom." + named + ".Display";
								String get_lore = "Job_Earnings_Custom." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getEarningsAllConfig().getStringList("All_Earnings_Custom.List").forEach((named) -> {

								String get_display = "All_Earnings_Custom." + named + ".Display";
								String get_lore = "All_Earnings_Custom." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getHelpSettings().getStringList("Help_Custom.List").forEach((named) -> {

								String get_display = "Help_Custom." + named + ".Display";
								String get_lore = "Help_Custom." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getConfirm().getStringList("AreYouSureGUI_Custom.List").forEach((named) -> {

								String get_display = "AreYouSureGUI_Custom." + named + ".Display";
								String get_lore = "AreYouSureGUI_Custom." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getSettings().getStringList("Settings_Custom.List").forEach((named) -> {

								String get_display = "Settings_Custom." + named + ".Display";
								String get_lore = "Settings_Custom." + named + ".Lore";

								if (!cfg.contains(get_display)) {

									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getRewardsConfig().getStringList("Rewards_Custom.List").forEach((named) -> {

								String get_display = "Rewards_Custom." + named + ".Display";
								String get_lore = "Rewards_Custom." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getRankingGlobalConfig().getStringList("Global_Custom.List").forEach((named) -> {

								String get_display = "Global_Custom." + named + ".Display";
								String get_lore = "Global_Custom." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getRankingGlobalConfig().getStringList("Global_RankingItems.List").forEach((named) -> {
								String get_display = "Global_RankingItems." + named + ".Display";
								String get_lore = "Global_RankingItems." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getRankingPerJobConfig().getStringList("PerJobRanking_Custom.List").forEach((named) -> {

								String get_display = "PerJobRanking_Custom." + named + ".Display";
								String get_lore = "PerJobRanking_Custom." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getRankingPerJobConfig().getStringList("PerJobRanking_Items.List").forEach((named) -> {
								String get_display = "PerJobRanking." + named + ".Display";
								String get_lore = "PerJobRanking." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getLevelGUIConfig().getStringList("Levels_Custom.List").forEach((named) -> {

								String get_display = "Levels_Custom." + named + ".Display";
								String get_lore = "Levels_Custom." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getStatsConfig().getStringList("Other_Custom.List").forEach((named) -> {

								String get_display = "Other_Custom." + named + ".Display";
								String get_lore = "Other_Custom." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

							fm.getStatsConfig().getStringList("Self_Custom.List").forEach((named) -> {

								String get_display = "Self_Custom." + named + ".Display";
								String get_lore = "Self_Custom." + named + ".Lore";

								if (!cfg.contains(get_display)) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix()
														+ "Failed to get Stringlist " + get_display + " from language "
														+ name + "!");
									}
									fails.add(get_display);
								}

								gui_languages_strings.put(get_display, cfg.getString(get_display));

								if (cfg.getStringList(get_lore) == null) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.GUI_RELATED_WARNING.getPrefix() + "Loading "
														+ named + " from language " + name + " without Lore...");
									}
								} else {
									gui_languages_lists.put(get_lore, cfg.getStringList(get_lore));
								}

							});

						}

					}

					if (fails.size() != 0) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
							Bukkit.getConsoleSender()
									.sendMessage(PluginColor.LANG_RELATED_ERROR.getPrefix() + "Failed to load language "
											+ name + " with " + fails.size() + " missing options!");
						}
					} else {

						String newname = name.replaceAll(".yml", " ").replaceAll(" ", "");

						Language lg = new Language(newname, lang_named, lang_icon, lang_display, lang_model,
								languages_strings, languages_lists, gui_languages_strings, gui_languages_lists,
								jobs_languages_strings, jobs_languages_lists);

						arraylangs.add(lg);
						langs.put(newname, lg);
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
							Bukkit.getConsoleSender().sendMessage(
									PluginColor.LANG_LOADED.getPrefix() + "Successfully loaded language " + name + "!");
						}
					}

				}

			}
		}
	}

}
