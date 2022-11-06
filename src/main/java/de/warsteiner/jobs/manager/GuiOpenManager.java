package de.warsteiner.jobs.manager;

import java.util.ArrayList;
 
import org.bukkit.Sound;
import org.bukkit.command.CommandSender; 
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.objects.commands.AdminCommandOptions;
import de.warsteiner.jobs.utils.objects.guis.GUIType;
import de.warsteiner.jobs.utils.objects.guis.UpdateTypes;
import de.warsteiner.jobs.utils.objects.jobs.Job;
import de.warsteiner.jobs.utils.objects.jobs.JobsPlayer;

public class GuiOpenManager {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public String isStatsMenuOpendAboutPlayer(Player player, String title) {
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.STATS_OTHER)) {
				String a = plugin.getGUI().getGUIsDetails().get(sp.getUUIDAsString());

				String name = sp.getLanguage().getGUIMessage("Other_Name")
						.replaceAll("<name>", a);

				if (name.equalsIgnoreCase(title)) {
					return a;
				}
			}

		}
		return null;
	}

	public Job isSettingsMenu(Player player, String title) {
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.SETTINGS)) {

				Job j = plugin.getGUI().getGUIsJobs().get(sp.getUUIDAsString());
				String dis = j.getDisplayOfJob(sp.getUUIDAsString());
				String named = sp.getLanguage().getGUIMessage("Settings_Name");
				String fin = plugin.getPluginManager().toHex(named.replaceAll("<job>", dis).replaceAll("&", "§"));

				if (fin.equalsIgnoreCase(title)) {
					return j;
				}

			}

		}
		return null;
	}

	public Job isRewardsMenu(Player player, String title) {
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.REWARDS)) {

				Job j = plugin.getGUI().getGUIsJobs().get(sp.getUUIDAsString());
				String dis = j.getDisplayOfJob(sp.getUUIDAsString());
				String named = sp.getLanguage().getGUIMessage("Rewards_Name");
				String fin = plugin.getPluginManager().toHex(named.replaceAll("<job>", dis).replaceAll("&", "§"));

				if (fin.equalsIgnoreCase(title)) {
					return j;
				}

			}

		}
		return null;
	}

	public String isStatsMenuOpendSelf(Player player, String title) {
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.STATS_SELF)) {
				String name = sp.getLanguage().getGUIMessage("Self_Name");

				if (name.equalsIgnoreCase(title)) {
					return "FOUND";
				}
			}

		}
		return null;
	}

	public String isMainOpend(Player player, String title) {
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.MAIN)) {
				String name = sp.getLanguage().getGUIMessage("Main_Name");

				if (name.equalsIgnoreCase(title)) {
					return "FOUND";
				}
			}

		}
		return null;
	}

	public Job isLevelsMenu(Player player, String title) {
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.LEVELS)) {

				Job j = plugin.getGUI().getGUIsJobs().get(sp.getUUIDAsString());
				String dis = j.getDisplayOfJob(sp.getUUIDAsString());
				String named = sp.getLanguage().getGUIMessage("Levels_Name");
				String fin = plugin.getPluginManager().toHex(named.replaceAll("<job>", dis).replaceAll("&", "§"));

				if (fin.equalsIgnoreCase(title)) {
					return j;
				}

			}

		}
		return null;
	}
	
	public Job isRankingJobMenu(Player player, String title) { 
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.JOB_RANKING)) {

				Job j = plugin.getGUI().getGUIsJobs().get(sp.getUUIDAsString());
				String dis = j.getDisplayOfJob(sp.getUUIDAsString());
				String named = sp.getLanguage().getGUIMessage("PerJobRanking_Name");
				String fin = plugin.getPluginManager().toHex(named.replaceAll("<job>", dis).replaceAll("&", "§"));

				if (fin.equalsIgnoreCase(title)) {
					return j;
				}

			}

		}
		return null;
	}
	
	public String isGlobalRankingMenu(Player player, String title) {
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {
		 
			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.GLOBAL_RANKING)) {
			  
				String named = sp.getLanguage().getGUIMessage("Global_Name");
				String fin = plugin.getPluginManager().toHex(named.replaceAll("&", "§"));
 
				if (fin.equalsIgnoreCase(title)) { 
					return "FOUND";
				}

			}

		}
		return null;
	}

	public String isHelpOpend(Player player, String title) {
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.HELP)) {
				String name = sp.getLanguage().getGUIMessage("Help_Name");

				if (name.equalsIgnoreCase(title)) {
					return "FOUND";
				}
			}

		}
		return null;
	}

	public String isWithdrawMenu(Player player, String title) { 
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.WITHDRAW)) {
				String name = sp.getLanguage().getGUIMessage("Withdraw_Name");

				if (name.equalsIgnoreCase(title)) {
					return "FOUND";
				}
			}

		}
		return null;
	}

	public String isWithdrawConfirmMenu(Player player, String title) { 
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.CONFIRM_WITHDRAW)) {
				String name = sp.getLanguage().getGUIMessage("ConfirmWithdraw_Name");

				if (name.equalsIgnoreCase(title)) {
					return "FOUND";
				}
			}

		}
		return null;
	}

	public String isEarningsALL(Player player, String title) {
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.EARNINGS_ALL)) {
				String name = sp.getLanguage().getGUIMessage("All_Earnings_Name");

				if (name.equalsIgnoreCase(title)) {
					return "FOUND";
				}
			}

		}
		return null;
	}

	public Job isEarningsAboutJob(Player player, String title) {
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.EARNINGS_JOB)) {

				Job j = plugin.getGUI().getGUIsJobs().get(sp.getUUIDAsString());
				String dis = j.getDisplayOfJob(sp.getUUIDAsString());
				String named = sp.getLanguage().getGUIMessage("Job_Earnings_Name");
				String fin = plugin.getPluginManager().toHex(named.replaceAll("<job>", dis).replaceAll("&", "§"));

				if (fin.equalsIgnoreCase(title)) {
					return j;
				}

			}

		}
		return null;
	}

	public String isLanguageOpend(Player player, String title) {
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.LANGUAGE)) {
				String name = sp.getLanguage().getGUIMessage("Name");

				if (name.equalsIgnoreCase(title)) {
					return "FOUND";
				}
			}

		}
		return null;
	}

	public Job isConfirmGUI(Player player, String title) {
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.CONFIRM)) {

				Job j = plugin.getGUI().getGUIsJobs().get(sp.getUUIDAsString());
				String dis = j.getDisplayOfJob(sp.getUUIDAsString());
				String named = sp.getLanguage().getGUIMessage("AreYouSureGUI_Name");
				String fin = plugin.getPluginManager().toHex(named.replaceAll("<job>", dis).replaceAll("&", "§"));

				if (fin.equalsIgnoreCase(title)) {
					return j;
				}

			}

		}
		return null;
	}

	public Job isLeaveConfirmGUI(Player player, String title) { 
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.CONFIRM_LEAVE)) {

				Job j = plugin.getGUI().getGUIsJobs().get(sp.getUUIDAsString());

				String dis = j.getDisplayOfJob(sp.getUUIDAsString());
				String named = sp.getLanguage().getGUIMessage("LeaveConfirm_Name");
				String fin = plugin.getPluginManager().toHex(named.replaceAll("<job>", dis).replaceAll("&", "§"));

				if (title.equalsIgnoreCase(fin)) {
					return j;
				}

			}

		}
		return null;
	}

	public void openGuiByGuiID(CommandSender sender, GUIType type, Player player, Job job, String about, boolean y, ArrayList<AdminCommandOptions> options) {
		GuiManager gui = plugin.getGUI();
		GuiAddonManager addon = plugin.getGUIAddonManager();

		if (type != null) {

			boolean b = false;

			if (type.equals(GUIType.SETTINGS)) {

				if (job == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: Job");
					return;
				}

				if (!plugin.getPlayerAPI().getRealJobPlayer(player.getUniqueId()).getStatsList()
						.containsKey(job.getConfigID())) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: No Data Found");
					return;
				}
				b = true;
				gui.createSettingsGUI(player, job, UpdateTypes.OPEN);
			} else if (type.equals(GUIType.MAIN)) {
				b = true;
				gui.createMainGUIOfJobs(player, UpdateTypes.OPEN);
			} else if (type.equals(GUIType.LANGUAGE)) {
				b = true;
				gui.openLanguageMenu(player, UpdateTypes.OPEN);
			} else if (type.equals(GUIType.HELP)) {
				b = true;
				gui.createHelpGUI(player, UpdateTypes.OPEN);
			} else if (type.equals(GUIType.CONFIRM)) {

				if (job == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: Job");
					return;
				}
				b = true;
				gui.createAreYouSureGUI(player, job, UpdateTypes.OPEN);
			} else if (type.equals(GUIType.CONFIRM_LEAVE)) {

				if (job == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: Job");
					return;
				}
				b = true;
				plugin.getGUIAddonManager().createLeaveConfirmGUI(player, UpdateTypes.OPEN, job);
			} else if (type.equals(GUIType.STATS_SELF)) {
				b = true;
				addon.createSelfStatsGUI(player, UpdateTypes.OPEN);
			} else if (type.equals(GUIType.STATS_OTHER)) {

				if (about == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: About Player");
					return;
				}

				if (plugin.getPlayerAPI().getJobsPlayerByName(about.toLowerCase()) == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: About Player Not Found");
					return;
				}

				String id = plugin.getPlayerAPI().getJobsPlayerByName(about.toLowerCase());
				b = true;
				addon.createOtherStatsGUI(player, UpdateTypes.OPEN, about, id);
			} else if (type.equals(GUIType.REWARDS)) {

				if (job == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: Job");
					return;
				}
				b = true;
				addon.createRewardsGUI(player, UpdateTypes.OPEN, job);

			} else if (type.equals(GUIType.LEVELS)) {
				if (job == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: Job");
					return;
				}
				if (!plugin.getPlayerAPI().getRealJobPlayer(player.getUniqueId()).getStatsList()
						.containsKey(job.getConfigID())) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: No Data Found");
					return;
				}
				b = true;
				addon.createLevelsGUI(player, UpdateTypes.OPEN, job);
			} else if (type.equals(GUIType.EARNINGS_ALL)) {
				b = true;
				addon.createEarningsGUI_ALL_Jobs(player, UpdateTypes.OPEN);
			} else if (type.equals(GUIType.EARNINGS_JOB)) {

				if (job == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: Job");
					return;
				}
				b = true;
				addon.createEarningsGUI_Single_Job(player, UpdateTypes.OPEN, job);
			} else if (type.equals(GUIType.JOB_RANKING)) {

				if (job == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: Job");
					return;
				}
				b = true;
				addon.createJobRankingGUI(player, UpdateTypes.OPEN, job);
			} else if (type.equals(GUIType.WITHDRAW)) {

				b = true;
				addon.createWithdrawMenu(player, UpdateTypes.OPEN);
			}  else if (type.equals(GUIType.GLOBAL_RANKING)) {

				b = true;
				addon.createGlobalRankingGUI(player, UpdateTypes.OPEN);
			}
			if (b) {
				if (y) {
					
					if(!options.contains(AdminCommandOptions.SILENT)) {
						if (job == null) {
							sender.sendMessage(AdminCommand.prefix + "Opend GUI §e" + type + " §7for Player §c"
									+ player.getName() + "§7!");
						} else {
							sender.sendMessage(AdminCommand.prefix + "Opend GUI §e" + type + " §7for Player §c"
									+ player.getName() + "§7! §8(§7Job: §b" + job.getConfigID() + "§8)");
						}
					}

					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
					}
				}
			}
		}

	}

}
