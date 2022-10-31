package de.warsteiner.jobs.command.playercommand;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.objects.jobs.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class LimitSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName(UUID UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + UUID);
		return jb.getLanguage().getMessage("Commands.Limit.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + UUID);
		return jb.getLanguage().getMessage("Commands.Limit.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		UUID UUID = player.getUniqueId();
		if (args.length == 2) {
			String pl = args[1].toUpperCase();

			if (plugin.getPlayerAPI().getUUIDByName(pl.toUpperCase()) == null) {
				plugin.getAPI().playSound("COMMAND_PLAYER_NOT_FOUND", player);
				player.sendMessage(jb.getLanguage().getMessage("command_limit_not_found")
						.replaceAll("<name>", args[1]));
				return;
			} else {
				String uuid = plugin.getPlayerAPI().getUUIDByName(pl.toUpperCase());

				plugin.getAPI().playSound("COMMAND_LIMIT_OTHER_SUCCES", player);

				int max = plugin.getPlayerAPI().getMaxJobs(uuid) + 1;
				player.sendMessage(jb.getLanguage().getMessage("command_limit_other")
						.replaceAll("<name>", args[1]).replaceAll("<max>", "" + max));
				return;

			}
		} else if (args.length == 1) {
			int max = jb.getMaxJobs() + 1;
			plugin.getAPI().playSound("COMMAND_LIMIT_SELF_SUCCES", player);
			player.sendMessage(
					jb.getLanguage().getMessage("command_limit_self").replaceAll("<max>", "" + max));
			return;
		} else {
			plugin.getAPI().playSound("COMMAND_USAGE", player);
			player.sendMessage(jb.getLanguage().getMessage("command_usage").replaceAll("<usage>",
					getUsage(UUID)));
		}
	}

	@Override
	public String FormatTab() {
		return "command limit players_online";
	}

	@Override
	public int getTabLength() {
		return 2;
	}

	@Override
	public boolean isEnabled() {
		return plugin.getLocalFileManager().getCMDSettings().getBoolean("Commands.Limit.Enabled");
	}

	@Override
	public String getUsage(UUID UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + UUID);
		return jb.getLanguage().getMessage("Commands.Limit.UsageMessage");
	}

}
