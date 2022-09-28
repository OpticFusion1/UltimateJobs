package de.warsteiner.jobs.command.admincommand;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.PlayerDataAPI;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.JsonMessage;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class ExpSub extends AdminSubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin(); 
	
	@Override
	public String getName() {
		return "exp";
	}

	@Override
	public String getDescription() {
		return "Update Player's Exp in a Job";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		 PlayerDataAPI pl = UltimateJobs.getPlugin().getPlayerDataAPI();
		if (args.length == 1) {
			 
			sender.sendMessage("§7");
			sender.sendMessage(" §8| §9UltimateJobs §8- §aPlayer Exp §8|");
			
			if(sender instanceof Player) {
				
				 Player player = (Player) sender;
				
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin exp §8| §7View all arguments")
				 .setClickAsSuggestCmd("/jobsadmin exp").save().send(player);
			 
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin exp set <player_name> <job> <amount> §8| §7Set a Exp Amount")
				 .setClickAsSuggestCmd("/jobsadmin exp set").save().send(player);
				 
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin exp add <player_name> <job> <amount> §8| §7Add a Exp Amount")
				 .setClickAsSuggestCmd("/jobsadmin exp add").save().send(player);
				 
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin exp remove <player_name> <job> <amount> §8| §7Remove a Exp Amount")
				 .setClickAsSuggestCmd("/jobsadmin exp remove").save().send(player);
				 
			} else {
				sender.sendMessage("§8-> §6/JobsAdmin exp §8| §7View all arguments");
				sender.sendMessage("§8-> §6/JobsAdmin exp set <player_name> <job> <amount> §8| §7Set a Exp Amount"); 
				sender.sendMessage("§8-> §6/JobsAdmin exp add <player_name> <job> <amount> §8| §7Add a Exp Amount"); 
				sender.sendMessage("§8-> §6/JobsAdmin exp remove <player_name> <job> <amount> §8| §7Remove a Exp Amount"); 
			}
			
			sender.sendMessage("§7");
			
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
			}
			
		}  else if(args.length == 5 && args[1].toLowerCase().equalsIgnoreCase("remove")) {
			 
			String player = args[2];
			String job = args[3];
			String value = args[4]; 
			
			if (plugin.getPlayerDataAPI().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
 
			String uuid =plugin.getPlayerDataAPI().getUUIDByName(player.toUpperCase());

			String how = plugin.getAPI().isCurrentlyInCache(uuid);

			if (plugin.getAPI().isInt(value)) {

				if(plugin.getAPI().isJobFromConfigID(job.toUpperCase()) != null) {
					Job j = plugin.getAPI().isJobFromConfigID(job.toUpperCase());
					if (how.equalsIgnoreCase("CACHE")) {

						JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(uuid);

						if(jb.ownJob(j.getConfigID())) { 
							double old = jb.getStatsOf(job).getExp();
							plugin.getPlayerAPI().updateExp(uuid, j, old-Integer.valueOf(value)); 
							sender.sendMessage(AdminCommand.prefix + "Removed §c" + player + " §7Exp in Job §a" + j.getConfigID()
									+ " §7-> §6"+value+". §8(§aOnline§8)");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
							}
							return;
						} else {
							sender.sendMessage(AdminCommand.prefix + "Error! Player does not own that Job!");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
							}
							return;
						}

					} else {

						if(pl.getOwnedJobs(uuid).contains(job.toUpperCase())) {
							double old = pl.getExpOf(uuid, job);
							pl.updateExp(uuid,old-Integer.valueOf(value), job);

							sender.sendMessage(AdminCommand.prefix + "Removed §c" + player + " §7Exp in Job §a" + j.getConfigID()
							+ " §7-> §6"+value+". §8(§cOffline§8)");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
							}
							return;
						} else {
							sender.sendMessage(AdminCommand.prefix + "Error! Player does not own that Job!");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
							}
							return;
						}
					}
				} else {
					sender.sendMessage(AdminCommand.prefix + "Error! Cannot find that Job!");
					if(sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					return;
				}

			} else {
				sender.sendMessage(AdminCommand.prefix + "Error! The value must be a Integer");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
			
		} else if(args.length == 5 && args[1].toLowerCase().equalsIgnoreCase("add")) {
			 
			String player = args[2];
			String job = args[3];
			String value = args[4]; 
			
			if (plugin.getPlayerDataAPI().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
 
			String uuid =plugin.getPlayerDataAPI().getUUIDByName(player.toUpperCase());

			String how = plugin.getAPI().isCurrentlyInCache(uuid);

			if (plugin.getAPI().isInt(value)) {

				if(plugin.getAPI().isJobFromConfigID(job.toUpperCase()) != null) {
					Job j = plugin.getAPI().isJobFromConfigID(job.toUpperCase());
					if (how.equalsIgnoreCase("CACHE")) {

						JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(uuid);

						if(jb.ownJob(j.getConfigID())) { 
							double old = jb.getStatsOf(job).getExp();
							plugin.getPlayerAPI().updateExp(uuid, j, old+Integer.valueOf(value)); 
							sender.sendMessage(AdminCommand.prefix + "Added §c" + player + " §7Exp in Job §a" + j.getConfigID()
									+ " §7-> §6"+value+". §8(§aOnline§8)");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
							}
							return;
						} else {
							sender.sendMessage(AdminCommand.prefix + "Error! Player does not own that Job!");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
							}
							return;
						}

					} else {

						if(pl.getOwnedJobs(uuid).contains(job.toUpperCase())) {
							double old = pl.getExpOf(uuid, job);
							pl.updateExp(uuid,old+Integer.valueOf(value), job);

							sender.sendMessage(AdminCommand.prefix + "Added §c" + player + " §7Exp in Job §a" + j.getConfigID()
							+ " §7-> §6"+value+". §8(§cOffline§8)");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
							}
							return;
						} else {
							sender.sendMessage(AdminCommand.prefix + "Error! Player does not own that Job!");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
							}
							return;
						}
					}
				} else {
					sender.sendMessage(AdminCommand.prefix + "Error! Cannot find that Job!");
					if(sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					return;
				}

			} else {
				sender.sendMessage(AdminCommand.prefix + "Error! The value must be a Integer");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
			
		} else if(args.length == 5 && args[1].toLowerCase().equalsIgnoreCase("set")) {
			 
			String player = args[2];
			String job = args[3];
			String value = args[4];

			if (plugin.getPlayerDataAPI().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
 
			String uuid =plugin.getPlayerDataAPI().getUUIDByName(player.toUpperCase());

			String how = plugin.getAPI().isCurrentlyInCache(uuid);

			if (plugin.getAPI().isInt(value)) {

				if(plugin.getAPI().isJobFromConfigID(job.toUpperCase()) != null) {
					Job j = plugin.getAPI().isJobFromConfigID(job.toUpperCase());
					if (how.equalsIgnoreCase("CACHE")) {

						JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(uuid);

						if(jb.ownJob(j.getConfigID())) { 
							plugin.getPlayerAPI().updateExp(uuid, j, Integer.valueOf(value)); 
							sender.sendMessage(AdminCommand.prefix + "Set §c" + player + "'s §7Exp in Job §a" + j.getConfigID()
									+ " §7to §6"+value+". §8(§aOnline§8)");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
							}
							return;
						} else {
							sender.sendMessage(AdminCommand.prefix + "Error! Player does not own that Job!");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
							}
							return;
						}

					} else {

						if(pl.getOwnedJobs(uuid).contains(job.toUpperCase())) {
							pl.updateExp(uuid,Integer.valueOf(value), job);

							sender.sendMessage(AdminCommand.prefix + "Set §c" + player + "'s §7Exp in Job §a" + j.getConfigID()
							+ " §7to §6"+value+". §8(§cOffline§8)");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
							}
							return;
						} else {
							sender.sendMessage(AdminCommand.prefix + "Error! Player does not own that Job!");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
							}
							return;
						}
					}
				} else {
					sender.sendMessage(AdminCommand.prefix + "Error! Cannot find that Job!");
					if(sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					return;
				}

			} else {
				sender.sendMessage(AdminCommand.prefix + "Error! The value must be a Integer");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
			
		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6"+getUsage());
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
			}
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command level admin_gen players_online jobs_listed";
	}
	
	@Override
	public String getUsage() { 
		return "/JobsAdmin exp";
	}

	@Override
	public String getPermission() { 
		return "ultimatejobs.admin.exp";
	}
	
	@Override
	public boolean showOnHelp() { 
		return true;
	}
	
}

