package optic_fusion1.jobs.listener;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.event.PlayerLanguageChangeEvent;
import optic_fusion1.jobs.event.PlayerQuitJobEvent;
import optic_fusion1.jobs.event.PlayerWithdrawMoneyEvent;
import optic_fusion1.jobs.util.Language;
import optic_fusion1.jobs.util.PluginColor;
import optic_fusion1.jobs.gui.UpdateTypes;
import optic_fusion1.jobs.job.Job;
import optic_fusion1.jobs.job.JobsPlayer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class JobsInventoryClickEvent implements Listener {

    private UltimateJobs plugin;

    public JobsInventoryClickEvent(UltimateJobs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }

        if (e.getView().getTitle() == null) {
            return;
        }

        if (e.getCurrentItem().getItemMeta() == null) {
            return;
        }

        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        if (!plugin.getPlayerAPI().existInCacheByUUID("" + e.getWhoClicked().getUniqueId())) {
            Bukkit.getConsoleSender().sendMessage(
                    PluginColor.ERROR.getPrefix() + "Failed to get JobsPlayer for " + e.getWhoClicked().getName() + "...");
            return;
        }

        Player p = (Player) e.getWhoClicked();
        String UUID = "" + p.getUniqueId();

        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);

        String display = plugin.getPluginManager()
                .toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));

        Material item = e.getCurrentItem().getType();;

        if (plugin.getGUIOpenManager().isConfirmGUI(p, e.getView().getTitle()) != null) {
            Job job = plugin.getGUIOpenManager().isConfirmGUI(p, e.getView().getTitle());
            plugin.getClickManager().executeCustomItem(p, "AreYouSureGUI_Custom", job, display, item, null);

            String name_yes = jb.getLanguage().getGUIMessage("AreYouSureItems.Button_YES.Display");
            String name_no = jb.getLanguage().getGUIMessage("AreYouSureItems.Button_NO.Display");

            String yes = plugin.getPluginManager().toHex(name_yes).replaceAll("<job>", job.getDisplayOfJob(UUID))
                    .replaceAll("&", "§");
            String no = plugin.getPluginManager().toHex(name_no).replaceAll("<job>", job.getDisplayOfJob(UUID))
                    .replaceAll("&", "§");

            if (display.equalsIgnoreCase(yes)) {

                double money = job.getPrice();

                plugin.getClickManager().buy(money, p, jb, job);
            } else if (display.equalsIgnoreCase(no)) {
                plugin.getGUI().createMainGUIOfJobs(p, UpdateTypes.REOPEN);
                plugin.getAPI().playSound("REOPEN_MAIN_GUI", p);
            }

            e.setCancelled(true);
            return;
        } else if (plugin.getGUIOpenManager().isLanguageOpend(p, e.getView().getTitle()) != null) {

            plugin.getClickManager().executeCustomItem(p, "Custom", null, display, item, null);

            if (plugin.getClickManager().isLanguageItem(display) != null) {
                Language lang = plugin.getClickManager().isLanguageItem(display);

                if (lang.getName().equalsIgnoreCase(
                        jb.getLanguage().getName())) {
                    p.sendMessage(
                            jb.getLanguage().getMessage("LanguageChoosenMessage"));
                    plugin.getAPI().playSound("LANGUAGE_ALREADY", p);
                } else {

                    Language old = jb.getLanguage();
                    Language newl = lang;

                    new PlayerLanguageChangeEvent(p, jb, p.getUniqueId(), old, newl);

                    if (plugin.getPlayerAPI().getRealJobPlayer(jb.getUUIDAsString()) != null) {

                        jb.updateLocalLanguage(lang);
                    }

                    plugin.getPlayerAPI().updateSettingData(jb.getUUIDAsString(), "LANG", lang.getName());

                    p.sendMessage(
                            jb.getLanguage().getMessage("LanguageChangedMessage")
                                    .replaceAll("<lang>", lang.getID().toLowerCase()));
                    plugin.getAPI().playSound("LANGUAGE_UPDATED", p);
                    plugin.getGUI().openLanguageMenu(p, UpdateTypes.REOPEN);
                }
            }

            e.setCancelled(true);
            return;
        } else if (plugin.getGUIOpenManager().isHelpOpend(p, e.getView().getTitle()) != null) {
            plugin.getClickManager().executeCustomItem(p, "Help_Custom", null, display, item, null);
            e.setCancelled(true);
            return;
        } else if (plugin.getGUIOpenManager().isEarningsALL(p, e.getView().getTitle()) != null) {

            FileConfiguration cfg = plugin.getLocalFileManager().getEarningsAllConfig();

            plugin.getClickManager().executeCustomItem(p, "All_Earnings_Custom", null, display, item, null);

            String next = jb.getLanguage().getGUIMessage("All_Earnings_Custom.Next.Display");
            String pre = jb.getLanguage().getGUIMessage("All_Earnings_Custom.Previous.Display");

            int page = 1;

            if (plugin.getPlayerAPI().existSettingData("" + UUID, "EARNINGS_ALL")) {
                page = plugin.getPlayerAPI().getPageData("" + UUID, "EARNINGS_ALL");
            }

            ArrayList<String> l2 = plugin.getGUIAddonManager().getAmountToDisplay(cfg, p, page);

            if (display.equalsIgnoreCase(plugin.getPluginManager().toHex(next).replaceAll("&", "§"))) {
                int d = cfg.getStringList("All_Earnings_Slots").size();
                int perpage = d + 1;
                int cl = page * perpage + 1;

                if (l2.size() >= cl) {
                    plugin.getPlayerAPI().addOnePage("" + p.getUniqueId(), "EARNINGS_ALL");
                    plugin.getGUIAddonManager().createEarningsGUI_ALL_Jobs(p, UpdateTypes.REOPEN);
                    plugin.getAPI().playSound("EARNINGS_ALL_PAGE_LEVELS", p);

                } else {
                    p.sendMessage(jb.getLanguage().getGUIMessage("Job_Earnings_Custom.Next.Message_NotFound"));
                    plugin.getAPI().playSound("EARNINGS_ALL_NO_NEXT", p);
                }

            } else if (display.equalsIgnoreCase(plugin.getPluginManager().toHex(pre).replaceAll("&", "§"))) {
                if (page == 1) {

                    String mode = cfg.getString("PageItems.WhatHappensWhenFirstPageAlreardyReached").toUpperCase();

                    if (mode.equalsIgnoreCase("MESSAGE")) {
                        p.sendMessage(jb.getLanguage().getGUIMessage("Job_Earnings_Custom.Previous.Message_NotFound"));
                    } else if (mode.equalsIgnoreCase("MAINGUI")) {
                        plugin.getGUI().createMainGUIOfJobs(p, UpdateTypes.REOPEN);
                    } else if (mode.equalsIgnoreCase("COMMAND")) {

                        String c = cfg.getString("PageItems.WhatHappensWhenFirstPage_Command");

                        p.performCommand(c);

                    }

                    plugin.getAPI().playSound("EARNINGS_ALL_FIRST_ALREADY", p);
                } else {
                    plugin.getPlayerAPI().removeOnePage("" + p.getUniqueId(), "EARNINGS_ALL");
                    plugin.getGUIAddonManager().createEarningsGUI_ALL_Jobs(p, UpdateTypes.REOPEN);
                    plugin.getAPI().playSound("LAST_EARNINGS_ALL", p);
                }
            }

            e.setCancelled(true);
            return;
        } else if (plugin.getGUIOpenManager().isEarningsAboutJob(p, e.getView().getTitle()) != null) {
            FileConfiguration cfg = plugin.getLocalFileManager().getEarningsJobConfig();
            Job found = plugin.getGUIOpenManager().isEarningsAboutJob(p, e.getView().getTitle());

            plugin.getClickManager().executeCustomItem(p, "Job_Earnings_Custom", found, display, item, null);

            String next = jb.getLanguage().getGUIMessage("Job_Earnings_Custom.Next.Display");
            String pre = jb.getLanguage().getGUIMessage("Job_Earnings_Custom.Previous.Display");

            int page = 1;

            if (plugin.getPlayerAPI().existSettingData("" + UUID, "EARNINGS_" + found.getConfigID())) {
                page = plugin.getPlayerAPI().getPageData("" + UUID, "EARNINGS_" + found.getConfigID());
            }

            ArrayList<String> l2 = plugin.getGUIAddonManager().getAmountToDisplay(cfg, p, page);

            if (display.equalsIgnoreCase(plugin.getPluginManager().toHex(next).replaceAll("&", "§"))) {
                int d = cfg.getStringList("Job_Earnings_Slots").size();
                int perpage = d + 1;
                int cl = page * perpage + 1;

                if (l2.size() >= cl) {
                    plugin.getPlayerAPI().addOnePage("" + p.getUniqueId(), "EARNINGS_" + found.getConfigID());
                    plugin.getGUIAddonManager().createEarningsGUI_Single_Job(p, UpdateTypes.REOPEN, found);
                    plugin.getAPI().playSound("EARNINGS_JOB_PAGE_LEVELS", p);

                } else {
                    p.sendMessage(jb.getLanguage().getGUIMessage("Job_Earnings_Custom.Next.Message_NotFound"));
                    plugin.getAPI().playSound("EARNINGS_JOB_NO_NEXT", p);
                }

            } else if (display.equalsIgnoreCase(plugin.getPluginManager().toHex(pre).replaceAll("&", "§"))) {
                if (page == 1) {

                    String mode = cfg.getString("PageItems.WhatHappensWhenFirstPageAlreardyReached").toUpperCase();

                    if (mode.equalsIgnoreCase("MESSAGE")) {
                        p.sendMessage(jb.getLanguage().getGUIMessage("Job_Earnings_Custom.Previous.Message_NotFound"));
                    } else if (mode.equalsIgnoreCase("MAINGUI")) {
                        plugin.getGUI().createMainGUIOfJobs(p, UpdateTypes.REOPEN);
                    } else if (mode.equalsIgnoreCase("COMMAND")) {

                        String c = cfg.getString("PageItems.WhatHappensWhenFirstPage_Command");

                        p.performCommand(c);

                    }

                    plugin.getAPI().playSound("EARNINGS_JOB_FIRST_ALREADY", p);
                } else {
                    plugin.getPlayerAPI().removeOnePage("" + p.getUniqueId(),
                            "EARNINGS_" + found.getConfigID());
                    plugin.getGUIAddonManager().createEarningsGUI_Single_Job(p, UpdateTypes.REOPEN, found);
                    plugin.getAPI().playSound("LAST_EARNINGS_ALL", p);
                }
            }

            e.setCancelled(true);
            return;
        } else if (plugin.getGUIOpenManager().isMainOpend(p, e.getView().getTitle()) != null) {

            plugin.getClickManager().executeCustomItem(p, "Main_Custom", null, display, item, null);

            plugin.getClickManager().executeJobClickEvent(display, p);

            e.setCancelled(true);
            return;
        } else if (plugin.getGUIOpenManager().isSettingsMenu(p, e.getView().getTitle()) != null) {

            Job job = plugin.getGUIOpenManager().isSettingsMenu(p, e.getView().getTitle());

            plugin.getClickManager().executeCustomItem(p, "Settings_Custom", job, display, item, null);

            e.setCancelled(true);

        } else if (plugin.getGUIOpenManager().isRewardsMenu(p, e.getView().getTitle()) != null) {

            FileConfiguration cfg = plugin.getLocalFileManager().getRewardsConfig();

            Job j = plugin.getGUIOpenManager().isRewardsMenu(p, e.getView().getTitle());

            plugin.getClickManager().executeCustomItem(p, "Rewards_Custom", j, display, item, null);

            String next = jb.getLanguage().getGUIMessage("Rewards_Custom.Next.Display");
            String pre = jb.getLanguage().getGUIMessage("Rewards_Custom.Previous.Display");

            int page = 1;

            if (plugin.getPlayerAPI().existSettingData("" + UUID, "REWARDS_" + j.getConfigID())) {
                page = plugin.getPlayerAPI().getPageData("" + UUID, "REWARDS_" + j.getConfigID());
            }

            if (display.equalsIgnoreCase(plugin.getPluginManager().toHex(next).replaceAll("&", "§"))) {
                int d = cfg.getStringList("Rewards_Slots").size();
                int perpage = d;
                int cl = page * perpage + 1;

                if (j.getAllNotRealIDSFromActionsAsArray().size() >= cl) {
                    plugin.getPlayerAPI().addOnePage("" + p.getUniqueId(), "REWARDS_" + j.getConfigID());
                    plugin.getGUIAddonManager().createRewardsGUI(p, UpdateTypes.REOPEN, j);
                    plugin.getAPI().playSound("NEW_PAGE_REWARDS", p);

                } else {
                    p.sendMessage(jb.getLanguage().getGUIMessage("Rewards_Custom.Next.Message_NotFound"));
                    plugin.getAPI().playSound("REWARDS_NO_NEXT", p);
                }

            } else if (display.equalsIgnoreCase(plugin.getPluginManager().toHex(pre).replaceAll("&", "§"))) {
                if (page == 1) {

                    String mode = cfg.getString("PageItems.WhatHappensWhenFirstPageAlreardyReached").toUpperCase();

                    if (mode.equalsIgnoreCase("MESSAGE")) {
                        p.sendMessage(jb.getLanguage().getGUIMessage("Rewards_Custom.Previous.Message_NotFound"));
                    } else if (mode.equalsIgnoreCase("MAINGUI")) {
                        plugin.getGUI().createMainGUIOfJobs(p, UpdateTypes.REOPEN);
                    } else if (mode.equalsIgnoreCase("COMMAND")) {

                        String c = cfg.getString("PageItems.WhatHappensWhenFirstPage_Command");

                        p.performCommand(c);

                    }

                    plugin.getAPI().playSound("REWARDS_FIRST_ALREADY", p);
                } else {
                    plugin.getPlayerAPI().removeOnePage("" + p.getUniqueId(), "REWARDS_" + j.getConfigID());
                    plugin.getGUIAddonManager().createRewardsGUI(p, UpdateTypes.REOPEN, j);
                    plugin.getAPI().playSound("LAST_PAGE_REWARDS", p);
                }
            }

            e.setCancelled(true);
            return;
        } else if (plugin.getGUIOpenManager().isStatsMenuOpendSelf(p, e.getView().getTitle()) != null) {

            plugin.getClickManager().executeCustomItem(p, "Self_Custom", null, display, item, null);

            e.setCancelled(true);
            return;
        } else if (plugin.getGUIOpenManager().isStatsMenuOpendAboutPlayer(p, e.getView().getTitle()) != null) {

            plugin.getClickManager().executeCustomItem(p, "Other_Custom", null, display, item, plugin.getGUI().getGUIsDetails().get(jb.getUUIDAsString()));

            e.setCancelled(true);
            return;
        } else if (plugin.getGUIOpenManager().isWithdrawConfirmMenu(p, e.getView().getTitle()) != null) {

            plugin.getClickManager().executeCustomItem(p, "ConfirmWithdraw_Custom", null, display, item, null);

            String dis_1 = plugin.getPluginManager()
                    .toHex(jb.getLanguage().getGUIMessage("ConfirmWithdraw.Button_YES.Display"))
                    .replaceAll("&", "§");

            String dis_2 = plugin.getPluginManager()
                    .toHex(jb.getLanguage().getGUIMessage("ConfirmWithdraw.Button_NO.Display"))
                    .replaceAll("&", "§");

            if (display.equalsIgnoreCase(dis_1)) {

                PlayerWithdrawMoneyEvent event = new PlayerWithdrawMoneyEvent(p, jb);

                if (!event.isCancelled()) {

                    double d = jb.getSalary();

                    plugin.getEco().depositPlayer(p, d);

                    if (plugin.getLocalFileManager().getConfig().getBoolean("NeedToConfirmOnWithdraw")) {

                        int days = plugin.getLocalFileManager().getConfig().getInt("WithdrawCooldownAmount");

                        DateFormat format = new SimpleDateFormat(plugin.getLocalFileManager().getConfig().getString("Date"));
                        Date data = new Date();

                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(data);

                        c1.add(Calendar.DATE, days);

                        Date newdate = c1.getTime();

                        String ddddddddd = "" + format.format(newdate);

                        plugin.getPlayerAPI().updateSalaryDate(jb.getUUIDAsString(), ddddddddd);
                    }

                    plugin.getPlayerAPI().updateSalary(jb.getUUIDAsString(), 0);

                    if (plugin.getLocalFileManager().getConfig().getBoolean("ReOpenMenuWhenSuccess")) {
                        plugin.getGUIAddonManager().createWithdrawMenu(p, UpdateTypes.REOPEN);
                    } else {
                        p.closeInventory();
                    }

                    if (plugin.getLocalFileManager().getConfig().getBoolean("SendMessageOnSuccess")) {
                        p.sendMessage(jb.getLanguage().getGUIMessage("Withdraw_Custom.CollectButton.WithdrawMessage")
                                .replaceAll("<amount>", plugin.getAPI().Format(d)));
                    }

                    plugin.getAPI().playSound("WITHDRAW_SUCCESS", p);
                }
            } else if (display.equalsIgnoreCase(dis_2)) {
                plugin.getGUIAddonManager().createWithdrawMenu(p, UpdateTypes.REOPEN);
                plugin.getAPI().playSound("WITHDRAW_CANCEL_PROGRESS", p);
            }

            e.setCancelled(true);
            return;
        } else if (plugin.getGUIOpenManager().isWithdrawMenu(p, e.getView().getTitle()) != null) {

            plugin.getClickManager().executeCustomItem(p, "Withdraw_Custom", null, display, item, null);

            String dis1 = jb.getLanguage().getGUIMessage("Withdraw_Custom.CollectButton.Display");

            if (display.equalsIgnoreCase(plugin.getPluginManager().toHex(dis1))) {

                if (plugin.getAPI().canWithdrawMoney(p, jb)) {

                    if (plugin.getLocalFileManager().getConfig().getBoolean("NeedToConfirmOnWithdraw")) {
                        plugin.getGUIAddonManager().createWithdrawConfigGUI(p, UpdateTypes.OPEN);
                    } else {

                        PlayerWithdrawMoneyEvent event = new PlayerWithdrawMoneyEvent(p, jb);

                        if (!event.isCancelled()) {

                            double d = jb.getSalary();

                            plugin.getEco().depositPlayer(p, d);

                            if (plugin.getLocalFileManager().getConfig().getBoolean("NeedToConfirmOnWithdraw")) {

                                int days = plugin.getLocalFileManager().getConfig().getInt("WithdrawCooldownAmount");

                                DateFormat format = new SimpleDateFormat(
                                        plugin.getLocalFileManager().getConfig().getString("Date"));
                                Date data = new Date();

                                Calendar c1 = Calendar.getInstance();
                                c1.setTime(data);

                                c1.add(Calendar.DATE, days);

                                Date newdate = c1.getTime();

                                String ddddddddd = "" + format.format(newdate);

                                plugin.getPlayerAPI().updateSalaryDate(jb.getUUIDAsString(), ddddddddd);
                            }

                            plugin.getPlayerAPI().updateSalary(jb.getUUIDAsString(), 0);

                            if (plugin.getLocalFileManager().getConfig().getBoolean("ReOpenMenuWhenSuccess")) {
                                plugin.getGUIAddonManager().createWithdrawMenu(p, UpdateTypes.REOPEN);
                            } else {
                                p.closeInventory();
                            }

                            if (plugin.getLocalFileManager().getConfig().getBoolean("SendMessageOnSuccess")) {
                                p.sendMessage(
                                        jb.getLanguage().getGUIMessage("Withdraw_Custom.CollectButton.WithdrawMessage")
                                                .replaceAll("<amount>", plugin.getAPI().Format(d)));
                            }

                            plugin.getAPI().playSound("WITHDRAW_SUCCESS", p);
                        }
                    }

                } else {
                    plugin.getAPI().playSound("WITHDRAW_REFUSED", p);
                    p.sendMessage(jb.getLanguage().getGUIMessage("Withdraw_Custom.CollectButton.CantMessage"));
                }

            }

            e.setCancelled(true);
            return;
        } else if (plugin.getGUIOpenManager().isGlobalRankingMenu(p, e.getView().getTitle()) != null) {

            plugin.getClickManager().executeCustomItem(p, "Global_Custom", null, display, item, null);

            e.setCancelled(true);
            return;
        } else if (plugin.getGUIOpenManager().isRankingJobMenu(p, e.getView().getTitle()) != null) {

            Job j = plugin.getGUIOpenManager().isRankingJobMenu(p, e.getView().getTitle());

            plugin.getClickManager().executeCustomItem(p, "PerJobRanking_Custom", j, display, item, null);

            FileConfiguration cfg2 = plugin.getLocalFileManager().getRankingPerJobConfig();

            if (cfg2.getBoolean("Categories.EnabledCategorieItem")) {

                int slot = cfg2.getInt("Categories.CatSlot");

                if (e.getSlot() == slot) {

                    String current = plugin.getPlayerAPI().getSettingData("" + p.getUniqueId(), "RANKING");

                    String next = null;

                    List<String> d = cfg2.getStringList("Categories.List");

                    for (int i = 0; i != d.size(); i++) {

                        String cr = d.get(i).toUpperCase();

                        if (cr.equalsIgnoreCase(current.toUpperCase())) {
                            int now = i;

                            int nx = now + 1;

                            if (nx <= d.size() - 1) {
                                next = d.get(nx);
                            } else {
                                next = d.get(0);
                            }

                        }

                    }

                    if (next != null) {
                        plugin.getPlayerAPI().updateSettingData("" + p.getUniqueId(), "RANKING", next.toUpperCase());

                        plugin.getAPI().playSound("UPDATED_RANKING_JOB", p);

                        plugin.getGUIAddonManager().updateJobRankingGUI(p, e.getView().getTitle(), jb, j);
                    }

                }

            }

            e.setCancelled(true);
            return;
        } else if (plugin.getGUIOpenManager().isLevelsMenu(p, e.getView().getTitle()) != null) {

            FileConfiguration cfg = plugin.getLocalFileManager().getLevelGUIConfig();

            Job j = plugin.getGUIOpenManager().isLevelsMenu(p, e.getView().getTitle());

            plugin.getClickManager().executeCustomItem(p, "Levels_Custom", j, display, item, null);

            String next = jb.getLanguage().getGUIMessage("Levels_Custom.Next.Display");
            String pre = jb.getLanguage().getGUIMessage("Levels_Custom.Previous.Display");

            int page = 1;

            if (plugin.getPlayerAPI().existSettingData("" + UUID, "LEVELS_" + j.getConfigID())) {
                page = plugin.getPlayerAPI().getPageData("" + UUID, "LEVELS_" + j.getConfigID());
            }

            if (display.equalsIgnoreCase(plugin.getPluginManager().toHex(next).replaceAll("&", "§"))) {
                int d = cfg.getStringList("Level_Slots").size();
                int perpage = d + 1;
                int cl = page * perpage + 1;

                if (j.getLevels().size() >= cl) {
                    plugin.getPlayerAPI().addOnePage("" + p.getUniqueId(), "LEVELS_" + j.getConfigID());
                    plugin.getGUIAddonManager().createLevelsGUI(p, UpdateTypes.REOPEN, j);
                    plugin.getAPI().playSound("NEW_PAGE_LEVELS", p);

                } else {
                    p.sendMessage(jb.getLanguage().getGUIMessage("Levels_Custom.Next.Message_NotFound"));
                    plugin.getAPI().playSound("LEVELS_NO_NEXT", p);
                }

            } else if (display.equalsIgnoreCase(plugin.getPluginManager().toHex(pre).replaceAll("&", "§"))) {
                if (page == 1) {

                    String mode = cfg.getString("PageItems.WhatHappensWhenFirstPageAlreardyReached").toUpperCase();

                    if (mode.equalsIgnoreCase("MESSAGE")) {
                        p.sendMessage(jb.getLanguage().getGUIMessage("Levels_Custom.Previous.Message_NotFound"));
                    } else if (mode.equalsIgnoreCase("MAINGUI")) {
                        plugin.getGUI().createMainGUIOfJobs(p, UpdateTypes.REOPEN);
                    } else if (mode.equalsIgnoreCase("COMMAND")) {

                        String c = cfg.getString("PageItems.WhatHappensWhenFirstPage_Command");

                        p.performCommand(c);

                    }

                    plugin.getAPI().playSound("LEVELS_FIRST_ALREADY", p);
                } else {
                    plugin.getPlayerAPI().removeOnePage("" + p.getUniqueId(), "LEVELS_" + j.getConfigID());
                    plugin.getGUIAddonManager().createLevelsGUI(p, UpdateTypes.REOPEN, j);
                    plugin.getAPI().playSound("LAST_PAGE_LEVELS", p);
                }
            }

            e.setCancelled(true);
            return;
        } else if (plugin.getGUIOpenManager().isLeaveConfirmGUI(p, e.getView().getTitle()) != null) {

            Job job = plugin.getGUIOpenManager().isLeaveConfirmGUI(p, e.getView().getTitle());

            plugin.getClickManager().executeCustomItem(p, "LeaveConfirm_Custom", job, display, item, null);

            String dis_1 = plugin.getPluginManager()
                    .toHex(jb.getLanguage().getGUIMessage("LeaveConfirm.Button_YES.Display"))
                    .replaceAll("<job>", job.getDisplayOfJob(jb.getUUIDAsString())).replaceAll("&", "§");

            String dis_2 = plugin.getPluginManager()
                    .toHex(jb.getLanguage().getGUIMessage("LeaveConfirm.Button_NO.Display"))
                    .replaceAll("<job>", job.getDisplayOfJob(jb.getUUIDAsString())).replaceAll("&", "§");

            if (display.equalsIgnoreCase(dis_1)) {

                if (job.getOptionValue("CannotLeaveJob")) {
                    plugin.getAPI().playSound("CANNOT_LEAVE_JOB", p);
                    if (jb.getLanguage().getMessage("CannotLeaveJobMessage") != null) {
                        p.sendMessage(jb.getLanguage().getMessage("CannotLeaveJobMessage").replaceAll("<job>",
                                job.getDisplayOfJob("" + p.getUniqueId())));
                    }
                } else {
                    plugin.getAPI().playSound("LEAVE_SINGLE", p);
                    plugin.getClickManager().updateSalaryOnLeave(p, jb);

                    PlayerQuitJobEvent event = new PlayerQuitJobEvent(p, jb, job);

                    plugin.getClickManager().OptionalJobQuit(event);

                    jb.remCurrentJob(job.getConfigID());

                    plugin.getGUI().createMainGUIOfJobs(p, UpdateTypes.REOPEN);

                    if (plugin.getLocalFileManager().getConfig().getBoolean("SendMessageOnLeave")) {
                        p.sendMessage(jb.getLanguage().getMessage("Other.Left_Job").replaceAll("<job>",
                                job.getDisplayOfJob("" + p.getUniqueId())));
                    }

                }

            } else if (display.equalsIgnoreCase(dis_2)) {
                plugin.getGUI().createSettingsGUI(p, job, UpdateTypes.REOPEN);
                plugin.getAPI().playSound("LEAVE_JOB_CANCEL_PROGRESS", p);
            }

            e.setCancelled(true);
            return;
        }

    }
}
