package optic_fusion1.jobs.job;

import optic_fusion1.jobs.UltimateJobs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.boss.BarColor;

public class Job {

    private String idt;
    private ArrayList<JobType> action;
    private String icon;
    private int slot;
    private double price;
    private String perm;
    private List<String> worlds;
    private String modeldata;
    private BarColor col;
    private HashMap<Integer, JobLevel> levels;
    private String bypassperm;
    private double maxear;
    private HashMap<JobType, HashMap<String, JobID>> ids;
    private List<String> custom;
    private HashMap<String, Boolean> options;
    private ArrayList<String> quit;
    private ArrayList<String> join;
    private String song;
    private String glass_color;
    private UltimateJobs plugin;

    public Job(UltimateJobs plugin, String id, ArrayList<JobType> action, String icon, int slot, double price, String permission,
            List<String> worlds, String model, BarColor bar, HashMap<Integer, JobLevel> levels2, String bypassperm,
            double maxear, HashMap<JobType, HashMap<String, JobID>> ids, List<String> customs,
            HashMap<String, Boolean> options, ArrayList<String> quit, ArrayList<String> join, String song, String glass_color) {
        this.plugin = plugin;
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
        this.bypassperm = bypassperm;
        this.maxear = maxear;
        this.ids = ids;
        this.custom = customs;
        this.options = options;
        this.quit = quit;
        this.join = join;
        this.song = song;
        this.glass_color = glass_color;
    }

    public String getGlassColor() {
        return this.glass_color;
    }

    public boolean hasGlassColor() {
        return this.glass_color != null;
    }

    public String getSong() {
        return this.song;
    }

    public boolean hasSong() {
        return getSong() != null;
    }

    public ArrayList<String> getQuitCommands() {
        return this.quit;
    }

    public ArrayList<String> getJoinCommands() {
        return this.join;
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

    public HashMap<JobType, HashMap<String, JobID>> getIDs() {
        return this.ids;
    }

    public boolean existID(JobType action, String id) {
        if (getIDs().containsKey(action)) {
            return getIDs().get(action).containsKey(id);
        }
        return false;
    }

    public HashMap<String, JobID> getIDsOf(JobType action) {
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
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        String where = jb.getLanguage().getJobMessage("Jobs." + getConfigID() + ".Levels." + i + ".Display");
        return where;
    }

    public boolean hasCustomBroadCastMessageForAll(String UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        return jb.getLanguage().getMessageContentOfJobs()
                .containsKey("Jobs." + getConfigID() + ".BroadCastMessageOnLevelUp");
    }

    public String getCustomBroadCastMessageForAll(String UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        String where = jb.getLanguage().getJobMessage("Jobs." + getConfigID() + ".BroadCastMessageOnLevelUp");
        return where;
    }

    public boolean hasCustomBroadCastMessageForLevel(int i, String UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        return jb.getLanguage().getMessageContentOfJobs()
                .containsKey("Jobs." + getConfigID() + ".Levels." + i + ".BroadCastMessageOnLevelUp");

    }

    public String getCustomBroadCastMessageForLevel(int i, String UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        String where = jb.getLanguage().getJobMessage("Jobs." + getConfigID() + ".Levels." + i + ".BroadCastMessageOnLevelUp");
        return where;
    }

    public String getLevelRankDisplay(int i, String UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        String where = null;
        if (jb.getLanguage().getMessageContentOfJobs()
                .containsKey("Jobs." + getConfigID() + ".Levels." + i + ".Rank")) {
            where = jb.getLanguage().getJobMessage("Jobs." + getConfigID() + ".Levels." + i + ".Rank");
        } else {
            where = "Unknown";
        }
        return where;

    }

    public List<String> getLevelLore(int i, String UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        List<String> result = jb.getLanguage().getJobList("Jobs." + this.getConfigID() + ".Levels." + i + ".Lore");
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
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        return jb.getLanguage().getJobList("Jobs." + this.getConfigID() + ".NoPermLore");
    }

    public String getPermMessage(String UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        return jb.getLanguage().getJobMessage("Jobs." + this.getConfigID() + ".NoPermMessage");
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
        return null;
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

    public List<String> getCommandsOfBlock(String id, JobType ac) {
        if (existID(ac, id)) {
            return getIDsOf(ac).get(id).getCommands();
        }
        return null;
    }

    public boolean hasCommandsOfBlock(String id, JobType ac) {
        return getCommandsOfBlock(id, ac) != null;
    }

    public String getDisplayOf(String id, String UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        String result = jb.getLanguage().getJobMessage("Jobs." + this.getConfigID() + ".IDS." + id + ".Display");
        return result;
    }

    public int getChanceOf(String id, JobType ac) {
        if (existID(ac, id)) {
            return getIDsOf(ac).get(id).getChance();
        }
        return 99;
    }

    public String getConfigIDOfRealID(JobType ac, String real, Job job) {
        AtomicReference<String> at = new AtomicReference<String>();
        getIDsOf(ac).forEach((id, item) -> {
            if (job.getRealIDOf(ac, id).equalsIgnoreCase(real)) {
                at.set(item.getInternalID());
            }
        });
        return at.get();
    }

    public double getRewardOf(String id, JobType ac) {
        if (existID(ac, id)) {
            return getIDsOf(ac).get(id).getReward();
        }
        return 0;
    }

    public boolean hasVaultReward(String id, JobType ac) {
        return getRewardOf(id, ac) != 0;
    }

    public List<String> getCustomitems() {
        return this.custom;
    }

    public double getExpOf(String id, JobType ac) {
        if (existID(ac, id)) {
            return getIDsOf(ac).get(id).getExp();
        }

        return 0.1;
    }

    public double getPointsOf(String id, JobType ac) {
        if (existID(ac, id)) {
            return getIDsOf(ac).get(id).getPoints();
        }
        return 0.1;
    }

    public String getRealIDOf(JobType ac, String id) {
        if (existID(ac, id)) {
            return getIDsOf(ac).get(id).getRealID();
        }
        return null;
    }

    public ArrayList<String> getListOfRealIDS(JobType ac) {
        ArrayList<String> list = new ArrayList<String>();
        getIDsOf(ac).forEach((id, item) -> {
            list.add(item.getRealID());
        });
        return list;
    }

    public JobType getActionofID(String id) {
        AtomicReference<JobType> at = new AtomicReference<JobType>();
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

    public String getNotRealIDByRealOne(String id, JobType ac) {
        AtomicReference<String> at = new AtomicReference<String>();
        HashMap<String, JobID> ids = getIDsOf(ac);
        ids.forEach((notreal, type) -> {
            if (id.toUpperCase().equalsIgnoreCase(type.getRealID().toUpperCase())) {
                at.set(type.getInternalID());
            }
        });
        return at.get();

    }

    public List<String> getLoreOfJob(String UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        List<String> result = jb.getLanguage().getJobList("Jobs." + this.getConfigID() + ".Lore");
        return result;

    }

    public List<String> getWorlds() {
        return worlds;
    }

    public List<String> getStatsLoreIfJob(String UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        return jb.getLanguage().getJobList("Jobs." + this.getConfigID() + ".Stats");
    }

    public double getPrice() {
        return price;
    }

    public int getSlot() {
        return slot;
    }

    public String getRawIcon() {

        if (icon == null) {
            return "BARRIER";
        }

        return icon;
    }

    public ArrayList<JobType> getActionList() {
        return action;
    }

    public String getDisplayOfJob(String UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        String display = jb.getLanguage().getJobMessage("Jobs." + this.idt + ".Display");
        if (display == null) {
            return "Error";
        }
        return plugin.getPluginManager().toHex(display).replaceAll("&", "§");
    }

    public String getConfigID() {
        return idt.toUpperCase();
    }

    public String getDisplayID(String UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        return jb.getLanguage().getJobMessage("Jobs." + this.getConfigID() + ".DisplayID");
    }

}
