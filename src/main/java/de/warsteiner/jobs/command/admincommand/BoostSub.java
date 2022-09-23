package de.warsteiner.jobs.command.admincommand;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.JsonMessage;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand; 

public class BoostSub extends AdminSubCommand {

	@Override
	public String getName() {
		return "boost";
	}

	@Override
	public String getDescription() {
		return "Set/Unset/Info Player Multipliers";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if (args.length == 1) {
			
			sender.sendMessage("§7");
			sender.sendMessage(" §8| §9UltimateJobs §8- §aPlayer Multipliers §8|");
			
			if(sender instanceof Player) {
				
				 Player player = (Player) sender;
				
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin boost §8| §7View all arguments")
				 .setClickAsSuggestCmd("/JobsAdmin boost").save().send(player);
			 
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin boost info §8| §7View all multipliers")
				 .setClickAsSuggestCmd("/JobsAdmin boost info").save().send(player);
				 
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin boost set <player_name> <name_of_boost> <type> <until example: 1m | 2h | 5d (use X for no date)> <weight> <value> <job> §8| §7Create a multiplier for a player")
				 .setClickAsSuggestCmd("/JobsAdmin set").save().send(player);
				 
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin boost unset <player_name> <name_of_boost> §8| §7Remove a multiplier from a player")
				 .setClickAsSuggestCmd("/JobsAdmin unset").save().send(player);
			} else {
				sender.sendMessage("§8-> §6/JobsAdmin boost §8| §7View all arguments");
				sender.sendMessage("§8-> §6/JobsAdmin info <player_name> §8| §7View all multipliers");
				sender.sendMessage("§8-> §6/JobsAdmin boost set <player_name> <name_of_boost> <type> <until example: 1m | 2h | 5d (use X for no date)> <weight> <value> <job> §8| §7Create a multiplier for a player");
				sender.sendMessage("§8-> §6/JobsAdmin boost unset <player_name> <name_of_boost> §8| §7Remove a multiplier from a player");
			}
			
			sender.sendMessage("§7");
			
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
			}
		} else if(args.length == 2) {
			 
		} else {
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
			}
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6"+getUsage());
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command boost";
	}

	@Override
	public String getUsage() { 
		return "/JobsAdmin boost";
	}

	@Override
	public String getPermission() { 
		return "ultimatejobs.admin.boost";
	}
	
	@Override
	public boolean showOnHelp() { 
		return true;
	}
	
}
