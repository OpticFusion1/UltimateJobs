package de.warsteiner.jobs.command.playercommand;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.objects.UpdateTypes;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class RankingSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName(UUID UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + UUID);
		return jb.getLanguage().getStringFromLanguage(UUID, "Commands.Ranking.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + UUID);
		return jb.getLanguage().getStringFromLanguage(UUID, "Commands.Ranking.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		UUID UUID = player.getUniqueId();
		if (args.length == 1) {

			if (plugin.getFileManager().getRankingGlobalConfig().getBoolean("EnabledGlobalRanking")) {
				plugin.getGUIAddonManager().createGlobalRankingGUI(player, UpdateTypes.OPEN);

			} else {
				plugin.getAPI().playSound("COMMAND_USAGE", player);
				player.sendMessage(jb.getLanguage().getStringFromLanguage(UUID, "command_usage").replaceAll("<usage>",
						getUsage(UUID)));
			}

		} else if (args.length == 2) {

			if (plugin.getFileManager().getRankingPerJobConfig().getBoolean("EnabledRankingPerJob")) {

				String notreal = args[1].toUpperCase();

				if (!plugin.getAPI().checkIfJobIsReal(notreal.toUpperCase(), player)) {
					plugin.getAPI().playSound("COMMAND_JOB_NOT_FOUND", player);
					return;
				}

				Job real = plugin.getAPI().checkIfJobIsRealWithResult(notreal.toUpperCase(), player);

				plugin.getGUIAddonManager().createJobRankingGUI(player, UpdateTypes.OPEN, real);

			} else {
				plugin.getAPI().playSound("COMMAND_USAGE", player);
				player.sendMessage(jb.getLanguage().getStringFromLanguage(UUID, "command_usage").replaceAll("<usage>",
						getUsage(UUID)));
			}

		} else {
			plugin.getAPI().playSound("COMMAND_USAGE", player);
			player.sendMessage(jb.getLanguage().getStringFromLanguage(UUID, "command_usage").replaceAll("<usage>",
					getUsage(UUID)));
		}
	}

	@Override
	public String FormatTab() {
		return "command ranking jobs_listed";
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public boolean isEnabled() {
		return plugin.getFileManager().getCMDSettings().getBoolean("Commands.Ranking.Enabled");
	}

	@Override
	public String getUsage(UUID UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + UUID);
		return jb.getLanguage().getStringFromLanguage(UUID, "Commands.Ranking.UsageMessage");
	}

}
