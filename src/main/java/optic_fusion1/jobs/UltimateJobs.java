package optic_fusion1.jobs;

import optic_fusion1.jobs.api.BlockAPI;
import optic_fusion1.jobs.api.DataBaseAPI;
import optic_fusion1.jobs.api.EffectAPI;
import optic_fusion1.jobs.api.ItemAPI;
import optic_fusion1.jobs.api.JobAPI;
import optic_fusion1.jobs.api.LanguageAPI;
import optic_fusion1.jobs.api.LevelAPI;
import optic_fusion1.jobs.api.LocationAPI;
import optic_fusion1.jobs.api.OfflinePlayerAPI;
import optic_fusion1.jobs.api.PlayerAPI;
import optic_fusion1.jobs.api.PlayerChunkAPI;
import optic_fusion1.jobs.api.SkullCreatorAPI;
import optic_fusion1.jobs.api.plugins.ItemsAdderManager;
import optic_fusion1.jobs.api.plugins.MythicMobsManager;
import optic_fusion1.jobs.api.plugins.NoteBlockManager;
import optic_fusion1.jobs.api.plugins.PlaceHolderManager;
import optic_fusion1.jobs.api.plugins.WorldGuardManager;
import optic_fusion1.jobs.command.admin.AdminCommand;
import optic_fusion1.jobs.command.admin.AdminTabComplete;
import optic_fusion1.jobs.command.player.JobTabComplete;
import optic_fusion1.jobs.command.admin.sub.BoostSub;
import optic_fusion1.jobs.command.admin.sub.ExpSub;
import optic_fusion1.jobs.command.admin.sub.HelpSub;
import optic_fusion1.jobs.command.admin.sub.IDSub;
import optic_fusion1.jobs.command.admin.sub.LanguageSub;
import optic_fusion1.jobs.command.admin.sub.LevelSub;
import optic_fusion1.jobs.command.admin.sub.MaxSub;
import optic_fusion1.jobs.command.admin.sub.OpenSub;
import optic_fusion1.jobs.command.admin.sub.PluginSub;
import optic_fusion1.jobs.command.player.sub.EarningsSub;
import optic_fusion1.jobs.command.player.sub.JoinSub;
import optic_fusion1.jobs.command.player.sub.LangSub;
import optic_fusion1.jobs.command.player.sub.LeaveAllSub;
import optic_fusion1.jobs.command.player.sub.LeaveSub;
import optic_fusion1.jobs.command.player.sub.LevelsSub;
import optic_fusion1.jobs.command.player.sub.LimitSub;
import optic_fusion1.jobs.command.player.sub.PointsSub;
import optic_fusion1.jobs.command.player.sub.RankingSub;
import optic_fusion1.jobs.command.player.sub.RewardsSub;
import optic_fusion1.jobs.command.player.sub.StatsSub;
import optic_fusion1.jobs.command.player.sub.WithdrawSub;
import optic_fusion1.jobs.listener.BlockFireWorkDamage;
import optic_fusion1.jobs.listener.JobsInventoryClickEvent;
import optic_fusion1.jobs.listener.PlayerExistEvent;
import optic_fusion1.jobs.listener.PlayerRewardCommandEvent;
import optic_fusion1.jobs.manager.ClickManager;
import optic_fusion1.jobs.manager.FileManager;
import optic_fusion1.jobs.manager.GuiAddonManager;
import optic_fusion1.jobs.manager.GuiManager;
import optic_fusion1.jobs.manager.GuiOpenManager;
import optic_fusion1.jobs.manager.JobWorkManager;
import optic_fusion1.jobs.manager.PluginManager;
import optic_fusion1.jobs.util.BossBarHandler;
import optic_fusion1.jobs.util.JsonMessage;
import optic_fusion1.jobs.util.PlayerDataFile;
import optic_fusion1.jobs.database.DatabaseInit;
import optic_fusion1.jobs.database.hikari.HikariAuthentication;
import optic_fusion1.jobs.database.statements.SQLStatementAPI;
import optic_fusion1.jobs.util.DataMode;
import optic_fusion1.jobs.util.PluginColor;
import optic_fusion1.jobs.job.Job;
import optic_fusion1.jobs.job.JobsPlayer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import net.milkbowl.vault.economy.Economy;
import optic_fusion1.jobs.command.player.JobsCommand;
import optic_fusion1.jobs.command.admin.sub.AdminSubCommandRegistry;
import optic_fusion1.jobs.command.player.sub.SubCommandRegistry;
import optic_fusion1.jobs.job.action.DefaultJobActions;
import optic_fusion1.jobs.job.action.JobActionAdvancement;
import optic_fusion1.jobs.job.action.JobActionBreak;
import optic_fusion1.jobs.job.action.JobActionBreed;
import optic_fusion1.jobs.job.action.JobActionCarve;
import optic_fusion1.jobs.job.action.JobActionCollectBerries;
import optic_fusion1.jobs.job.action.JobActionCraft;
import optic_fusion1.jobs.job.action.JobActionDrinkPotion;
import optic_fusion1.jobs.job.action.JobActionEat;
import optic_fusion1.jobs.job.action.JobActionEnchant;
import optic_fusion1.jobs.job.action.JobActionFarmBreak;
import optic_fusion1.jobs.job.action.JobActionFarmGrow;
import optic_fusion1.jobs.job.action.JobActionFindATreasure;
import optic_fusion1.jobs.job.action.JobActionFish;
import optic_fusion1.jobs.job.action.JobActionGrowSapling;
import optic_fusion1.jobs.job.action.JobActionHoney;
import optic_fusion1.jobs.job.action.JobActionItemPickUp;
import optic_fusion1.jobs.job.action.JobActionKillByBow;
import optic_fusion1.jobs.job.action.JobActionKillMob;
import optic_fusion1.jobs.job.action.JobActionMilk;
import optic_fusion1.jobs.job.action.JobActionPlace;
import optic_fusion1.jobs.job.action.JobActionPlayerMove;
import optic_fusion1.jobs.job.action.JobActionShear;
import optic_fusion1.jobs.job.action.JobActionSmelt;
import optic_fusion1.jobs.job.action.JobActionStripLog;
import optic_fusion1.jobs.job.action.JobActionTame;
import optic_fusion1.jobs.job.action.JobActionVillagerTrade;
import optic_fusion1.jobs.job.action.itemsadder.JobAction_IA_Break;
import optic_fusion1.jobs.job.action.itemsadder.JobAction_IA_Kill;
import optic_fusion1.jobs.job.action.mythocmobs.JobActionMMKill;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main Class of UltimateJobs
 */
public class UltimateJobs extends JavaPlugin {

    /**
     * Util Classes
     */
    private BossBarHandler bossBarHandler;
    private Economy econ;
    private LevelAPI levels;
    private ArrayList<String> loaded;
    private HashMap<String, Job> ld;
    private GuiManager gui;
    private JobAPI api;
    private ClickManager click;
    private SubCommandRegistry cmdmanager;
    private AdminSubCommandRegistry admincmdmanager;

    private JobWorkManager work;
    private PluginManager plapi;
    private FileManager filemanager;
    private GuiAddonManager adddongui;
    private MythicMobsManager mm;

    private PlayerAPI papi;
    private OfflinePlayerAPI dataapi;
    private JsonMessage js;
    private LanguageAPI langapi;
    private BlockAPI bapi;

    private PlayerDataFile loc;
    private SkullCreatorAPI skull;
    private LocationAPI locapi;
    public DataMode mode = null;
    private ItemAPI i;
    private DatabaseInit init;
    private GuiOpenManager ogui;

    private PlayerDataFile chunk;
    private PlayerChunkAPI capi;

    private ItemsAdderManager aim;
    private NoteBlockManager ntb;

    private EffectAPI ef;

    private PlayerDataFile data_global;
    private PlayerDataFile data_stats;
    private PlayerDataFile data_earnings;
    private PlayerDataFile data_current_owned;
    private PlayerDataFile data_other;
    private PlayerDataFile data_multi;
    private PlayerDataFile bdata;

    public void onLoad() {
        bossBarHandler = new BossBarHandler(this);
        plapi = new PluginManager(this);
        langapi = new LanguageAPI(this);
        filemanager = new FileManager(this);

        createFolders();

        filemanager.generateFiles();

        try {
            mode = DataMode.valueOf(getLocalFileManager().getDataConfig().getString("Mode").toUpperCase());
        } catch (IllegalArgumentException ex) {
            Bukkit.getConsoleSender()
                    .sendMessage(PluginColor.FAILED + "Failed to load Data Mode! Continue with files...");
            mode = DataMode.FILE;
        }

        Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading Languages...");

        loadClasses();

        if (getPluginManager().isInstalled("WorldGuard")) {
            Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading WorldGuard Support...");
            WorldGuardManager.setClass();
            WorldGuardManager.load();
        }

    }

    @Override
    public void onEnable() {
        if (mode.equals(DataMode.SQL)) {

            Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Connecting to SQL...");

            loadSQL();

            connect();

            getPlayerOfflineAPI().createtables();

        } else if (mode.equals(DataMode.FILE)) {

            Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading Files...");

            data_global = new PlayerDataFile(this, "jobs");

            data_stats = new PlayerDataFile(this, "stats");
            data_earnings = new PlayerDataFile(this, "earnings");
            data_current_owned = new PlayerDataFile(this, "cr_ow_jobs");
            data_other = new PlayerDataFile(this, "other");
            data_multi = new PlayerDataFile(this, "multipliers");

            data_multi.create();
            data_global.create();
            data_stats.create();
            data_earnings.create();
            data_current_owned.create();
            data_other.create();
        }

        Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading Vault...");

        setupEconomy();

        if (getPluginManager().isInstalled("PlaceHolderAPI")) {
            Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading PlaceHolderAPI Support...");
            new PlaceHolderManager(this).register();
        }

        if (getPluginManager().isInstalled("NoteBlockAPI")) {
            Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading NoteBlockAPI Support...");
            ntb = new NoteBlockManager();
        }

        Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Register Commands...");

        getCommand("jobs").setExecutor(new JobsCommand(this));
        getCommand("jobs").setTabCompleter(new JobTabComplete(this));

        getCommand("jobsadmin").setExecutor(new AdminCommand(this));
        getCommand("jobsadmin").setTabCompleter(new AdminTabComplete(this));

        Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Register Sub-Commands...");

        registerSubCommands();

        bossBarHandler.startSystemCheck();

        getPluginManager().startCheck();

        createBackups();

        Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading Job Events...");

        loadEvents();

        Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading and checking for Jobs...");

        api.loadJobs(getLogger());
        i.loadItems();

        getLanguageAPI().loadLanguages();

        Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading basic Events...");
        loadBasicEvents();

        List<String> players = getPlayerOfflineAPI().getAllPlayersFromData();

        if (!players.isEmpty() && players != null) {
            if (getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.PlayerInfo")) {
                Bukkit.getConsoleSender()
                        .sendMessage(PluginColor.INFO.getPrefix() + "Loading data for " + players.size() + " Players!");
            }
            players.forEach((uuid) -> {
                if (getLocalFileManager().getUtilsConfig()
                        .getBoolean("Plugin.DebugMessagesOnStart.PlayerInfo")) {
                    Bukkit.getConsoleSender()
                            .sendMessage(PluginColor.INFO.getPrefix() + "Checking Data for " + uuid + "...");
                }
                String name = getPlayerOfflineAPI().getANameFromUUID(uuid);
                String display = getPlayerOfflineAPI().getADisplayNameFromUUID(uuid);

                getPlayerAPI().LoadDataForServerStart(name, display, UUID.fromString(uuid.toString()));

            });
        }

        Bukkit.getOnlinePlayers().forEach((p) -> {
            getPlayerAPI().checkAndLoadIntoOnlineCache(p, p.getName().toLowerCase(), p.getName(),
                    "" + p.getUniqueId());
        });

        bossBarHandler.removeBossbarFromAllPlayers();

        getPlayerAPI().calculateRanking();
        getPlayerAPI().startUtil();

        getBlockAPI().loadBlocks();

        if (getLocalFileManager().getDataConfig().getBoolean("EnableSaving")) {
            int time = getLocalFileManager().getDataConfig().getInt("SaveDataEvery");
            getPlayerAPI().startSavingData(time);
        }

        if (getLanguageAPI().getLoadedLanguagesAsArray().size() != 0 && getItemAPI().fails.size() == 0
                && getAPI().fails.size() == 0) {
            Bukkit.getConsoleSender().sendMessage("§7");
            Bukkit.getConsoleSender().sendMessage("§7");
            Bukkit.getConsoleSender().sendMessage(
                    "§a,--. ,--.,--.,--------.,--.,--.   ,--.  ,---. ,--------.,------.     ,--. ,-----. ,-----.   ,---.   ");
            Bukkit.getConsoleSender().sendMessage(
                    "§a|  | |  ||  |'--.  .--'|  ||   `.'   | /  O  \\'--.  .--'|  .---'     |  |'  .-.  '|  |) /_ '   .-'  ");
            Bukkit.getConsoleSender().sendMessage(
                    "§a|  | |  ||  |   |  |   |  ||  |'.'|  ||  .-.  |  |  |   |  `--, ,--. |  ||  | |  ||  .-.  \\`.  `-.  ");
            Bukkit.getConsoleSender().sendMessage(
                    "§a'  '-'  '|  '--.|  |   |  ||  |   |  ||  | |  |  |  |   |  `---.|  '-'  /'  '-'  '|  '--' /.-'    | ");
            Bukkit.getConsoleSender().sendMessage(
                    "§a `-----' `-----'`--'   `--'`--'   `--'`--' `--'  `--'   `------' `-----'  `-----' `------' `-----'  ");
            Bukkit.getConsoleSender().sendMessage("       §aRunning plugin UltimateJobs "
                    + getDescription().getVersion() + " (" + getDescription().getAPIVersion() + ")");
            Bukkit.getConsoleSender()
                    .sendMessage("       §aRunning UltimateJobs with " + getLoaded().size() + " Jobs ");
            Bukkit.getConsoleSender()
                    .sendMessage("       §aLoaded " + getLanguageAPI().getLanguages().size() + " Languages");
            Bukkit.getConsoleSender().sendMessage("§7");
            Bukkit.getConsoleSender().sendMessage("§7");
        } else {
            printFailed();
        }

    }

    public void printFailed() {
        Bukkit.getConsoleSender().sendMessage("§4");
        Bukkit.getConsoleSender().sendMessage("§4");
        Bukkit.getConsoleSender().sendMessage(
                "§4,--. ,--.,--.,--------.,--.,--.   ,--.  ,---. ,--------.,------.     ,--. ,-----. ,-----.   ,---.   ");
        Bukkit.getConsoleSender().sendMessage(
                "§4|  | |  ||  |'--.  .--'|  ||   `.'   | /  O  \\'--.  .--'|  .---'     |  |'  .-.  '|  |) /_ '   .-'  ");
        Bukkit.getConsoleSender().sendMessage(
                "§4|  | |  ||  |   |  |   |  ||  |'.'|  ||  .-.  |  |  |   |  `--, ,--. |  ||  | |  ||  .-.  \\`.  `-.  ");
        Bukkit.getConsoleSender().sendMessage(
                "§4'  '-'  '|  '--.|  |   |  ||  |   |  ||  | |  |  |  |   |  `---.|  '-'  /'  '-'  '|  '--' /.-'    | ");
        Bukkit.getConsoleSender().sendMessage(
                "§4 `-----' `-----'`--'   `--'`--'   `--'`--' `--'  `--'   `------' `-----'  `-----' `------' `-----'  ");
        Bukkit.getConsoleSender().sendMessage("§4        §4Failed to load plugin UltimateJobs "
                + getDescription().getVersion() + "§4 (" + getDescription().getAPIVersion() + ")");
        Bukkit.getConsoleSender().sendMessage("§7");
        Bukkit.getConsoleSender().sendMessage("§7");

        Bukkit.getPluginManager().disablePlugin(this);
    }

    /**
     * Method to connect to SQL Databases
     */
    public void connect() {

        FileConfiguration cfg = getLocalFileManager().getDataConfig();

        String host = cfg.getString("Config.host");
        int port = cfg.getInt("Config.port");
        String database = cfg.getString("Config.database");
        String username = cfg.getString("Config.user");
        String password = cfg.getString("Config.password");

        String type = cfg.getString("Database.type");
        int size = cfg.getInt("Database.timeout");
        int pool = cfg.getInt("Database.poolsize");

        init = getInit();
        if (getInit().getDataSource() == null || getInit().isClosed()) {
            getInit().initDatabase(this, new HikariAuthentication(host, port, database, username, password), type, size,
                    pool);
            Bukkit.getConsoleSender()
                    .sendMessage(PluginColor.INFO.getPrefix() + "Connected to " + type + " to save data!");
        }

    }

    public PlayerDataFile getChunkData() {
        return chunk;
    }

    public SQLStatementAPI getSQLStatementAPI() {
        return DataBaseAPI.getSQLStatementAPI();
    }

    public ItemAPI getItemAPI() {
        return i;
    }

    public DatabaseInit getInit() {
        return init;
    }

    public LocationAPI getLocationAPI() {
        return locapi;
    }

    public void loadSQL() {
        init = new DatabaseInit();
    }

    public PlayerDataFile getLocationDataFile() {
        return loc;
    }

    public DataMode getPluginMode() {
        return mode;
    }

    public SkullCreatorAPI getSkullCreatorAPI() {
        return skull;
    }

    /**
     * Saving data, closing connection
     */
    public void onDisable() {
        bossBarHandler.removeBossbarFromAllPlayers();

        getBlockAPI().saveBlocks();

        HashMap<String, JobsPlayer> players = getPlayerAPI().getOfflineCachePlayers();
        players.forEach((uuid, jb) -> {

            getPlayerOfflineAPI().savePlayerAsFinal(jb, uuid, jb.getName(), jb.getDisplayName());

        });

        HashMap<String, JobsPlayer> players2 = getPlayerAPI().getOnlinePlayersListed();
        players2.forEach((uuid, jb) -> {

            getPlayerOfflineAPI().savePlayerAsFinal(jb, uuid, jb.getName(), jb.getDisplayName());
        });

        createBackups();

        if (mode.equals(DataMode.SQL)) {
            if (!this.init.isClosed()) {
                this.init.close();
                Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Closed SQL Connection...");
            }
        }

        getItemAPI().items.clear();
    }

    /**
     * Method to create a Backup of the Files
     */
    public void createBackups() {
        if (getLocalFileManager().getConfig().getBoolean("CreateBackupsOfFiles")) {
            if (mode.equals(DataMode.FILE)) {
                Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Creating Backup...");
                File log = new File(getDataFolder().getAbsolutePath() + "/backups");
                if (!log.exists()) {
                    log.mkdir();
                }
                String filePath = getDataFolder().getAbsolutePath() + "/data/jobs.json";
                String zipPath = log.getAbsolutePath() + "/"
                        + (new SimpleDateFormat("yyyy-MM-dd-HH-mm")).format(new Date()).toString() + ".zip";
                try {
                    ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipPath));
                    try {
                        File fileToZip = new File(filePath);
                        zipOut.putNextEntry(new ZipEntry(fileToZip.getName()));
                        Files.copy(fileToZip.toPath(), zipOut);
                        zipOut.close();

                        Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Created Backup!");
                    } catch (Throwable throwable) {
                        try {
                            zipOut.close();
                        } catch (Throwable throwable1) {
                            throwable.addSuppressed(throwable1);
                        }
                        throw throwable;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Method to register all Sub-Commands
     */
    public void registerSubCommands() {

        // user
        getSubCommandManager().getSubCommandList().add(new optic_fusion1.jobs.command.player.sub.HelpSub(this));
        getSubCommandManager().getSubCommandList().add(new LeaveSub(this));
        getSubCommandManager().getSubCommandList().add(new LeaveAllSub(this));
        getSubCommandManager().getSubCommandList().add(new PointsSub(this));
        getSubCommandManager().getSubCommandList().add(new LimitSub(this));
        getSubCommandManager().getSubCommandList().add(new JoinSub(this));
        if (getLocalFileManager().getLanguageConfig().getBoolean("EnabledLanguages")) {
            getSubCommandManager().getSubCommandList().add(new LangSub(this));
        }
        getSubCommandManager().getSubCommandList().add(new StatsSub(this));
        getSubCommandManager().getSubCommandList().add(new LevelsSub(this));
        getSubCommandManager().getSubCommandList().add(new EarningsSub(this));
        getSubCommandManager().getSubCommandList().add(new RewardsSub(this));
        getSubCommandManager().getSubCommandList().add(new WithdrawSub(this));
        getSubCommandManager().getSubCommandList().add(new RankingSub(this));

        // admin
        getAdminSubCommandManager().getSubCommandList().add(new HelpSub(this));

        if (getLocalFileManager().getLanguageConfig().getBoolean("EnabledLanguages")) {
            getAdminSubCommandManager().getSubCommandList().add(new LanguageSub(this));
        }

        getAdminSubCommandManager().getSubCommandList().add(new OpenSub(this));
        getAdminSubCommandManager().getSubCommandList().add(new IDSub(this));

        getAdminSubCommandManager().getSubCommandList().add(new PluginSub(this));

        getAdminSubCommandManager().getSubCommandList().add(new BoostSub(this));
        getAdminSubCommandManager().getSubCommandList().add(new MaxSub(this));
        getAdminSubCommandManager().getSubCommandList().add(new LevelSub(this));
        getAdminSubCommandManager().getSubCommandList().add(new ExpSub(this));
        getAdminSubCommandManager().getSubCommandList().add(new optic_fusion1.jobs.command.admin.sub.PointsSub(this));

        Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loaded Sub-Commands...");
    }

    /**
     * Method to load all needed Classes
     */
    public void loadClasses() {

        loc = new PlayerDataFile(this, "locations");
        loc.create();

        chunk = new PlayerDataFile(this, "chunk");
        chunk.create();

        bdata = new PlayerDataFile(this, "blocks");
        bdata.create();

        loaded = new ArrayList<>();
        ld = new HashMap<>();
        levels = new LevelAPI(this);

        cmdmanager = new SubCommandRegistry();
        api = new JobAPI(this);
        gui = new GuiManager(this);
        adddongui = new GuiAddonManager(this);
        click = new ClickManager(this, this.filemanager.getConfig(), this.gui);
        admincmdmanager = new AdminSubCommandRegistry();
        work = new JobWorkManager(this, this.api);

        js = new JsonMessage();
        papi = new PlayerAPI(this);

        dataapi = new OfflinePlayerAPI(this);

        i = new ItemAPI(this);
        skull = new SkullCreatorAPI();
        locapi = new LocationAPI(this);
        ogui = new GuiOpenManager(this);

        capi = new PlayerChunkAPI(this);

        ef = new EffectAPI(this);
        bapi = new BlockAPI(this);

        Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loaded Classes...");

    }

    public PlayerDataFile getBlockData() {
        return bdata;
    }

    public BlockAPI getBlockAPI() {
        return bapi;
    }

    public EffectAPI getEffectAPI() {
        return ef;
    }

    public ItemsAdderManager getItemsAdderManager() {
        return aim;
    }

    public PlayerChunkAPI getPlayerChunkAPI() {
        return capi;
    }

    public GuiOpenManager getGUIOpenManager() {
        return ogui;
    }

    public JsonMessage getJsonMessage() {
        return js;
    }

    public GuiAddonManager getGUIAddonManager() {
        return adddongui;
    }

    public PluginManager getPluginManager() {
        return plapi;
    }

    public JobWorkManager getJobWorkManager() {
        return work;
    }

    public PlayerDataFile getGlobalDataFile() {
        return data_global;
    }

    public PlayerDataFile getMultipliersDataFile() {
        return data_multi;
    }

    public PlayerDataFile getStatsDataFile() {
        return data_stats;
    }

    public PlayerDataFile getEarningsDataFile() {
        return data_earnings;
    }

    public PlayerDataFile getCrAndOwDataFile() {
        return data_current_owned;
    }

    public PlayerDataFile getOtherDataFile() {
        return data_other;
    }

    public LanguageAPI getLanguageAPI() {
        return langapi;
    }

    public PlayerAPI getPlayerAPI() {
        return papi;
    }

    public OfflinePlayerAPI getPlayerOfflineAPI() {
        return dataapi;
    }

    public AdminSubCommandRegistry getAdminSubCommandManager() {
        return admincmdmanager;
    }

    public SubCommandRegistry getSubCommandManager() {
        return cmdmanager;
    }

    public NoteBlockManager getNoteBlockManager() {
        return ntb;
    }

    public ClickManager getClickManager() {
        return click;
    }

    public optic_fusion1.jobs.manager.GuiManager getGUI() {
        return gui;
    }

    public JobAPI getAPI() {
        return api;
    }

    public LevelAPI getLevelAPI() {
        return levels;
    }

    /**
     * Method to create all Folders
     */
    private void createFolders() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
            Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Created Datafolder...");
        }

        File folder_1 = new File(getDataFolder(), "jobs");

        if (!folder_1.exists()) {
            folder_1.mkdir();
            Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Created Jobs folder...");

            // create default jobs
            Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading Default Jobs...");
            getLocalFileManager().createDefaultJobs();
        }

        File folder_2 = new File(getDataFolder(), "lang");

        if (!folder_2.exists()) {
            folder_2.mkdir();
            Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Created Language folder...");
        }

        File folder_4 = new File(getDataFolder(), "settings");

        if (!folder_4.exists()) {
            folder_4.mkdir();
            Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Created Settings folder...");
        }

        File folder_5 = new File(getDataFolder(), "addons");

        File folder_7 = new File(getDataFolder(), "guis");

        if (!folder_7.exists()) {
            Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Create GUIs folder...");
            folder_7.mkdir();
        }

        if (!folder_5.exists()) {
            Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Create Addons folder...");
            folder_5.mkdir();

            File folder_6 = new File(getDataFolder() + "/addons/", "songs");
            if (!folder_6.exists()) {
                Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Create Songs folder...");
                folder_6.mkdir();

                getLocalFileManager().createDefaultSongs();
            }

            File folder_8 = new File(getDataFolder() + "/addons/", "dailyquests");
            if (!folder_8.exists()) {
                Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Create Daily-Quests folder...");
                folder_8.mkdir();
            }
        }

    }

    /**
     * Method to load the Events for GUIs etc.
     */
    public void loadBasicEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerExistEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockFireWorkDamage(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRewardCommandEvent(), this);
        Bukkit.getPluginManager().registerEvents(new JobsInventoryClickEvent(this), this);
        Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loaded basic Events!");
    }

    /**
     * Method to load the Events for Jobs
     */
    public void loadEvents() {

        FileConfiguration cfg = getLocalFileManager().getUtilsConfig();

        if (cfg.getBoolean("Actions.Break")) {
            Bukkit.getPluginManager().registerEvents(new JobActionBreak(this), this);
        }
        if (cfg.getBoolean("Actions.FarmBreak")) {
            Bukkit.getPluginManager().registerEvents(new JobActionFarmBreak(this), this);
        }
        if (cfg.getBoolean("Actions.Place")) {
            Bukkit.getPluginManager().registerEvents(new JobActionPlace(this), this);
        }
        if (cfg.getBoolean("Actions.Fish")) {
            Bukkit.getPluginManager().registerEvents(new JobActionFish(this), this);
        }
        if (cfg.getBoolean("Actions.Milk")) {
            Bukkit.getPluginManager().registerEvents(new JobActionMilk(this), this);
        }
        if (cfg.getBoolean("Actions.KillMob")) {
            Bukkit.getPluginManager().registerEvents(new JobActionKillMob(this), this);
        }
        if (cfg.getBoolean("Actions.Shear")) {
            Bukkit.getPluginManager().registerEvents(new JobActionShear(this), this);
        }
        if (cfg.getBoolean("Actions.Craft")) {
            Bukkit.getPluginManager().registerEvents(new JobActionCraft(this), this);
        }
        if (cfg.getBoolean("Actions.Advancement")) {
            Bukkit.getPluginManager().registerEvents(new JobActionAdvancement(this), this);
        }
        if (cfg.getBoolean("Actions.Eat")) {
            Bukkit.getPluginManager().registerEvents(new JobActionEat(this), this);
        }
        if (cfg.getBoolean("Actions.Honey")) {
            Bukkit.getPluginManager().registerEvents(new JobActionHoney(this), this);
        }
        if (cfg.getBoolean("Actions.Tame")) {
            Bukkit.getPluginManager().registerEvents(new JobActionTame(this), this);
        }
        if (cfg.getBoolean("Actions.StripLog")) {
            Bukkit.getPluginManager().registerEvents(new JobActionStripLog(this), this);
        }
        if (cfg.getBoolean("Actions.Breed")) {
            Bukkit.getPluginManager().registerEvents(new JobActionBreed(this), this);
        }
        if (cfg.getBoolean("Actions.DrinkPotion")) {
            Bukkit.getPluginManager().registerEvents(new JobActionDrinkPotion(this), this);
        }
        if (cfg.getBoolean("Actions.CollectBerrys")) {
            Bukkit.getPluginManager().registerEvents(new JobActionCollectBerries(this), this);
        }
        if (cfg.getBoolean("Actions.KillByBow")) {
            Bukkit.getPluginManager().registerEvents(new JobActionKillByBow(this), this);
        }
        if (cfg.getBoolean("Actions.GrowSapling")) {
            Bukkit.getPluginManager().registerEvents(new JobActionGrowSapling(this), this);
        }
        if (cfg.getBoolean("Actions.FarmGrow")) {
            Bukkit.getPluginManager().registerEvents(new JobActionFarmGrow(this), this);
        }
        if (cfg.getBoolean("Actions.FindTreasure")) {
            Bukkit.getPluginManager().registerEvents(new JobActionFindATreasure(this), this);
        }
        if (cfg.getBoolean("Actions.VillagerTradeBuy")) {
            Bukkit.getPluginManager().registerEvents(new JobActionVillagerTrade(this), this);
        }
        if (cfg.getBoolean("Actions.Smelt")) {
            Bukkit.getPluginManager().registerEvents(new JobActionSmelt(this), this);
        }
        if (cfg.getBoolean("Actions.Explore")) {
            Bukkit.getPluginManager().registerEvents(new JobActionPlayerMove(this), this);
        }
        if (cfg.getBoolean("Actions.Enchant")) {
            Bukkit.getPluginManager().registerEvents(new JobActionEnchant(this), this);
        }

        if (cfg.getBoolean("Actions.PickUp")) {
            Bukkit.getPluginManager().registerEvents(new JobActionItemPickUp(this), this);
        }
        if (cfg.getBoolean("Actions.Carve")) {
            Bukkit.getPluginManager().registerEvents(new JobActionCarve(this), this);
        }

        Bukkit.getPluginManager().registerEvents(new DefaultJobActions(this), this);

        if (getPluginManager().isInstalled("MythicMobs")) {
            mm = new MythicMobsManager(this);
            Bukkit.getPluginManager().registerEvents(new JobActionMMKill(this), this);
            Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loaded MythicMobs Support!");
        }

        if (getPluginManager().isInstalled("ItemsAdder")) {

            Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading ItemsAdder Support...");

            aim = new ItemsAdderManager(this);

            if (getLocalFileManager().getConfig().getBoolean("Actions.IABreak")) {
                Bukkit.getPluginManager().registerEvents(new JobAction_IA_Break(this), this);
            }

            if (getLocalFileManager().getConfig().getBoolean("Actions.IAKill")) {
                Bukkit.getPluginManager().registerEvents(new JobAction_IA_Kill(this), this);
            }

            Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loaded ItemsAdder Support!");
        }

        Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loaded Job Events!");
    }

    public MythicMobsManager getMythicMobsManager() {
        return mm;
    }

    /**
     * Vault-Support
     */
    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getConsoleSender().sendMessage("'Vault' is not loaded or enabled");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            Bukkit.getConsoleSender().sendMessage(PluginColor.ERROR.getPrefix() + "Failed to load vault for Ultimatejobs!");
            return false;
        }
        econ = (Economy) rsp.getProvider();
        Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loaded Vault Support!");
        return true;
    }

    public String getDate() {
        return plapi.getDateTodayFromCal();
    }

    public Economy getEco() {
        return econ;
    }

    public HashMap<String, Job> getJobCache() {
        return ld;
    }

    public FileManager getLocalFileManager() {
        return filemanager;
    }

    public ArrayList<String> getLoaded() {
        return loaded;
    }

    public BossBarHandler getBossBarHandler() {
        return bossBarHandler;
    }

}
