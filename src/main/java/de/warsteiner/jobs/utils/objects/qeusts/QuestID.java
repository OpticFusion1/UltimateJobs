package de.warsteiner.jobs.utils.objects.qeusts;

import java.util.ArrayList;
import java.util.HashMap;

import de.warsteiner.jobs.utils.objects.jobs.JobAction;

public class QuestID {

	private String id;
	private String real;
	private JobAction ac;
	private int amount;
	
	public QuestID(String id, String real, JobAction ac, int amount) {
		this.id = id;
		this.real = real;
		this.ac = ac;
		this.amount = amount;
	}
	
	public int getNeededAmount() {
		return this.amount;
	}
	
	public JobAction getAction() {
		return this.ac;
	}
	
	public String getRealID() {
		return this.real;
	}
	
	public String getID() {
		return this.id;
	}
	
}
