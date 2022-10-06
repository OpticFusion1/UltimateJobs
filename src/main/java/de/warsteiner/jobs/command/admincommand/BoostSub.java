package de.warsteiner.jobs.command.admincommand;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job; 
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.JsonMessage;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;
import de.warsteiner.jobs.utils.objects.JobsMultiplier;
import de.warsteiner.jobs.utils.objects.MultiplierType;
import de.warsteiner.jobs.utils.objects.MultiplierWeight; 

public class BoostSub extends AdminSubCommand {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin(); 

	@Override
	public String getName() {
		return "boost";
	}

	@Override
	public String getDescription() {
		return "Player Multipliers/Boosts";
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
				 .setClickAsSuggestCmd("/jobsadmin boost").save().send(player);
			 
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin boost info §8| §7View all multipliers")
				 .setClickAsSuggestCmd("/jobsadmin boost info").save().send(player);
				 
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin boost set <player_name> <name_of_boost> <type> <until example: 1m | 2h | 5d (use X for no date)> <weight> <value> <job> §8| §7Create a multiplier for a player")
				 .setClickAsSuggestCmd("/jobsadmin boost set").save().send(player);
				 
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin boost unset <player_name> <name_of_boost> §8| §7Remove a multiplier from a player")
				 .setClickAsSuggestCmd("/jobsadmin boost unset").save().send(player);
			} else {
				sender.sendMessage("§8-> §6/JobsAdmin boost §8| §7View all arguments");
				sender.sendMessage("§8-> §6/JobsAdmin boost info <player_name> §8| §7View all multipliers");
				sender.sendMessage("§8-> §6/JobsAdmin boost set <player_name> <name_of_boost> <type> <until example: 1m | 2h | 5d (use X for no date)> <weight> <value> <job> §8| §7Create a multiplier for a player");
				sender.sendMessage("§8-> §6/JobsAdmin boost unset <player_name> <name_of_boost> §8| §7Remove a multiplier from a player");
			}
			
			sender.sendMessage("§7");
			
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
			}
		} else if(args.length == 3 && args[1].toLowerCase().equalsIgnoreCase("info")) {
			 
			String player = args[2];
			
			if (plugin.getPlayerAPI().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
 
			String uuid = plugin.getPlayerAPI().getUUIDByName(player.toUpperCase());
			
		 
			if(plugin.getPlayerAPI().getMultipliers(uuid) == null || plugin.getPlayerAPI().getMultipliers(uuid).size() == 0) {
				sender.sendMessage(AdminCommand.prefix + "Player does not have any active multipliers.");
			} else {
				
				ArrayList<JobsMultiplier> m = plugin.getPlayerAPI().getMultipliers(uuid);
				
				sender.sendMessage("§7");
				sender.sendMessage(" §8| §9UltimateJobs §8- §aMultipliers Info §8: §c"+player+" §8|");
				
				for(JobsMultiplier d : m) {
					sender.sendMessage("§8-> §a"+d.getName()+" §8(§7"+d.getByPlugin()+"§8) §8| §7Until§8: §7"+d.getUntil()
					+ " §8| §7Type§8: §7"+d.getType().toString()+" §8| §7Weight§8: §7"+d.getWeight().toString()+" §8| §7Value§8: §7"+d.getValue()+" §8| §7Job§8: §e"+d.getJob());
				}
				
				sender.sendMessage("§a");
				
			}
			
			 
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
			}
			
		} else if(args.length == 4 && args[1].toLowerCase().equalsIgnoreCase("unset")) { 
			
			String player = args[2];
			
			if (plugin.getPlayerAPI().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
 
			String uuid =plugin.getPlayerAPI().getUUIDByName(player.toUpperCase());
			
			String named = args[3];
			
			if(!plugin.getPlayerAPI().existMultiplier(uuid, named)) {
				sender.sendMessage(AdminCommand.prefix + "There is no boost called : "+named);
				return;
			}
			
			plugin.getPlayerAPI().removeMultiplier(uuid, named);
			plugin.getPlayerOfflineAPI().removeMultiplier(uuid, named);
			
			sender.sendMessage(AdminCommand.prefix + "Removed the boost : "+named);
			
		} else if(args.length == 9 && args[1].toLowerCase().equalsIgnoreCase("set")) {
		 
			String player = args[2];
			
			if (plugin.getPlayerAPI().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
 
			String uuid =plugin.getPlayerAPI().getUUIDByName(player.toUpperCase());
			
			String named = args[3];
			
			if(plugin.getPlayerAPI().existMultiplier(uuid, named)) {
				sender.sendMessage(AdminCommand.prefix + "There already exist a boost called : "+named);
				return;
			}
			
			String type = args[4];
			
			if(MultiplierType.valueOf(type.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Cannot find type: "+type);
				return;
			}
			
			String u = args[5].toLowerCase();
			
			String until = "X";
			
			//creating date
			
			if(!u.equalsIgnoreCase("X")) {
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date data = new Date();

				Calendar c1 = Calendar.getInstance();
				c1.setTime(data);
				
				if(u.contains("m")) {
					Integer text = Integer.valueOf(u.replaceAll("m", " ").replaceAll(" ", ""));
					c1.add(Calendar.MINUTE, text);
					
					Date newdate = c1.getTime();
	
					until = format.format(newdate);
				} else if(u.contains("h")) {
					Integer text = Integer.valueOf(u.replaceAll("h", " ").replaceAll(" ", ""));
					c1.add(Calendar.HOUR, text);
					
					Date newdate = c1.getTime();
	
					until = format.format(newdate);
				} if(u.contains("d")) {
					Integer text = Integer.valueOf(u.replaceAll("d", " ").replaceAll(" ", ""));
					c1.add(Calendar.HOUR, text*24);
					
					Date newdate = c1.getTime();
	
					until = format.format(newdate);
				}
				 	 
			}
		 
			 
			String weight = args[6];
			
			if(MultiplierWeight.valueOf(weight.toUpperCase())==null) {
				sender.sendMessage(AdminCommand.prefix + "Cannot find weight: "+weight);
				return;
			}
			
			String value = args[7];
			
			if(Double.valueOf(value) == null) {
				sender.sendMessage(AdminCommand.prefix + "Value must be a double");
				return;
			}
			
			String job = args[8];
			
			String realjob = null;
			
			if(plugin.getAPI().isJobFromConfigID(job.toUpperCase()) != null) {
				Job j = plugin.getAPI().isJobFromConfigID(job.toUpperCase());
				
				realjob = j.getConfigID();
			} else {
				realjob = "NONE";
			}
			
			JobsMultiplier n = new JobsMultiplier(named, "ultimatejobs", MultiplierType.valueOf(type.toUpperCase()), until, MultiplierWeight.valueOf(weight.toUpperCase()), Double.valueOf(value), realjob);
			
			plugin.getPlayerAPI().addMultiplier(uuid, n);
			
			sender.sendMessage(AdminCommand.prefix + "Added boost §a"+n.getName()+" §7to the player : "+player);

			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
			}
			
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
		return "command boost admin_gen players_online none boost_types boost_until boost_weight none jobs_listed";
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
