
package de.warsteiner.jobs;

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

import de.warsteiner.jobs.events.*;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.api.DailyQuestsAPI;
import de.warsteiner.jobs.api.DataBaseAPI;
import de.warsteiner.jobs.api.EffectAPI;
import de.warsteiner.jobs.api.ItemAPI;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.LanguageAPI;
import de.warsteiner.jobs.api.LevelAPI;
import de.warsteiner.jobs.api.LocationAPI;
import de.warsteiner.jobs.api.OfflinePlayerAPI;
import de.warsteiner.jobs.api.PlayerAPI;
import de.warsteiner.jobs.api.PlayerChunkAPI;
import de.warsteiner.jobs.api.SkullCreatorAPI;
import de.warsteiner.jobs.api.plugins.ItemsAdderManager;
import de.warsteiner.jobs.api.plugins.MythicMobsManager;
import de.warsteiner.jobs.api.plugins.NoteBlockManager;
import de.warsteiner.jobs.api.plugins.PlaceHolderManager;
import de.warsteiner.jobs.api.plugins.WorldGuardManager;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.command.AdminTabComplete;
import de.warsteiner.jobs.command.JobTabComplete;
import de.warsteiner.jobs.command.JobsCommand;
import de.warsteiner.jobs.command.admincommand.BoostSub;
import de.warsteiner.jobs.command.admincommand.ExpSub; 
import de.warsteiner.jobs.command.admincommand.HelpSub;
import de.warsteiner.jobs.command.admincommand.IDSub;
import de.warsteiner.jobs.command.admincommand.LanguageSub;
import de.warsteiner.jobs.command.admincommand.LevelSub;
import de.warsteiner.jobs.command.admincommand.MaxSub;
import de.warsteiner.jobs.command.admincommand.OpenSub;
import de.warsteiner.jobs.command.admincommand.PluginSub;
import de.warsteiner.jobs.command.playercommand.EarningsSub;
import de.warsteiner.jobs.command.playercommand.JoinSub;
import de.warsteiner.jobs.command.playercommand.LangSub;
import de.warsteiner.jobs.command.playercommand.LeaveAllSub;
import de.warsteiner.jobs.command.playercommand.LeaveSub;
import de.warsteiner.jobs.command.playercommand.LevelsSub;
import de.warsteiner.jobs.command.playercommand.LimitSub;
import de.warsteiner.jobs.command.playercommand.PointsSub;
import de.warsteiner.jobs.command.playercommand.RankingSub;
import de.warsteiner.jobs.command.playercommand.RewardsSub;
import de.warsteiner.jobs.command.playercommand.StatsSub;
import de.warsteiner.jobs.command.playercommand.WithdrawSub; 
import de.warsteiner.jobs.jobs.DefaultJobActions;
import de.warsteiner.jobs.jobs.JobActionAdvancement;
import de.warsteiner.jobs.jobs.JobActionBreak;
import de.warsteiner.jobs.jobs.JobActionBreed;
import de.warsteiner.jobs.jobs.JobActionCraft;
import de.warsteiner.jobs.jobs.JobActionDrinkPotion;
import de.warsteiner.jobs.jobs.JobActionEat;
import de.warsteiner.jobs.jobs.JobActionEnchant;
import de.warsteiner.jobs.jobs.JobActionExploreChunks;
import de.warsteiner.jobs.jobs.JobActionFarm_Break;
import de.warsteiner.jobs.jobs.JobActionFarm_Grow;
import de.warsteiner.jobs.jobs.JobActionFindATreasure;
import de.warsteiner.jobs.jobs.JobActionFish;
import de.warsteiner.jobs.jobs.JobActionGrowSapling;
import de.warsteiner.jobs.jobs.JobActionHoney;
import de.warsteiner.jobs.jobs.JobActionKillByBow;
import de.warsteiner.jobs.jobs.JobActionKillMob;
import de.warsteiner.jobs.jobs.JobActionMMKill;
import de.warsteiner.jobs.jobs.JobActionMilk;
import de.warsteiner.jobs.jobs.JobActionPlace;
import de.warsteiner.jobs.jobs.JobActionShear;
import de.warsteiner.jobs.jobs.JobActionSmelt;
import de.warsteiner.jobs.jobs.JobActionStripLog;
import de.warsteiner.jobs.jobs.JobActionTame;
import de.warsteiner.jobs.jobs.JobActionVillagerTrade_Buy;
import de.warsteiner.jobs.jobs.JobAction_IA_Break;
import de.warsteiner.jobs.jobs.JobAction_IA_Kill;
import de.warsteiner.jobs.jobs.JobActionsCollectBerrys;
import de.warsteiner.jobs.manager.ClickManager;
import de.warsteiner.jobs.manager.FileManager;
import de.warsteiner.jobs.manager.GuiAddonManager;
import de.warsteiner.jobs.manager.GuiManager;
import de.warsteiner.jobs.manager.GuiOpenManager;
import de.warsteiner.jobs.manager.JobWorkManager;
import de.warsteiner.jobs.manager.PluginManager; 
import de.warsteiner.jobs.utils.BossBarHandler;
import de.warsteiner.jobs.utils.JsonMessage;
import de.warsteiner.jobs.utils.Metrics;
import de.warsteiner.jobs.utils.PlayerDataFile;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommandRegistry;
import de.warsteiner.jobs.utils.database.DatabaseInit;
import de.warsteiner.jobs.utils.database.hikari.HikariAuthentication;
import de.warsteiner.jobs.utils.database.statements.SQLStatementAPI;
import de.warsteiner.jobs.utils.objects.DataMode;
import de.warsteiner.jobs.utils.objects.PluginColor;
import de.warsteiner.jobs.utils.objects.jobs.Job;
import de.warsteiner.jobs.utils.objects.jobs.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommandRegistry;
import net.milkbowl.vault.economy.Economy;

/**
 * Main Class of UltimateJobs
 */

public class UltimateJobs extends JavaPlugin {

	/**
	 * Util Classes
	 */

	private static UltimateJobs plugin;
	private Economy econ;
	private LevelAPI levels;
	private ArrayList<String> loaded;
	private HashMap<String, Job> ld;
	private de.warsteiner.jobs.manager.GuiManager gui;
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
	private DailyQuestsAPI qapi;
	
	private PlayerDataFile data_global;
	private PlayerDataFile data_stats;
	private PlayerDataFile data_earnings;
	private PlayerDataFile data_current_owned;
	private PlayerDataFile data_other;
	private PlayerDataFile data_multi;
	
	/**
	 * Loading Data, Config-Files and checking for Updates
	 */

	public void onLoad() {

		plugin = this;
		plapi = new PluginManager();
		langapi = new LanguageAPI();
		filemanager = new FileManager();
 
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

	/**
	 * Loading Events, Commands, starting Runnables
	 */

	@Override
	public void onEnable() {

		if (mode.equals(DataMode.SQL)) {

			Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Connecting to SQL...");

			loadSQL();

			connect();

			getPlayerOfflineAPI().createtables();

		} else if (mode.equals(DataMode.FILE)) {

			Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading Files...");

			data_global = new PlayerDataFile("jobs");

			data_stats = new PlayerDataFile("stats");
			data_earnings = new PlayerDataFile("earnings");
			data_current_owned = new PlayerDataFile("cr_ow_jobs");
			data_other = new PlayerDataFile("other");
			data_multi = new PlayerDataFile("multipliers");
			 
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
			new PlaceHolderManager().register();
		}

		if (getPluginManager().isInstalled("NoteBlockAPI")) {
			Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading NoteBlockAPI Support...");
			ntb = new NoteBlockManager();
		}

		if (getPluginManager().isInstalled("ItemsAdder")) {

			Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading ItemsAdder Support...");

			aim = new ItemsAdderManager();

			if (getLocalFileManager().getConfig().getBoolean("Actions.IABreak")) {
				Bukkit.getPluginManager().registerEvents(new JobAction_IA_Break(), this);
			}

			if (getLocalFileManager().getConfig().getBoolean("Actions.IAKill")) {
				Bukkit.getPluginManager().registerEvents(new JobAction_IA_Kill(), this);
			}

			Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loaded ItemsAdder Support!");
		}

		Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Register Commands...");

		getCommand("jobs").setExecutor(new JobsCommand());
		getCommand("jobs").setTabCompleter(new JobTabComplete());

		getCommand("jobsadmin").setExecutor(new AdminCommand());
		getCommand("jobsadmin").setTabCompleter(new AdminTabComplete());

		Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Register Sub-Commands...");

		registerSubCommands();

		BossBarHandler.startSystemCheck();

		getPluginManager().startCheck();

		createBackups();

		new Metrics(this, 15424);
 
		Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading Job Events...");

		loadEvents();
		
		Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading and checking for Jobs...");

		api.loadJobs(getLogger());
		i.loadItems();

		getLanguageAPI().loadLanguages();
		
		getDailyQuestsAPI().loadQuests();
 

		Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading basic Events...");
		loadBasicEvents();
		
	  
 
		
		 
	 
		if (getLanguageAPI().getLoadedLanguagesAsArray().size() != 0 && getItemAPI().fails.size() == 0 && getAPI().fails.size() == 0) {
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
			Bukkit.getConsoleSender().sendMessage("       §aRunning UltimateJobs with " + getLoaded().size() + " Jobs");
			Bukkit.getConsoleSender()
					.sendMessage("       §aLoaded " + getLanguageAPI().getLanguages().size() + " Languages");
			Bukkit.getConsoleSender().sendMessage("§7");
			Bukkit.getConsoleSender().sendMessage("§7");
		} else {
			 printFailed();
		}

	 

		List<String> players = plugin.getPlayerOfflineAPI().getAllPlayersFromData();
		Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loading data for "+players.size()+" Players!");
		players.forEach((uuid) -> {
			
			Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Checking Data for "+uuid+"...");
			
			String name = plugin.getPlayerOfflineAPI().getANameFromUUID(uuid);
			String display = plugin.getPlayerOfflineAPI().getADisplayNameFromUUID(uuid);
			
			plugin.getPlayerAPI().LoadDataForServerStart(name, display, UUID.fromString(uuid.toString()));
			
		});
		
		Bukkit.getOnlinePlayers().forEach((p) -> {
			plugin.getPlayerAPI().checkAndLoadIntoOnlineCache(p, p.getName().toLowerCase(), p.getName(), ""+p.getUniqueId());
		});
	 
		BossBarHandler.g.forEach((d1, d2) -> {
			BossBarHandler.removeBossBar(d1);
		});
		
		getPlayerAPI().calculateRanking();
		getPlayerAPI().startUtil();
		
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
			getInit().initDatabase(new HikariAuthentication(host, port, database, username, password), type, size,
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
		
		BossBarHandler.g.forEach((d1, d2) -> {
			BossBarHandler.removeBossBar(d1);
		});
		 
		HashMap<String, JobsPlayer> players = plugin.getPlayerAPI().getOfflineCachePlayers(); 
		players.forEach((uuid, jb) -> {
			
			plugin.getPlayerOfflineAPI().savePlayerAsFinal(jb, uuid, jb.getName(), jb.getDisplayName()); 
		});
		 
		HashMap<String, JobsPlayer> players2 = plugin.getPlayerAPI().getOnlinePlayersListed(); 
		players2.forEach((uuid, jb) -> {
			
			plugin.getPlayerOfflineAPI().savePlayerAsFinal(jb, uuid, jb.getName(), jb.getDisplayName()); 
		}); 
	 
		 
		
		createBackups();

		if (mode.equals(DataMode.SQL)) {
			if (!this.init.isClosed()) {
				this.init.close();
				Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Closed SQL Connection...");
			}
		}
 
		plugin.getItemAPI().items.clear(); 
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

		getSubCommandManager().getSubCommandList().add(new de.warsteiner.jobs.command.playercommand.HelpSub());
		getSubCommandManager().getSubCommandList().add(new LeaveSub());
		getSubCommandManager().getSubCommandList().add(new LeaveAllSub());
		getSubCommandManager().getSubCommandList().add(new PointsSub());
		getSubCommandManager().getSubCommandList().add(new LimitSub());
		getSubCommandManager().getSubCommandList().add(new JoinSub());
		if (getLocalFileManager().getLanguageConfig().getBoolean("EnabledLanguages")) {
			getSubCommandManager().getSubCommandList().add(new LangSub());
		}
		getSubCommandManager().getSubCommandList().add(new StatsSub());
		getSubCommandManager().getSubCommandList().add(new LevelsSub());
		getSubCommandManager().getSubCommandList().add(new EarningsSub());
		getSubCommandManager().getSubCommandList().add(new RewardsSub());
		getSubCommandManager().getSubCommandList().add(new WithdrawSub());
		getSubCommandManager().getSubCommandList().add(new RankingSub());

		// admin

		getAdminSubCommandManager().getSubCommandList().add(new HelpSub());

		if (getLocalFileManager().getLanguageConfig().getBoolean("EnabledLanguages")) {
			getAdminSubCommandManager().getSubCommandList().add(new LanguageSub());
		}

		getAdminSubCommandManager().getSubCommandList().add(new OpenSub());
		getAdminSubCommandManager().getSubCommandList().add(new IDSub());

		getAdminSubCommandManager().getSubCommandList().add(new PluginSub());
 
		getAdminSubCommandManager().getSubCommandList().add(new BoostSub());
		getAdminSubCommandManager().getSubCommandList().add(new MaxSub());
		getAdminSubCommandManager().getSubCommandList().add(new LevelSub());
		getAdminSubCommandManager().getSubCommandList().add(new ExpSub());
		getAdminSubCommandManager().getSubCommandList().add(new de.warsteiner.jobs.command.admincommand.PointsSub());

		Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loaded Sub-Commands...");
	}

	/**
	 * Method to load all needed Classes
	 */

	public void loadClasses() {

		loc = new PlayerDataFile("locations");
		loc.create();

		chunk = new PlayerDataFile("chunk");
		chunk.create();

		loaded = new ArrayList<>();
		ld = new HashMap<>();
		levels = new LevelAPI(plugin);

		cmdmanager = new SubCommandRegistry();
		api = new JobAPI(plugin);
		gui = new GuiManager(plugin);
		adddongui = new GuiAddonManager(plugin);
		click = new ClickManager(plugin, this.filemanager.getConfig(), this.gui);
		admincmdmanager = new AdminSubCommandRegistry();
		work = new JobWorkManager(plugin, this.api);

		js = new JsonMessage();
		papi = new PlayerAPI(plugin);

		dataapi = new OfflinePlayerAPI();

		i = new ItemAPI();
		skull = new SkullCreatorAPI();
		locapi = new LocationAPI();
		ogui = new GuiOpenManager();

		capi = new PlayerChunkAPI();
		
		ef = new EffectAPI();
		qapi = new DailyQuestsAPI();

		Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loaded Classes...");

		 
	}
	
	public DailyQuestsAPI getDailyQuestsAPI() {
		return qapi;
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

	public static UltimateJobs getPlugin() {
		return plugin;
	}

	public ClickManager getClickManager() {
		return click;
	}

	public de.warsteiner.jobs.manager.GuiManager getGUI() {
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
		Bukkit.getPluginManager().registerEvents(new PlayerExistEvent(), this); 
		Bukkit.getPluginManager().registerEvents(new BlockFireWorkDamage(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerRewardCommandEvent(), this);
		Bukkit.getPluginManager().registerEvents(new JobsInventoryClickEvent(), this); 
		Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loaded basic Events!");
	}

	/**
	 * Method to load the Events for Jobs
	 */

	public void loadEvents() {

		FileConfiguration cfg = getLocalFileManager().getUtilsConfig();

		if (cfg.getBoolean("Actions.Break")) {
			Bukkit.getPluginManager().registerEvents(new JobActionBreak(), this);
		}
		if (cfg.getBoolean("Actions.FarmBreak")) {
			Bukkit.getPluginManager().registerEvents(new JobActionFarm_Break(), this);
		}
		if (cfg.getBoolean("Actions.Place")) {
			Bukkit.getPluginManager().registerEvents(new JobActionPlace(), this);
		}
		if (cfg.getBoolean("Actions.Fish")) {
			Bukkit.getPluginManager().registerEvents(new JobActionFish(), this);
		}
		if (cfg.getBoolean("Actions.Milk")) {
			Bukkit.getPluginManager().registerEvents(new JobActionMilk(), this);
		}
		if (cfg.getBoolean("Actions.KillMob")) {
			Bukkit.getPluginManager().registerEvents(new JobActionKillMob(), this);
		}
		if (cfg.getBoolean("Actions.Shear")) {
			Bukkit.getPluginManager().registerEvents(new JobActionShear(), this);
		}
		if (cfg.getBoolean("Actions.Craft")) {
			Bukkit.getPluginManager().registerEvents(new JobActionCraft(), this);
		}
		if (cfg.getBoolean("Actions.Advancement")) {
			Bukkit.getPluginManager().registerEvents(new JobActionAdvancement(), this);
		}
		if (cfg.getBoolean("Actions.Eat")) {
			Bukkit.getPluginManager().registerEvents(new JobActionEat(), this);
		}
		if (cfg.getBoolean("Actions.Honey")) {
			Bukkit.getPluginManager().registerEvents(new JobActionHoney(), this);
		}
		if (cfg.getBoolean("Actions.Tame")) {
			Bukkit.getPluginManager().registerEvents(new JobActionTame(), this);
		}
		if (cfg.getBoolean("Actions.StripLog")) {
			Bukkit.getPluginManager().registerEvents(new JobActionStripLog(), this);
		}
		if (cfg.getBoolean("Actions.Breed")) {
			Bukkit.getPluginManager().registerEvents(new JobActionBreed(), this);
		}
		if (cfg.getBoolean("Actions.DrinkPotion")) {
			Bukkit.getPluginManager().registerEvents(new JobActionDrinkPotion(), this);
		}
		if (cfg.getBoolean("Actions.CollectBerrys")) {
			Bukkit.getPluginManager().registerEvents(new JobActionsCollectBerrys(), this);
		}
		if (cfg.getBoolean("Actions.KillByBow")) {
			Bukkit.getPluginManager().registerEvents(new JobActionKillByBow(), this);
		}
		if (cfg.getBoolean("Actions.GrowSapling")) {
			Bukkit.getPluginManager().registerEvents(new JobActionGrowSapling(), this);
		}
		if (cfg.getBoolean("Actions.FarmGrow")) {
			Bukkit.getPluginManager().registerEvents(new JobActionFarm_Grow(), this);
		}
		if (cfg.getBoolean("Actions.FindTreasure")) {
			Bukkit.getPluginManager().registerEvents(new JobActionFindATreasure(), this);
		}
		if (cfg.getBoolean("Actions.VillagerTradeBuy")) {
			Bukkit.getPluginManager().registerEvents(new JobActionVillagerTrade_Buy(), this);
		}
		if (cfg.getBoolean("Actions.Smelt")) {
			Bukkit.getPluginManager().registerEvents(new JobActionSmelt(), this);
		}
		if (cfg.getBoolean("Actions.Explore")) {
			Bukkit.getPluginManager().registerEvents(new JobActionExploreChunks(), this);
		}
		if (cfg.getBoolean("Actions.Enchant")) {
			Bukkit.getPluginManager().registerEvents(new JobActionEnchant(), this);
		}
		Bukkit.getPluginManager().registerEvents(new DefaultJobActions(), this);

		if (getPluginManager().isInstalled("MythicMobs")) {
			mm = new MythicMobsManager();
			Bukkit.getPluginManager().registerEvents(new JobActionMMKill(), this);
			Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loaded MythicMobs Support!");
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
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(Economy.class);
		if (economyProvider != null) {
			econ = (Economy) economyProvider.getProvider();
			Bukkit.getConsoleSender().sendMessage(PluginColor.INFO.getPrefix() + "Loaded Vault Support!");
		} else {
			Bukkit.getConsoleSender()
					.sendMessage(PluginColor.ERROR.getPrefix() + "Failed to load vault for Ultimatejobs!");
		}
		return (econ != null);
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

}
