package de.warsteiner.jobs.command.playercommand;

import java.util.UUID;

import org.bukkit.command.CommandSender; 
import org.bukkit.entity.Player;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.objects.guis.UpdateTypes;
import de.warsteiner.jobs.utils.objects.jobs.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class HelpSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin(); 

	@Override
	public String getName(UUID UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(""+UUID);
		return  jb.getLanguage().getMessage("Commands.Help.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(""+UUID);
		return  jb.getLanguage().getMessage("Commands.Help.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		UUID UUID = player.getUniqueId();
		if (args.length == 1) {
			String mode = plugin.getLocalFileManager().getHelpSettings().getString("Help_Mode").toUpperCase();
			if(mode.equalsIgnoreCase("GUI")) {
				plugin.getGUI().createHelpGUI(player, UpdateTypes.OPEN);
			} else {
		 
				plugin.getAPI().playSound("SEND_HELP", player);
				
				for (String m :jb.getLanguage().getList("Commands.Help.List")) {
					player.sendMessage(plugin.getPluginManager().toHex(m).replaceAll("&", "§"));
				}
			}
		} else {
			plugin.getAPI().playSound("COMMAND_USAGE", player);
			player.sendMessage(jb.getLanguage().getMessage("command_usage").replaceAll("<usage>", getUsage(UUID)));
		}
	}

	@Override
	public String FormatTab() {
		return "command help";
	}

	@Override
	public int getTabLength() {
		return 1;
	}
	
	@Override
	public boolean isEnabled() { 
		return  plugin.getLocalFileManager().getCMDSettings().getBoolean("Commands.Help.Enabled");
	}

	@Override
	public String getUsage(UUID UUID) { 
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(""+UUID);
		return  jb.getLanguage().getMessage("Commands.Help.UsageMessage");
	}
  
}
