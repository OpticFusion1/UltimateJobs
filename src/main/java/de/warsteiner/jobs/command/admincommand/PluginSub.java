package de.warsteiner.jobs.command.admincommand;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.JsonMessage;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class PluginSub extends AdminSubCommand {

	@Override
	public String getName() {
		return "plugin";
	}

	@Override
	public String getDescription() {
		return "Plugin Commands";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
	 
		 
			if (args.length == 1) {

				sender.sendMessage("§7");
				sender.sendMessage(" §8| §9UltimateJobs §8- §ePlugin Commands §8|");

				if (sender instanceof Player) {

					Player player = (Player) sender;

					new JsonMessage().append("§8-> §6/JobsAdmin plugin §8| §7View all arguments")
							.setClickAsSuggestCmd("/jobsadmin plugin").save().send(player);

					new JsonMessage()
							.append("§8-> §6/JobsAdmin plugin version §8| §7See the Plugin Version")
							.setClickAsSuggestCmd("/jobsadmin plugin version").save().send(player);

				} else {
					sender.sendMessage("§8-> §6/JobsAdmin plugin §8| §7View all arguments");
					sender.sendMessage("§8-> §6/JobsAdmin plugin version §8| §7See the Plugin Version");
				}

				sender.sendMessage("§7");

				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}

			} else if (args.length == 2 && args[1].toLowerCase().equalsIgnoreCase("version")) {

				PluginDescriptionFile d = UltimateJobs.getPlugin().getDescription();
				
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}
			 
				sender.sendMessage("§7");
				sender.sendMessage("§8-> §9UltimateJobs §7Plugin Details§8:");
				sender.sendMessage(" §8| §7Plugin Version§8: §b"+d.getVersion()+" §8(§7API §c"+d.getAPIVersion()+"§8)"); 
				sender.sendMessage(" §8| §7Server Version§8: §a"+Bukkit.getVersion()); 
				sender.sendMessage(" §8| §7Java Version§8: §6"+(System.getProperty("java.version") != null ? System.getProperty("java.version") : "null"));
				sender.sendMessage("§7");
			
			} else {
				sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6" + getUsage());
				if (sender instanceof Player) {
					Player player = (Player) sender;
					player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
		 
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command plugin";
	}

	@Override
	public String getUsage() {
		return "/JobsAdmin plugin";
	}

	@Override
	public String getPermission() {
		return "ultimatejobs.admin.plugin";
	}

	@Override
	public boolean showOnHelp() {
		return true;
	}

}