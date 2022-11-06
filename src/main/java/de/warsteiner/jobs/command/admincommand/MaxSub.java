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

public class MaxSub extends AdminSubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin(); 
	
	@Override
	public String getName() {
		return "max";
	}

	@Override
	public String getDescription() {
		return "Update Player's Max Jobs";
	}

	@Override
	public void perform(CommandSender sender, String[] args) { 
		if (args.length == 1) {
			 
			sender.sendMessage("§7");
			sender.sendMessage(" §8| §9UltimateJobs §8- §aPlayer Max Jobs §8|");
			
			if(sender instanceof Player) {
				
				 Player player = (Player) sender;
				
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin max §8| §7View all arguments")
				 .setClickAsSuggestCmd("/jobsadmin max").save().send(player);
			 
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin max set <player_name> <amount> §8| §7Set a Amount")
				 .setClickAsSuggestCmd("/jobsadmin max set").save().send(player);
				 
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin max add <player_name> <amount> §8| §7Add a Amount")
				 .setClickAsSuggestCmd("/jobsadmin max add").save().send(player);
				 
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin max remove <player_name> <amount> §8| §7Remove a Amount")
				 .setClickAsSuggestCmd("/jobsadmin max remove").save().send(player);
			 
			} else {
				sender.sendMessage("§8-> §6/JobsAdmin max §8| §7View all arguments");
				sender.sendMessage("§8-> §6/JobsAdmin max set <player_name> <amount> §8| §7Set a Amount");
				sender.sendMessage("§8-> §6/JobsAdmin max add <player_name> <amount> §8| §7Add a Amount");
				sender.sendMessage("§8-> §6/JobsAdmin max remove <player_name> <amount> §8| §7Remove a Amount");
			}
			
			sender.sendMessage("§7");
			
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
			}
			
		}  else if(args.length == 4 && args[1].toLowerCase().equalsIgnoreCase("remove")) {  
			
			String player = args[2];
			String value = args[3];

			if (plugin.getPlayerAPI().getJobsPlayerByName(player.toLowerCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
 
			String uuid = plugin.getPlayerAPI().getJobsPlayerByName(player.toLowerCase());

			 
			if (plugin.getAPI().isInt(value)) {
				
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}
 
					int old = plugin.getPlayerAPI().getMaxJobs(uuid);
					
					if(old-Integer.valueOf(value) <= 0) {
						sender.sendMessage(AdminCommand.prefix + "Error! Value cannot be less then 0!");
						if(sender instanceof Player) {
							Player player3 = (Player) sender;
							player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
						}
						return;
					}
					
					plugin.getPlayerAPI().updateMax(uuid, old-Integer.valueOf(value));

					sender.sendMessage(AdminCommand.prefix + "Removed §c" + value + " §7to §a"+player+"§7's Max Jobs" 
							+ "§7.");
					return;
				 
			} else {
				sender.sendMessage(AdminCommand.prefix + "Error! The value must be a Integer");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
			
		}else if(args.length == 4 && args[1].toLowerCase().equalsIgnoreCase("add")) { 
			 
			String player = args[2];
			String value = args[3];

			if (plugin.getPlayerAPI().getJobsPlayerByName(player.toLowerCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
 
			String uuid = plugin.getPlayerAPI().getJobsPlayerByName(player.toLowerCase());

		 
			if (plugin.getAPI().isInt(value)) {
				
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}
 
					int old = plugin.getPlayerAPI().getMaxJobs(uuid);
					
					plugin.getPlayerAPI().updateMax(uuid, old+Integer.valueOf(value));

					sender.sendMessage(AdminCommand.prefix + "Added §a" + value + " §7to §c"+player+"§7's Max Jobs" 
							+ "§7.");
					return;
				 

			} else {
				sender.sendMessage(AdminCommand.prefix + "Error! The value must be a Integer");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
			
		}else if(args.length == 4 && args[1].toLowerCase().equalsIgnoreCase("set")) {
			 
			String player = args[2];
			String value = args[3];

			if (plugin.getPlayerAPI().getJobsPlayerByName(player.toLowerCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
 
			String uuid = plugin.getPlayerAPI().getJobsPlayerByName(player.toLowerCase());
 
			if (plugin.getAPI().isInt(value)) {
				
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}
 
				plugin.getPlayerAPI().updateMax(uuid, Integer.valueOf(value));

					sender.sendMessage(AdminCommand.prefix + "Changed §c" + player + "'s §7max Jobs to §a" + value
							+ "§7.");
					return;
				 

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
		return "command max admin_gen players_online";
	}
	
	@Override
	public String getUsage() { 
		return "/JobsAdmin max";
	}

	@Override
	public String getPermission() { 
		return "ultimatejobs.admin.max";
	}
	
	@Override
	public boolean showOnHelp() { 
		return true;
	}
	
}

