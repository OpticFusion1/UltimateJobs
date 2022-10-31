package de.warsteiner.jobs.command.admincommand;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.JsonMessage;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;
import de.warsteiner.jobs.utils.objects.Language;
import de.warsteiner.jobs.utils.objects.jobs.Job;
import de.warsteiner.jobs.utils.objects.jobs.JobsPlayer;

public class LanguageSub extends AdminSubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin(); 
	
	@Override
	public String getName() {
		return "language";
	}

	@Override
	public String getDescription() {
		return "Update Player's Language";
	}

	@Override
	public void perform(CommandSender sender, String[] args) { 
		if (args.length == 1) {
			 
			sender.sendMessage("§7");
			sender.sendMessage(" §8| §9UltimateJobs §8- §aPlayer Languages §8|");
			
			if(sender instanceof Player) {
				
				 Player player = (Player) sender;
				
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin language §8| §7View all arguments")
				 .setClickAsSuggestCmd("/jobsadmin language").save().send(player);
			 
				 new JsonMessage() 
				 .append("§8-> §6/JobsAdmin language set <player_name> <lang> §8| §7Set a Language")
				 .setClickAsSuggestCmd("/jobsadmin language set").save().send(player);
				  
			} else {
				sender.sendMessage("§8-> §6/JobsAdmin language §8| §7View all arguments");
				sender.sendMessage("§8-> §6/JobsAdmin language set <player_name> <lang> §8| §7Set a Language");  
			}
			
			sender.sendMessage("§7");
			
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
			}
			
		} else if(args.length == 4 && args[1].toLowerCase().equalsIgnoreCase("set")) {
			 
			String player = args[2]; 
			String value = args[3]; 

			if (plugin.getPlayerAPI().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}

			String uuid = plugin.getPlayerAPI().getUUIDByName(player.toUpperCase());
 
			if (plugin.getLanguageAPI().getLanguageFromName(value.toUpperCase()) != null) {

				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}

				Language lang = plugin.getLanguageAPI().getLanguageFromName(value.toUpperCase());

				if (UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(uuid) != null) {
					JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(uuid);

					jb.updateLocalLanguage(lang);
				}
				
				plugin.getPlayerAPI().updateSettingData(uuid, "LANG", lang.getName());

				sender.sendMessage(AdminCommand.prefix + "Changed §c" + player + "'s §7Language to §a" + value
						+ "§7.");
				return;

			} else {
				sender.sendMessage(AdminCommand.prefix + "Error! Language not found!");
				if (sender instanceof Player) {
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
		return "command language language_options players_online languages";
	}
	
	@Override
	public String getUsage() { 
		return "/JobsAdmin language";
	}

	@Override
	public String getPermission() { 
		return "ultimatejobs.admin.language";
	}
	
	@Override
	public boolean showOnHelp() { 
		return true;
	}
	
}

