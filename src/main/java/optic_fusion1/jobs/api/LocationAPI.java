package optic_fusion1.jobs.api;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.util.PlayerDataFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Class to manage Locations
 */
public class LocationAPI {

    private UltimateJobs plugin;

    public LocationAPI(UltimateJobs plugin) {
        this.plugin = plugin;
    }

    public void setLocation(Location loc, String name) {
        PlayerDataFile file = plugin.getLocationDataFile();
        FileConfiguration cfg = file.get();

        cfg.set(name + ".world", loc.getWorld().getName());
        cfg.set(name + ".x", loc.getX());
        cfg.set(name + ".y", loc.getY());
        cfg.set(name + ".z", loc.getZ());
        cfg.set(name + ".yaw", loc.getYaw());
        cfg.set(name + ".pitch", loc.getPitch());

        file.save();
    }

    public Location getLocation(String name) {

        PlayerDataFile file = plugin.getLocationDataFile();
        FileConfiguration cfg = file.get();

        String world = cfg.getString(name + ".world");
        double x = cfg.getDouble(name + ".x");
        double y = cfg.getDouble(name + ".y");
        double z = cfg.getDouble(name + ".z");
        float yaw = (float) cfg.getDouble(name + ".yaw");
        float pitch = (float) cfg.getDouble(name + ".pitch");

        Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

        return loc;
    }

}
