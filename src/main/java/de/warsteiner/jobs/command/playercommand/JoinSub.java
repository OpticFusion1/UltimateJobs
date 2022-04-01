package de.warsteiner.jobs.command.playercommand;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class JoinSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName(UUID UUID) {
		return plugin.getPluginManager().getMessage(UUID, "Commands.Join.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		return plugin.getPluginManager().getMessage(UUID, "Commands.Join.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		UUID UUID = player.getUniqueId();
		String ID = "" + UUID;
		if (args.length == 2) {
			String d = args[1].toUpperCase();

			if (!plugin.getAPI().checkIfJobIsReal(d.toUpperCase(), player)) {
				return;
			}
			
			String job = plugin.getAPI().checkIfJobIsRealAndGet(d.toUpperCase(), player).getConfigID();

			Job file = plugin.getAPI().checkIfJobIsRealAndGet(job.toUpperCase(), player);
			if (jb.getOwnJobs().contains(file.getConfigID())) {
				int max = jb.getMaxJobs();

				if (jb.getCurrentJobs().size() <= max) {
					if (!jb.isInJob(file.getConfigID())) {
						jb.addCurrentJob(file.getConfigID());
						player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_join_Joined")
								.replaceAll("<job>", file.getDisplay(ID)));
					} else {
						player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_join_already")
								.replaceAll("<job>", file.getDisplay(ID)));
					}
				} else {
					player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_join_max")
							.replaceAll("<job>", file.getDisplay(ID)));
				}
			} else {
				player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_join_not_own")
						.replaceAll("<job>", file.getDisplay(ID)));

			}
		} else {
			player.sendMessage(
					plugin.getPluginManager().getMessage(UUID, "command_usage").replaceAll("<usage>", getUsage(UUID)));
		}
	}

	@Override
	public String FormatTab() {
		return "command join jobs_owned";
	}

	@Override
	public int getTabLength() {
		return 2;
	}

	@Override
	public boolean isEnabled() {
		return plugin.getFileManager().getCMDSettings().getBoolean("Commands.Join.Enabled");
	}

	@Override
	public String getUsage(UUID UUID) {
		return plugin.getPluginManager().getMessage(UUID, "Commands.Join.UsageMessage");
	}

}