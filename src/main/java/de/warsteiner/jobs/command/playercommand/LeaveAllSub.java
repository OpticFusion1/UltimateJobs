package de.warsteiner.jobs.command.playercommand;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.cevents.PlayerQuitJobEvent;
import de.warsteiner.jobs.utils.objects.jobs.Job;
import de.warsteiner.jobs.utils.objects.jobs.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class LeaveAllSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName(UUID UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(""+UUID);
		return  jb.getLanguage().getMessage("Commands.LeaveALL.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(""+UUID);
		return  jb.getLanguage().getMessage("Commands.LeaveALL.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		UUID UUID = player.getUniqueId();
		if (args.length == 1) {
			
			ArrayList<String> jobs = jb.getCurrentJobs();
			
			if (jobs.size() != 0) {

				if (jobs != null) {

					for (Job mine : jb.getCurrentJobsAsObject()) {

						if (!mine.getOptionValue("CannotLeaveJob")) {

							PlayerQuitJobEvent event = new PlayerQuitJobEvent(player, jb, mine);

							plugin.getClickManager().OptionalJobQuit(event);

							jb.remCurrentJob(mine.getConfigID());

						}
					}

				}

				plugin.getClickManager().updateSalaryOnLeave(player, jb);

				plugin.getAPI().playSound("LEAVE_ALL", player);

				player.sendMessage(jb.getLanguage().getMessage("command_leaveall_message"));
				plugin.getAPI().playSound("COMMAND_LEAVEALL_SUCCESS", player);
 
			} else {
				plugin.getAPI().playSound("COMMAND_LEAVEALL_NO_JOBS", player);
				player.sendMessage(jb.getLanguage().getMessage("command_leaveall_already"));
			}
			 
		} else {
			plugin.getAPI().playSound("COMMAND_USAGE", player);
			player.sendMessage(
					jb.getLanguage().getMessage("command_usage").replaceAll("<usage>", getUsage(UUID)));
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command leaveall";
	}

	@Override
	public boolean isEnabled() {
		return plugin.getLocalFileManager().getCMDSettings().getBoolean("Commands.LeaveALL.Enabled");
	}

	@Override
	public String getUsage(UUID UUID) {
		 JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(""+UUID);
		return  jb.getLanguage().getMessage("Commands.LeaveALL.UsageMessage");
	}

}
