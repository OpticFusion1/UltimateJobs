package de.warsteiner.jobs.utils.objects.qeusts;

import java.util.ArrayList;
import java.util.HashMap;

public class Quest {

	private String id;
	private double reward;
	private ArrayList<String> commands;
	private ArrayList<String> jobs;
	private ArrayList<String> req;
	private HashMap<String, QuestID> ids;
	
	public Quest(String id, double reward, ArrayList<String> commands,
			ArrayList<String> jobs, ArrayList<String> req, HashMap<String, QuestID> ids) {
		this.id = id;
		this.reward = reward;
		this.commands = commands;
		this.jobs = jobs;
		this.req = req;
		this.ids = ids;
	}
	
	public String getID() {
		return this.id;
	}
	
	public double getReward() {
		return this.reward;
	}
	
	public ArrayList<String> getCommands() {
		return this.commands;
	}
	
	public ArrayList<String> getJobs() {
		return this.jobs;
	}
	
	public ArrayList<String> getReqJobs() {
		return this.req;
	}
	
	public HashMap<String, QuestID> getIDs() {
		return this.ids;
	}
	
}
