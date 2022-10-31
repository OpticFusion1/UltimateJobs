package de.warsteiner.jobs.command.admincommand;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.JsonMessage;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;
import de.warsteiner.jobs.utils.objects.jobs.Job;
import de.warsteiner.jobs.utils.objects.jobs.JobsPlayer;

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
			
			if (plugin.getPlayerAPI().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
 
			String uuid =plugin.getPlayerAPI().getUUIDByName(player.toUpperCase());
 
			if (plugin.getAPI().isInt(value)) {

				if(plugin.getAPI().isJobFromConfigID(job.toUpperCase()) != null) {
					Job j = plugin.getAPI().isJobFromConfigID(job.toUpperCase());
				  
						if(plugin.getPlayerAPI().getOwnedJobs(uuid).contains(job.toUpperCase())) {
							double old = plugin.getPlayerAPI().getExpOf(uuid, job);
							plugin.getPlayerAPI().updateExp(uuid,job,old-Integer.valueOf(value));

							sender.sendMessage(AdminCommand.prefix + "Removed §c" + player + " §7Exp in Job §a" + j.getConfigID()
							+ " §7-> §6"+value+".");
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
			
			if (plugin.getPlayerAPI().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
 
			String uuid =plugin.getPlayerAPI().getUUIDByName(player.toUpperCase());
 
			if (plugin.getAPI().isInt(value)) {

				if(plugin.getAPI().isJobFromConfigID(job.toUpperCase()) != null) {
					Job j = plugin.getAPI().isJobFromConfigID(job.toUpperCase());
				   
						if(plugin.getPlayerAPI().getOwnedJobs(uuid).contains(job.toUpperCase())) {
							double old = plugin.getPlayerAPI().getExpOf(uuid, job);
							plugin.getPlayerAPI().updateExp(uuid,job,old+Integer.valueOf(value));

							sender.sendMessage(AdminCommand.prefix + "Added §c" + player + " §7Exp in Job §a" + j.getConfigID()
							+ " §7-> §6"+value+".");
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

			if (plugin.getPlayerAPI().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
 
			String uuid =plugin.getPlayerAPI().getUUIDByName(player.toUpperCase());

			 
			if (plugin.getAPI().isInt(value)) {

				if(plugin.getAPI().isJobFromConfigID(job.toUpperCase()) != null) {
					if(plugin.getPlayerAPI().getOwnedJobs(uuid).contains(job.toUpperCase())) { 
							plugin.getPlayerAPI().updateExp(uuid, job, Integer.valueOf(value)); 
							sender.sendMessage(AdminCommand.prefix + "Set §c" + player + "'s §7Exp in Job §a" + job 
									+ " §7to §6"+value+".");
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

