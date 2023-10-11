package optic_fusion1.jobs.job;

import java.util.List;

public class JobLevel {

    private int level;
    private double ep;
    private double multi;
    private List<String> commands;
    private double reward;
    private String icon;
    private String modeldata;
    private String song;

    public JobLevel(int level, double ep, double mu, List<String> commands2, double reward, String icon, String md, String song) {
        this.level = level;
        this.multi = mu;
        this.commands = commands2;
        this.reward = reward;
        this.icon = icon;
        this.modeldata = md;
        this.ep = ep;
        this.song = song;
    }

    public boolean hasSong() {
        return this.song != null;
    }

    public String getSongPath() {
        return this.song;
    }

    public double getExp() {
        return this.ep;
    }

    public int getModelData() {
        return Integer.valueOf(this.modeldata);
    }

    public boolean hasModelData() {
        return this.modeldata != null;
    }

    public String getIcon() {
        return this.icon;
    }

    public double getReward() {
        return this.reward;
    }

    public List<String> getCommands() {
        return this.commands;
    }

    public double getMultiplier() {
        return this.multi;
    }

    public int getLevel() {
        return this.level;
    }

}
