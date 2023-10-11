package optic_fusion1.jobs.util;

import optic_fusion1.jobs.UltimateJobs;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Class to manage the local data file
 */
public class PlayerDataFile {

    public String name;
    public File cfile;
    public FileConfiguration config;

    /**
     * Where the file is saved
     */
    public String location = "plugins/UltimateJobs/data/";
    public UltimateJobs plugin;

    public PlayerDataFile(UltimateJobs plugin, String file) {
        this.plugin = plugin;
        this.name = file;
    }

    /**
     * Name of the File
     */
    public String getName() {
        return name;
    }

    /**
     * Create and Load the File
     */
    public void create() {
        if (getfile() != null && !plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdir();
        }
        if (!getfile().exists()) {
            try {
                getfile().createNewFile();
            } catch (Exception e) {
            }
        }
        config = (FileConfiguration) YamlConfiguration.loadConfiguration(this.cfile);
    }

    /**
     * Get the File
     */
    public File getfile() {
        this.cfile = new File(this.location, name + ".yml");
        if (this.cfile != null) {
            return this.cfile;
        }
        return null;
    }

    /**
     * Load the File without Create
     */
    public void load() {
        this.config = (FileConfiguration) YamlConfiguration.loadConfiguration(this.cfile);
    }

    /**
     * Get the File as Config
     */
    public FileConfiguration get() {

        if (this.config == null) {
            load();
        }

        return this.config;
    }

    /**
     * Save the File
     */
    public void save() {
        try {
            this.config.save(getfile());
        } catch (Exception e) {
        }
    }

}
