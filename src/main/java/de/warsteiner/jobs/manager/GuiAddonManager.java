package de.warsteiner.jobs.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.utils.objects.guis.GUIType;
import de.warsteiner.jobs.utils.objects.guis.UpdateTypes;
import de.warsteiner.jobs.utils.objects.jobs.Job;
import de.warsteiner.jobs.utils.objects.jobs.JobAction;
import de.warsteiner.jobs.utils.objects.jobs.JobStats;
import de.warsteiner.jobs.utils.objects.jobs.JobsPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.upperlevel.spigot.book.BookUtil;

public class GuiAddonManager {

	private UltimateJobs plugin;
	private JobAPI api = UltimateJobs.getPlugin().getAPI();

	public GuiAddonManager(UltimateJobs plugin) {
		this.plugin = plugin;
	}
 
	public void createJobRankingGUI(Player player, UpdateTypes t, Job job) {
		FileConfiguration cfg = plugin.getLocalFileManager().getRankingPerJobConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);

		int size = cfg.getInt("Size");

		String name = sp.getLanguage().getGUIMessage("PerJobRanking_Name").replaceAll("<job>",
				job.getDisplayOfJob(UUID));

		plugin.getGUI().openInventory(player, size, name, GUIType.JOB_RANKING, null, job);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_RANKING_JOB", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		new BukkitRunnable() {

			@Override
			public void run() {

				plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("PerJobRanking_Place"), name, job);
				plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "PerJobRanking_Custom.",
						cfg.getStringList("PerJobRanking_Custom.List"), name, cfg, null);
				setJobRankingItems(inv_view, cfg, player, job);

				cancel();
			}
		}.runTaskAsynchronously(plugin);
	}

	public void updateJobRankingGUI(Player player, String name, JobsPlayer jb, Job job) {
		FileConfiguration cfg = plugin.getLocalFileManager().getRankingPerJobConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				new BukkitRunnable() {

					@Override
					public void run() {
						plugin.getGUI().setPlaceHolders(player, inv, cfg.getStringList("PerJobRanking_Place"), name, job);
						plugin.getGUI().setCustomitems(player, player.getName(), inv, "PerJobRanking_Custom.",
								cfg.getStringList("PerJobRanking_Custom.List"), name, cfg, null);
						setJobRankingItems(inv, cfg, player, job);

						cancel();
					}
				}.runTaskAsynchronously(plugin);
			}
		}.runTaskLater(plugin, 2);
	}

	public void setJobRankingItems(InventoryView inv, FileConfiguration cf, Player pl, Job job) {

		String MyUUID = "" + pl.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(MyUUID);

		if (!plugin.getPlayerAPI().existSettingData(MyUUID, "RANKING")) {
			plugin.getPlayerAPI().createSettingData(MyUUID, "RANKING",
					cf.getString("Categories.PlayerDefaultCat").toUpperCase());
		}

		String current = plugin.getPlayerAPI().getSettingData(MyUUID, "RANKING");

		if (inv != null) {

			String icon = null;
			String display = null;
			List<String> lore = null;
			String cldt = null;

			int slot = cf.getInt("Categories.CatSlot");

			inv.setItem(slot, null);

			if (current.equalsIgnoreCase("TODAY")) {

				icon = cf.getString("Categories.Earnings_Today.Material");
				display = sp.getLanguage().getGUIMessage("RankingCategories.Today.Display");
				lore = sp.getLanguage().getGUIList("RankingCategories.Today.Lore");
				cldt = "Icon.Today";

			} else if (current.equalsIgnoreCase("BLOCKS")) {

				icon = cf.getString("Categories.Destroyed_Blocks.Material");
				display = sp.getLanguage().getGUIMessage("RankingCategories.Blocks.Display");
				lore = sp.getLanguage().getGUIList("RankingCategories.Blocks.Lore");
				cldt = "Icon.Blocks";

			} else if (current.equalsIgnoreCase("LEVEL")) {

				icon = cf.getString("Categories.Level.Material");
				display = sp.getLanguage().getGUIMessage("RankingCategories.Level.Display");
				lore = sp.getLanguage().getGUIList("RankingCategories.Level.Lore");
				cldt = "Icon.Level";

			}

			if (icon != null) {

				if (cf.getBoolean("Categories.EnabledCategorieItem")) {

					ItemStack it = plugin.getItemAPI().getItemStack(cldt,pl.getName(), icon);
					ItemMeta meta = it.getItemMeta();

					if (cf.contains("Categories.CustomModelData")) {
						meta.setCustomModelData(cf.getInt("Categories.CustomModelData"));
					}

					ArrayList<String> old = new ArrayList<String>();

					for (String b : lore) {
						old.add(plugin.getPluginManager().toHex(b));
					}

					meta.setLore(old);

					meta.setDisplayName(plugin.getPluginManager().toHex(display).replaceAll("&", "§"));

					it.setItemMeta(meta);

					inv.setItem(slot, it);
				}
			}

		}

		List<String> howmany = plugin.getLocalFileManager().getRankingPerJobConfig()
				.getStringList("PerJobRanking_Items.List");

		for (int i = 0; i != howmany.size(); i++) {

			int rank = i + 1;

			int slot = 999;

			if (cf.contains("PerJobRanking_Items." + rank + ".Slot")) {
				slot = cf.getInt("PerJobRanking_Items." + rank + ".Slot");
			}

			if (slot != 999) {

				ItemStack it = plugin.getItemAPI().getItemStack("Icon.RankingNotFound", pl.getName(),
						cf.getString("PerJobRanking_Items.NoneFound.Material"));
				ItemMeta meta = it.getItemMeta();

				if (cf.contains("PerJobRanking_Items.NoneFound.CustomModelData")) {
					meta.setCustomModelData(cf.getInt("PerJobRanking_Items.NoneFound.CustomModelData"));
				}

				String dis = cf.getString("PerJobRanking_Items.NoneFound.Display").replaceAll("<rank>", "" + rank);

				meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

				it.setItemMeta(meta);

				inv.setItem(slot, it);
			}
		}
		;

		if (current.equalsIgnoreCase("TODAY")) {

			if (!plugin.getPlayerAPI().today_ranked.isEmpty()
					&& plugin.getPlayerAPI().today_ranked.containsKey(job.getConfigID())) {
				if (!plugin.getPlayerAPI().today_ranked.get(job.getConfigID()).isEmpty()) {
					plugin.getPlayerAPI().today_ranked.get(job.getConfigID()).forEach((rank, id) -> {

						int slot = 999;

						if (cf.contains("PerJobRanking_Items." + rank + ".Slot")) {
							slot = cf.getInt("PerJobRanking_Items." + rank + ".Slot");
						}

						if (slot != 999) {

							if (id != null) {

								UUID ID = UUID.fromString(id.toString());

								String name = plugin.getPlayerAPI().getDisplayByUUID("" + ID);

								String icon = null;

								if (cf.getBoolean("PerJobRanking_Items." + rank + ".UseSkullAsMaterial")) {
									icon = "name;<name>";
								} else {
									icon = cf.getString("PerJobRanking_Items." + rank + ".Material");
								}

								if (icon != null) {
									ItemStack it = plugin.getItemAPI().getItemStack("NotSet",name, icon);
									ItemMeta meta = it.getItemMeta();

									if (cf.contains("PerJobRanking_Items." + rank + ".CustomModelData")) {
										meta.setCustomModelData(
												cf.getInt("PerJobRanking_Items." + rank + ".CustomModelData"));
									}

									String dis = sp.getLanguage().getGUIMessage("PerJobRanking." + rank + ".Display");

									List<String> lore = sp.getLanguage().getGUIList("PerJobRanking.LoreForRanks.Today");

									ArrayList<String> old = new ArrayList<String>();

									for (String b : lore) {
										old.add(plugin.getPluginManager().toHex(b)
												.replaceAll("<job>", job.getDisplayOfJob(MyUUID))
												.replaceAll("<earnings>",
														plugin.getAPI().Format(
																plugin.getPlayerAPI().getEarningsOfToday("" + ID, job)))
												.replaceAll("<rank>", "" + rank).replaceAll("<name>", name));
									}

									if (ID.equals(pl.getUniqueId())) {
										if (cf.getBoolean("EnchantOwnSkullInRanking")) {
											meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
											meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
										}
									}

									meta.setLore(old);

									meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("<name>", name)
											.replaceAll("&", "§"));

									it.setItemMeta(meta);

									inv.setItem(slot, it);
								}
							}

						}
					});
				}
			}

		} else if (current.equalsIgnoreCase("BLOCKS")) {

			if (!plugin.getPlayerAPI().blocks_ranked.isEmpty()
					&& plugin.getPlayerAPI().blocks_ranked.containsKey(job.getConfigID())) {
				if (!plugin.getPlayerAPI().blocks_ranked.get(job.getConfigID()).isEmpty()) {
					plugin.getPlayerAPI().blocks_ranked.get(job.getConfigID()).forEach((rank, id) -> {

						int slot = 999;

						if (cf.contains("PerJobRanking_Items." + rank + ".Slot")) {
							slot = cf.getInt("PerJobRanking_Items." + rank + ".Slot");
						}

						if (slot != 999) {

							if (id != null) {

								UUID ID = UUID.fromString(id.toString());

								String name = plugin.getPlayerAPI().getDisplayByUUID("" + ID);

								String icon = null;

								if (cf.getBoolean("PerJobRanking_Items." + rank + ".UseSkullAsMaterial")) {
									icon = "name;<name>";
								} else {
									icon = cf.getString("PerJobRanking_Items." + rank + ".Material");
								}

								if (icon != null) {
									ItemStack it = plugin.getItemAPI().getItemStack("NotSet",name, icon);
									ItemMeta meta = it.getItemMeta();

									if (cf.contains("PerJobRanking_Items." + rank + ".CustomModelData")) {
										meta.setCustomModelData(
												cf.getInt("PerJobRanking_Items." + rank + ".CustomModelData"));
									}

									String dis = sp.getLanguage().getGUIMessage("PerJobRanking." + rank + ".Display");

									List<String> lore = sp.getLanguage()
											.getGUIList("PerJobRanking.LoreForRanks.Blocks");

									ArrayList<String> old = new ArrayList<String>();

									for (String b : lore) {
										old.add(plugin.getPluginManager().toHex(b)
												.replaceAll("<job>", job.getDisplayOfJob(MyUUID)).replaceAll("<times>",

														"" + plugin.getPlayerAPI().getBrokenTimes("" + ID, job))
												.replaceAll("<rank>", "" + rank).replaceAll("<name>", name));
									}

									if (ID.equals(pl.getUniqueId())) {
										if (cf.getBoolean("EnchantOwnSkullInRanking")) {
											meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
											meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
										}
									}

									meta.setLore(old);

									meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("<name>", name)
											.replaceAll("&", "§"));

									it.setItemMeta(meta);

									inv.setItem(slot, it);
								}
							}

						}
					});
				}
			}

		} else if (current.equalsIgnoreCase("LEVEL")) {

			if (!plugin.getPlayerAPI().level_ranked.isEmpty()
					&& plugin.getPlayerAPI().level_ranked.containsKey(job.getConfigID())) {
				if (!plugin.getPlayerAPI().level_ranked.get(job.getConfigID()).isEmpty()) {
					plugin.getPlayerAPI().level_ranked.get(job.getConfigID()).forEach((rank, id) -> {

						int slot = 999;

						if (cf.contains("PerJobRanking_Items." + rank + ".Slot")) {
							slot = cf.getInt("PerJobRanking_Items." + rank + ".Slot");
						}

						if (slot != 999) {

							if (id != null) {

								UUID ID = UUID.fromString(id.toString());

								String name = plugin.getPlayerAPI().getDisplayByUUID("" + ID);

								String icon = null;

								if (cf.getBoolean("PerJobRanking_Items." + rank + ".UseSkullAsMaterial")) {
									icon = "name;<name>";
								} else {
									icon = cf.getString("PerJobRanking_Items." + rank + ".Material");
								}

								if (icon != null) {
									ItemStack it =plugin.getItemAPI().getItemStack("NotSet",name, icon);
									ItemMeta meta = it.getItemMeta();

									if (cf.contains("PerJobRanking_Items." + rank + ".CustomModelData")) {
										meta.setCustomModelData(
												cf.getInt("PerJobRanking_Items." + rank + ".CustomModelData"));
									}

									String dis = sp.getLanguage().getGUIMessage("PerJobRanking." + rank + ".Display");

									List<String> lore = sp.getLanguage().getGUIList("PerJobRanking.LoreForRanks.Level");

									ArrayList<String> old = new ArrayList<String>();

									for (String b : lore) {
										old.add(plugin.getPluginManager().toHex(b)
												.replaceAll("<job>", job.getDisplayOfJob(MyUUID))
												.replaceAll("<level>",
														"" + plugin.getPlayerAPI().getLevelOF("" + ID, job))
												.replaceAll("<rank>", "" + rank).replaceAll("<name>", name));
									}

									if (ID.equals(pl.getUniqueId())) {
										if (cf.getBoolean("EnchantOwnSkullInRanking")) {
											meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
											meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
										}
									}

									meta.setLore(old);

									meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("<name>", name)
											.replaceAll("&", "§"));

									it.setItemMeta(meta);

									inv.setItem(slot, it);
								}
							}

						}
					});
				}
			}

		}

	}

	public void createGlobalRankingGUI(Player player, UpdateTypes t) {
		FileConfiguration cfg = plugin.getLocalFileManager().getRankingGlobalConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);

		int size = cfg.getInt("Size");

		String name = sp.getLanguage().getGUIMessage("Global_Name");

		plugin.getGUI().openInventory(player, size, name, GUIType.GLOBAL_RANKING, null, null);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_RANKING_GLOBAL", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		new BukkitRunnable() {

			@Override
			public void run() {

				plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Global_Place"), name,null);
				plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Global_Custom.",
						cfg.getStringList("Global_Custom.List"), name, cfg, null);
				setGlobalRankingItems(inv_view, cfg, player);

				cancel();
			}
		}.runTaskAsynchronously(plugin);
	}

	public void setGlobalRankingItems(InventoryView inv, FileConfiguration cf, Player pl) {

		String UUID = "" + pl.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);

		int d = plugin.getPlayerAPI().ranked_points.size() + 1;

		List<String> a1 = plugin.getLocalFileManager().getRankingGlobalConfig().getStringList("Global_RankingItems.List");

		if (cf.getBoolean("Global_RankingItems.OwnSkullItem.Enabled")) {

			ItemStack it = plugin.getItemAPI().getItemStack("NotSet",pl.getName(), "name;<name>");
			ItemMeta meta = it.getItemMeta();

			if (cf.contains("Global_RankingItems.OwnSkullItem.CustomModelData")) {
				meta.setCustomModelData(cf.getInt("Global_RankingItems.OwnSkullItem.CustomModelData"));
			}

			String dis = sp.getLanguage().getGUIMessage("Global_RankingItems.OwnSkull.Display");
			List<String> lore = sp.getLanguage().getGUIList("Global_RankingItems.OwnSkull.Lore");

			ArrayList<String> old = new ArrayList<String>();

			String name = pl.getName();
			double points = plugin.getPlayerAPI().getPoints("" + pl.getUniqueId());

			for (String b : lore) {
				old.add(plugin.getPluginManager().toHex(b).replaceAll("<points>", plugin.getAPI().Format(points))
						.replaceAll("<rank>", "" + plugin.getPlayerAPI().getRankOfGlobalPlayer("" + pl.getUniqueId()))
						.replaceAll("<name>", name));
			}

			meta.setLore(old);

			meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("<points>", "" + points)
					.replaceAll("<rank>", "" + plugin.getPlayerAPI().getRankOfGlobalPlayer("" + pl.getUniqueId()))
					.replaceAll("<name>", name).replaceAll("&", "§"));

			it.setItemMeta(meta);

			inv.setItem(cf.getInt("Global_RankingItems.OwnSkullItem.Slot"), it);

		}

		for (int i = 0; i != a1.size(); i++) {

			String choosen = null;

			if (d >= i) {
				choosen = a1.get(i);

				int r = Integer.valueOf(choosen);

				int usedfromlist = r - 1;

				int slot = cf.getInt("Global_RankingItems." + r + ".Slot");

				if (pl != null) {

					ItemStack it = plugin.getItemAPI().getItemStack("Icon.GlobalRankingNotFound",pl.getName(),
							cf.getString("Global_RankingItems.NoneFound.Material"));
					ItemMeta meta = it.getItemMeta();

					if (cf.contains("Global_RankingItems.NoneFound.CustomModelData")) {
						meta.setCustomModelData(cf.getInt("Global_RankingItems.NoneFound.CustomModelData"));
					}

					String dis = cf.getString("Global_RankingItems.NoneFound.Display").replaceAll("<rank>", "" + r);

					meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

					it.setItemMeta(meta);

					inv.setItem(slot, it);
				}

				if (plugin.getPlayerAPI().ranked_points.size() >= usedfromlist) {

					String rank_uuid = plugin.getPlayerAPI().ranked_points.get(usedfromlist);

					String icon = null;

					if (cf.getBoolean("Global_RankingItems." + r + ".UseSkullAsMaterial")) {
						icon = "name;<name>";
					} else {
						icon = cf.getString("Global_RankingItems." + r + ".Material");
					}

					double points = plugin.getPlayerAPI().getPoints(rank_uuid);
					String name = plugin.getPlayerAPI().getDisplayByUUID(rank_uuid);

					if (name != null) {
						ItemStack it = plugin.getItemAPI().getItemStack("NotSet", name, icon);
						ItemMeta meta = it.getItemMeta();

						if (cf.contains("Global_RankingItems." + r + ".CustomModelData")) {
							meta.setCustomModelData(cf.getInt("Global_RankingItems." + r + ".CustomModelData"));
						}

						String dis = sp.getLanguage().getGUIMessage("Global_RankingItems." + r + ".Display");
						List<String> lore = sp.getLanguage().getGUIList("Global_RankingItems." + r + ".Lore");

						ArrayList<String> old = new ArrayList<String>();

						for (String b : lore) {
							old.add(plugin.getPluginManager().toHex(b)
									.replaceAll("<points>", plugin.getAPI().Format(points)).replaceAll("<name>", name));
						}

						if (rank_uuid.equalsIgnoreCase("" + pl.getUniqueId())) {
							if (cf.getBoolean("EnchantOwnSkullInRanking")) {
								meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
								meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
							}
						}

						meta.setLore(old);

						meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("<points>", "" + points)
								.replaceAll("<name>", name).replaceAll("&", "§"));

						it.setItemMeta(meta);

						inv.setItem(slot, it);
					}
				}

			}
		}

	}

	public void createEarningsGUI_Single_Job(Player player, UpdateTypes t, Job job) {
		FileConfiguration cfg = plugin.getLocalFileManager().getEarningsJobConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getGUIMessage("Job_Earnings_Name").replaceAll("<job>",
				job.getDisplayOfJob("" + player.getUniqueId()));
		int size = cfg.getInt("Job_Earnings_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.EARNINGS_JOB, null, job);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_JOB_EARNINGS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		new BukkitRunnable() {

			@Override
			public void run() {

				plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Job_Earnings_Place"), name, job);
				plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Job_Earnings_Custom.",
						cfg.getStringList("Job_Earnings_Custom.List"), name, cfg, job);
				setEarningsItems_Single(inv_view, cfg, player, job);

				cancel();
			}
		}.runTaskAsynchronously(plugin);
	}

	public void updateEarningsGUI_Single_Job(Player player, String name, JobsPlayer jb, Job job) {
		FileConfiguration cfg = plugin.getLocalFileManager().getEarningsJobConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				new BukkitRunnable() {

					@Override
					public void run() {
						plugin.getGUI().setCustomitems(player, player.getName(), inv, "Job_Earnings_Custom.",
								cfg.getStringList("Job_Earnings_Custom.List"), name, cfg, job);
						setEarningsItems_Single(inv, cfg, player, job);

						cancel();
					}
				}.runTaskAsynchronously(plugin);
			}
		}.runTaskLater(plugin, 2);
	}

	public void setEarningsItems_Single(InventoryView inv, FileConfiguration cf, Player pl, Job job) {

		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + pl.getUniqueId());

		int page = 1;

		if (plugin.getPlayerAPI().existSettingData(sp.getUUIDAsString(), "EARNINGS_" + job.getConfigID())) {
			page = plugin.getPlayerAPI().getPageData(sp.getUUIDAsString(), "EARNINGS_" + job.getConfigID());
		}

		int days = cf.getInt("HowManyDays");

		ArrayList<String> dates = new ArrayList<String>();

		for (int i = 0; i != days; i++) {

			DateFormat format = new SimpleDateFormat(plugin.getLocalFileManager().getConfig().getString("Date"));
			Date data = new Date();

			Calendar c1 = Calendar.getInstance();
			c1.setTime(data);

			c1.add(Calendar.DATE, -i);

			Date newdate = c1.getTime();

			String d = "" + format.format(newdate);

			dates.add(d);
		}

		Collections.reverse(dates);

		List<String> slots = cf.getStringList("Job_Earnings_Slots");

		int li = dates.size();

		int sizeStop = page * slots.size();

		ArrayList<String> l2 = getAmountToDisplay_SingleJob(cf, pl, page, job);

		if (l2 != null) {

			if (inv != null) {

				if (l2.size() != 0) {
					for (int i3 = 0; i3 < l2.size(); i3++) {

						String date = l2.get(i3);

						String dis = sp.getLanguage().getGUIMessage("Job_Earnings_Items.Display");
						List<String> lore = sp.getLanguage().getGUIList("Job_Earnings_Items.Lore");

						String ic = cf.getString("Job_Earnings_Items.Icon");

						ItemStack it = plugin.getItemAPI().getItemStack("Icon.Job_Earnings_Items",pl.getName(), ic);
						ItemMeta meta = it.getItemMeta();

						meta.setDisplayName(dis.replaceAll("<date>", date).replaceAll("<job>",
								job.getDisplayOfJob("" + pl.getUniqueId())));

						ArrayList<String> l = new ArrayList<String>();

						double er = plugin.getPlayerAPI().getEarnedAt("" + pl.getUniqueId(), job, date);

						for (String b : lore) {
							l.add(plugin.getPluginManager().toHex(b).replaceAll("<date>", date)
									.replaceAll("<job>", job.getDisplayOfJob("" + pl.getUniqueId()))
									.replaceAll("<money>", plugin.getAPI().Format(er)));
						}

						if (cf.contains("Job_Earnings_Items.CustomModelData")) {
							meta.setCustomModelData(cf.getInt("Job_Earnings_Items.CustomModelData"));
						}

						meta.setLore(l);

						it.setItemMeta(meta);

						inv.setItem(Integer.valueOf(slots.get(i3)), it);

					}
				} else {

					String icon = cf.getString("Job_Earnings_Items.NotAnyEarnings.Icon");
					String dis = cf.getString("Job_Earnings_Items.NotAnyEarnings.Display");
					int slot = cf.getInt("Job_Earnings_Items.NotAnyEarnings.Slot");

					ItemStack it = plugin.getItemAPI().getItemStack("Icon.Job_Earnings_ItemsNotFound",pl.getName(), icon);
					ItemMeta meta = it.getItemMeta();

					if (cf.contains("Job_Earnings_Items.NotAnyEarnings.CustomModelData")) {
						meta.setCustomModelData(cf.getInt("Job_Earnings_Items.NotAnyEarnings.CustomModelData"));
					}

					meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

					it.setItemMeta(meta);

					inv.setItem(slot, it);

				}
			}
		} else {

			String icon = cf.getString("Job_Earnings_Items.NotAnyEarnings.Icon");
			String dis = cf.getString("Job_Earnings_Items.NotAnyEarnings.Display");
			int slot = cf.getInt("Job_Earnings_Items.NotAnyEarnings.Slot");

			ItemStack it = plugin.getItemAPI().getItemStack("Icon.Job_Earnings_ItemsNotFound",pl.getName(), icon);
			ItemMeta meta = it.getItemMeta();

			if (cf.contains("Job_Earnings_Items.NotAnyEarnings.CustomModelData")) {
				meta.setCustomModelData(cf.getInt("Job_Earnings_Items.NotAnyEarnings.CustomModelData"));
			}

			meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

			it.setItemMeta(meta);

			inv.setItem(slot, it);

		}

		if (inv != null) {
			boolean next = true;
			boolean pre = true;

			if (cf.getBoolean("PageItems.Next.ShowOnlyIfNextPageExist")) {
				next = sizeStop + 1 < li;
			}
			if (!cf.getBoolean("PageItems.Previous.ShowOnPageOne")) {
				if (page == 1) {
					pre = false;
				}
			}

			if (pre) {

				boolean show = cf.getBoolean("PageItems.Previous.Show");

				if (show) {
					String icon = cf.getString("PageItems.Previous.Material");
					String dis = sp.getLanguage().getGUIMessage("Job_Earnings_Custom.Previous.Display");
					List<String> lore = sp.getLanguage().getGUIList("Job_Earnings_Custom.Previous.Lore");
					int slot = cf.getInt("PageItems.Previous.Slot");

					ItemStack it = plugin.getItemAPI().getItemStack("Icon.Job_Earnings_Previous", pl.getName(), icon);
					ItemMeta meta = it.getItemMeta();

					meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

					ArrayList<String> l = new ArrayList<String>();

					if (lore != null) {
						for (String b : lore) {
							l.add(plugin.getPluginManager().toHex(b).replaceAll("&", "§"));
						}
					}

					if (cf.contains("PageItems.Previous.CustomModelData")) {
						meta.setCustomModelData(cf.getInt("PageItems.Previous.CustomModelData"));
					}

					meta.setLore(l);

					it.setItemMeta(meta);

					inv.setItem(slot, it);
				}

			}

			if (next) {

				boolean show = cf.getBoolean("PageItems.Next.Show");

				if (show) {
					String icon = cf.getString("PageItems.Next.Material");
					String dis = sp.getLanguage().getGUIMessage("Job_Earnings_Custom.Next.Display");
					List<String> lore = sp.getLanguage().getGUIList("Job_Earnings_Custom.Next.Lore");
					int slot = cf.getInt("PageItems.Next.Slot");

					ItemStack it = plugin.getItemAPI().getItemStack("Icon.Job_Earnings_Next", pl.getName(), icon);
					ItemMeta meta = it.getItemMeta();

					meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

					ArrayList<String> l = new ArrayList<String>();

					if (lore != null) {
						for (String b : lore) {
							l.add(plugin.getPluginManager().toHex(b).replaceAll("&", "§"));
						}
					}

					if (cf.contains("PageItems.Next.CustomModelData")) {
						meta.setCustomModelData(cf.getInt("PageItems.Next.CustomModelData"));
					}

					meta.setLore(l);

					it.setItemMeta(meta);

					inv.setItem(slot, it);
				}

			}
		}

	}

	public void createEarningsGUI_ALL_Jobs(Player player, UpdateTypes t) {
		FileConfiguration cfg = plugin.getLocalFileManager().getEarningsAllConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getGUIMessage("All_Earnings_Name");
		int size = cfg.getInt("All_Earnings_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.EARNINGS_ALL, null, null);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_ALL_EARNINGS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		new BukkitRunnable() {

			@Override
			public void run() {
				new BukkitRunnable() {

					@Override
					public void run() {
						plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("All_Earnings_Place"),
								name, null);
						plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "All_Earnings_Custom.",
								cfg.getStringList("All_Earnings_Custom.List"), name, cfg, null);
						setEarningsItems_ALL(inv_view, cfg, player);

						cancel();
					}
				}.runTaskAsynchronously(plugin);
			}
		}.runTaskAsynchronously(plugin);
	}

	public void updateEarningsGUI_All(Player player, String name, JobsPlayer jb) {
		FileConfiguration cfg = plugin.getLocalFileManager().getEarningsAllConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				new BukkitRunnable() {

					@Override
					public void run() {
						plugin.getGUI().setCustomitems(player, player.getName(), inv, "All_Earnings_Custom.",
								cfg.getStringList("All_Earnings_Custom.List"), name, cfg, null);
						setEarningsItems_ALL(inv, cfg, player);

						cancel();
					}
				}.runTaskAsynchronously(plugin);
			}
		}.runTaskLater(plugin, 2);
	}

	public void setEarningsItems_ALL(InventoryView inv, FileConfiguration cf, Player pl) {

		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + pl.getUniqueId());

		int page = 1;

		if (plugin.getPlayerAPI().existSettingData(sp.getUUIDAsString(), "EARNINGS_ALL")) {
			page = plugin.getPlayerAPI().getPageData(sp.getUUIDAsString(), "EARNINGS_ALL");
		}

		int days = cf.getInt("HowManyDays");

		ArrayList<String> dates = new ArrayList<String>();

		for (int i = 0; i != days; i++) {

			DateFormat format = new SimpleDateFormat(plugin.getLocalFileManager().getConfig().getString("Date"));
			Date data = new Date();

			Calendar c1 = Calendar.getInstance();
			c1.setTime(data);

			c1.add(Calendar.DATE, -i);

			Date newdate = c1.getTime();

			String d = "" + format.format(newdate);

			dates.add(d);
		}

		Collections.reverse(dates);

		List<String> slots = cf.getStringList("All_Earnings_Slots");

		int li = dates.size();

		int sizeStop = page * slots.size();

		ArrayList<String> l2 = getAmountToDisplay(cf, pl, page);

		if (inv != null) {

			if (l2.size() != 0) {

				for (int i3 = 0; i3 < l2.size(); i3++) {

					String date = l2.get(i3);

					String dis = sp.getLanguage().getGUIMessage("All_Earnings_Items.Display");
					List<String> lore = sp.getLanguage().getGUIList("All_Earnings_Items.Lore");

					String ic = cf.getString("All_Earnings_Items.Icon");

					ItemStack it = plugin.getItemAPI().getItemStack( "Icon.All_Earnings_Items",pl.getName(), ic);
					ItemMeta meta = it.getItemMeta();

					meta.setDisplayName(dis.replaceAll("<date>", date));

					ArrayList<String> l = new ArrayList<String>();

					double er = plugin.getPlayerAPI().getEarnedAtDateFromAllJobs("" + pl.getUniqueId(), date);

					for (String b : lore) {
						l.add(plugin.getPluginManager().toHex(b).replaceAll("<date>", date).replaceAll("<money>",
								plugin.getAPI().Format(er)));
					}

					if (cf.contains("All_Earnings_Items.CustomModelData")) {
						meta.setCustomModelData(cf.getInt("All_Earnings_Items.CustomModelData"));
					}

					meta.setLore(l);

					it.setItemMeta(meta);

					inv.setItem(Integer.valueOf(slots.get(i3)), it);

				}

			} else {

				String icon = cf.getString("All_Earnings_Items.NotAnyEarnings.Icon");
				String dis = cf.getString("All_Earnings_Items.NotAnyEarnings.Display");
				int slot = cf.getInt("All_Earnings_Items.NotAnyEarnings.Slot");

				ItemStack it = plugin.getItemAPI().getItemStack("Icon.NotAnyEarningsGlobal",pl.getName(), icon);
				ItemMeta meta = it.getItemMeta();

				if (cf.contains("All_Earnings_Items.NotAnyEarnings.CustomModelData")) {
					meta.setCustomModelData(cf.getInt("All_Earnings_Items.NotAnyEarnings.CustomModelData"));
				}

				meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

				it.setItemMeta(meta);

				inv.setItem(slot, it);

			}
		}

		if (inv != null) {
			boolean next = true;
			boolean pre = true;

			if (cf.getBoolean("PageItems.Next.ShowOnlyIfNextPageExist")) {
				next = sizeStop + 1 < li;
			}
			if (!cf.getBoolean("PageItems.Previous.ShowOnPageOne")) {
				if (page == 1) {
					pre = false;
				}
			}

			if (pre) {

				boolean show = cf.getBoolean("PageItems.Previous.Show");

				if (show) {
					String icon = cf.getString("PageItems.Previous.Material");
					String dis = sp.getLanguage().getGUIMessage("All_Earnings_Custom.Previous.Display");
					List<String> lore = sp.getLanguage().getGUIList("All_Earnings_Custom.Previous.Lore");
					int slot = cf.getInt("PageItems.Previous.Slot");

					ItemStack it = plugin.getItemAPI().getItemStack("Icon.GlobalPrevious", pl.getName(), icon);
					ItemMeta meta = it.getItemMeta();

					meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

					ArrayList<String> l = new ArrayList<String>();

					if (lore != null) {
						for (String b : lore) {
							l.add(plugin.getPluginManager().toHex(b).replaceAll("&", "§"));
						}
					}

					if (cf.contains("PageItems.Previous.CustomModelData")) {
						meta.setCustomModelData(cf.getInt("PageItems.Previous.CustomModelData"));
					}

					meta.setLore(l);

					it.setItemMeta(meta);

					inv.setItem(slot, it);
				}

			}

			if (next) {

				boolean show = cf.getBoolean("PageItems.Next.Show");

				if (show) {
					String icon = cf.getString("PageItems.Next.Material");
					String dis = sp.getLanguage().getGUIMessage("All_Earnings_Custom.Next.Display");
					List<String> lore = sp.getLanguage().getGUIList("All_Earnings_Custom.Next.Lore");
					int slot = cf.getInt("PageItems.Next.Slot");

					ItemStack it = plugin.getItemAPI().getItemStack( "Icon.GlobalNext", pl.getName(), icon);
					ItemMeta meta = it.getItemMeta();

					meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

					ArrayList<String> l = new ArrayList<String>();

					if (lore != null) {
						for (String b : lore) {
							l.add(plugin.getPluginManager().toHex(b).replaceAll("&", "§"));
						}
					}

					if (cf.contains("PageItems.Next.CustomModelData")) {
						meta.setCustomModelData(cf.getInt("PageItems.Next.CustomModelData"));
					}

					meta.setLore(l);

					it.setItemMeta(meta);

					inv.setItem(slot, it);
				}

			}
		}

	}

	public ArrayList<String> getAmountToDisplay_SingleJob(FileConfiguration cf, Player pl, int page, Job job) {
		int days = cf.getInt("HowManyDays");

		if (plugin.getPlayerAPI().getRealJobPlayer(pl.getUniqueId()).getStatsList().containsKey(job.getConfigID())) {
			ArrayList<String> dates = new ArrayList<String>();

			for (int i = 0; i != days; i++) {

				DateFormat format = new SimpleDateFormat(plugin.getLocalFileManager().getConfig().getString("Date"));
				Date data = new Date();

				Calendar c1 = Calendar.getInstance();
				c1.setTime(data);

				c1.add(Calendar.DATE, -i);

				Date newdate = c1.getTime();

				String d = "" + format.format(newdate);

				dates.add(d);
			}

			Collections.reverse(dates);

			List<String> slots = cf.getStringList("Job_Earnings_Slots");

			ArrayList<String> itemslist = new ArrayList<String>();

			int sizeStart = (page - 1) * slots.size();
			int sizeStop = page * slots.size();

			for (int i = sizeStart; i != sizeStop; i++) {

				if (i < dates.size()) {

					if (!dates.get(i).isEmpty()) {

						boolean show = true;

						double er = plugin.getPlayerAPI().getEarnedAt("" + pl.getUniqueId(), job, dates.get(i));

						if (er <= 0) {
							if (cf.getBoolean("HideWhenEarningsIsZero")) {
								show = false;
							}
						}

						if (show) {
							itemslist.add(dates.get(i));
						}
					}
				}

			}
			return itemslist;

		}
		return null;
	}

	public ArrayList<String> getAmountToDisplay(FileConfiguration cf, Player pl, int page) {
		int days = cf.getInt("HowManyDays");

		ArrayList<String> dates = new ArrayList<String>();

		for (int i = 0; i != days; i++) {

			DateFormat format = new SimpleDateFormat(plugin.getLocalFileManager().getConfig().getString("Date"));
			Date data = new Date();

			Calendar c1 = Calendar.getInstance();
			c1.setTime(data);

			c1.add(Calendar.DATE, -i);

			Date newdate = c1.getTime();

			String d = "" + format.format(newdate);

			dates.add(d);
		}

		Collections.reverse(dates);

		List<String> slots = cf.getStringList("All_Earnings_Slots");

		ArrayList<String> itemslist = new ArrayList<String>();

		int sizeStart = (page - 1) * slots.size();
		int sizeStop = page * slots.size();

		for (int i = sizeStart; i != sizeStop; i++) {

			if (i < dates.size()) {

				if (!dates.get(i).isEmpty()) {

					boolean show = true;

					double er = plugin.getPlayerAPI().getEarnedAtDateFromAllJobs("" + pl.getUniqueId(), dates.get(i));

					if (er <= 0) {
						if (cf.getBoolean("HideWhenEarningsIsZero")) {
							show = false;
						}
					}

					if (show) {
						itemslist.add(dates.get(i));
					}
				}
			}

		}
		return itemslist;
	}

	public void createLeaveConfirmGUI(Player player, UpdateTypes t, Job job) {
		FileConfiguration cfg = plugin.getLocalFileManager().getLeaveConfirmConfig();

		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());

		String name = sp.getLanguage().getGUIMessage("LeaveConfirm_Name").replaceAll("<job>",
				job.getDisplayOfJob(sp.getUUIDAsString()));
		int size = cfg.getInt("LeaveConfirm_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.CONFIRM_LEAVE, null, job);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_LEAVE_CONFIRM", player);
		}
		InventoryView inv_view = player.getOpenInventory();

		new BukkitRunnable() {

			@Override
			public void run() {

				plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("LeaveConfirm_Place"), name, job);
				plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "LeaveConfirm_Custom.",
						cfg.getStringList("LeaveConfirm_Custom.List"), name, cfg, null);
				setLeaveConfirmItems(player, name, inv_view, job);

				cancel();
			}
		}.runTaskAsynchronously(plugin);
	}

	public void setLeaveConfirmItems(Player player, String tit, InventoryView inv, Job job) {
		FileConfiguration cfg = plugin.getLocalFileManager().getLeaveConfirmConfig();

		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		if (player != null) {

			if (cfg.getBoolean("LeaveConfirmItems.Button_YES.Show")) {

				ItemStack item = plugin.getItemAPI().getItemStack("LeaveConfirmItems.Button_YES", player.getName(),
						cfg.getString("LeaveConfirmItems.Button_YES.Icon"));

				String dis = plugin.getPluginManager()
						.toHex(sp.getLanguage().getGUIMessage("LeaveConfirm.Button_YES.Display")).replaceAll("&", "§");
				int slot = cfg.getInt("LeaveConfirmItems.Button_YES.Slot");
				List<String> lore = sp.getLanguage().getGUIList("LeaveConfirm.Button_YES.Lore");
				ArrayList<String> l = new ArrayList<String>();

				ItemMeta meta = item.getItemMeta();

				for (String line : lore) {
					l.add(plugin.getPluginManager().toHex(line)
							.replaceAll("<job>", job.getDisplayOfJob(sp.getUUIDAsString())).replaceAll("&", "§"));
				}

				if (cfg.contains("LeaveConfirmItems.Button_YES.CustomModelData")) {
					meta.setCustomModelData(cfg.getInt("LeaveConfirmItems.Button_YES.CustomModelData"));
				}

				meta.setDisplayName(dis.replaceAll("<job>", job.getDisplayOfJob(sp.getUUIDAsString())));

				meta.setLore(l);

				item.setItemMeta(meta);

				inv.setItem(slot, item);
			}

		}

		if (player != null) {
			if (cfg.getBoolean("LeaveConfirmItems.Button_NO.Show")) {
				ItemStack item = plugin.getItemAPI().getItemStack("LeaveConfirmItems.Button_NO", player.getName(),
						cfg.getString("LeaveConfirmItems.Button_NO.Icon"));

				String dis = plugin.getPluginManager()
						.toHex(sp.getLanguage().getGUIMessage("LeaveConfirm.Button_NO.Display")).replaceAll("&", "§");
				int slot = cfg.getInt("LeaveConfirmItems.Button_NO.Slot");
				List<String> lore = sp.getLanguage().getGUIList("LeaveConfirm.Button_NO.Lore");
				ArrayList<String> l = new ArrayList<String>();

				ItemMeta meta = item.getItemMeta();

				for (String line : lore) {
					l.add(plugin.getPluginManager().toHex(line)
							.replaceAll("<job>", job.getDisplayOfJob(sp.getUUIDAsString())).replaceAll("&", "§"));
				}

				if (cfg.contains("LeaveConfirmItems.Button_NO.CustomModelData")) {
					meta.setCustomModelData(cfg.getInt("LeaveConfirmItems.Button_NO.CustomModelData"));
				}

				meta.setDisplayName(dis.replaceAll("<job>", job.getDisplayOfJob(sp.getUUIDAsString())));

				meta.setLore(l);

				item.setItemMeta(meta);

				inv.setItem(slot, item);
			}
		}

	}

	public void createWithdrawConfigGUI(Player player, UpdateTypes t) {
		FileConfiguration cfg = plugin.getLocalFileManager().getWithdrawConfirmConfig();

		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());

		String name = sp.getLanguage().getGUIMessage("ConfirmWithdraw_Name");
		int size = cfg.getInt("ConfirmWithdraw_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.CONFIRM_WITHDRAW, null, null);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_WITHDRAW_CONFIRM", player);
		}
		InventoryView inv_view = player.getOpenInventory();

		new BukkitRunnable() {

			@Override
			public void run() {

				plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("ConfirmWithdraw_Place"), name, null);
				plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "ConfirmWithdraw_Custom.",
						cfg.getStringList("ConfirmWithdraw_Custom.List"), name, cfg, null);
				setWithdrawConfigItems(player, name, inv_view);

				cancel();
			}
		}.runTaskAsynchronously(plugin);
	}

	public void setWithdrawConfigItems(Player player, String tit, InventoryView inv) {
		FileConfiguration cfg = plugin.getLocalFileManager().getWithdrawConfirmConfig();

		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		if (player != null) {

			if (cfg.getBoolean("ConfirmWithdrawItems.Button_YES.Show")) {

				ItemStack item = plugin.getItemAPI().getItemStack( "ConfirmWithdrawItems.Button_YES", player.getName(),
						cfg.getString("ConfirmWithdrawItems.Button_YES.Icon"));

				String dis = plugin.getPluginManager()
						.toHex(sp.getLanguage().getGUIMessage("ConfirmWithdraw.Button_YES.Display"))
						.replaceAll("&", "§");
				int slot = cfg.getInt("ConfirmWithdrawItems.Button_YES.Slot");
				List<String> lore = sp.getLanguage().getGUIList("ConfirmWithdraw.Button_YES.Lore");
				ArrayList<String> l = new ArrayList<String>();

				ItemMeta meta = item.getItemMeta();

				for (String line : lore) {
					l.add(plugin.getPluginManager().toHex(line).replaceAll("&", "§"));
				}

				if (cfg.contains("ConfirmWithdrawItems.Button_YES.CustomModelData")) {
					meta.setCustomModelData(cfg.getInt("ConfirmWithdrawItems.Button_YES.CustomModelData"));
				}

				meta.setDisplayName(dis);

				meta.setLore(l);

				item.setItemMeta(meta);

				inv.setItem(slot, item);
			}

		}

		if (player != null) {
			if (cfg.getBoolean("ConfirmWithdrawItems.Button_NO.Show")) {
				ItemStack item = plugin.getItemAPI().getItemStack("ConfirmWithdrawItems.Button_NO", player.getName(),
						cfg.getString("ConfirmWithdrawItems.Button_NO.Icon"));

				String dis = plugin.getPluginManager()
						.toHex(sp.getLanguage().getGUIMessage("ConfirmWithdraw.Button_NO.Display"))
						.replaceAll("&", "§");
				int slot = cfg.getInt("ConfirmWithdrawItems.Button_NO.Slot");
				List<String> lore = sp.getLanguage().getGUIList("ConfirmWithdraw.Button_NO.Lore");
				ArrayList<String> l = new ArrayList<String>();

				ItemMeta meta = item.getItemMeta();

				for (String line : lore) {
					l.add(plugin.getPluginManager().toHex(line).replaceAll("&", "§"));
				}

				if (cfg.contains("ConfirmWithdrawItems.Button_NO.CustomModelData")) {
					meta.setCustomModelData(cfg.getInt("ConfirmWithdrawItems.Button_NO.CustomModelData"));
				}

				meta.setDisplayName(dis);

				meta.setLore(l);

				item.setItemMeta(meta);

				inv.setItem(slot, item);
			}
		}

	}

	public void createWithdrawMenu(Player player, UpdateTypes t) {
		FileConfiguration cfg = plugin.getLocalFileManager().getWithdrawConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getGUIMessage("Withdraw_Name");
		int size = cfg.getInt("Withdraw_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.WITHDRAW, null, null);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_WITHDRAW_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		new BukkitRunnable() {

			@Override
			public void run() {

				plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Withdraw_Place"), name, null);
				plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Withdraw_Custom.",
						cfg.getStringList("Withdraw_Custom.List"), name, cfg, null);
				setWithdrawItems(inv_view, player, sp);

				cancel();
			}
		}.runTaskAsynchronously(plugin);
	}

	public void updateWithdrawGUI(Player player, String name, JobsPlayer jb) {
		FileConfiguration cfg = plugin.getLocalFileManager().getWithdrawConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				new BukkitRunnable() {

					@Override
					public void run() {
						plugin.getGUI().setCustomitems(player, player.getName(), inv, "Withdraw_Custom.",
								cfg.getStringList("Withdraw_Custom.List"), name, cfg, null);
						setWithdrawItems(inv, player, jb);

						cancel();
					}
				}.runTaskAsynchronously(plugin);
			}
		}.runTaskLater(plugin, 2);
	}

	public void setWithdrawItems(InventoryView inv, Player player, JobsPlayer jb) {

		FileConfiguration cfg = plugin.getLocalFileManager().getWithdrawConfig();

		if (jb.getSalary() == 0) {

			if (inv != null) {

				if (cfg.getBoolean("Withdraw_Items.NoSalaryToCollect.Show")) {

					String icon = cfg.getString("Withdraw_Items.NoSalaryToCollect.Material");
					int slot = cfg.getInt("Withdraw_Items.NoSalaryToCollect.Slot");
					String dis = jb.getLanguage().getGUIMessage("Withdraw_Custom.NoSalaryToCollect.Display");

					ItemStack it = plugin.getItemAPI().getItemStack( "Withdraw_Items.NoSalaryToCollect", player.getName(), icon);
					ItemMeta meta = it.getItemMeta();

					meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

					if (cfg.contains("Withdraw_Items.NoSalaryToCollect.CustomModelData")) {
						meta.setCustomModelData(cfg.getInt("Withdraw_Items.NoSalaryToCollect.CustomModelData"));
					}

					if (cfg.getBoolean("Withdraw_Items.NoSalaryToCollect.ShowLore")) {

						List<String> lore = jb.getLanguage().getGUIList("Withdraw_Custom.NoSalaryToCollect.Lore");

						ArrayList<String> l = new ArrayList<String>();

						if (lore != null) {
							for (String b : lore) {
								l.add(plugin.getPluginManager().toHex(b).replaceAll("&", "§"));
							}
						}

						meta.setLore(l);
					}

					it.setItemMeta(meta);

					inv.setItem(slot, it);

				}
			}

		} else {

			if (inv != null) {

				if (cfg.getBoolean("Withdraw_Items.Info.Show")) {

					String icon = cfg.getString("Withdraw_Items.Info.Material");
					int slot = cfg.getInt("Withdraw_Items.Info.Slot");
					String dis = jb.getLanguage().getGUIMessage("Withdraw_Custom.Info.Display");

					ItemStack it = plugin.getItemAPI().getItemStack("Withdraw_Items.Info", player.getName(), icon);
					ItemMeta meta = it.getItemMeta();

					meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

					if (cfg.contains("Withdraw_Items.Info.CustomModelData")) {
						meta.setCustomModelData(cfg.getInt("Withdraw_Items.Info.CustomModelData"));
					}

					if (cfg.getBoolean("Withdraw_Items.Info.ShowLore")) {

						List<String> lore = jb.getLanguage().getGUIList("Withdraw_Custom.Info.Lore");

						ArrayList<String> l = new ArrayList<String>();

						if (lore != null) {
							for (String b : lore) {
								l.add(plugin.getPluginManager().toHex(b).replaceAll("&", "§"));
							}
						}

						meta.setLore(l);
					}

					it.setItemMeta(meta);

					inv.setItem(slot, it);

				}

			}

			if (inv != null) {

				if (cfg.getBoolean("Withdraw_Items.CollectButton.Show")) {

					String icon = cfg.getString("Withdraw_Items.CollectButton.Material");
					int slot = cfg.getInt("Withdraw_Items.CollectButton.Slot");
					String dis = jb.getLanguage().getGUIMessage("Withdraw_Custom.CollectButton.Display");

					ItemStack it = plugin.getItemAPI().getItemStack("Withdraw_Items.CollectButton", player.getName(), icon);
					ItemMeta meta = it.getItemMeta();

					meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

					if (cfg.contains("Withdraw_Items.CollectButton.CustomModelData")) {
						meta.setCustomModelData(cfg.getInt("Withdraw_Items.CollectButton.CustomModelData"));
					}

					if (cfg.getBoolean("Withdraw_Items.CollectButton.ShowLore")) {

						List<String> lore1 = jb.getLanguage()
								.getGUIList("Withdraw_Custom.CollectButton.LoreCanCollect");
						List<String> lore2 = jb.getLanguage()
								.getGUIList("Withdraw_Custom.CollectButton.LoreCantCollect");

						ArrayList<String> l = new ArrayList<String>();

						if (plugin.getAPI().canWithdrawMoney(player, jb)) {
							if (lore1 != null) {
								for (String b : lore1) {
									l.add(plugin.getPluginManager().toHex(b).replaceAll("&", "§").replaceAll("<salary>",
											plugin.getAPI().Format(jb.getSalary())));
								}
							}
						} else {
							if (lore2 != null) {
								for (String b : lore2) {
									l.add(plugin.getPluginManager().toHex(b).replaceAll("&", "§")
											.replaceAll("<date>", jb.getSalaryDate())
											.replaceAll("<salary>", plugin.getAPI().Format(jb.getSalary())));
								}
							}
						}

						meta.setLore(l);
					}

					it.setItemMeta(meta);

					inv.setItem(slot, it);

				}

			}

		}

	}

	public void createLevelsGUI(Player player, UpdateTypes t, Job job) {
		FileConfiguration cfg = plugin.getLocalFileManager().getLevelGUIConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getGUIMessage("Levels_Name").replaceAll("<job>", job.getDisplayOfJob(UUID));
		int size = cfg.getInt("Levels_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.LEVELS, null, job);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_LEVELS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		new BukkitRunnable() {

			@Override
			public void run() {

				plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Levels_Place"), name, job);
				plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Levels_Custom.",
						cfg.getStringList("Levels_Custom.List"), name, cfg, null);
				setLevelsItems(inv_view, name, cfg, player, job);

				cancel();
			}
		}.runTaskAsynchronously(plugin);
	}

	public void updateLevelsGUI(Player player, String name, JobsPlayer jb, Job job) {
		FileConfiguration cfg = plugin.getLocalFileManager().getLevelGUIConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				new BukkitRunnable() {

					@Override
					public void run() {
						plugin.getGUI().setCustomitems(player, player.getName(), inv, "Levels_Custom.",
								cfg.getStringList("Levels_Custom.List"), name, cfg, null);
						setLevelsItems(inv, name, cfg, player, job);

						cancel();
					}
				}.runTaskAsynchronously(plugin);
			}
		}.runTaskLater(plugin, 2);
	}

	public void setLevelsItems(InventoryView inv, String name, FileConfiguration cf, Player pl, Job job) {

		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + pl.getUniqueId());

		int page = 1;

		if (plugin.getPlayerAPI().existSettingData(sp.getUUIDAsString(), "LEVELS_" + job.getConfigID())) {
			page = plugin.getPlayerAPI().getPageData(sp.getUUIDAsString(), "LEVELS_" + job.getConfigID());
		}

		List<String> slots = cf.getStringList("Level_Slots");

		int li = job.getLevels().size();

		ArrayList<String> levelslist = new ArrayList<String>();

		int sizeStart = (page - 1) * slots.size() + 1;
		int sizeStop = page * slots.size() + 1;

		for (int i = sizeStart; i != sizeStop; i++) {
			levelslist.add("" + i);
		}

		for (int i = 0; i < levelslist.size(); i++) {

			int lvl = Integer.valueOf(levelslist.get(i));

			if (lvl != 0) {
				if (levelslist.size() - 1 >= i) {
					int slot = 0;

					if (slots.size() >= i) {
						if (page == 1) {
							slot = Integer.valueOf(slots.get(i));
						} else {
							slot = Integer.valueOf(slots.get(i));
						}

						if (job.getIconOfLevel(lvl) != null) {

							if (job.getLevelDisplay(lvl, "" + pl.getUniqueId()) != null) {
								ArrayList<String> lore = new ArrayList<String>();

								JobStats stats = sp.getStatsOf(job.getConfigID());

								String ic = null;
								int SPLEVEL = stats.getLevel();

								boolean is = SPLEVEL + 1 == lvl;

								int iii = 0;

								String idk = null;
								
								if (!cf.getBoolean("Options.UseLevelIconsOrCustom")) {

									if (is) {
										iii = cf.getInt("Options.Icons.CurrentlyWorkingOnOption.CustomModelData");
										ic = cf.getString("Options.Icons.CurrentlyWorkingOn");
										idk = "Icons.CurrentlyWorkingOn";
									} else if (SPLEVEL >= lvl) {
										iii = cf.getInt("Options.Icons.ReachedOption.CustomModelData");
										ic = cf.getString("Options.Icons.Reached");
										idk = "Icons.Reached";
									} else {
										iii = cf.getInt("Options.Icons.NotReachedOption.CustomModelData");
										ic = cf.getString("Options.Icons.NotReached");
										idk = "Icons.NotReached";
									}
								} else {
									ic = job.getIconOfLevel(lvl);
									iii = job.getModelData();
									idk = job.getConfigID()+"_LevelMat_"+lvl;
								}

								String dis = job.getLevelDisplay(lvl, "" + pl.getUniqueId()).replaceAll("&", "§");
								
								String prefix = null;

								if (is) {
									prefix = "Currently";
								} else if (SPLEVEL >= lvl) {
									prefix = "Reached";
								} else {
									prefix = "Locked";
								}
								
								double exp2 = sp.getStatsOf(job.getConfigID()).getExp();
								double need = plugin.getLevelAPI().getJobNeedExpWithOutPlayer(job, lvl);
								 
								double calc = need - exp2;
								
								double more = job.getMultiOfLevel(lvl);
								
								if (job.getLevelLore(lvl, "" + pl.getUniqueId()) != null) {
									for (String line : job.getLevelLore(lvl, "" + pl.getUniqueId())) {
										lore.add(plugin.getPluginManager().toHex(line).replaceAll("<more>", ""+more));
									}
								} 
								
								List<String> l1 = sp.getLanguage().getGUIList("Rewards_Item_Lores." + prefix);
								
								for (String line : l1) {
									lore.add(plugin.getPluginManager().toHex(line).replaceAll("<exp>",
											plugin.getAPI().Format(calc)).replaceAll("<more>", ""+more));
								}
								
								ItemStack it = plugin.getItemAPI().getItemStack(idk, pl.getName(), ic);
								ItemMeta meta = it.getItemMeta();

								meta.setCustomModelData(iii);

								meta.setDisplayName(dis);

								if (SPLEVEL >= lvl) {
									if (cf.getBoolean("Options.EnchantedWhenReached")) {
										meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
									}
								}

							 

								 

							 

							 

								it.setAmount(lvl);

								meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

								meta.setLore(lore);

								it.setItemMeta(meta);

								inv.setItem(slot, it);
							}
						}
					}
				}
			}
		}

		if (inv != null) {
			boolean next = true;
			boolean pre = true;

			if (cf.getBoolean("PageItems.Next.ShowOnlyIfNextPageExist")) {
				next = sizeStop + 1 < li;
			}
			if (!cf.getBoolean("PageItems.Previous.ShowOnPageOne")) {
				if (page == 1) {
					pre = false;
				}
			}

			if (pre) {

				boolean show = cf.getBoolean("PageItems.Previous.Show");

				if (show) {
					String icon = cf.getString("PageItems.Previous.Material");
					String dis = sp.getLanguage().getGUIMessage("Levels_Custom.Previous.Display");
					List<String> lore = sp.getLanguage().getGUIList("Levels_Custom.Previous.Lore");
					int slot = cf.getInt("PageItems.Previous.Slot");

					ItemStack it = plugin.getItemAPI().getItemStack("Levels.Previous", pl.getName(), icon);
					ItemMeta meta = it.getItemMeta();

					meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

					if (cf.contains("PageItems.Previous.CustomModelData")) {
						meta.setCustomModelData(cf.getInt("PageItems.Previous.CustomModelData"));
					}

					ArrayList<String> l = new ArrayList<String>();

					if (lore != null) {
						for (String b : lore) {
							l.add(plugin.getPluginManager().toHex(b).replaceAll("&", "§"));
						}
					}

					meta.setLore(l);

					it.setItemMeta(meta);

					inv.setItem(slot, it);
				}

			}

			if (next) {

				boolean show = cf.getBoolean("PageItems.Next.Show");

				if (show) {
					String icon = cf.getString("PageItems.Next.Material");
					String dis = sp.getLanguage().getGUIMessage("Levels_Custom.Next.Display");
					List<String> lore = sp.getLanguage().getGUIList("Levels_Custom.Next.Lore");
					int slot = cf.getInt("PageItems.Next.Slot");

					ItemStack it = plugin.getItemAPI().getItemStack("Levels.Next", pl.getName(), icon);
					ItemMeta meta = it.getItemMeta();

					if (cf.contains("PageItems.Next.CustomModelData")) {
						meta.setCustomModelData(cf.getInt("PageItems.Next.CustomModelData"));
					}

					meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

					ArrayList<String> l = new ArrayList<String>();

					if (lore != null) {
						for (String b : lore) {
							l.add(plugin.getPluginManager().toHex(b).replaceAll("&", "§"));
						}
					}

					meta.setLore(l);

					it.setItemMeta(meta);

					inv.setItem(slot, it);
				}

			}
		}

	}

	public void createRewardsGUI(Player player, UpdateTypes t, Job job) {
		FileConfiguration cfg = plugin.getLocalFileManager().getRewardsConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getGUIMessage("Rewards_Name").replaceAll("<job>", job.getDisplayOfJob(UUID));
		int size = cfg.getInt("Rewards_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.REWARDS, null, job);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_REWARDS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		new BukkitRunnable() {

			@Override
			public void run() {

				plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Rewards_Place"), name, job);
				plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Rewards_Custom.",
						cfg.getStringList("Rewards_Custom.List"), name, cfg, null);
				setRewardsItems(inv_view, name, cfg, player, job);

				cancel();
			}
		}.runTaskAsynchronously(plugin);
	}

	public void updateRewardsGUI(Player player, String name, JobsPlayer jb, Job job) {
		FileConfiguration cfg = plugin.getLocalFileManager().getRewardsConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				new BukkitRunnable() {

					@Override
					public void run() {
						plugin.getGUI().setCustomitems(player, player.getName(), inv, "Rewards_Custom.",
								cfg.getStringList("Rewards_Custom.List"), name, cfg, null);
						setRewardsItems(inv, name, cfg, player, job);

						cancel();
					}
				}.runTaskAsynchronously(plugin);
			}
		}.runTaskLater(plugin, 2);
	}

	public void setRewardsItems(InventoryView inv, String name, FileConfiguration cf, Player pl, Job job) {

		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + pl.getUniqueId());

		int page = 1;

		if (plugin.getPlayerAPI().existSettingData(sp.getUUIDAsString(), "REWARDS_" + job.getConfigID())) {
			page = plugin.getPlayerAPI().getPageData(sp.getUUIDAsString(), "REWARDS_" + job.getConfigID());
		}

		List<String> slots = cf.getStringList("Rewards_Slots");
		int pageLength = slots.size();
		ArrayList<String> d = job.getAllNotRealIDSFromActionsAsArray();

		int cl = page * pageLength;

		if (inv != null) {
			boolean next = true;
			boolean pre = true;

			if (cf.getBoolean("PageItems.Next.ShowOnlyIfNextPageExist")) {
				next = d.size() >= cl + 1;
			}
			if (!cf.getBoolean("PageItems.Previous.ShowOnPageOne")) {
				if (page == 1) {
					pre = false;
				}
			}

			if (pre) {

				boolean show = cf.getBoolean("PageItems.Previous.Show");

				if (show) {
					String icon = cf.getString("PageItems.Previous.Material");
					String dis = sp.getLanguage().getGUIMessage("Rewards_Custom.Previous.Display");
					List<String> lore = sp.getLanguage().getGUIList("Rewards_Custom.Previous.Lore");
					int slot = cf.getInt("PageItems.Previous.Slot");

					ItemStack it = plugin.getItemAPI().getItemStack("Rewards.Previous", pl.getName(), icon);
					ItemMeta meta = it.getItemMeta();

					meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

					if (cf.contains("PageItems.Previous.CustomModelData")) {
						meta.setCustomModelData(cf.getInt("PageItems.Previous.CustomModelData"));
					}

					ArrayList<String> l = new ArrayList<String>();

					if (lore != null) {
						for (String b : lore) {
							l.add(plugin.getPluginManager().toHex(b).replaceAll("&", "§"));
						}
					}

					meta.setLore(l);

					it.setItemMeta(meta);

					inv.setItem(slot, it);
				}

			}

			if (next) {

				boolean show = cf.getBoolean("PageItems.Next.Show");

				if (show) {
					String icon = cf.getString("PageItems.Next.Material");
					String dis = sp.getLanguage().getGUIMessage("Rewards_Custom.Next.Display");
					List<String> lore = sp.getLanguage().getGUIList("Rewards_Custom.Next.Lore");
					int slot = cf.getInt("PageItems.Next.Slot");

					ItemStack it = plugin.getItemAPI().getItemStack("Rewards.Next", pl.getName(), icon);
					ItemMeta meta = it.getItemMeta();

					if (cf.contains("PageItems.Next.CustomModelData")) {
						meta.setCustomModelData(cf.getInt("PageItems.Next.CustomModelData"));
					}

					meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

					ArrayList<String> l = new ArrayList<String>();

					if (lore != null) {
						for (String b : lore) {
							l.add(plugin.getPluginManager().toHex(b).replaceAll("&", "§"));
						}
					}

					meta.setLore(l);

					it.setItemMeta(meta);

					inv.setItem(slot, it);
				}

			}
		}

		ArrayList<String> itemslist = new ArrayList<String>();

		int sizeStart = (page - 1) * slots.size();
		int sizeStop = page * slots.size();

		for (int i = sizeStart; i != sizeStop; i++) {

			if (i < d.size()) {

				if (d.get(i).isEmpty()) {
					return;
				}

				itemslist.add(d.get(i));
			}

		}

		if (inv != null) {

			for (int i3 = 0; i3 < itemslist.size(); i3++) {

				String type = itemslist.get(i3);

				JobAction real = job.getActionofID(type);

				String icon = job.getIconOfID(type);
				String display = sp.getLanguage()
						.getJobMessage("Jobs." + job.getConfigID() + ".IDS." + type + ".Rewards.Display");
				
				List<String> lore = sp.getLanguage()
						.getJobList("Jobs." + job.getConfigID() + ".IDS." + type + ".Rewards.Lore");

				ItemStack i2 = plugin.getItemAPI().getItemStack(job.getConfigID()+"_JobItems_"+type, pl.getName(), icon);
				ItemMeta m = i2.getItemMeta();

				if (job.getModelData(type) != 0) {
					m.setCustomModelData(job.getModelData(type));
				}

				ArrayList<String> l = new ArrayList<String>();

				String used = null;

				if (display == null) {
					used = "§cNot Found";
				} else {
					used = display;
				}

				m.setDisplayName(plugin.getPluginManager().toHex(used).replaceAll("&", "§"));

				double fromfile = job.getRewardOf(type, real);
				double reward = plugin.getPlayerAPI().getRealCalculatedAmountOfMoney("" + pl.getUniqueId(), job,
						fromfile);
				int chance = job.getChanceOf(type, real);
				double points = job.getPointsOf(type, real);
				double exp = job.getExpOf(type, real);
				double ep = plugin.getPlayerAPI().getRealCalculatedAmountOfExp("" + pl.getUniqueId(), job, exp);

				 
				
				int brokentimes = 0;
				double ear = 0.0;
				int level = 1;

				if (sp.getOwnJobs().contains(job.getConfigID())) {

					brokentimes = plugin.getPlayerAPI().getBrokenTimesOfID("" + pl.getUniqueId(), job, type,
							real.toString());
					ear = plugin.getPlayerAPI().getEarnedFrom("" + pl.getUniqueId(), job, type, real.toString());
					level = plugin.getPlayerAPI().getLevelOF(""+pl.getUniqueId(), job);
				}

				for (String line : lore) {
					l.add(plugin.getPluginManager().toHex(line).replaceAll("<exp>", plugin.getAPI().Format(ep))
							.replaceAll("<more>", ""+job.getMultiOfLevel(level)).replaceAll("<points>", "" + points).replaceAll("<earned>", plugin.getAPI().Format(ear))
							.replaceAll("<action>", real.toString().toLowerCase()).replaceAll("<chance>", "" + chance)
							.replaceAll("<times>", "" + brokentimes)
							.replaceAll("<money>", plugin.getAPI().Format(reward)).replaceAll("&", "§"));
				}

				m.setLore(l);

				i2.setItemMeta(m);

				inv.setItem(Integer.valueOf(slots.get(i3)), i2);
			}

		}

	}

	public void createSelfStatsGUI(Player player, UpdateTypes t) {
		FileConfiguration cfg = plugin.getLocalFileManager().getStatsConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getGUIMessage("Self_Name");
		int size = cfg.getInt("Self_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.STATS_SELF, null, null);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_SELF_STATS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		new BukkitRunnable() {

			@Override
			public void run() {

				plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Self_Place"), name, null);
				plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Self_Custom.",
						cfg.getStringList("Self_Custom.List"), name, cfg, null);
				setStatsItems(inv_view, name, cfg, UUID, player.getName(), player, "Self");

				cancel();
			}
		}.runTaskAsynchronously(plugin);
	}

	public void updateSelfUpdateGUI(Player player, String name, JobsPlayer jb) {
		FileConfiguration cfg = plugin.getLocalFileManager().getStatsConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				new BukkitRunnable() {

					@Override
					public void run() {
						plugin.getGUI().setCustomitems(player, player.getName(), inv, "Self_Custom.",
								cfg.getStringList("Self_Custom.List"), name, cfg, null);
						setStatsItems(inv, name, cfg, "" + player.getUniqueId(), player.getName(), player, "Self");

						cancel();
					}
				}.runTaskAsynchronously(plugin);
			}
		}.runTaskLater(plugin, 2);
	}

	public void createOtherStatsGUI(Player player, UpdateTypes t, String named, String ud) {
		FileConfiguration cfg = plugin.getLocalFileManager().getStatsConfig();

		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		String name = sp.getLanguage().getGUIMessage("Other_Name").replaceAll("<name>", named);
		int size = cfg.getInt("Other_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.STATS_OTHER, named, null);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_OTHER_STATS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		new BukkitRunnable() {

			@Override
			public void run() {

				plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Other_Place"), name, null);
				plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Other_Custom.",
						cfg.getStringList("Other_Custom.List"), name, cfg, null);
				setStatsItems(inv_view, name, cfg, ud, named, player, "Other");

				cancel();
			}
		}.runTaskAsynchronously(plugin);
	}

	public void updateOtherStatsGUI(Player player, String name, JobsPlayer jb, String ud) {
		FileConfiguration cfg = plugin.getLocalFileManager().getStatsConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {

				new BukkitRunnable() {

					@Override
					public void run() {

						plugin.getGUI().setCustomitems(player, player.getName(), inv, "Other_Custom.",
								cfg.getStringList("Other_Custom.List"), name, cfg, null);
						setStatsItems(inv, name, cfg, ud, name, player, "Other");

						cancel();
					}
				}.runTaskAsynchronously(plugin);
			}
		}.runTaskLater(plugin, 2);
	}

	public void setStatsItems(InventoryView inv, String name, FileConfiguration cf, String WATCHUUID, String NAME,
			Player pl, String prefix) {

		String title = pl.getOpenInventory().getTitle();
		String need = plugin.getPluginManager().toHex(name).replaceAll("&", "§");

		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + pl.getUniqueId());

		if (title.equalsIgnoreCase(need)) {

			// informations

			String mode = plugin.getLocalFileManager().getStatsConfig().getString("DisplayMode").toUpperCase();

			ArrayList<String> li = null;

			if (mode.equalsIgnoreCase("CURRENT")) {
				li = plugin.getPlayerAPI().getCurrentJobs(WATCHUUID);
			} else if (mode.equalsIgnoreCase("OWNED")) {
				li = plugin.getPlayerAPI().getOwnedJobs(WATCHUUID);
			}

			double points = plugin.getPlayerAPI().getPoints(WATCHUUID);
			int max = plugin.getPlayerAPI().getMaxJobs(WATCHUUID);

			if (cf.getString(prefix + "_Skull.Material") != null) {
				String skull_item = cf.getString(prefix + "_Skull.Material");
				String skull_display = sp.getLanguage().getGUIMessage(prefix + "_Custom.Skull.Display");
				int skull_slot = cf.getInt(prefix + "_Skull.Slot");
				List<String> skull_lore = sp.getLanguage().getGUIList(prefix + "_Custom.Skull.Lore");

				ItemStack skull = plugin.getItemAPI().getItemStack("NotSet",NAME, skull_item);
				ItemMeta m = skull.getItemMeta();

				if (cf.contains(prefix + "_Skull.CustomModelData")) {
					m.setCustomModelData(cf.getInt(prefix + "_Skull.CustomModelData"));
				}

				ArrayList<String> l = new ArrayList<String>();

				int finalmaxjobs = max + 1;

				for (String b : skull_lore) {
					l.add(plugin.getPluginManager().toHex(b).replaceAll("<points>", api.Format(points))
							.replaceAll("<max>", "" + finalmaxjobs).replaceAll("<name>", NAME).replaceAll("&", "§"));
				}

				m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				m.addItemFlags(ItemFlag.HIDE_ENCHANTS);

				m.setLore(l);

				m.setDisplayName(
						plugin.getPluginManager().toHex(skull_display).replaceAll("<name>", NAME).replaceAll("&", "§"));

				skull.setItemMeta(m);

				inv.setItem(skull_slot, skull);
			}

			List<String> slots = cf.getStringList(prefix + "_Slots");

			for (int i = 0; i < slots.size(); i++) {

				if (i >= li.size()) {

					String error_item = cf.getString(prefix + "_Not_Found_Item.Icon");

					if (error_item != null) {
						String error_display = sp.getLanguage().getGUIMessage(prefix + "_Custom.NotFound.Display");
						List<String> errorl_lore = sp.getLanguage().getGUIList(prefix + "_Custom.NotFound.Lore");

						ItemStack error = plugin.getItemAPI().getItemStack("NotSet", pl.getName(), error_item);
						ItemMeta m = error.getItemMeta();

						if (cf.contains(prefix + "_Not_Found_Item.CustomModelData")) {
							m.setCustomModelData(cf.getInt(prefix + "_Not_Found_Item.CustomModelData"));
						}

						ArrayList<String> l = new ArrayList<String>();

						for (String b : errorl_lore) {
							l.add(plugin.getPluginManager().toHex(b).replaceAll("&", "§"));
						}

						m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						m.addItemFlags(ItemFlag.HIDE_ENCHANTS);

						m.setLore(l);

						m.setDisplayName(plugin.getPluginManager().toHex(error_display).replaceAll("<name>", NAME)
								.replaceAll("&", "§"));

						error.setItemMeta(m);

						inv.setItem(Integer.valueOf(slots.get(i)), error);
					}

				} else {

					Job job = plugin.getJobCache().get(li.get(i));
					// loading stats

					int level = plugin.getPlayerAPI().getLevelOF(WATCHUUID, job);
					double exp = plugin.getPlayerAPI().getExpOf(WATCHUUID, job);
					;
					String bought = plugin.getPlayerAPI().getBoughtDate(WATCHUUID, job);
					Integer broken = plugin.getPlayerAPI().getBrokenTimes(WATCHUUID, job);

					String usedbuy = "";
					String lvl = job.getLevelDisplay(level, WATCHUUID);
					String usedlvl = "";

					String item = job.getRawIcon();
					String display = sp.getLanguage()
							.getJobMessage("Jobs." + job.getConfigID().toUpperCase() + ".StatsGUI.Display");
					List<String> lore = sp.getLanguage()
							.getJobList("Jobs." + job.getConfigID().toUpperCase() + ".StatsGUI.Lore." + prefix);

					ItemStack it = plugin.getItemAPI().getItemStack(job.getConfigID()+"_Material", pl.getName(), item);
					ItemMeta m = it.getItemMeta();

					if (cf.contains("Jobs." + job.getConfigID().toUpperCase() + ".StatsGUI.CustomModelData")) {
						m.setCustomModelData(
								cf.getInt("Jobs." + job.getConfigID().toUpperCase() + ".StatsGUI.CustomModelData"));
					}

					if (lore != null) {
						ArrayList<String> l = new ArrayList<String>();

						if (lvl == null) {
							usedlvl = "Error";
						} else {
							usedlvl = lvl;
						}
						if (bought == null) {
							usedbuy = "Error";
						} else {
							usedbuy = bought;
						}

						for (String b : lore) {
							l.add(plugin.getPluginManager().toHex(b).replaceAll("<stats_args_4>", usedlvl)
									.replaceAll("<name>", NAME)
								.replaceAll("<level_rank>", job.getLevelRankDisplay(level, WATCHUUID))	.replace("<earned>",
											"" + api.Format(plugin.getPlayerAPI().getEarnedAt(WATCHUUID, job,
													plugin.getDate())))
									.replaceAll("<stats_args_3>", "" + level).replaceAll("<stats_args_2>", "" + broken)
									.replaceAll("<stats_args_6>",
											"" + api.Format(
													plugin.getLevelAPI().getJobNeedExpWithOutPlayer(job, level)))
									.replaceAll("<stats_args_5>", "" + api.Format(exp))
									.replaceAll("<stats_args_1>", "" + usedbuy).replaceAll("&", "§"));
						}

						m.setLore(l);
					}

					m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
					m.addItemFlags(ItemFlag.HIDE_ENCHANTS);

					m.setDisplayName(
							plugin.getPluginManager().toHex(display).replaceAll("<name>", NAME).replaceAll("&", "§"));

					it.setItemMeta(m);

					inv.setItem(Integer.valueOf(slots.get(i)), it);

				}

			}

		}

	}

}
