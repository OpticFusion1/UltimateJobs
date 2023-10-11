package optic_fusion1.jobs.api;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.manager.FileManager;
import optic_fusion1.jobs.util.PluginColor;
import optic_fusion1.jobs.item.Item;
import optic_fusion1.jobs.item.ItemType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class ItemAPI {

    private UltimateJobs plugin;

    public HashMap<String, Item> items = new HashMap<String, Item>();
    public ArrayList<String> fails = new ArrayList<String>();

    public HashMap<String, List<Item>> custom_items = new HashMap<String, List<Item>>();
    public HashMap<String, Item> other_items = new HashMap<String, Item>();

    public ItemAPI(UltimateJobs plugin) {
        this.plugin = plugin;
    }

    public HashMap<String, Item> getOtherItems() {
        return this.other_items;
    }

    public HashMap<String, Item> getItems() {
        return this.items;
    }

    public Item getItem(String named) {
        return getItems().get(named);
    }

    @SuppressWarnings("deprecation")
    public ItemStack getItemStack(String named, String player, String item) {

        if (getOtherItems().containsKey(named)) {
            return getOtherItems().get(named).getItemStack();
        }

        if (getItems().containsKey(named)) {
            return getItem(named).getItemStack();
        }

        if (!getItems().containsKey(named)) {

            // creating items
            ItemStack it = null;

            if (item != null) {
                if (plugin.getPluginManager().isInstalled("ItemsAdder")) {

                    if (plugin.getItemsAdderManager().checkIfItemStackExist(item) != null) {
                        it = plugin.getItemsAdderManager().checkIfItemStackExist(item);
                    }
                }

                if (item.contains(";")) {
                    String[] split = item.split(";");
                    if (split[1] != null) {
                        String s = split[1];
                        if (split[0].toLowerCase().equalsIgnoreCase("url")) {
                            it = plugin.getSkullCreatorAPI().itemFromUrl(s);
                        } else if (split[0].toLowerCase().equalsIgnoreCase("uuid")) {
                            it = plugin.getSkullCreatorAPI().itemFromUuid(s);
                        }
                        if (split[0].toLowerCase().equalsIgnoreCase("name")) {
                            it = plugin.getSkullCreatorAPI().itemFromName(s.replaceAll("<name>", player));
                        }
                        if (split[0].toLowerCase().equalsIgnoreCase("base64")) {
                            it = plugin.getSkullCreatorAPI().itemFromBase64(s);
                        }
                    }
                } else {

                    try {
                        Material used = Material.valueOf(item.toUpperCase());

                        it = new ItemStack(used, 1);
                    } catch (IllegalArgumentException ex) {
                    }
                }
            }

            return it;
        }
        return null;
    }

    public void loadItems() {

        items.clear();

        FileManager fm = plugin.getLocalFileManager();

        if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Info")) {
            Bukkit.getConsoleSender()
                    .sendMessage(PluginColor.ITEM_RELATED_INFO.getPrefix() + "Checking Plugin Items...");
        }

        fm.getGUI().getStringList("Main_Custom.List").forEach((named) -> {
            checkCustomItems("Main_Custom", named, fm.getGUI());

        });

        fm.getWithdrawConfig().getStringList("Withdraw_Custom.List").forEach((named) -> {
            checkCustomItems("Withdraw_Custom", named, fm.getWithdrawConfig());
        });

        if (fm.getWithdrawConfig() != null) {
            checkOtherItems("Withdraw_Items.NoSalaryToCollect.Material", "Withdraw_Items.NoSalaryToCollect",
                    fm.getWithdrawConfig());
            checkOtherItems("Withdraw_Items.Info.Material", "Withdraw_Items.Info", fm.getWithdrawConfig());
            checkOtherItems("Withdraw_Items.CollectButton.Material", "Withdraw_Items.CollectButton",
                    fm.getWithdrawConfig());
        }

        fm.getWithdrawConfirmConfig().getStringList("ConfirmWithdraw_Custom.List").forEach((named) -> {
            checkCustomItems("ConfirmWithdraw_Custom", named, fm.getWithdrawConfirmConfig());
        });

        if (fm.getWithdrawConfirmConfig() != null) {
            checkOtherItems("ConfirmWithdrawItems.Button_YES.Icon", "ConfirmWithdrawItems.Button_YES",
                    fm.getWithdrawConfirmConfig());
            checkOtherItems("ConfirmWithdrawItems.Button_NO.Icon", "ConfirmWithdrawItems.Button_NO",
                    fm.getWithdrawConfirmConfig());
        }

        fm.getStatsConfig().getStringList("Other_Custom.List").forEach((named) -> {
            checkCustomItems("Other_Custom", named, fm.getStatsConfig());
        });

        fm.getStatsConfig().getStringList("Self_Custom.List").forEach((named) -> {
            checkCustomItems("Self_Custom", named, fm.getStatsConfig());
        });

        fm.getSettings().getStringList("Settings_Custom.List").forEach((named) -> {
            checkCustomItems("Settings_Custom", named, fm.getSettings());
        });
        fm.getRewardsConfig().getStringList("Rewards_Custom.List").forEach((named) -> {
            checkCustomItems("Rewards_Custom", named, fm.getRewardsConfig());
        });

        if (fm.getRewardsConfig() != null) {
            checkOtherItems("PageItems.Previous.Material", "Rewards.Previous", fm.getRewardsConfig());
            checkOtherItems("PageItems.Next.Material", "Rewards.Next", fm.getRewardsConfig());
        }

        fm.getRankingPerJobConfig().getStringList("PerJobRanking_Custom.List").forEach((named) -> {
            checkCustomItems("PerJobRanking_Custom", named, fm.getRankingPerJobConfig());
        });

        if (fm.getRankingPerJobConfig().getStringList("Categories.List").contains("LEVEL")) {
            checkOtherItems("Categories.Level.Material", "Icon.Level", fm.getRankingPerJobConfig());
        }
        if (fm.getRankingPerJobConfig().getStringList("Categories.List").contains("BLOCKS")) {
            checkOtherItems("Categories.Destroyed_Blocks.Material", "Icon.Blocks", fm.getRankingPerJobConfig());
        }
        if (fm.getRankingPerJobConfig().getStringList("Categories.List").contains("TODAY")) {
            checkOtherItems("Categories.Earnings_Today.Material", "Icon.Today", fm.getRankingPerJobConfig());
        }
        if (fm.getRankingPerJobConfig() != null) {
            checkOtherItems("PerJobRanking_Items.NoneFound.Material", "Icon.RankingNotFound",
                    fm.getRankingPerJobConfig());
        }

        fm.getRankingGlobalConfig().getStringList("Global_Custom.List").forEach((named) -> {
            checkCustomItems("Global_Custom", named, fm.getRankingGlobalConfig());
        });

        if (fm.getRankingGlobalConfig() != null) {
            checkOtherItems("Global_RankingItems.NoneFound.Material", "Icon.GlobalRankingNotFound",
                    fm.getRankingGlobalConfig());
        }

        fm.getLevelGUIConfig().getStringList("Levels_Custom.List").forEach((named) -> {
            checkCustomItems("Levels_Custom", named, fm.getLevelGUIConfig());
        });

        if (fm.getLevelGUIConfig() != null) {
            checkOtherItems("Options.Icons.CurrentlyWorkingOn", "Icons.CurrentlyWorkingOn", fm.getLevelGUIConfig());
            checkOtherItems("Options.Icons.Reached", "Icons.Reached", fm.getLevelGUIConfig());
            checkOtherItems("Options.Icons.NotReached", "Icons.NotReached", fm.getLevelGUIConfig());

            checkOtherItems("PageItems.Previous.Material", "Levels.Previous", fm.getLevelGUIConfig());
            checkOtherItems("PageItems.Next.Material", "Levels.Next", fm.getLevelGUIConfig());
        }

        fm.getLeaveConfirmConfig().getStringList("LeaveConfirm_Custom.List").forEach((named) -> {
            checkCustomItems("LeaveConfirm_Custom", named, fm.getLeaveConfirmConfig());
        });

        if (fm.getLeaveConfirmConfig() != null) {
            checkOtherItems("LeaveConfirmItems.Button_YES.Icon", "LeaveConfirmItems.Button_YES",
                    fm.getLeaveConfirmConfig());
            checkOtherItems("LeaveConfirmItems.Button_NO.Icon", "LeaveConfirmItems.Button_NO",
                    fm.getLeaveConfirmConfig());
        }

        fm.getLanguageGUIConfig().getStringList("Custom.List").forEach((named) -> {
            checkCustomItems("Custom", named, fm.getLanguageGUIConfig());
        });

        fm.getHelpSettings().getStringList("Help_Custom.List").forEach((named) -> {
            checkCustomItems("Help_Custom", named, fm.getHelpSettings());
        });

        fm.getEarningsJobConfig().getStringList("Job_Earnings_Custom.List").forEach((named) -> {
            checkCustomItems("Job_Earnings_Custom", named, fm.getEarningsJobConfig());
        });

        if (fm.getEarningsJobConfig() != null) {
            checkOtherItems("Job_Earnings_Items.Icon", "Icon.Job_Earnings_Items", fm.getEarningsJobConfig());
            checkOtherItems("Job_Earnings_Items.NotAnyEarnings.Icon", "Icon.Job_Earnings_ItemsNotFound",
                    fm.getEarningsJobConfig());

            checkOtherItems("PageItems.Previous.Material", "Icon.Job_Earnings_Previous", fm.getEarningsJobConfig());
            checkOtherItems("PageItems.Next.Material", "Icon.Job_Earnings_Next", fm.getEarningsJobConfig());
            checkOtherItems("Job_Earnings_Items.NotAnyEarnings.Icon", "Icon.Job_Earnings_ItemsNotFound",
                    fm.getEarningsJobConfig());
        }

        fm.getEarningsAllConfig().getStringList("All_Earnings_Custom.List").forEach((named) -> {
            checkCustomItems("All_Earnings_Custom", named, fm.getEarningsAllConfig());
        });

        if (fm.getEarningsAllConfig() != null) {
            checkOtherItems("All_Earnings_Items.Icon", "Icon.All_Earnings_Items", fm.getEarningsAllConfig());
            checkOtherItems("All_Earnings_Items.NotAnyEarnings.Icon", "Icon.NotAnyEarningsGlobal",
                    fm.getEarningsAllConfig());

            checkOtherItems("PageItems.Previous.Material", "Icon.GlobalPrevious", fm.getEarningsAllConfig());
            checkOtherItems("PageItems.Next.Material", "Icon.GlobalNext", fm.getEarningsAllConfig());
        }

        fm.getConfirm().getStringList("AreYouSureGUI_Custom.List").forEach((named) -> {
            checkCustomItems("AreYouSureGUI_Custom", named, fm.getConfirm());
        });

        String path1 = "AreYouSureItems.Button_YES.Icon";
        String path2 = "AreYouSureItems.Button_YES";

        if (!fm.getConfirm().contains(path1)) {
            checkOtherItems(path1, path2, fm.getConfirm());
        }

        String path3 = "AreYouSureItems.Button_NO.Icon";
        String path4 = "AreYouSureItems.Button_NO";

        if (!fm.getConfirm().contains(path3)) {
            checkOtherItems(path3, path4, fm.getConfirm());
        }

        plugin.getJobCache().forEach((id, real) -> {

            String cfgid = real.getConfigID();

            if (real.getRawIcon() != null) {

                String mat = real.getRawIcon();

                boolean ignore = false;

                ItemType type = null;

                ItemStack item = null;

                if (plugin.getPluginManager().isInstalled("ItemsAdder")) {

                    if (plugin.getItemsAdderManager().checkIfItemStackExist(mat) != null) {
                        type = ItemType.ITEMSADDER;
                        item = plugin.getItemsAdderManager().checkIfItemStackExist(mat);
                    }
                }

                if (mat.contains(";")) {
                    String[] split = mat.split(";");
                    if (split[1] != null) {
                        String s = split[1];
                        if (split[0].toLowerCase().equalsIgnoreCase("url")) {
                            type = ItemType.URL;
                            item = plugin.getSkullCreatorAPI().itemFromUrl(s);
                        } else if (split[0].toLowerCase().equalsIgnoreCase("uuid")) {
                            type = ItemType.UUID;
                            item = plugin.getSkullCreatorAPI().itemFromUuid(s);
                        }
                        if (split[0].toLowerCase().equalsIgnoreCase("name")) {
                            type = ItemType.NAME;
                            if (s.toLowerCase().equalsIgnoreCase("<name>")) {
                                ignore = true;
                            }
                        }
                        if (split[0].toLowerCase().equalsIgnoreCase("base64")) {
                            type = ItemType.BASE64;
                            item = plugin.getSkullCreatorAPI().itemFromBase64(s);
                        }
                    }
                } else {

                    try {
                        Material used = Material.valueOf(mat.toUpperCase());

                        type = ItemType.NORMAL;
                        item = new ItemStack(used, 1);
                    } catch (IllegalArgumentException ex) {
                    }
                }

                if (!ignore) {
                    if (type == null) {
                        if (plugin.getLocalFileManager().getUtilsConfig()
                                .getBoolean("Plugin.DebugMessagesOnStart.Error")) {
                            Bukkit.getConsoleSender().sendMessage(PluginColor.ITEM_RELATED_ERROR.getPrefix()
                                    + "Failed to create Item " + mat + " for Job " + cfgid + "!");
                        }
                    } else {

                        String pr = cfgid + "_Material";

                        Item it = new Item(pr, cfgid, type, item, null);

                        items.put(pr, it);
                        if (plugin.getLocalFileManager().getUtilsConfig()
                                .getBoolean("Plugin.DebugMessagesOnStart.Info")) {
                            Bukkit.getConsoleSender().sendMessage(PluginColor.ITEM_RELATED_INFO.getPrefix()
                                    + "Created and Saved Item " + mat + "for Job " + cfgid + "!");
                        }
                    }
                } else {
                    if (plugin.getLocalFileManager().getUtilsConfig()
                            .getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
                        Bukkit.getConsoleSender().sendMessage(PluginColor.ITEM_RELATED_WARNING.getPrefix()
                                + "Ignoring Item " + mat + " for Job" + cfgid + "...");
                    }
                }

            }

            real.getLevels().forEach((lvl, rlvl) -> {

                String mat = rlvl.getIcon();

                boolean ignore = false;

                ItemType type = null;

                ItemStack item = null;

                if (mat != null) {
                    if (plugin.getPluginManager().isInstalled("ItemsAdder")) {

                        if (plugin.getItemsAdderManager().checkIfItemStackExist(mat) != null) {
                            type = ItemType.ITEMSADDER;
                            item = plugin.getItemsAdderManager().checkIfItemStackExist(mat);
                        }
                    }

                    if (mat.contains(";")) {
                        String[] split = mat.split(";");
                        if (split[1] != null) {
                            String s = split[1];
                            if (split[0].toLowerCase().equalsIgnoreCase("url")) {
                                type = ItemType.URL;
                                item = plugin.getSkullCreatorAPI().itemFromUrl(s);
                            } else if (split[0].toLowerCase().equalsIgnoreCase("uuid")) {
                                type = ItemType.UUID;
                                item = plugin.getSkullCreatorAPI().itemFromUuid(s);
                            }
                            if (split[0].toLowerCase().equalsIgnoreCase("name")) {
                                type = ItemType.NAME;
                                if (s.toLowerCase().equalsIgnoreCase("<name>")) {
                                    ignore = true;
                                } else {
                                    item = plugin.getSkullCreatorAPI().itemFromName(s);
                                }
                            }
                            if (split[0].toLowerCase().equalsIgnoreCase("base64")) {
                                type = ItemType.BASE64;
                                item = plugin.getSkullCreatorAPI().itemFromBase64(s);
                            }
                        }
                    } else {

                        try {
                            Material used = Material.valueOf(mat.toUpperCase());

                            type = ItemType.NORMAL;
                            item = new ItemStack(used, 1);
                        } catch (IllegalArgumentException ex) {
                        }
                    }

                    if (!ignore) {
                        if (type == null) {
                            if (plugin.getLocalFileManager().getUtilsConfig()
                                    .getBoolean("Plugin.DebugMessagesOnStart.Error")) {
                                Bukkit.getConsoleSender().sendMessage(PluginColor.ITEM_RELATED_ERROR.getPrefix()
                                        + "Failed to create Item " + mat + " for Job " + cfgid + "!");
                            }
                            fails.add("Failed");
                        } else {

                            String pr = cfgid + "_LevelMat_" + rlvl.getLevel();

                            Item it = new Item(pr, cfgid, type, item, null);

                            items.put(pr, it);
                            if (plugin.getLocalFileManager().getUtilsConfig()
                                    .getBoolean("Plugin.DebugMessagesOnStart.Info")) {
                                Bukkit.getConsoleSender().sendMessage(PluginColor.ITEM_RELATED_INFO.getPrefix()
                                        + "Created and Saved Item " + mat + " for Job " + cfgid + "!");
                            }
                        }
                    } else {
                        if (plugin.getLocalFileManager().getUtilsConfig()
                                .getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
                            Bukkit.getConsoleSender().sendMessage(PluginColor.ITEM_RELATED_WARNING.getPrefix()
                                    + "Ignoring Item " + mat + " for Job " + cfgid + "...");
                        }
                    }
                }

            });

            real.getActionList().forEach((action) -> {

                real.getIDsOf(action).forEach((i, realid) -> {

                    String icon = realid.getIcon();

                    if (icon != null) {
                        boolean ignore = false;

                        ItemType type = null;

                        ItemStack item = null;

                        if (plugin.getPluginManager().isInstalled("ItemsAdder")) {

                            if (plugin.getItemsAdderManager().checkIfItemStackExist(icon) != null) {
                                type = ItemType.ITEMSADDER;
                                item = plugin.getItemsAdderManager().checkIfItemStackExist(icon);
                            }
                        }

                        if (icon.contains(";")) {
                            String[] split = icon.split(";");
                            if (split[1] != null) {
                                String s = split[1];
                                if (split[0].toLowerCase().equalsIgnoreCase("url")) {
                                    type = ItemType.URL;
                                    item = plugin.getSkullCreatorAPI().itemFromUrl(s);
                                } else if (split[0].toLowerCase().equalsIgnoreCase("uuid")) {
                                    type = ItemType.UUID;
                                    item = plugin.getSkullCreatorAPI().itemFromUuid(s);
                                }
                                if (split[0].toLowerCase().equalsIgnoreCase("name")) {
                                    type = ItemType.NAME;
                                    if (s.toLowerCase().equalsIgnoreCase("<name>")) {
                                        ignore = true;
                                    } else {
                                        item = plugin.getSkullCreatorAPI().itemFromName(s);
                                    }
                                }
                                if (split[0].toLowerCase().equalsIgnoreCase("base64")) {
                                    type = ItemType.BASE64;
                                    item = plugin.getSkullCreatorAPI().itemFromBase64(s);
                                }
                            }
                        } else {

                            try {
                                Material used = Material.valueOf(icon.toUpperCase());

                                type = ItemType.NORMAL;
                                item = new ItemStack(used, 1);
                            } catch (IllegalArgumentException ex) {
                            }
                        }

                        if (!ignore) {
                            if (type == null) {
                                if (plugin.getLocalFileManager().getUtilsConfig()
                                        .getBoolean("Plugin.DebugMessagesOnStart.Error")) {
                                    Bukkit.getConsoleSender().sendMessage(PluginColor.ITEM_RELATED_ERROR.getPrefix()
                                            + "Failed to create Item " + icon + "for Job " + cfgid + "!");
                                }
                            } else {

                                String pr = cfgid + "_JobItems_" + i;

                                Item it = new Item(pr, cfgid, type, item, null);

                                items.put(pr, it);

                                Bukkit.getConsoleSender().sendMessage(PluginColor.ITEM_RELATED_INFO.getPrefix()
                                        + "Created and Saved Item " + icon + " for Job " + cfgid + "!");
                            }
                        } else {
                            if (plugin.getLocalFileManager().getUtilsConfig()
                                    .getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
                                Bukkit.getConsoleSender().sendMessage(PluginColor.ITEM_RELATED_WARNING.getPrefix()
                                        + "Ignoring Item " + icon + " for Job" + cfgid + "...");
                            }
                        }

                    }

                });

            });

        });

        if (fails.size() == 0) {

            if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Info")) {
                Bukkit.getConsoleSender()
                        .sendMessage(PluginColor.ITEM_RELATED_INFO.getPrefix() + "Successfully loaded Items!");
            }
        } else {
            if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Error")) {
                Bukkit.getConsoleSender().sendMessage(PluginColor.ITEM_RELATED_ERROR.getPrefix()
                        + "Failed to load Items with " + fails.size() + " Issues!");
            }
        }

    }

    public void checkOtherItems(String path, String named, FileConfiguration cfg) {

        if (!cfg.contains(path)) {
            if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Error")) {
                Bukkit.getConsoleSender().sendMessage(
                        PluginColor.ITEM_RELATED_ERROR.getPrefix() + "Failed to get icon for Item " + path + ".");
            }
            fails.add(path);
        }

        String material = cfg.getString(path);

        boolean ignore = false;

        ItemType type = null;

        ItemStack item = null;

        if (plugin.getPluginManager().isInstalled("ItemsAdder")) {

            if (plugin.getItemsAdderManager().checkIfItemStackExist(material) != null) {
                type = ItemType.ITEMSADDER;
                item = plugin.getItemsAdderManager().checkIfItemStackExist(material);
            }
        }

        if (material.contains(";")) {
            String[] split = material.split(";");
            if (split[1] != null) {
                String s = split[1];
                if (split[0].toLowerCase().equalsIgnoreCase("url")) {
                    type = ItemType.URL;
                    item = plugin.getSkullCreatorAPI().itemFromUrl(s);
                } else if (split[0].toLowerCase().equalsIgnoreCase("uuid")) {
                    type = ItemType.UUID;
                    item = plugin.getSkullCreatorAPI().itemFromUuid(s);
                }
                if (split[0].toLowerCase().equalsIgnoreCase("name")) {
                    type = ItemType.NAME;
                    if (s.toLowerCase().equalsIgnoreCase("<name>")) {
                        ignore = true;
                    } else {
                        item = plugin.getSkullCreatorAPI().itemFromName(s);
                    }
                }
                if (split[0].toLowerCase().equalsIgnoreCase("base64")) {
                    type = ItemType.BASE64;
                    item = plugin.getSkullCreatorAPI().itemFromBase64(s);
                }
            }
        } else {

            try {
                Material used = Material.valueOf(material.toUpperCase());

                type = ItemType.NORMAL;
                item = new ItemStack(used, 1);
            } catch (IllegalArgumentException ex) {
            }
        }

        if (!ignore) {
            if (type == null) {
                if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Error")) {
                    Bukkit.getConsoleSender().sendMessage(PluginColor.ITEM_RELATED_ERROR.getPrefix()
                            + "Failed to create Item " + path + "named; " + named + "!");
                }
                fails.add("Failed");
            } else {
                Item it = new Item(path, named, type, item, null);

                other_items.put(named, it);
                if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Info")) {
                    Bukkit.getConsoleSender().sendMessage(
                            PluginColor.ITEM_RELATED_INFO.getPrefix() + "Created and Saved Item" + named + "!");
                }
            }
        }
    }

    public void checkCustomItems(String prefix, String named, FileConfiguration cfg) {
        if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Info")) {
            Bukkit.getConsoleSender()
                    .sendMessage(PluginColor.ITEM_RELATED_INFO.getPrefix() + "Loading Custom Item " + named + "...");
        }
        String actions_path = prefix + "." + named + ".ActionList";

        if (cfg.getStringList(actions_path) == null) {
            if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Error")) {
                Bukkit.getConsoleSender().sendMessage(
                        PluginColor.ITEM_RELATED_ERROR.getPrefix() + "Failed to get ActionList forItem " + named + ".");
            }
            fails.add(actions_path);
        }

        List<String> cfgactions = cfg.getStringList(actions_path);

        String b = prefix + "." + named + ".Material";

        if (!cfg.contains(b)) {
            if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Error")) {
                Bukkit.getConsoleSender().sendMessage(PluginColor.ITEM_RELATED_ERROR.getPrefix()
                        + "Failed to get Material for Item" + prefix + "." + named + ".");
            }
            fails.add(actions_path);
        }

        String material = cfg.getString(prefix + "." + named + ".Material");

        if (material != null) {
            boolean ignore = false;

            ItemType type = null;

            ItemStack item = null;

            if (plugin.getPluginManager().isInstalled("ItemsAdder")) {

                if (plugin.getItemsAdderManager().checkIfItemStackExist(material) != null) {
                    type = ItemType.ITEMSADDER;
                    item = plugin.getItemsAdderManager().checkIfItemStackExist(material);
                }
            }

            if (material.contains(";")) {
                String[] split = material.split(";");
                if (split[1] != null) {
                    String s = split[1];
                    if (split[0].toLowerCase().equalsIgnoreCase("url")) {
                        type = ItemType.URL;
                        item = plugin.getSkullCreatorAPI().itemFromUrl(s);
                    } else if (split[0].toLowerCase().equalsIgnoreCase("uuid")) {
                        type = ItemType.UUID;
                        item = plugin.getSkullCreatorAPI().itemFromUuid(s);
                    }
                    if (split[0].toLowerCase().equalsIgnoreCase("name")) {
                        type = ItemType.NAME;
                        if (s.toLowerCase().equalsIgnoreCase("<name>")) {
                            ignore = true;
                        } else {
                            item = plugin.getSkullCreatorAPI().itemFromName(s);
                        }
                    }
                    if (split[0].toLowerCase().equalsIgnoreCase("base64")) {
                        type = ItemType.BASE64;
                        item = plugin.getSkullCreatorAPI().itemFromBase64(s);
                    }
                }
            } else {

                try {
                    Material used = Material.valueOf(material.toUpperCase());

                    type = ItemType.NORMAL;
                    item = new ItemStack(used, 1);
                } catch (IllegalArgumentException ex) {
                }
            }

            if (!ignore) {
                if (type == null) {
                    if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Error")) {
                        Bukkit.getConsoleSender().sendMessage(PluginColor.ITEM_RELATED_ERROR.getPrefix()
                                + "Failed to create Item " + prefix + "." + named + " MaterialPath; " + material + "!");
                    }
                    fails.add("Failed");
                } else {

                    String pr = prefix + "_" + named;

                    Item it = new Item(pr, named, type, item, cfgactions);

                    items.put(pr, it);

                    List<Item> listed = null;

                    if (!custom_items.containsKey(prefix)) {
                        listed = new ArrayList<Item>();
                    } else {
                        listed = custom_items.get(prefix);
                    }

                    listed.add(it);

                    custom_items.put(prefix, listed);
                    if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Info")) {
                        Bukkit.getConsoleSender().sendMessage(
                                PluginColor.ITEM_RELATED_INFO.getPrefix() + "Created and Saved Item" + named + "!");
                    }
                }
            } else {
                if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
                    Bukkit.getConsoleSender().sendMessage(
                            PluginColor.ITEM_RELATED_WARNING.getPrefix() + "Ignoring Item " + named + "...");
                }
            }
        }

    }

}
