package optic_fusion1.jobs.job;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.util.Language;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class JobsPlayer {

    private String name;
    private String display;
    private String UUID;
    private UUID rUUID;
    private ArrayList<String> current;
    private ArrayList<String> owned;
    private double points;
    private int max;
    private Language lang;
    private HashMap<String, JobStats> stats;
    private double sal;
    private String saldate;
    private ArrayList<JobsMultiplier> multi;
    private HashMap<String, String> settings;
    private UltimateJobs plugin;

    public JobsPlayer(UltimateJobs plugin, String name, String display, ArrayList<String> current2, ArrayList<String> owned2, double points,
            int max, String UUID, UUID rUUID, Language lang, HashMap<String, JobStats> stats, double s, String saldate, ArrayList<JobsMultiplier> multis, HashMap<String, String> settings) {
        this.plugin = plugin;
        this.name = name;
        this.display = display;
        this.UUID = UUID;
        this.points = points;
        this.max = max;
        this.owned = owned2;
        this.rUUID = rUUID;
        this.current = current2;
        this.lang = lang;
        this.stats = stats;
        this.sal = s;
        this.saldate = saldate;
        this.multi = multis;
        this.settings = settings;
    }

    public String getDisplayName() {
        return this.display;
    }

    public void addSetting(String name, String val) {
        HashMap<String, String> listed = getPlayerSettings();
        listed.put(name, val);
        this.settings = listed;
    }

    public void updateSetting(String name, String val) {
        HashMap<String, String> listed = getPlayerSettings();
        listed.remove(name);
        listed.put(name, val);
        this.settings = listed;
    }

    public HashMap<String, String> getPlayerSettings() {
        return settings;
    }

    public ArrayList<JobsMultiplier> getMultipliers() {
        return multi;
    }

    public void updateMultiList(ArrayList<JobsMultiplier> list) {
        this.multi = list;
    }

    public void updateCacheSalaryDate(String date) {
        saldate = date;
    }

    public String getSalaryDate() {
        return saldate;
    }

    public void updateCacheSalary(double d) {
        this.sal = d;
    }

    public double getSalary() {
        return sal;
    }

    public void updateLocalLanguage(Language n) {
        this.lang = n;
    }

    public boolean hasStatsOf(String job) {
        return stats.get(job) != null;
    }

    public JobStats getStatsOf(String job) {
        JobStats used = null;
        if (!stats.containsKey(job)) {
            HashMap<String, JobStats> newstats = this.stats;
            Job real = plugin.getJobCache().get(job);
            HashMap<String, Double> list01 = new HashMap<String, Double>();
            HashMap<String, Integer> list02 = new HashMap<String, Integer>();
            HashMap<String, Double> list03 = new HashMap<String, Double>();
            HashMap<String, Integer> list04 = new HashMap<String, Integer>();
            HashMap<String, Double> list05 = new HashMap<String, Double>();
            HashMap<String, Double> list06 = new HashMap<String, Double>();
            real.getActionList().forEach((action) -> {
                real.getIDsOf(action).forEach((id, type) -> {
                    list01.put(id, 0.0);
                    list02.put(id, 1);
                });
            });
            list03.put(plugin.getDate(), 0.0);
            used = new JobStats(real, job, 0, 1, 0, plugin.getDate(), list01, list02, list03, plugin.getDate(), list04, list05, list06);
            newstats.put(job, used);
            this.stats = newstats;
        } else {
            used = stats.get(job);
        }
        return used;
    }

    public HashMap<String, JobStats> getStatsList() {
        return stats;
    }

    public Language getLanguage() {
        if (!plugin.getLocalFileManager().getLanguageConfig().getBoolean("EnabledLanguages")) {
            String used = plugin.getLocalFileManager().getLanguageConfig().getString("UseLanguageWhenDisabled");
            return plugin.getLanguageAPI().getLanguages().get(used);
        }
        return lang;
    }

    public java.util.UUID getUUID() {
        return this.rUUID;
    }

    public String getUUIDAsString() {
        return UUID;
    }

    public JobsPlayer getJobsPlayer() {
        return this;
    }

    public double getPoints() {
        return points;
    }

    public void updateCachePoints(double d) {
        points = d;
    }

    public String getName() {
        return name;
    }

    public int getMaxJobs() {
        return max;
    }

    public void updateCacheMax(int nw) {
        max = nw;
    }

    public void addCurrentJob(String job) {
        ArrayList<String> l = getCurrentJobs();
        l.add(job);
        updateCurrentJobs(l);
    }

    public void remCurrentJob(String job) {
        ArrayList<String> l = getCurrentJobs();
        l.remove(job);
        updateCurrentJobs(l);
    }

    public boolean ownJob(String id) {
        if (getOwnJobs().contains(id)) {
            return true;
        }
        return false;
    }

    public boolean isInJob(String id) {
        if (getCurrentJobs() == null) {
            return false;
        }
        return getCurrentJobs().contains(id.toUpperCase());
    }

    public ArrayList<String> getCurrentJobs() {
        return current;
    }

    public ArrayList<Job> getCurrentJobsAsObject() {
        ArrayList<Job> n = new ArrayList<Job>();
        for (String job : current) {
            n.add(plugin.getJobCache().get(job));
        }
        return n;
    }

    public void updateCurrentJobs(ArrayList<String> l) {
        current = l;
    }

    public ArrayList<String> getOwnJobs() {
        return owned;
    }

    public void addOwnedJob(String job) {
        ArrayList<String> l = getOwnJobs();
        l.add(job);
        updateOwnJobs(l);
    }

    public void remOwnedJob(String job) {
        ArrayList<String> l = getOwnJobs();
        l.remove(job);
        updateOwnJobs(l);
    }

    public void updateOwnJobs(ArrayList<String> l) {
        owned = l;
    }

}
