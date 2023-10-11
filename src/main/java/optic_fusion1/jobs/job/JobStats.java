package optic_fusion1.jobs.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class JobStats {

    private Job job;
    private String jobnamed;
    private double exp;
    private int level;
    private int broken;
    private String date;
    private HashMap<String, Double> brokenof_money;
    private HashMap<String, Integer> brokenof_times;
    private HashMap<String, Double> earnings;
    private HashMap<String, Integer> avg_work;
    private HashMap<String, Double> avg_earnings;
    private HashMap<String, Double> avg_exp;
    private String joined;

    public JobStats(Job job, String jobnamed, double exp, int level, int broken, String date, HashMap<String, Double> brokenof, HashMap<String, Integer> brokenof_times, HashMap<String, Double> earnings,
            String joinedy, HashMap<String, Integer> avg_work, HashMap<String, Double> avg_earnings, HashMap<String, Double> avg_exp) {
        this.job = job;
        this.jobnamed = jobnamed;
        this.exp = exp;
        this.level = level;
        this.broken = broken;
        this.date = date;
        this.joined = joinedy;
        this.brokenof_money = brokenof;
        this.brokenof_times = brokenof_times;
        this.earnings = earnings;
        this.avg_work = avg_work;
        this.avg_earnings = avg_earnings;
        this.avg_exp = avg_exp;
    }

    public void updateAverageEarnings(String date, double value) {
        HashMap<String, Double> listed = null;
        if (!hasAverageEarnings()) {
            listed = new HashMap<String, Double>();
        } else {
            listed = getAverageEarningsList();
        }
        if (listed.containsKey(date)) {
            Double befor = listed.get(date);
            listed.remove(date);
            listed.put(date, befor + value);
        } else {
            listed.put(date, value);
        }
        this.avg_earnings = listed;

    }

    public void updateAverageExp(String date, double value) {
        HashMap<String, Double> listed = null;
        if (!hasAverageExp()) {
            listed = new HashMap<String, Double>();
        } else {
            listed = getAverageExpList();
        }
        if (listed.containsKey(date)) {
            Double before = listed.get(date);
            listed.remove(date);
            listed.put(date, before + value);
        } else {
            listed.put(date, value);
        }
        this.avg_exp = listed;

    }

    public boolean hasAverageExp() {
        return this.avg_exp != null;
    }

    public HashMap<String, Double> getAverageExpList() {
        return this.avg_exp;
    }

    public boolean hasAverageEarnings() {
        return this.avg_earnings != null;
    }

    public HashMap<String, Double> getAverageEarningsList() {
        return this.avg_earnings;
    }

    public void updateAverageWork(String date, int value) {
        HashMap<String, Integer> listed = null;
        if (!hasAverageWork()) {
            listed = new HashMap<String, Integer>();
        } else {
            listed = getAverageWorkList();
        }
        if (listed.containsKey(date)) {
            Integer befor = listed.get(date);
            listed.remove(date);
            listed.put(date, befor + value);
        } else {
            listed.put(date, value);
        }
        this.avg_work = listed;

    }

    public boolean hasAverageWork() {
        return this.avg_work != null;
    }

    public HashMap<String, Integer> getAverageWorkList() {
        return this.avg_work;
    }

    public void updateCacheJoinedDate(String jn) {
        joined = jn;
    }

    public String getJoinedDate() {
        return joined;
    }

    public HashMap<String, Integer> getWorkedTimesOfIDList() {
        return brokenof_times;
    }

    public Integer getWorkedTimesOf(String ID) {
        return brokenof_times.get(ID);
    }

    public void updateCacheBrokenTimesOf(String id, int value) {

        getWorkedTimesOfIDList().put(id, value);
    }

    public HashMap<String, Double> getEarningDatesList() {
        return earnings;
    }

    public Collection<String> getEarningsOnlyDates() {
        Collection<String> dates = new ArrayList<String>();
        getEarningDatesList().forEach((date, v) -> {
            dates.add(date);
        });
        return dates;
    }

    public Double getEarningsofDate(String date2) {
        if (!getEarningDatesList().containsKey(date2)) {
            return 0.0;
        }
        return getEarningDatesList().get(date2);
    }

    public void updateDateEarningsOf(String date, double value) {
        getEarningDatesList().put(date, value);
    }

    public HashMap<String, Double> getTimesExecutedMoneyList() {
        return brokenof_money;
    }

    public Double getTimesExecutedMoneyOf(String ID) {
        if (!brokenof_money.containsKey(ID)) {
            return 0.0;
        }
        return brokenof_money.get(ID);
    }

    public void updateTimesExecutedMoneyOf(String id, double value) {
        brokenof_money.put(id, value);
    }

    public String getDate() {
        return date;
    }

    public int getHowManyTimesWorked() {
        return broken;
    }

    public void updateHowmanyTimesWorked(int val) {
        this.broken = val;
    }

    public int getLevel() {
        return level;
    }

    public void changeCacheLevel(int lvl) {
        this.level = lvl;
    }

    public double getExp() {
        return exp;
    }

    public void changeCacheExp(double val) {
        this.exp = val;
    }

    public Job getJob() {
        return job;
    }

    public String getJobName() {
        return jobnamed;
    }

}
