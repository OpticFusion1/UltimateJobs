package optic_fusion1.jobs.util;

import optic_fusion1.jobs.UltimateJobs;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BossBarHandler {

    private UltimateJobs plugin;

    public static HashMap<String, BossBar> g = new HashMap<String, BossBar>();

    public BossBarHandler(UltimateJobs plugin) {
        this.plugin = plugin;
    }

    public void createBar(Player p, String name, BarColor color, String ID, double d) {
        BossBar b = Bukkit.createBossBar(name, color, BarStyle.SOLID, new BarFlag[]{});

        double real = d;

        if (d >= 1.0) {
            real = 1.0;
        }

        if (d <= 0.1) {
            real = 0.1;
        }

        b.setProgress(real);
        b.setVisible(true);
        g.put(ID, b);

        ((BossBar) g.get(ID)).addPlayer(p);
    }

    public boolean exist(String id) {
        return g.get(id) != null;
    }

    public void removeBossbarFromAllPlayers() {
        Iterator<Map.Entry<String, BossBar>> iterator = g.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, BossBar> entry = iterator.next();
            String key = entry.getKey();
            BossBar value = entry.getValue();
            if (g.containsKey(key)) {
                value.setVisible(true);
                g.remove(key);
            }
        }
    }

    public void removeBossBar(String ID) {
        if (g.containsKey(ID)) {
            ((BossBar) g.get(ID)).setVisible(false);
            g.remove(ID);
        }
    }

    public void renameBossBar(String name, String ID) {
        ((BossBar) g.get(ID)).setTitle(name);
    }

    public void updateProgress(double pr, String ID) {
        double real = pr;

        if (pr >= 1.0) {
            real = 1.0;
        }

        if (pr <= 0.1) {
            real = 0.1;
        }

        ((BossBar) g.get(ID)).setProgress(real);
    }

    public void recolorBossBar(BarColor color, String ID) {
        ((BossBar) g.get(ID)).setColor(color);
    }

    public void createTempBossBar(Player p, String name, BarColor color, final String ID,
            Integer timeInSecondsBeforeRemove, double pr) {
        createBar(p, name, color, ID, pr);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) plugin, new Runnable() {
            public void run() {
                removeBossBar(ID);
            }
        }, (timeInSecondsBeforeRemove.intValue() * 20));
    }

    public void startSystemCheck() {

        new BukkitRunnable() {

            public void run() {

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        for (Player p : Bukkit.getOnlinePlayers()) {

                            Date lastworked = plugin.getAPI().lastworked_list.get(p.getName());

                            if (lastworked == null) {
                                continue;
                            }

                            boolean check = lastworked.after(new Date());

                            if (check == false) {
                                if (plugin.getAPI().lastworked_list.containsKey(p.getName())) {
                                    removeBossBar(p.getName());
                                    plugin.getAPI().lastworked_list.remove(p.getName());
                                }
                            }

                        }
                    }
                }.runTaskAsynchronously(plugin);

            }
        }.runTaskTimer(plugin, 0, 25);
    }

    public double calc(double exp, boolean ismaxlevel, double need) {
        double use = 1.0;
        if (!ismaxlevel) {
            double jobneed = need / 100;

            double p = exp / jobneed;

            double max = 1.0 / 100;

            double one = max * p;

            if (one >= 1.0) {
                one = 1.0;
            }
            use = one;

        } else {
            use = 1.0;

        }

        return use;
    }
}
