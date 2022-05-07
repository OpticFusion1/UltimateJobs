package de.warsteiner.jobs.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
 
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.JobAction;
import de.warsteiner.jobs.utils.cevents.PlayerFinishedWorkEvent;
import de.warsteiner.jobs.utils.cevents.PlayerLevelJobEvent;

public class PlayerRewardCommandEvent implements Listener  {
 
	@EventHandler
	public void onReward(PlayerFinishedWorkEvent event) {
		Player player = event.getPlayer();
		String id = event.getID();
		Job job = event.getJob();
		JobAction ac = event.getActionUsed();
	 
		if(job.isCommandonBlock(id, ac)) {
			ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		 
			List<String> commands = job.getCommandsOfBlock(id, ac);
			  
			for(String command : commands) {
				Bukkit.dispatchCommand(console, command.replaceAll("<name>", player.getName()));
			}
		}
	}
	
	@EventHandler
	public void onReward(PlayerLevelJobEvent event) {
		Player player = event.getPlayer(); 
		Job job = event.getJob();
		Integer level = event.getJobsPlayer().getStatsOf(job.getConfigID()).getLevel();
		if(job.isCommand(level)) {
			ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		 
			List<String> commands = job.getCommands(level);
			  
			for(String command : commands) {
				Bukkit.dispatchCommand(console, command.replaceAll("<name>", player.getName()));
			}
		}
	}
	
}
