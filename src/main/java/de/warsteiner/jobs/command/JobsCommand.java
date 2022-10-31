package de.warsteiner.jobs.command;
 

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.manager.GuiManager;
import de.warsteiner.jobs.utils.objects.PluginColor;
import de.warsteiner.jobs.utils.objects.guis.UpdateTypes;
import de.warsteiner.jobs.utils.objects.jobs.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class JobsCommand implements CommandExecutor {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		FileConfiguration config = plugin.getLocalFileManager().getConfig();

		int length = args.length;
		 
		if (sender instanceof Player) {
			Player player = (Player) sender;
			String UUID = ""+player.getUniqueId();
			
			if(!plugin.getPlayerAPI().existInCacheByUUID(UUID)) {
				Bukkit.getConsoleSender().sendMessage(PluginColor.ERROR.getPrefix() + "Seems like a player was not found... ("+UUID+")");
				return true;
			}
			
			JobsPlayer jb =plugin.getPlayerAPI().getRealJobPlayer(UUID);
			GuiManager gui = plugin.getGUI();

			if (length == 0) {
				gui.createMainGUIOfJobs(player, UpdateTypes.OPEN);
				
				//updating max permission
				
				if (config.getBoolean("EnableMaxJobPermissions")) {

					if (config.getBoolean("UpdateOnGuiOpen")) {

						for (PermissionAttachmentInfo perms : player.getEffectivePermissions()) {

							if (perms.getPermission().startsWith("ultimatejobs.max.")) {

								int real = Integer
										.valueOf(perms.getPermission().split("ultimatejobs.max.")[1]) - 1;

								jb.updateCacheMax(real);
							}

						}
					}
				}

			} else {
				String ar = args[0].toLowerCase();

				if (find(ar, jb.getUUID()) == null) {
					player.sendMessage(jb.getLanguage().getMessage("command_notfound").replaceAll("<cmd>", ar));
					return true;
				} else {

					SubCommand cmd = find(ar, jb.getUUID());

					if(cmd.isEnabled()) {
						cmd.perform(player, args, jb);
					} else {
						player.sendMessage(jb.getLanguage().getMessage("command_notfound").replaceAll("<cmd>", ar));
						return true;
					}

				}

			}
		} else {
			sender.sendMessage("§c§lThis Command is only for players!");
		}

		return false;
	}

	public SubCommand find(String given, UUID UUID) {
		for (SubCommand subCommand : plugin.getSubCommandManager().getSubCommandList()) {
			if (given.equalsIgnoreCase(subCommand.getName(UUID).toLowerCase())) {
				return subCommand;
			}
		}
		return null;
	}

}