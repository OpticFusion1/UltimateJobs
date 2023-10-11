package optic_fusion1.jobs.manager;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.api.JobAPI;
import optic_fusion1.jobs.command.admin.AdminCommand;
import optic_fusion1.jobs.event.PlayerJoinJobEvent;
import optic_fusion1.jobs.event.PlayerQuitJobEvent;
import optic_fusion1.jobs.util.Language;
import optic_fusion1.jobs.util.PluginColor;
import optic_fusion1.jobs.gui.GUIType;
import optic_fusion1.jobs.gui.UpdateTypes;
import optic_fusion1.jobs.item.ItemAction;
import optic_fusion1.jobs.job.Job;
import optic_fusion1.jobs.job.JobsPlayer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ClickManager {

    private UltimateJobs plugin;
    private JobAPI api;
    private GuiManager gui;

    public ClickManager(UltimateJobs plugin, FileConfiguration fileConfiguration, GuiManager gui) {
        this.plugin = plugin;
        this.gui = gui;
        api = plugin.getAPI();
    }

    public Language isLanguageItem(String display) {

        ArrayList<Language> langs = plugin.getLanguageAPI().getLoadedLanguagesAsArray();

        for (Language lang : langs) {
            String di = plugin.getPluginManager().toHex(lang.getDisplay());
            if (di.equalsIgnoreCase(plugin.getPluginManager().toHex(display))) {
                return lang;
            }
        }
        return null;

    }

    public void executeCustomItem(final Player player, String prf, Job job, String display_of_item, Material item,
            String about) {

        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());

        ArrayList<String> jobs = jb.getCurrentJobs();

        if (plugin.getItemAPI().custom_items.containsKey(prf)) {

            plugin.getItemAPI().custom_items.get(prf).forEach((real) -> {

                String me = prf + "." + real.getNamed() + ".Display";

                String display = jb.getLanguage().getGUIMessage(me);

                if (display != null) {

                    if (display.equalsIgnoreCase(display_of_item)) {

                        if (real.getActions() != null && !real.getActions().isEmpty()) {
                            real.getActions().forEach((action) -> {

                                if (action.contains(";")) {

                                    String[] s = action.split(";");

                                    ItemAction act = null;

                                    try {
                                        act = ItemAction.valueOf(s[0]);
                                    } catch (IllegalArgumentException ex) {
                                        Bukkit.getConsoleSender()
                                                .sendMessage(PluginColor.GUI_RELATED_ERROR.getPrefix() + "Failed to parse " + s[0] + " as Inventory Action.");
                                    }

                                    if (s.length == 2 && act.equals(ItemAction.PLAYER_COMMAND)) {

                                        String command = s[1];

                                        if (job != null) {
                                            player.performCommand(command.replaceAll("<job>", job.getConfigID())
                                                    .replaceAll("<name>", player.getName()));
                                        } else {
                                            player.performCommand(command.replaceAll("<name>", player.getName()));
                                        }
                                    } else if (s.length == 2 && act.equals(ItemAction.CONSOLE_COMMAND)) {

                                        String command = s[1];

                                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

                                        if (job != null) {
                                            Bukkit.dispatchCommand(console, command.replaceAll("<job>", job.getConfigID())
                                                    .replaceAll("<name>", player.getName()));
                                        } else {
                                            Bukkit.dispatchCommand(console, command.replaceAll("<name>", player.getName()));
                                        }

                                    } else if (s.length == 2 && act.equals(ItemAction.GUI)) {

                                        String gui = s[1].toUpperCase();

                                        if (GUIType.valueOf(gui.toUpperCase()) == null) {
                                            player.sendMessage(AdminCommand.prefix + "Error: GUI Type does not exist");
                                            return;
                                        }

                                        GUIType fin = GUIType.valueOf(gui.toUpperCase());

                                        plugin.getGUIOpenManager().openGuiByGuiID(player, fin, player,
                                                job, about, false, null);

                                    }
                                } else {
                                    ItemAction act = null;

                                    try {
                                        act = ItemAction.valueOf(action);
                                    } catch (IllegalArgumentException ex) {

                                    }

                                    if (act != null) {
                                        if (act.equals(ItemAction.CLOSE)) {
                                            new BukkitRunnable() {
                                                public void run() {
                                                    player.closeInventory();

                                                    cancel();
                                                }
                                            }.runTaskLater(plugin, 2);
                                        } else if (act.equals(ItemAction.LEAVE)) {
                                            if (job == null) {
                                                player.sendMessage(AdminCommand.prefix + "Missing Element: Job");
                                                return;
                                            }

                                            if (plugin.getLocalFileManager().getConfig()
                                                    .getBoolean("LeaveJobNeedsToBeConfirmed")) {
                                                plugin.getGUIAddonManager().createLeaveConfirmGUI(player, UpdateTypes.OPEN,
                                                        job);
                                            } else {

                                                if (job.getOptionValue("CannotLeaveJob")) {
                                                    api.playSound("CANNOT_LEAVE_JOB", player);
                                                    if (jb.getLanguage().getMessage("CannotLeaveJobMessage") != null) {
                                                        player.sendMessage(jb.getLanguage().getMessage("CannotLeaveJobMessage")
                                                                .replaceAll("<job>",
                                                                        job.getDisplayOfJob("" + player.getUniqueId())));
                                                    }
                                                } else {
                                                    api.playSound("LEAVE_SINGLE", player);

                                                    updateSalaryOnLeave(player, jb);
                                                    jb.remCurrentJob(job.getConfigID());

                                                    PlayerQuitJobEvent event = new PlayerQuitJobEvent(player, jb, job);

                                                    OptionalJobQuit(event);

                                                    plugin.getGUI().createMainGUIOfJobs(player, UpdateTypes.REOPEN);

                                                    if (plugin.getLocalFileManager().getConfig()
                                                            .getBoolean("SendMessageOnLeave")) {
                                                        player.sendMessage(jb.getLanguage().getMessage("Other.Left_Job")
                                                                .replaceAll("<job>",
                                                                        job.getDisplayOfJob("" + player.getUniqueId())));
                                                    }
                                                }

                                            }
                                        } else if (act.equals(ItemAction.LEAVEALL)) {
                                            if (jobs.size() != 0) {

                                                if (jobs != null) {

                                                    for (Job mine : jb.getCurrentJobsAsObject()) {

                                                        if (!mine.getOptionValue("CannotLeaveJob")) {

                                                            PlayerQuitJobEvent event = new PlayerQuitJobEvent(player, jb, mine);

                                                            plugin.getClickManager().OptionalJobQuit(event);

                                                            jb.remCurrentJob(mine.getConfigID());

                                                        }
                                                    }

                                                }

                                                updateSalaryOnLeave(player, jb);

                                                api.playSound("LEAVE_ALL", player);

                                                player.sendMessage(jb.getLanguage().getMessage("Other.Leave_All"));

                                                gui.UpdateMainInventoryItems(player,
                                                        jb.getLanguage().getGUIMessage("Main_Name"));

                                            } else {
                                                player.sendMessage(jb.getLanguage().getMessage("Other.Already_Left_All")
                                                        .replaceAll("<job>", display));
                                            }
                                        }
                                    }
                                }
                            });
                        }

                    }

                }

            });

        }

    }

    public void updateSalaryOnLeave(Player player, JobsPlayer jb) {
        if (plugin.getLocalFileManager().getConfig().getString("PayMentMode").toUpperCase().equalsIgnoreCase("STORED")) {
            if (plugin.getLocalFileManager().getConfig().getBoolean("ResetAmountOnJobLeave")) {

                plugin.getPlayerAPI().updateSalary(jb.getUUIDAsString(), 0);

                if (plugin.getLocalFileManager().getConfig().getBoolean("SendResetMessage")) {
                    player.sendMessage(jb.getLanguage().getGUIMessage("Withdraw_Reset_Salary_Message"));
                }

            } else if (plugin.getLocalFileManager().getConfig().getBoolean("RemovePercentOnJobLeave")) {

                double amount = plugin.getLocalFileManager().getConfig().getDouble("PercentAmount");

                double sal = jb.getSalary();

                if (sal >= 0) {
                    double oneper = sal / 100;

                    double removed = oneper * amount;

                    if (plugin.getLocalFileManager().getConfig().getBoolean("SendRemovePercentMessage")) {
                        player.sendMessage(jb.getLanguage().getGUIMessage("Withdraw_Remove_Percent_Of_Salary_Message")
                                .replaceAll("<amount>", plugin.getAPI().Format(removed)));

                    }

                    plugin.getPlayerAPI().updateSalary(jb.getUUIDAsString(), sal - removed);
                }
            }
        }
    }

    public void joinJob(Player player, String job, JobsPlayer jb, String name, String dis, Job j) {
        FileConfiguration cfg = plugin.getLocalFileManager().getGUI();

        PlayerJoinJobEvent event = new PlayerJoinJobEvent(player, jb, j);

        if (!event.isCancelled()) {

            OptionalJobJoin(event);

            jb.addCurrentJob(job);

            plugin.getPlayerAPI().updateDateJoinedOfJob(jb.getUUIDAsString(), job,
                    plugin.getPluginManager().getDateTodayFromCal());

            api.playSound("JOB_JOINED", player);

            new BukkitRunnable() {
                public void run() {
                    gui.setCustomitems(player, player.getName(), player.getOpenInventory(), "Main_Custom.",
                            cfg.getStringList("Main_Custom.List"), name, cfg, plugin.getJobCache().get(job));
                    gui.setMainInventoryJobItems(player.getOpenInventory(), player, name);

                    cancel();
                }
            }.runTaskLater(plugin, 1);

            player.sendMessage(plugin.getPluginManager().toHex(jb.getLanguage().getMessage("Other.Joined")
                    .replaceAll("<job>", j.getDisplayOfJob("" + player.getUniqueId()))));
        }

    }

    public void OptionalJobQuit(PlayerQuitJobEvent event) {
        if (event.getJob() != null) {

            Job job = event.getJob();

            if (job.getQuitCommands() != null) {

                Player pl = event.getPlayer();

                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

                List<String> commands = job.getQuitCommands();

                for (String command : commands) {
                    Bukkit.dispatchCommand(console,
                            command.replaceAll("<job>", job.getConfigID()).replaceAll("<name>", pl.getName()));
                }
            }
        }
    }

    public void OptionalJobJoin(PlayerJoinJobEvent event) {
        if (event.getJob() != null) {

            Job job = event.getJob();

            if (job.getJoinCommands() != null) {

                Player pl = event.getPlayer();

                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

                List<String> commands = job.getJoinCommands();

                for (String command : commands) {
                    Bukkit.dispatchCommand(console,
                            command.replaceAll("<job>", job.getConfigID()).replaceAll("<name>", pl.getName()));
                }
            }
        }
    }

    public void executeJobClickEvent(String display, Player player) {
        List<String> jobs = plugin.getLoaded();
        String UUID = "" + player.getUniqueId();

        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        int max = jb.getMaxJobs();

        for (int i = 0; i <= jobs.size() - 1; i++) {
            Job j = plugin.getJobCache().get(jobs.get(i));
            String dis = j.getDisplayOfJob(UUID);
            if (display.equalsIgnoreCase(dis)) {
                String job = j.getConfigID();

                String name = jb.getLanguage().getGUIMessage("Main_Name");
                if (api.canBuyWithoutPermissions(player, j)) {

                    List<String> d = api.canGetJobWithSubOptions(player, j);
                    if (d == null) {

                        if (jb.ownJob(job) == true || api.canByPass(player, j) == true) {

                            if (jb.isInJob(job)) {
                                gui.createSettingsGUI(player, j, UpdateTypes.OPEN);
                                return;
                            } else {

                                if (jb.getCurrentJobs().size() <= max) {
                                    joinJob(player, job, jb, name, dis, j);
                                    return;
                                } else {
                                    player.sendMessage(jb.getLanguage().getMessage("Other.Full").replaceAll("<job>",
                                            j.getDisplayOfJob(UUID)));
                                    return;
                                }

                            }
                        } else {
                            double money = j.getPrice();
                            if (plugin.getEco().getBalance(player) >= money) {

                                if (plugin.getLocalFileManager().getConfig().getBoolean("Jobs.AreYouSureGUIonBuy")) {
                                    gui.createAreYouSureGUI(player, j, UpdateTypes.OPEN);
                                    return;
                                } else {
                                    buy(money, player, jb, j);

                                    if (plugin.getLocalFileManager().getConfig().getBoolean("AutoJoinJobsWhenBought")) {
                                        if (jb.getCurrentJobs().size() <= max) {
                                            joinJob(player, job, jb, name, dis, j);
                                        }
                                    }

                                    return;
                                }

                            } else {
                                player.sendMessage(jb.getLanguage().getMessage("Other.Not_Enough_Money")
                                        .replaceAll("<job>", j.getDisplayOfJob(UUID)));
                                return;
                            }
                        }
                    }
                } else {
                    player.sendMessage(j.getPermMessage(UUID).replaceAll("<job>", j.getDisplayOfJob(UUID)));
                    return;

                }

            }
        }
    }

    public void buy(double money, Player player, JobsPlayer jb, Job job) {
        plugin.getEco().withdrawPlayer(player, money);

        jb.addOwnedJob(job.getConfigID());

        api.playSound("JOB_BOUGHT", player);

        String title = player.getOpenInventory().getTitle();

        if (title.equalsIgnoreCase(jb.getLanguage().getGUIMessage("Main_Name"))) {
            gui.UpdateMainInventoryItems(player, title);
        } else {
            gui.createMainGUIOfJobs(player, UpdateTypes.REOPEN);
        }

        if (job.hasSong()) {

            if (plugin.getPluginManager().isInstalled("NoteBlockAPI")) {

                String path = job.getSong();

                plugin.getNoteBlockManager().playSong(player, job, path);
            }
        }

        player.sendMessage(jb.getLanguage().getMessage("Other.Bought_Job").replaceAll("<job>",
                job.getDisplayOfJob("" + player.getUniqueId())));
    }

    public String isCustomItem(String display, String path, FileConfiguration config, String UUID) {
        List<String> custom_items = config.getStringList(path + ".List");
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
        for (String b : custom_items) {
            String real = jb.getLanguage().getGUIMessage(path + "." + b + ".Display");
            String dis = plugin.getPluginManager().toHex(real.replaceAll("&", "§"));
            if (display.equalsIgnoreCase(dis)) {
                return b;
            }
        }
        return "NOT_FOUND";
    }

}
