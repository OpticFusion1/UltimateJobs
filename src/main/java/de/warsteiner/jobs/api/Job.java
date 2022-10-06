package de.warsteiner.jobs.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.JobAction;
import de.warsteiner.jobs.utils.objects.JobID;
import de.warsteiner.jobs.utils.objects.JobLevel;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class Job {

	private String idt;
	private ArrayList<JobAction> action;
	private String icon;
	private int slot;
	private double price;
	private String perm;
	private List<String> worlds;
	private String modeldata;
	private BarColor col;
	private HashMap<Integer, JobLevel> levels;
	private String permlore;
	private String permmessage;
	private String bypassperm;
	private double maxear;
	private HashMap<JobAction, HashMap<String, JobID>> ids;
	private List<String> custom;
	private HashMap<String, Boolean> options;
	private HashMap<String, String> option_messages;

	private ArrayList<String> quit;
	private ArrayList<String> join;

	public Job(String id, ArrayList<JobAction> action, String icon, int slot, double price, String permission,
			String permlore, String permmessage, List<String> worlds, String model, BarColor bar,
			HashMap<Integer, JobLevel> levels2, String bypassperm, double maxear,
			HashMap<JobAction, HashMap<String, JobID>> ids, List<String> customs, HashMap<String, Boolean> options,
			HashMap<String, String> option_messages, ArrayList<String> quit, ArrayList<String> join) {

		this.action = action;
		this.idt = id;
		this.icon = icon;
		this.slot = slot;
		this.price = price;
		this.perm = permission;
		this.worlds = worlds;
		this.modeldata = model;
		this.col = bar;
		this.levels = levels2;
		this.permlore = permlore;
		this.permmessage = permmessage;
		this.bypassperm = bypassperm;
		this.maxear = maxear;
		this.ids = ids;
		this.custom = customs;
		this.options = options;
		this.option_messages = option_messages;

		this.quit = quit;
		this.join = join;
	}

	public ArrayList<String> getQuitCommands() {
		return this.quit;
	}

	public ArrayList<String> getJoinCommands() {
		return this.join;
	}

	public String getOptionMessageOf(String id) {
		if (this.option_messages.containsKey(id)) {
			return this.option_messages.get(id);
		}
		return null;
	}

	public HashMap<String, Boolean> getOptions() {
		return this.options;
	}

	public boolean getOptionValue(String ID) {
		if (getOptions().containsKey(ID)) {
			return getOptions().get(ID);
		}
		return false;
	}

	public HashMap<JobAction, HashMap<String, JobID>> getIDs() {
		return this.ids;
	}

	public boolean existID(JobAction action, String id) {
		if (getIDs().containsKey(action)) {
			return getIDs().get(action).containsKey(id);
		}
		return false;
	}

	public HashMap<String, JobID> getIDsOf(JobAction action) {
		if (getIDs().containsKey(action)) {
			return getIDs().get(action);
		}
		return null;
	}

	public HashMap<Integer, JobLevel> getLevels() {
		return levels;
	}

	public boolean existLevel(int i) {
		return getLevels().containsKey(i);
	}

	public int getModelData() {
		return Integer.valueOf(this.modeldata);
	}

	public boolean hasModelData() {
		return this.modeldata != null;
	}

	public BarColor getBarColor() {
		return this.col;
	}

	public String getLevelDisplay(int i, String UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		String where = jb.getLanguage().getStringFromLanguage(jb.getUUID(),
				"Jobs." + getConfigID() + ".Levels." + i + ".Display");

		if (where == null) {
			Bukkit.getConsoleSender()
					.sendMessage("§cMissing Option of " + this.getConfigID() + " Job -> Level Display from Level " + i);
		}

		return where;

	}

	public List<String> getLevelLore(int i, String UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);

		List<String> result = jb.getLanguage().getListFromLanguage(jb.getUUID(),
				"Jobs." + this.getConfigID() + ".Levels." + i + ".Lore");

		return result;

	}

	public List<String> getLevelCommands(int i) {
		if (existLevel(i)) {
			return getLevels().get(i).getCommands();
		}
		return null;
	}

	public double getMultiOfLevel(int i) {
		if (existLevel(i)) {
			return getLevels().get(i).getMultiplier();
		}
		return 0.0;
	}

	public List<String> getPermissionsLore(String UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		if (this.permlore == null) {
			Bukkit.getConsoleSender()
					.sendMessage("§cMissing Option of " + this.getConfigID() + " Job -> Permissions Lore");
		}

		return jb.getLanguage().getListFromPath(jb.getUUID(), this.permlore);
	}

	public String getPermMessage(String UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		if (this.permmessage == null) {
			Bukkit.getConsoleSender()
					.sendMessage("§cMissing Option of " + this.getConfigID() + " Job -> Permissions Message");
		}
		return jb.getLanguage().getStringFromPath(jb.getUUID(), this.permmessage);
	}

	public boolean hasPermission() {
		return this.perm != null;
	}

	public boolean hasBypassPermission() {
		return this.bypassperm != null;
	}

	public String getByPassPermission() {
		return this.bypassperm;
	}

	public String getPermission() {
		return this.perm;
	}

	public Job get() {
		return this;
	}

	public boolean hasMaxEarningsPerDay() {
		return this.maxear != 0;
	}

	public double getMaxEarningsPerDay() {
		return this.maxear;
	}

	public boolean hasLevelCommands(int level) {
		return getLevelCommands(level) != null;
	}

	public double getVaultOnLevel(int i) {
		if (existLevel(i)) {
			return getLevels().get(i).getReward();
		}
		return 0;
	}

	public String getIconOfLevel(int i) {
		if (existLevel(i)) {
			return getLevels().get(i).getIcon();
		}
		return "BARRIER";
	}

	public int getModelDataOfLevel(int i) {
		if (existLevel(i)) {
			return getLevels().get(i).getModelData();
		}
		return 0;
	}

	public boolean isVaultOnLevel(int i) {
		if (existLevel(i)) {
			return getLevels().get(i).getReward() != 0;
		}
		return false;
	}

	public double getExpOfLevel(int i) {
		if (existLevel(i)) {
			return getLevels().get(i).getExp();
		}
		return 0.0;
	}

	public List<String> getCommandsOfBlock(String id, JobAction ac) {
		if (existID(ac, id)) {
			return getIDsOf(ac).get(id).getCommands();
		}
		return null;
	}

	public boolean hasCommandsOfBlock(String id, JobAction ac) {
		return getCommandsOfBlock(id, ac) != null;
	}

	public String getDisplayOf(String id, String UUID, JobAction ac) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);

		String result = jb.getLanguage().getStringFromLanguage(jb.getUUID(),
				"Jobs." + this.getConfigID() + ".IDS." + id + ".Display");

		if (result == null) {
			Bukkit.getConsoleSender()
					.sendMessage("§cMissing Option of " + this.getConfigID() + " Job -> Display of " + id);
		}

		return result;
	}

	public int getChanceOf(String id, JobAction ac) {
		if (existID(ac, id)) {
			return getIDsOf(ac).get(id).getChance();
		}
		return 99;
	}

	public String getConfigIDOfRealID(JobAction ac, String real, Job job) {

		AtomicReference<String> at = new AtomicReference<String>();

		getIDsOf(ac).forEach((id, item) -> {
			if (job.getRealIDOf(ac, id).equalsIgnoreCase(real)) {
				at.set(item.getInternalID());
			}
		});

		return at.get();
	}

	public double getRewardOf(String id, JobAction ac) {
		if (existID(ac, id)) {
			return getIDsOf(ac).get(id).getReward();
		}
		return 0;
	}

	public boolean hasVaultReward(String id, JobAction ac) {
		return getRewardOf(id, ac) != 0;
	}

	public List<String> getCustomitems() {
		return this.custom;
	}

	public double getExpOf(String id, JobAction ac) {

		if (existID(ac, id)) {
			return getIDsOf(ac).get(id).getExp();
		}

		return 0.1;
	}

	public double getPointsOf(String id, JobAction ac) {

		if (existID(ac, id)) {
			return getIDsOf(ac).get(id).getPoints();
		}

		return 0.1;
	}

	public String getRealIDOf(JobAction ac, String id) {
		if (existID(ac, id)) {
			return getIDsOf(ac).get(id).getRealID();
		}

		return null;
	}

	public ArrayList<String> getListOfRealIDS(JobAction ac) {
		ArrayList<String> list = new ArrayList<String>();

		getIDsOf(ac).forEach((id, item) -> {
			list.add(item.getRealID());
		});

		return list;
	}

	public JobAction getActionofID(String id) {

		AtomicReference<JobAction> at = new AtomicReference<JobAction>();

		getIDs().forEach((action, listed) -> {

			getIDsOf(action).forEach((ids, type) -> {

				if (ids.equalsIgnoreCase(id)) {
					at.set(action);
				}
			});

		});

		return at.get();

	}

	public String getIconOfID(String id) {

		AtomicReference<String> at = new AtomicReference<String>();

		getIDs().forEach((action, listed) -> {

			getIDsOf(action).forEach((ids, type) -> {

				if (ids.equalsIgnoreCase(id)) {
					at.set(type.getIcon());
				}
			});

		});

		return at.get();

	}

	public int getModelData(String id) {

		AtomicInteger at = new AtomicInteger();

		getIDs().forEach((action, listed) -> {

			getIDsOf(action).forEach((ids, type) -> {

				if (ids.equalsIgnoreCase(id)) {
					if (type.hasModelData()) {
						at.set(type.getModelData());
					} else {
						at.set(0);
					}
				}
			});

		});

		return at.get();

	}

	public ArrayList<String> getAllNotRealIDSFromActionsAsArray() {
		ArrayList<String> list = new ArrayList<String>();

		getIDs().forEach((action, listed) -> {

			listed.forEach((ids, type) -> {

				list.add(type.getInternalID());

			});

		});

		return list;
	}

	public String getNotRealIDByRealOne(String id, JobAction ac) {

		AtomicReference<String> at = new AtomicReference<String>();

		HashMap<String, JobID> ids = getIDsOf(ac);

		ids.forEach((notreal, type) -> {

			if (id.toUpperCase().equalsIgnoreCase(type.getRealID().toUpperCase())) {
		 
					at.set(type.getInternalID());
				 
			}

		});

		return at.get();

	}

	public List<String> getLore(String UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		List<String> result = jb.getLanguage().getListFromLanguage(jb.getUUID(),
				"Jobs." + this.getConfigID() + ".Lore");

		if (result == null) {
			Bukkit.getConsoleSender().sendMessage("§cMissing Option of " + this.getConfigID() + " Job -> Lore of Job");
		}

		return result;

	}

	public List<String> getWorlds() {
		return worlds;
	}

	public List<String> getStatsMessage(String UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		return jb.getLanguage().getListFromLanguage(jb.getUUID(), "Jobs." + this.getConfigID() + ".Stats");
	}

	public double getPrice() {
		return price;
	}

	public int getSlot() {
		return slot;
	}

	public String getIcon() {

		if (icon == null) {
			return "BARRIER";
		}

		return icon;
	}

	public ArrayList<JobAction> getActionList() {

		return action;
	}

	public String getDisplay(String UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		String display = jb.getLanguage().getStringFromLanguage(jb.getUUID(),
				"Jobs." + this.getConfigID() + ".Display");

		if (display == null) {
			return "Error";
		}

		return UltimateJobs.getPlugin().getPluginManager().toHex(display).replaceAll("&", "§");
	}

	public String getConfigID() {
		return idt;
	}

	public String getDisplayID(String UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "Jobs." + this.getConfigID() + ".DisplayID");
	}

}
