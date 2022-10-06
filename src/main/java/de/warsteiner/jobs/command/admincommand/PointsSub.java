package de.warsteiner.jobs.command.admincommand;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.JsonMessage;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class PointsSub extends AdminSubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName() {
		return "points";
	}

	@Override
	public String getDescription() {
		return "Update Player's Points";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if (args.length == 1) {

			sender.sendMessage("§7");
			sender.sendMessage(" §8| §9UltimateJobs §8- §aPlayer Points §8|");

			if (sender instanceof Player) {

				Player player = (Player) sender;

				new JsonMessage().append("§8-> §6/JobsAdmin points §8| §7View all arguments")
						.setClickAsSuggestCmd("/jobsadmin points").save().send(player);

				new JsonMessage()
						.append("§8-> §6/JobsAdmin points set <player_name> <amount> §8| §7Set a Points Amount")
						.setClickAsSuggestCmd("/jobsadmin points set").save().send(player);

				new JsonMessage()
						.append("§8-> §6/JobsAdmin points add <player_name> <amount> §8| §7Add a Points Amount")
						.setClickAsSuggestCmd("/jobsadmin points add").save().send(player);

				new JsonMessage()
						.append("§8-> §6/JobsAdmin points remove <player_name> <amount> §8| §7Remove a Exp Amount")
						.setClickAsSuggestCmd("/jobsadmin points remove").save().send(player);

			} else {
				sender.sendMessage("§8-> §6/JobsAdmin points §8| §7View all arguments");
				sender.sendMessage("§8-> §6/JobsAdmin points set <player_name> <amount> §8| §7Set a Points Amount");
				sender.sendMessage("§8-> §6/JobsAdmin points add <player_name> <amount> §8| §7Add a Points Amount");
				sender.sendMessage("§8-> §6/JobsAdmin points remove <player_name> <amount> §8| §7Remove a Exp Amount");
			}

			sender.sendMessage("§7");

			if (sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
			}

		} else if (args.length == 4 && args[1].toLowerCase().equalsIgnoreCase("remove")) {

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

			if (plugin.getAPI().isInt(value)) {

				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}

				double old = plugin.getPlayerAPI().getPoints(uuid);

				plugin.getPlayerAPI().updatePoints(uuid, old - Integer.valueOf(value));

				sender.sendMessage(AdminCommand.prefix + "Removed §c" + player + " §7Points -> §a" + value
						+ "§7.");

				return;

			} else {
				sender.sendMessage(AdminCommand.prefix + "Error! The value must be a Integer");
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}

		} else if (args.length == 4 && args[1].toLowerCase().equalsIgnoreCase("add")) {

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

			if (plugin.getAPI().isInt(value)) {

				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}

				double old = plugin.getPlayerAPI().getPoints(uuid);

				plugin.getPlayerAPI().updatePoints(uuid, old + Integer.valueOf(value));

				sender.sendMessage(
						AdminCommand.prefix + "Added §c" + player + " §7Points -> §a" + value + "§7.");
				return;

			} else {
				sender.sendMessage(AdminCommand.prefix + "Error! The value must be a Integer");
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}

		} else if (args.length == 4 && args[1].toLowerCase().equalsIgnoreCase("set")) {

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
 
			if (plugin.getAPI().isInt(value)) {

				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}

				plugin.getPlayerAPI().updatePoints(uuid, Integer.valueOf(value));

				sender.sendMessage(AdminCommand.prefix + "Changed §c" + player + "'s §7Points to §a" + value
						+ "§7.");
				return;

			} else {
				sender.sendMessage(AdminCommand.prefix + "Error! The value must be a Integer");
				if (sender instanceof Player) {
					Player player2 = (Player) sender;
					player2.playSound(player2.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}

		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6" + getUsage());
			if (sender instanceof Player) {
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
		return "command points admin_gen players_online";
	}

	@Override
	public String getUsage() {
		return "/JobsAdmin points";
	}

	@Override
	public String getPermission() {
		return "ultimatejobs.admin.points";
	}

	@Override
	public boolean showOnHelp() {
		return true;
	}

}
