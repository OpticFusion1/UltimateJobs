package de.warsteiner.jobs.command.admincommand;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;
import de.warsteiner.jobs.utils.objects.commands.AdminCommandOptions;
import de.warsteiner.jobs.utils.objects.guis.GUIType;
import de.warsteiner.jobs.utils.objects.jobs.Job;
import de.warsteiner.jobs.utils.objects.multipliers.MultiplierType;

public class OpenSub extends AdminSubCommand {

	@Override
	public String getName() {
		return "open";
	}

	@Override
	public String getDescription() {
		return "Open a Player a GUI per Command";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		ArrayList<AdminCommandOptions> listed = new ArrayList<AdminCommandOptions>();
		
		if (args.length == 3) {

			String name = args[1];
			String type = args[2];

			if (!Bukkit.getPlayer(name).isOnline()) {
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				sender.sendMessage(AdminCommand.prefix + "§cPlayer is not online!");
			}
			 
			GUIType f;
			
			try {
				f = GUIType.valueOf(type.toUpperCase());
			} catch (IllegalArgumentException ex) {
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				sender.sendMessage(AdminCommand.prefix + "§cError: GUI Type does not exist");
				return;
			}
 
			UltimateJobs.getPlugin().getGUIOpenManager().openGuiByGuiID(sender, f, Bukkit.getPlayer(name), null, null, true, listed);
  
		} else if (args.length == 4) {

			String name = args[1];
			String type = args[2];
			String job = args[3];

			if (!Bukkit.getPlayer(name).isOnline()) {
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				sender.sendMessage(AdminCommand.prefix + "§cPlayer is not online!");
			}
			
			GUIType f;
			
			try {
				f = GUIType.valueOf(type.toUpperCase());
			} catch (IllegalArgumentException ex) {
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				sender.sendMessage(AdminCommand.prefix + "§cError: GUI Type does not exist");
				return;
			}

			Job fd = null;

			for (String jobs : UltimateJobs.getPlugin().getLoaded()) {

				Job j = UltimateJobs.getPlugin().getJobCache().get(jobs);

				if (j.getConfigID().equalsIgnoreCase(job)) {
					fd = j;
				}
			}

			if (fd != null) {
				UltimateJobs.getPlugin().getGUIOpenManager().openGuiByGuiID(sender, f, Bukkit.getPlayer(name), fd,
						null, true, listed);

				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}
			}

		} else if (args.length == 5) {

			String name = args[1];
			String type = args[2];
			String job = args[3];
			String about = args[4];

			if (!Bukkit.getPlayer(name).isOnline()) {
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				sender.sendMessage(AdminCommand.prefix + "§cPlayer is not online!");
			}
			
			GUIType f2;
			
			try {
				f2 = GUIType.valueOf(type.toUpperCase());
			} catch (IllegalArgumentException ex) {
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				sender.sendMessage(AdminCommand.prefix + "§cError: GUI Type does not exist");
				return;
			}

			Job f = null;

			for (String jobs : UltimateJobs.getPlugin().getLoaded()) {

				Job j = UltimateJobs.getPlugin().getJobCache().get(jobs);

				if (j.getConfigID().equalsIgnoreCase(job)) {
					f = j;
				}
			}

			if (f != null) {
				UltimateJobs.getPlugin().getGUIOpenManager().openGuiByGuiID(sender, f2, Bukkit.getPlayer(name), f,
						about, true, listed);

				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}
			}

		}else if (args.length == 6) {

			String name = args[1];
			String type = args[2];
			String job = args[3];
			String about = args[4];
			String options = args[5];
			
			String[] split = options.split(",");
			 
			for(String ss : split) {
				if (AdminCommandOptions.valueOf(ss.toUpperCase()) == null) {

					if (!Bukkit.getPlayer(name).isOnline()) {
						if (sender instanceof Player) {
							Player player3 = (Player) sender;
							player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
						}
						sender.sendMessage(AdminCommand.prefix + "§cOption "+ss+" is not valid!");
					} 
				}
				AdminCommandOptions tt = AdminCommandOptions.valueOf(ss.toUpperCase());
				listed.add(tt);
			}

			if (!Bukkit.getPlayer(name).isOnline()) {
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				sender.sendMessage(AdminCommand.prefix + "§cPlayer is not online!");
			}
			

			GUIType f2;
			
			try {
				f2 = GUIType.valueOf(type.toUpperCase());
			} catch (IllegalArgumentException ex) {
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				sender.sendMessage(AdminCommand.prefix + "§cError: GUI Type does not exist");
				return;
			}

			Job f = null;

			for (String jobs : UltimateJobs.getPlugin().getLoaded()) {

				Job j = UltimateJobs.getPlugin().getJobCache().get(jobs);

				if (j.getConfigID().equalsIgnoreCase(job)) {
					f = j;
				}
			}
			

			if (f != null) {
				UltimateJobs.getPlugin().getGUIOpenManager().openGuiByGuiID(sender, f2, Bukkit.getPlayer(name), f,
						about, true, listed);

				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}
			}

		} else {
			if (sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
			}
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6" + getUsage());
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command open players_online gui_types jobs_listed players_online options";
	}

	@Override
	public String getUsage() {
		return "/JobsAdmin open <player> <gui> <job> <about_player> <options>";
	}

	@Override
	public String getPermission() {
		return "ultimatejobs.admin.open";
	}

	@Override
	public boolean showOnHelp() {
		return true;
	}

}
