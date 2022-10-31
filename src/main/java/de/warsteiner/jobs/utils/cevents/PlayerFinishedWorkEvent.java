package de.warsteiner.jobs.utils.cevents;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.warsteiner.jobs.utils.objects.jobs.Job;
import de.warsteiner.jobs.utils.objects.jobs.JobAction;
import de.warsteiner.jobs.utils.objects.jobs.JobsPlayer;

public class PlayerFinishedWorkEvent  extends Event  {

	private static HandlerList list = new HandlerList();

	public UUID id; 
	public JobsPlayer pl;
	public Job job;
	public String ID;
	public Player player; 
	public JobAction ac; 
	
	public PlayerFinishedWorkEvent(Player player, JobsPlayer plt, Job job, String id, JobAction ac) { 
		this.pl = plt; 
		this.ac = ac;
		this.ID = id;
		this.job = job;
		this.player = player;
		Bukkit.getPluginManager().callEvent(this);
	}
	
	public JobAction getActionUsed() {
		return ac;
	}
	
	public String getID() {
		return ID;
	}

	public HandlerList getHandlers() {
		return list;
	}
	
	public Job getJob() {
		return job;
	}

	public JobsPlayer getJobsPlayer() {
		return pl;
	}

	public Player getPlayer() {
		return player;
	}

	public static HandlerList getHandlerList() {
		return list;
	}
 
}
