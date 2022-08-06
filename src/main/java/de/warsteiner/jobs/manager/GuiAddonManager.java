package de.warsteiner.jobs.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.PlayerDataAPI;
import de.warsteiner.jobs.utils.JobAction;
import de.warsteiner.jobs.utils.objects.GUIType;
import de.warsteiner.jobs.utils.objects.JobStats;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.objects.UpdateTypes;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.upperlevel.spigot.book.BookUtil;

public class GuiAddonManager {

	private UltimateJobs plugin;
	private JobAPI api = UltimateJobs.getPlugin().getAPI();

	public GuiAddonManager(UltimateJobs plugin) {
		this.plugin = plugin;
	}

	public void createFirstStartMenu(Player player) {

		player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1, 2);

		ItemStack item = BookUtil.writtenBook().author("Warsteiner37").title("§fWelcome").pages(

				new BaseComponent[] { new TextComponent(
						"§aWelcome to the first launch of the Plugin UltimateJobs! \n§7\n§7\n§8First of all, §6thanks §8for deciding and buying my Plugin!\n§7\n§7\n§7\n"
								+ "§b<< §8Visit the other pages for more information §b>>") },

				new BookUtil.PageBuilder()

						.add("§aPlugin Help\n§7\n§8You can get help at our Discord Server§8: §bhttps://dcto.mc-plugins.org §8or you can visit the Plugin's Official Docs Site§8: §9https://dto.mc-plugins.org")

						.newLine().newLine()

						.add(BookUtil.TextBuilder.of("Discord Invite")
								.onHover(BookUtil.HoverAction.showText("Get the Discord Invite"))
								.onClick(BookUtil.ClickAction.openUrl("https://dcto.mc-plugins.org"))
								.color(ChatColor.GREEN).build())
						.newLine()

						.add(BookUtil.TextBuilder.of("Docs Website")
								.onHover(BookUtil.HoverAction.showText("Visit the Docs Website"))
								.onClick(BookUtil.ClickAction.openUrl("https://dto.mc-plugins.org"))
								.color(ChatColor.BLUE).build())

						.build(),

				new BookUtil.PageBuilder()
						.add("§bFinish Setup\n§7\n§8Click the Button below, to finish the UltimateJobs Setup!\n§7\n")

						.add("§c> Thank you for your Support! If you want, leave a Review on the Plugin's Post! :) <")
						.newLine().newLine().newLine()
						.add(BookUtil.TextBuilder.of("Finish")
								.onHover(BookUtil.HoverAction.showText("Finish UltimateJobs Setup"))
								.onClick(BookUtil.ClickAction.runCommand("/jobsadmin first finish"))
								.color(ChatColor.LIGHT_PURPLE).build())
						.build()

		)

				.build();

		BookUtil.openPlayer(player, item);
	}

	public void createUpdateMenu(Player player) {

		if (plugin.getWebManager().canUpdate) {
			plugin.getGUI().openInventory(player, 5, "§aUpdate Found", null, null, null);

			InventoryView inv = player.getOpenInventory();

			if (inv != null) {
				ItemStack it = plugin.getItemAPI().createItem(player.getName(), "BLACK_STAINED_GLASS_PANE");
				ItemMeta meta = it.getItemMeta();

				meta.setDisplayName("§8-/-");

				it.setItemMeta(meta);

				inv.setItem(36, it);
				inv.setItem(37, it);
				inv.setItem(38, it);
				inv.setItem(39, it);
				inv.setItem(40, it);
				inv.setItem(41, it);
				inv.setItem(42, it);
				inv.setItem(43, it);
				inv.setItem(44, it);

				inv.setItem(0, it);
				inv.setItem(1, it);
				inv.setItem(2, it);
				inv.setItem(3, it);
				inv.setItem(4, it);
				inv.setItem(5, it);
				inv.setItem(6, it);
				inv.setItem(7, it);
				inv.setItem(8, it);
			}

			if (inv != null) {
				ItemStack it = plugin.getItemAPI().createItem(player.getName(),
						"url;http://textures.minecraft.net/texture/ce1f3cc63c73a6a1dde72fe09c6ac5569376d7b61231bb740764368788cbf1fa");
				ItemMeta meta = it.getItemMeta();

				meta.setDisplayName("§e☆ §7Download Update §e☆");

				ArrayList<String> lore = new ArrayList<String>();

				lore.add("§7§oOn click, the new Version gets downloaded...");

				meta.setLore(lore);

				it.setItemMeta(meta);

				inv.setItem(20, it);
			}

			if (inv != null) {
				ItemStack it = plugin.getItemAPI().createItem(player.getName(), "GREEN_DYE");
				ItemMeta meta = it.getItemMeta();

				meta.setDisplayName("§a✚ §7Added Features §a✚");

				ArrayList<String> lore = new ArrayList<String>();

				if (plugin.getWebManager().added.size() != 0) {
					for (int i3 = 0; i3 < plugin.getWebManager().added.size(); i3++) {
						int i4 = i3 + 1;
						lore.add("§b#" + i4 + plugin.getPluginManager().toHex(plugin.getWebManager().added.get(i3)));
					}
				} else {
					lore.add("§7§oNothing");
				}

				meta.setLore(lore);

				it.setItemMeta(meta);

				inv.setItem(22, it);
			}

			if (inv != null) {
				ItemStack it = plugin.getItemAPI().createItem(player.getName(), "LIGHT_BLUE_DYE");
				ItemMeta meta = it.getItemMeta();

				meta.setDisplayName("§b○ §7Updated Features §b○");

				ArrayList<String> lore = new ArrayList<String>();

				if (plugin.getWebManager().updated.size() != 0) {
					for (int i3 = 0; i3 < plugin.getWebManager().updated.size(); i3++) {
						int i4 = i3 + 1;
						lore.add("§b#" + i4 + plugin.getPluginManager().toHex(plugin.getWebManager().updated.get(i3)));
					}
				} else {
					lore.add("§7§oNothing");
				}

				meta.setLore(lore);

				it.setItemMeta(meta);

				inv.setItem(23, it);
			}

			if (inv != null) {
				ItemStack it = plugin.getItemAPI().createItem(player.getName(), "RED_DYE");
				ItemMeta meta = it.getItemMeta();

				meta.setDisplayName("§c✖ §7Removed Features §c✖");

				ArrayList<String> lore = new ArrayList<String>();

				if (plugin.getWebManager().removed.size() != 0) {
					for (int i3 = 0; i3 < plugin.getWebManager().removed.size(); i3++) {
						int i4 = i3 + 1;
						lore.add("§b#" + i4 + plugin.getPluginManager().toHex(plugin.getWebManager().removed.get(i3)));
					}
				} else {
					lore.add("§7§oNothing");
				}

				meta.setLore(lore);

				it.setItemMeta(meta);

				inv.setItem(24, it);
			}

			if (inv != null) {
				ItemStack it = plugin.getItemAPI().createItem(player.getName(),
						"url;http://textures.minecraft.net/texture/afd2400002ad9fbbbd0066941eb5b1a384ab9b0e48a178ee96e4d129a5208654");
				ItemMeta meta = it.getItemMeta();

				meta.setDisplayName("§c✘ §7Close §c✘");

				it.setItemMeta(meta);

				inv.setItem(40, it);
			}

			if (inv != null) {
				ItemStack it = plugin.getItemAPI().createItem(player.getName(),
						"url;http://textures.minecraft.net/texture/352723aa0637ea5204447be536151f529fe4906826d8c066c4dcc50079a64465");
				ItemMeta meta = it.getItemMeta();

				meta.setDisplayName("§a◇ §7Documentation Site §a◇");

				it.setItemMeta(meta);

				inv.setItem(39, it);
			}

			if (inv != null) {
				ItemStack it = plugin.getItemAPI().createItem(player.getName(), "name;MHf_Discord");
				ItemMeta meta = it.getItemMeta();

				meta.setDisplayName("§5☆ §7Our Discord Server §5☆");

				it.setItemMeta(meta);

				inv.setItem(41, it);
			}

		} else {
			plugin.getGUI().openInventory(player, 5, "§cNo Update Found", null, null, null);

			InventoryView inv = player.getOpenInventory();

			if (inv != null) {
				ItemStack it = plugin.getItemAPI().createItem(player.getName(), "BLACK_STAINED_GLASS_PANE");
				ItemMeta meta = it.getItemMeta();

				meta.setDisplayName("§8-/-");

				it.setItemMeta(meta);

				inv.setItem(36, it);
				inv.setItem(37, it);
				inv.setItem(38, it);
				inv.setItem(39, it);
				inv.setItem(40, it);
				inv.setItem(41, it);
				inv.setItem(42, it);
				inv.setItem(43, it);
				inv.setItem(44, it);

				inv.setItem(0, it);
				inv.setItem(1, it);
				inv.setItem(2, it);
				inv.setItem(3, it);
				inv.setItem(4, it);
				inv.setItem(5, it);
				inv.setItem(6, it);
				inv.setItem(7, it);
				inv.setItem(8, it);
			}

			if (inv != null) {
				ItemStack it = plugin.getItemAPI().createItem(player.getName(),
						"url;http://textures.minecraft.net/texture/ce1f3cc63c73a6a1dde72fe09c6ac5569376d7b61231bb740764368788cbf1fa");
				ItemMeta meta = it.getItemMeta();

				meta.setDisplayName("§e☆ §7Check for Updates §e☆");

				ArrayList<String> lore = new ArrayList<String>();

				lore.add("§7§oClick to check for new Updates...");

				meta.setLore(lore);

				it.setItemMeta(meta);

				inv.setItem(21, it);
			}

			if (inv != null) {
				ItemStack it = plugin.getItemAPI().createItem(player.getName(), "BARRIER");
				ItemMeta meta = it.getItemMeta();

				meta.setDisplayName("§cNo Update Found...");

				it.setItemMeta(meta);

				inv.setItem(23, it);
			}

			if (inv != null) {
				ItemStack it = plugin.getItemAPI().createItem(player.getName(),
						"url;http://textures.minecraft.net/texture/afd2400002ad9fbbbd0066941eb5b1a384ab9b0e48a178ee96e4d129a5208654");
				ItemMeta meta = it.getItemMeta();

				meta.setDisplayName("§c✘ §7Close §c✘");

				it.setItemMeta(meta);

				inv.setItem(40, it);
			}

			if (inv != null) {
				ItemStack it = plugin.getItemAPI().createItem(player.getName(),
						"url;http://textures.minecraft.net/texture/352723aa0637ea5204447be536151f529fe4906826d8c066c4dcc50079a64465");
				ItemMeta meta = it.getItemMeta();

				meta.setDisplayName("§a◇ §7Documentation Site §a◇");

				it.setItemMeta(meta);

				inv.setItem(39, it);
			}

			if (inv != null) {
				ItemStack it = plugin.getItemAPI().createItem(player.getName(), "name;MHf_Discord");
				ItemMeta meta = it.getItemMeta();

				meta.setDisplayName("§5☆ §7Our Discord Server §5☆");

				it.setItemMeta(meta);

				inv.setItem(41, it);
			}

		}

	}

	public void createJobRankingGUI(Player player, UpdateTypes t, Job job) {
		FileConfiguration cfg = plugin.getFileManager().getRankingPerJobConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);

		int size = cfg.getInt("Size");

		String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("PerJobRanking_Name"))
				.replaceAll("<job>", job.getDisplay(UUID));

		plugin.getGUI().openInventory(player, size, name, GUIType.JOB_RANKING, null, job);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_RANKING_JOB", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("PerJobRanking_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "PerJobRanking_Custom.",
				cfg.getStringList("PerJobRanking_Custom.List"), name, cfg, null);
		setJobRankingItems(inv_view, cfg, player, job);
	}

	public void updateJobRankingGUI(Player player, String name, JobsPlayer jb, Job job) {
		FileConfiguration cfg = plugin.getFileManager().getRankingPerJobConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				plugin.getGUI().setPlaceHolders(player, inv, cfg.getStringList("PerJobRanking_Place"), name);
				plugin.getGUI().setCustomitems(player, player.getName(), inv, "PerJobRanking_Custom.",
						cfg.getStringList("PerJobRanking_Custom.List"), name, cfg, null);
				setJobRankingItems(inv, cfg, player, job);
			}
		}.runTaskLater(plugin, 2);
	}

	public void setJobRankingItems(InventoryView inv, FileConfiguration cf, Player pl, Job job) {
		plugin.getExecutor().execute(() -> {

			String UUID = "" + pl.getUniqueId();
			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);

			PlayerDataAPI d = plugin.getPlayerDataAPI();

			if (d.getSettingData(UUID, "RANKING") == null) {
				d.createSettingData(UUID, "RANKING", cf.getString("Categories.PlayerDefaultCat").toUpperCase());
			}

			String current = d.getSettingData(UUID, "RANKING");

			if (inv != null) {

				String icon = null;
				String display = null;
				List<String> lore = null;

				int slot = cf.getInt("Categories.CatSlot");

				inv.setItem(slot, null);
				
				if (current.equalsIgnoreCase("TODAY")) {

					icon = cf.getString("Categories.Earnings_Today.Material");
					display = sp.getLanguage().getStringFromPath(sp.getUUID(),
							cf.getString("Categories.Earnings_Today.Display"));
					lore = sp.getLanguage().getListFromPath(sp.getUUID(),
							cf.getString("Categories.Earnings_Today.Lore"));

				} else if (current.equalsIgnoreCase("BLOCKS")) {

					icon = cf.getString("Categories.Destroyed_Blocks.Material");
					display = sp.getLanguage().getStringFromPath(sp.getUUID(),
							cf.getString("Categories.Destroyed_Blocks.Display"));
					lore = sp.getLanguage().getListFromPath(sp.getUUID(),
							cf.getString("Categories.Destroyed_Blocks.Lore"));

				} else if (current.equalsIgnoreCase("LEVEL")) {

					icon = cf.getString("Categories.Level.Material");
					display = sp.getLanguage().getStringFromPath(sp.getUUID(),
							cf.getString("Categories.Level.Display"));
					lore = sp.getLanguage().getListFromPath(sp.getUUID(), cf.getString("Categories.Level.Lore"));

				}

				if (icon != null) {

					if (cf.getBoolean("Categories.EnabledCategorieItem")) {

						ItemStack it = plugin.getItemAPI().createItem(pl.getName(), icon);
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


			List<String> d3 = plugin.getFileManager().getRankingPerJobConfig().getStringList("Categories.List");

			
			if (current.equalsIgnoreCase("TODAY")) {
 
				HashMap<Integer, String> F = plugin.getPlayerAPI().today_ranked.get(job);

				for (int i = 0; i != d3.size() + 1; i++) {

					if (d3.size() >= i) {

						int get = i + 1;

						int slot = 999;

						if (cf.contains("PerJobRankingl_Items." + get + ".Slot")) {
							slot = cf.getInt("PerJobRankingl_Items." + get + ".Slot");
						}

					 

						if (slot != 999) {
							
							if (pl != null) {

								ItemStack it = plugin.getItemAPI().createItem(pl.getName(),
										cf.getString("PerJobRankingl_Items.NoneFound.Material"));
								ItemMeta meta = it.getItemMeta();

								if (cf.contains("PerJobRankingl_Items.NoneFound.CustomModelData")) {
									meta.setCustomModelData(cf.getInt("PerJobRankingl_Items.NoneFound.CustomModelData"));
								}

								String dis = cf.getString("PerJobRankingl_Items.NoneFound.Display").replaceAll("<rank>",
										"" + get);

								meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

								it.setItemMeta(meta);

								inv.setItem(slot, it);
							}

							if (F.size() >= i) {

								String rank_uuid = plugin.getPlayerAPI().today_ranked.get(job).get(i);

								if (rank_uuid != null) {
 
									String name = plugin.getPlayerDataAPI().getDisplayByUUID(rank_uuid);

									String icon = null;

									if (cf.getBoolean("PerJobRankingl_Items." + get + ".UseSkullAsMaterial")) {
										icon = "name;<name>";
									} else {
										icon = cf.getString("PerJobRankingl_Items." + get + ".Material");
									}

									if (icon != null) {
										ItemStack it = plugin.getItemAPI().createItem(name, icon);
										ItemMeta meta = it.getItemMeta();

										if (cf.contains("PerJobRankingl_Items." + get + ".CustomModelData")) {
											meta.setCustomModelData(
													cf.getInt("PerJobRankingl_Items." + get + ".CustomModelData"));
										}

										String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
												cf.getString("PerJobRankingl_Items." + get + ".Display"));

										List<String> lore = sp.getLanguage().getListFromLanguage(sp.getUUID(),
												"PerJobRanking.LoreForRanks.Today");

										ArrayList<String> old = new ArrayList<String>();

										for (String b : lore) {
											old.add(plugin.getPluginManager().toHex(b)
													.replaceAll("<job>", job.getDisplay(UUID))
													.replaceAll("<earnings>",
															plugin.getAPI()
																	.Format(plugin.getPlayerAPI()
																			.getEarningsOfToday(rank_uuid, job)))
													.replaceAll("<rank>", "" + get).replaceAll("<name>", name));
										}

										if (rank_uuid.equalsIgnoreCase("" + pl.getUniqueId())) {
											if (cf.getBoolean("EnchantOwnSkullInRanking")) {
												meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
												meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
											}
										}

										meta.setLore(old);

										meta.setDisplayName(plugin.getPluginManager().toHex(dis)
												.replaceAll("<name>", name).replaceAll("&", "§"));

										it.setItemMeta(meta);

										inv.setItem(slot, it);
									}
								}

							}

						}
					}
				}

			} else 		if (current.equalsIgnoreCase("BLOCKS")) {
				
				HashMap<Integer, String> F = plugin.getPlayerAPI().blocks_ranked.get(job);

				for (int i = 0; i != d3.size() + 1; i++) {

					if (d3.size() >= i) {

						int get = i + 1;

						int slot = 999;

						if (cf.contains("PerJobRankingl_Items." + get + ".Slot")) {
							slot = cf.getInt("PerJobRankingl_Items." + get + ".Slot");
						}

					 

						if (slot != 999) {
							
							if (pl != null) {

								ItemStack it = plugin.getItemAPI().createItem(pl.getName(),
										cf.getString("PerJobRankingl_Items.NoneFound.Material"));
								ItemMeta meta = it.getItemMeta();

								if (cf.contains("PerJobRankingl_Items.NoneFound.CustomModelData")) {
									meta.setCustomModelData(cf.getInt("PerJobRankingl_Items.NoneFound.CustomModelData"));
								}

								String dis = cf.getString("PerJobRankingl_Items.NoneFound.Display").replaceAll("<rank>",
										"" + get);

								meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

								it.setItemMeta(meta);

								inv.setItem(slot, it);
							}

							if (F.size() >= i) {

								String rank_uuid = plugin.getPlayerAPI().today_ranked.get(job).get(i);

								if (rank_uuid != null) {
 
									String name = plugin.getPlayerDataAPI().getDisplayByUUID(rank_uuid);

									String icon = null;

									if (cf.getBoolean("PerJobRankingl_Items." + get + ".UseSkullAsMaterial")) {
										icon = "name;<name>";
									} else {
										icon = cf.getString("PerJobRankingl_Items." + get + ".Material");
									}

									if (icon != null) {
										ItemStack it = plugin.getItemAPI().createItem(name, icon);
										ItemMeta meta = it.getItemMeta();

										if (cf.contains("PerJobRankingl_Items." + get + ".CustomModelData")) {
											meta.setCustomModelData(
													cf.getInt("PerJobRankingl_Items." + get + ".CustomModelData"));
										}

										String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
												cf.getString("PerJobRankingl_Items." + get + ".Display"));

										List<String> lore = sp.getLanguage().getListFromLanguage(sp.getUUID(),
												"PerJobRanking.LoreForRanks.Blocks");

										ArrayList<String> old = new ArrayList<String>();

										for (String b : lore) {
											old.add(plugin.getPluginManager().toHex(b)
													.replaceAll("<job>", job.getDisplay(UUID))
													.replaceAll("<times>",
														""+	plugin.getPlayerAPI()
																			.getBrokenTimes(rank_uuid, job))
													.replaceAll("<rank>", "" + get).replaceAll("<name>", name));
										}

										if (rank_uuid.equalsIgnoreCase("" + pl.getUniqueId())) {
											if (cf.getBoolean("EnchantOwnSkullInRanking")) {
												meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
												meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
											}
										}

										meta.setLore(old);

										meta.setDisplayName(plugin.getPluginManager().toHex(dis)
												.replaceAll("<name>", name).replaceAll("&", "§"));

										it.setItemMeta(meta);

										inv.setItem(slot, it);
									}
								}

							}

						}
					}
				}
				
			} else 		if (current.equalsIgnoreCase("LEVEL")) {
				
				HashMap<Integer, String> F = plugin.getPlayerAPI().level_ranked.get(job);

				for (int i = 0; i != d3.size() + 1; i++) {

					if (d3.size() >= i) {

						int get = i + 1;

						int slot = 999;

						if (cf.contains("PerJobRankingl_Items." + get + ".Slot")) {
							slot = cf.getInt("PerJobRankingl_Items." + get + ".Slot");
						}

					 

						if (slot != 999) {
							
							if (pl != null) {

								ItemStack it = plugin.getItemAPI().createItem(pl.getName(),
										cf.getString("PerJobRankingl_Items.NoneFound.Material"));
								ItemMeta meta = it.getItemMeta();

								if (cf.contains("PerJobRankingl_Items.NoneFound.CustomModelData")) {
									meta.setCustomModelData(cf.getInt("PerJobRankingl_Items.NoneFound.CustomModelData"));
								}

								String dis = cf.getString("PerJobRankingl_Items.NoneFound.Display").replaceAll("<rank>",
										"" + get);

								meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

								it.setItemMeta(meta);

								inv.setItem(slot, it);
							}

							if (F.size() >= i) {

								String rank_uuid = plugin.getPlayerAPI().today_ranked.get(job).get(i);

								if (rank_uuid != null) {
 
									String name = plugin.getPlayerDataAPI().getDisplayByUUID(rank_uuid);

									String icon = null;

									if (cf.getBoolean("PerJobRankingl_Items." + get + ".UseSkullAsMaterial")) {
										icon = "name;<name>";
									} else {
										icon = cf.getString("PerJobRankingl_Items." + get + ".Material");
									}

									if (icon != null) {
										ItemStack it = plugin.getItemAPI().createItem(name, icon);
										ItemMeta meta = it.getItemMeta();

										if (cf.contains("PerJobRankingl_Items." + get + ".CustomModelData")) {
											meta.setCustomModelData(
													cf.getInt("PerJobRankingl_Items." + get + ".CustomModelData"));
										}

										String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
												cf.getString("PerJobRankingl_Items." + get + ".Display"));

										List<String> lore = sp.getLanguage().getListFromLanguage(sp.getUUID(),
												"PerJobRanking.LoreForRanks.Level");

										ArrayList<String> old = new ArrayList<String>();

										for (String b : lore) {
											old.add(plugin.getPluginManager().toHex(b)
													.replaceAll("<job>", job.getDisplay(UUID))
													.replaceAll("<level>",
														""+plugin.getPlayerAPI()
																			.getLevelOF(rank_uuid, job))
													.replaceAll("<rank>", "" + get).replaceAll("<name>", name));
										}

										if (rank_uuid.equalsIgnoreCase("" + pl.getUniqueId())) {
											if (cf.getBoolean("EnchantOwnSkullInRanking")) {
												meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
												meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
											}
										}

										meta.setLore(old);

										meta.setDisplayName(plugin.getPluginManager().toHex(dis)
												.replaceAll("<name>", name).replaceAll("&", "§"));

										it.setItemMeta(meta);

										inv.setItem(slot, it);
									}
								}

							}

						}
					}
				}
				
			}

		});
	}

	public void createGlobalRankingGUI(Player player, UpdateTypes t) {
		FileConfiguration cfg = plugin.getFileManager().getRankingGlobalConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);

		int size = cfg.getInt("Size");

		String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("GlobalRanking_Name"));

		plugin.getGUI().openInventory(player, size, name, GUIType.GLOBAL_RANKING, null, null);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_RANKING_GLOBAL", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Global_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Global_Custom.",
				cfg.getStringList("Global_Custom.List"), name, cfg, null);
		setGlobalRankingItems(inv_view, cfg, player);
	}

	public void setGlobalRankingItems(InventoryView inv, FileConfiguration cf, Player pl) {
		plugin.getExecutor().execute(() -> {

			String UUID = "" + pl.getUniqueId();
			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);

			int d = plugin.getPlayerAPI().ranked_points.size() + 1;

			List<String> a1 = plugin.getFileManager().getRankingGlobalConfig()
					.getStringList("Global_RankingItems.List");

			if (cf.getBoolean("Global_RankingItems.OwnSkullItem.Enabled")) {

				ItemStack it = plugin.getItemAPI().createItem(pl.getName(), "name;<name>");
				ItemMeta meta = it.getItemMeta();

				if (cf.contains("Global_RankingItems.OwnSkullItem.CustomModelData")) {
					meta.setCustomModelData(cf.getInt("Global_RankingItems.OwnSkullItem.CustomModelData"));
				}

				String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
						cf.getString("Global_RankingItems.OwnSkullItem.Display"));
				List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
						cf.getString("Global_RankingItems.OwnSkullItem.Lore"));

				ArrayList<String> old = new ArrayList<String>();

				String name = pl.getName();
				double points = plugin.getPlayerAPI().getPoints("" + pl.getUniqueId());

				for (String b : lore) {
					old.add(plugin.getPluginManager().toHex(b).replaceAll("<points>", plugin.getAPI().Format(points))
							.replaceAll("<rank>",
									"" + plugin.getPlayerAPI().getRankOfGlobalPlayer("" + pl.getUniqueId()))
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

						ItemStack it = plugin.getItemAPI().createItem(pl.getName(),
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
						String name = plugin.getPlayerDataAPI().getDisplayByUUID(rank_uuid);

						if (name != null) {
							ItemStack it = plugin.getItemAPI().createItem(name, icon);
							ItemMeta meta = it.getItemMeta();

							if (cf.contains("Global_RankingItems." + r + ".CustomModelData")) {
								meta.setCustomModelData(cf.getInt("Global_RankingItems." + r + ".CustomModelData"));
							}

							String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
									cf.getString("Global_RankingItems." + r + ".Display"));
							List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
									cf.getString("Global_RankingItems." + r + ".Lore"));

							ArrayList<String> old = new ArrayList<String>();

							for (String b : lore) {
								old.add(plugin.getPluginManager().toHex(b)
										.replaceAll("<points>", plugin.getAPI().Format(points))
										.replaceAll("<name>", name));
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
		});
	}

	public void createEarningsGUI_Single_Job(Player player, UpdateTypes t, Job job) {
		FileConfiguration cfg = plugin.getFileManager().getEarningsJobConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Job_Earnings_Name"))
				.replaceAll("<job>", job.getDisplay("" + player.getUniqueId()));
		int size = cfg.getInt("Job_Earnings_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.EARNINGS_JOB, null, job);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_JOB_EARNINGS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Job_Earnings_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Job_Earnings_Custom.",
				cfg.getStringList("Job_Earnings_Custom.List"), name, cfg, job);
		setEarningsItems_Single(inv_view, cfg, player, job);
	}

	public void updateEarningsGUI_Single_Job(Player player, String name, JobsPlayer jb, Job job) {
		FileConfiguration cfg = plugin.getFileManager().getEarningsJobConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				plugin.getGUI().setCustomitems(player, player.getName(), inv, "Job_Earnings_Custom.",
						cfg.getStringList("Job_Earnings_Custom.List"), name, cfg, job);
				setEarningsItems_Single(inv, cfg, player, job);
			}
		}.runTaskLater(plugin, 2);
	}

	public void setEarningsItems_Single(InventoryView inv, FileConfiguration cf, Player pl, Job job) {
		plugin.getExecutor().execute(() -> {

			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + pl.getUniqueId());
			int page = plugin.getPlayerDataAPI().getPageFromID(sp.getUUIDAsString(), "EARNINGS_" + job.getConfigID());

			int days = cf.getInt("HowManyDays");

			ArrayList<String> dates = new ArrayList<String>();

			for (int i = 0; i != days; i++) {

				DateFormat format = new SimpleDateFormat(plugin.getFileManager().getConfig().getString("Date"));
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

							String dis = sp.getLanguage().getStringFromLanguage(pl.getUniqueId(),
									"Job_Earnings_Items.Display");
							List<String> lore = sp.getLanguage().getListFromLanguage(pl.getUniqueId(),
									"Job_Earnings_Items.Lore");

							String ic = cf.getString("Job_Earnings_Items.Icon");

							ItemStack it = plugin.getItemAPI().createItem(pl.getName(), ic);
							ItemMeta meta = it.getItemMeta();

							meta.setDisplayName(dis.replaceAll("<date>", date).replaceAll("<job>",
									job.getDisplay("" + pl.getUniqueId())));

							ArrayList<String> l = new ArrayList<String>();

							double er = plugin.getPlayerAPI().getEarnedAt("" + pl.getUniqueId(), job, date);

							for (String b : lore) {
								l.add(plugin.getPluginManager().toHex(b).replaceAll("<date>", date)
										.replaceAll("<job>", job.getDisplay("" + pl.getUniqueId()))
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

						ItemStack it = plugin.getItemAPI().createItem(pl.getName(), icon);
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

				ItemStack it = plugin.getItemAPI().createItem(pl.getName(), icon);
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
						String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
								cf.getString("PageItems.Previous.Display"));
						List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
								cf.getString("PageItems.Previous.Lore"));
						int slot = cf.getInt("PageItems.Previous.Slot");

						ItemStack it = plugin.getItemAPI().createItem(pl.getName(), icon);
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
						String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
								cf.getString("PageItems.Next.Display"));
						List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
								cf.getString("PageItems.Next.Lore"));
						int slot = cf.getInt("PageItems.Next.Slot");

						ItemStack it = plugin.getItemAPI().createItem(pl.getName(), icon);
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

		});
	}

	public void createEarningsGUI_ALL_Jobs(Player player, UpdateTypes t) {
		FileConfiguration cfg = plugin.getFileManager().getEarningsAllConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("All_Earnings_Name"));
		int size = cfg.getInt("All_Earnings_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.EARNINGS_ALL, null, null);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_ALL_EARNINGS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("All_Earnings_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "All_Earnings_Custom.",
				cfg.getStringList("All_Earnings_Custom.List"), name, cfg, null);
		setEarningsItems_ALL(inv_view, cfg, player);
	}

	public void updateEarningsGUI_All(Player player, String name, JobsPlayer jb) {
		FileConfiguration cfg = plugin.getFileManager().getEarningsAllConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				plugin.getGUI().setCustomitems(player, player.getName(), inv, "All_Earnings_Custom.",
						cfg.getStringList("All_Earnings_Custom.List"), name, cfg, null);
				setEarningsItems_ALL(inv, cfg, player);
			}
		}.runTaskLater(plugin, 2);
	}

	public void setEarningsItems_ALL(InventoryView inv, FileConfiguration cf, Player pl) {
		plugin.getExecutor().execute(() -> {

			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + pl.getUniqueId());
			int page = plugin.getPlayerDataAPI().getPageFromID(sp.getUUIDAsString(), "EARNINGS_ALL");

			int days = cf.getInt("HowManyDays");

			ArrayList<String> dates = new ArrayList<String>();

			for (int i = 0; i != days; i++) {

				DateFormat format = new SimpleDateFormat(plugin.getFileManager().getConfig().getString("Date"));
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

						String dis = sp.getLanguage().getStringFromLanguage(pl.getUniqueId(),
								"All_Earnings_Items.Display");
						List<String> lore = sp.getLanguage().getListFromLanguage(pl.getUniqueId(),
								"All_Earnings_Items.Lore");

						String ic = cf.getString("All_Earnings_Items.Icon");

						ItemStack it = plugin.getItemAPI().createItem(pl.getName(), ic);
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

					ItemStack it = plugin.getItemAPI().createItem(pl.getName(), icon);
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
						String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
								cf.getString("PageItems.Previous.Display"));
						List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
								cf.getString("PageItems.Previous.Lore"));
						int slot = cf.getInt("PageItems.Previous.Slot");

						ItemStack it = plugin.getItemAPI().createItem(pl.getName(), icon);
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
						String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
								cf.getString("PageItems.Next.Display"));
						List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
								cf.getString("PageItems.Next.Lore"));
						int slot = cf.getInt("PageItems.Next.Slot");

						ItemStack it = plugin.getItemAPI().createItem(pl.getName(), icon);
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

		});
	}

	public ArrayList<String> getAmountToDisplay_SingleJob(FileConfiguration cf, Player pl, int page, Job job) {
		int days = cf.getInt("HowManyDays");

		if (plugin.getPlayerAPI().getRealJobPlayer(pl.getUniqueId()).getStatsList().containsKey(job.getConfigID())) {
			ArrayList<String> dates = new ArrayList<String>();

			for (int i = 0; i != days; i++) {

				DateFormat format = new SimpleDateFormat(plugin.getFileManager().getConfig().getString("Date"));
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

			DateFormat format = new SimpleDateFormat(plugin.getFileManager().getConfig().getString("Date"));
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
		FileConfiguration cfg = plugin.getFileManager().getLeaveConfirmConfig();

		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());

		String name = sp.getLanguage().getStringFromPath(player.getUniqueId(),
				cfg.getString("LeaveConfirm_Name").replaceAll("<job>", job.getDisplay(sp.getUUIDAsString())));
		int size = cfg.getInt("LeaveConfirm_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.CONFIRM_LEAVE, null, job);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_LEAVE_CONFIRM", player);
		}
		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("LeaveConfirm_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "LeaveConfirm_Custom.",
				cfg.getStringList("LeaveConfirm_Custom.List"), name, cfg, null);
		setLeaveConfirmItems(player, name, inv_view, job);
	}

	public void setLeaveConfirmItems(Player player, String tit, InventoryView inv, Job job) {
		FileConfiguration cfg = plugin.getFileManager().getLeaveConfirmConfig();
		plugin.getExecutor().execute(() -> {
			String UUID = "" + player.getUniqueId();
			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
			if (player != null) {

				if (cfg.getBoolean("LeaveConfirmItems.Button_YES.Show")) {

					ItemStack item = plugin.getItemAPI().createItem(player,
							cfg.getString("LeaveConfirmItems.Button_YES.Icon"));

					String dis = plugin.getPluginManager().toHex(sp.getLanguage().getStringFromPath(sp.getUUID(),
							cfg.getString("LeaveConfirmItems.Button_YES.Display"))).replaceAll("&", "§");
					int slot = cfg.getInt("LeaveConfirmItems.Button_YES.Slot");
					List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
							cfg.getString("LeaveConfirmItems.Button_YES.Lore"));
					ArrayList<String> l = new ArrayList<String>();

					ItemMeta meta = item.getItemMeta();

					for (String line : lore) {
						l.add(plugin.getPluginManager().toHex(line)
								.replaceAll("<job>", job.getDisplay(sp.getUUIDAsString())).replaceAll("&", "§"));
					}

					if (cfg.contains("LeaveConfirmItems.Button_YES.CustomModelData")) {
						meta.setCustomModelData(cfg.getInt("LeaveConfirmItems.Button_YES.CustomModelData"));
					}

					meta.setDisplayName(dis.replaceAll("<job>", job.getDisplay(sp.getUUIDAsString())));

					meta.setLore(l);

					item.setItemMeta(meta);

					inv.setItem(slot, item);
				}

			}

			if (player != null) {
				if (cfg.getBoolean("LeaveConfirmItems.Button_NO.Show")) {
					ItemStack item = plugin.getItemAPI().createItem(player,
							cfg.getString("LeaveConfirmItems.Button_NO.Icon"));

					String dis = plugin.getPluginManager().toHex(sp.getLanguage().getStringFromPath(sp.getUUID(),
							cfg.getString("LeaveConfirmItems.Button_NO.Display"))).replaceAll("&", "§");
					int slot = cfg.getInt("LeaveConfirmItems.Button_NO.Slot");
					List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
							cfg.getString("LeaveConfirmItems.Button_NO.Lore"));
					ArrayList<String> l = new ArrayList<String>();

					ItemMeta meta = item.getItemMeta();

					for (String line : lore) {
						l.add(plugin.getPluginManager().toHex(line)
								.replaceAll("<job>", job.getDisplay(sp.getUUIDAsString())).replaceAll("&", "§"));
					}

					if (cfg.contains("LeaveConfirmItems.Button_NO.CustomModelData")) {
						meta.setCustomModelData(cfg.getInt("LeaveConfirmItems.Button_NO.CustomModelData"));
					}

					meta.setDisplayName(dis.replaceAll("<job>", job.getDisplay(sp.getUUIDAsString())));

					meta.setLore(l);

					item.setItemMeta(meta);

					inv.setItem(slot, item);
				}
			}

		});
	}

	public void createWithdrawConfigGUI(Player player, UpdateTypes t) {
		FileConfiguration cfg = plugin.getFileManager().getWithdrawConfirmConfig();

		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());

		String name = sp.getLanguage().getStringFromPath(player.getUniqueId(), cfg.getString("ConfirmWithdraw_Name"));
		int size = cfg.getInt("ConfirmWithdraw_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.CONFIRM_WITHDRAW, null, null);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_WITHDRAW_CONFIRM", player);
		}
		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("ConfirmWithdraw_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "ConfirmWithdraw_Custom.",
				cfg.getStringList("ConfirmWithdraw_Custom.List"), name, cfg, null);
		setWithdrawConfigItems(player, name, inv_view);
	}

	public void setWithdrawConfigItems(Player player, String tit, InventoryView inv) {
		FileConfiguration cfg = plugin.getFileManager().getWithdrawConfirmConfig();
		plugin.getExecutor().execute(() -> {
			String UUID = "" + player.getUniqueId();
			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
			if (player != null) {

				if (cfg.getBoolean("ConfirmWithdrawItems.Button_YES.Show")) {

					ItemStack item = plugin.getItemAPI().createItem(player,
							cfg.getString("ConfirmWithdrawItems.Button_YES.Icon"));

					String dis = plugin.getPluginManager().toHex(sp.getLanguage().getStringFromPath(sp.getUUID(),
							cfg.getString("ConfirmWithdrawItems.Button_YES.Display"))).replaceAll("&", "§");
					int slot = cfg.getInt("ConfirmWithdrawItems.Button_YES.Slot");
					List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
							cfg.getString("ConfirmWithdrawItems.Button_YES.Lore"));
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
					ItemStack item = plugin.getItemAPI().createItem(player,
							cfg.getString("ConfirmWithdrawItems.Button_NO.Icon"));

					String dis = plugin.getPluginManager().toHex(sp.getLanguage().getStringFromPath(sp.getUUID(),
							cfg.getString("ConfirmWithdrawItems.Button_NO.Display"))).replaceAll("&", "§");
					int slot = cfg.getInt("ConfirmWithdrawItems.Button_NO.Slot");
					List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
							cfg.getString("ConfirmWithdrawItems.Button_NO.Lore"));
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

		});
	}

	public void createWithdrawMenu(Player player, UpdateTypes t) {
		FileConfiguration cfg = plugin.getFileManager().getWithdrawConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Withdraw_Name"));
		int size = cfg.getInt("Withdraw_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.WITHDRAW, null, null);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_WITHDRAW_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Withdraw_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Withdraw_Custom.",
				cfg.getStringList("Withdraw_Custom.List"), name, cfg, null);
		setWithdrawItems(inv_view, player, sp);
	}

	public void updateWithdrawGUI(Player player, String name, JobsPlayer jb) {
		FileConfiguration cfg = plugin.getFileManager().getWithdrawConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				plugin.getGUI().setCustomitems(player, player.getName(), inv, "Withdraw_Custom.",
						cfg.getStringList("Withdraw_Custom.List"), name, cfg, null);
				setWithdrawItems(inv, player, jb);
			}
		}.runTaskLater(plugin, 2);
	}

	public void setWithdrawItems(InventoryView inv, Player player, JobsPlayer jb) {
		plugin.getExecutor().execute(() -> {

			FileConfiguration cfg = plugin.getFileManager().getWithdrawConfig();
			YamlConfiguration lg = jb.getLanguage().getConfig();

			if (jb.getSalary() == 0) {

				if (inv != null) {

					if (cfg.getBoolean("Withdraw_Items.NoSalaryToCollect.Show")) {

						String icon = cfg.getString("Withdraw_Items.NoSalaryToCollect.Material");
						int slot = cfg.getInt("Withdraw_Items.NoSalaryToCollect.Slot");
						String dis = lg.getString("Withdraw_Custom.NoSalaryToCollect.Display");

						ItemStack it = plugin.getItemAPI().createItem(player.getName(), icon);
						ItemMeta meta = it.getItemMeta();

						meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

						if (cfg.contains("Withdraw_Items.NoSalaryToCollect.CustomModelData")) {
							meta.setCustomModelData(cfg.getInt("Withdraw_Items.NoSalaryToCollect.CustomModelData"));
						}

						if (cfg.getBoolean("Withdraw_Items.NoSalaryToCollect.ShowLore")) {

							List<String> lore = lg.getStringList("Withdraw_Custom.NoSalaryToCollect.Lore");

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
						String dis = lg.getString("Withdraw_Custom.Info.Display");

						ItemStack it = plugin.getItemAPI().createItem(player.getName(), icon);
						ItemMeta meta = it.getItemMeta();

						meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

						if (cfg.contains("Withdraw_Items.Info.CustomModelData")) {
							meta.setCustomModelData(cfg.getInt("Withdraw_Items.Info.CustomModelData"));
						}

						if (cfg.getBoolean("Withdraw_Items.Info.ShowLore")) {

							List<String> lore = lg.getStringList("Withdraw_Custom.Info.Lore");

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
						String dis = lg.getString("Withdraw_Custom.CollectButton.Display");

						ItemStack it = plugin.getItemAPI().createItem(player.getName(), icon);
						ItemMeta meta = it.getItemMeta();

						meta.setDisplayName(plugin.getPluginManager().toHex(dis).replaceAll("&", "§"));

						if (cfg.contains("Withdraw_Items.CollectButton.CustomModelData")) {
							meta.setCustomModelData(cfg.getInt("Withdraw_Items.CollectButton.CustomModelData"));
						}

						if (cfg.getBoolean("Withdraw_Items.CollectButton.ShowLore")) {

							List<String> lore1 = lg.getStringList("Withdraw_Custom.CollectButton.LoreCanCollect");
							List<String> lore2 = lg.getStringList("Withdraw_Custom.CollectButton.LoreCantCollect");

							ArrayList<String> l = new ArrayList<String>();

							if (plugin.getAPI().canWithdrawMoney(player, jb)) {
								if (lore1 != null) {
									for (String b : lore1) {
										l.add(plugin.getPluginManager().toHex(b).replaceAll("&", "§")
												.replaceAll("<salary>", plugin.getAPI().Format(jb.getSalary())));
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

		});
	}

	public void createLevelsGUI(Player player, UpdateTypes t, Job job) {
		FileConfiguration cfg = plugin.getFileManager().getLevelGUIConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Levels_Name")).replaceAll("<job>",
				job.getDisplay(UUID));
		int size = cfg.getInt("Levels_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.LEVELS, null, job);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_LEVELS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Levels_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Levels_Custom.",
				cfg.getStringList("Levels_Custom.List"), name, cfg, null);
		setLevelsItems(inv_view, name, cfg, player, job);
	}

	public void updateLevelsGUI(Player player, String name, JobsPlayer jb, Job job) {
		FileConfiguration cfg = plugin.getFileManager().getLevelGUIConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				plugin.getGUI().setCustomitems(player, player.getName(), inv, "Levels_Custom.",
						cfg.getStringList("Levels_Custom.List"), name, cfg, null);
				setLevelsItems(inv, name, cfg, player, job);
			}
		}.runTaskLater(plugin, 2);
	}

	public void setLevelsItems(InventoryView inv, String name, FileConfiguration cf, Player pl, Job job) {
		plugin.getExecutor().execute(() -> {

			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + pl.getUniqueId());
			int page = plugin.getPlayerDataAPI().getPageFromID(sp.getUUIDAsString(), "LEVELS_" + job.getConfigID());

			List<String> slots = cf.getStringList("Level_Slots");

			int li = job.getCountOfLevels();

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

								ArrayList<String> lore = new ArrayList<String>();

								JobStats stats = sp.getStatsOf(job.getConfigID());

								String ic = null;
								int SPLEVEL = stats.getLevel();

								boolean is = SPLEVEL + 1 == lvl;

								int iii = 0;

								if (!cf.getBoolean("Options.UseLevelIconsOrCustom")) {

									if (is) {
										iii = cf.getInt("Options.Icons.CurrentlyWorkingOnOption.CustomModelData");
										ic = cf.getString("Options.Icons.CurrentlyWorkingOn");
									} else if (SPLEVEL >= lvl) {
										iii = cf.getInt("Options.Icons.ReachedOption.CustomModelData");
										ic = cf.getString("Options.Icons.Reached");
									} else {
										iii = cf.getInt("Options.Icons.NotReachedOption.CustomModelData");
										ic = cf.getString("Options.Icons.NotReached");
									}
								} else {
									ic = job.getIconOfLevel(lvl);
									iii = job.getModelData();
								}

								ItemStack it = plugin.getItemAPI().createItem(pl.getName(), ic);
								ItemMeta meta = it.getItemMeta();

								meta.setCustomModelData(iii);

								meta.setDisplayName(plugin.getPluginManager()
										.toHex(job.getLevelDisplay(lvl, "" + pl.getUniqueId())).replaceAll("&", "§"));

								if (SPLEVEL >= lvl) {
									if (cf.getBoolean("Options.EnchantedWhenReached")) {
										meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
									}
								}

								if (job.getLevelLore(lvl, "" + pl.getUniqueId()) != null) {
									for (String line : job.getLevelLore(lvl, "" + pl.getUniqueId())) {
										lore.add(plugin.getPluginManager().toHex(line));
									}
								}

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

								for (String line : sp.getLanguage().getListFromLanguage(pl.getUniqueId(),
										"Rewards_Item_Lores." + prefix)) {
									lore.add(plugin.getPluginManager().toHex(line).replaceAll("<exp>",
											plugin.getAPI().Format(calc)));
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
						String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
								cf.getString("PageItems.Previous.Display"));
						List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
								cf.getString("PageItems.Previous.Lore"));
						int slot = cf.getInt("PageItems.Previous.Slot");

						ItemStack it = plugin.getItemAPI().createItem(pl.getName(), icon);
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
						String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
								cf.getString("PageItems.Next.Display"));
						List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
								cf.getString("PageItems.Next.Lore"));
						int slot = cf.getInt("PageItems.Next.Slot");

						ItemStack it = plugin.getItemAPI().createItem(pl.getName(), icon);
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

		});
	}

	public void createRewardsGUI(Player player, UpdateTypes t, Job job) {
		FileConfiguration cfg = plugin.getFileManager().getRewardsConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Rewards_Name"))
				.replaceAll("<job>", job.getDisplay(UUID));
		int size = cfg.getInt("Rewards_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.REWARDS, null, job);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_REWARDS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Rewards_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Rewards_Custom.",
				cfg.getStringList("Rewards_Custom.List"), name, cfg, null);
		setRewardsItems(inv_view, name, cfg, player, job);
	}

	public void updateRewardsGUI(Player player, String name, JobsPlayer jb, Job job) {
		FileConfiguration cfg = plugin.getFileManager().getRewardsConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				plugin.getGUI().setCustomitems(player, player.getName(), inv, "Rewards_Custom.",
						cfg.getStringList("Rewards_Custom.List"), name, cfg, null);
				setRewardsItems(inv, name, cfg, player, job);
			}
		}.runTaskLater(plugin, 2);
	}

	public void setRewardsItems(InventoryView inv, String name, FileConfiguration cf, Player pl, Job job) {
		plugin.getExecutor().execute(() -> {

			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + pl.getUniqueId());

			int page = plugin.getPlayerDataAPI().getPageFromID(sp.getUUIDAsString(), "REWARDS_" + job.getConfigID());

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
						String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
								cf.getString("PageItems.Previous.Display"));
						List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
								cf.getString("PageItems.Previous.Lore"));
						int slot = cf.getInt("PageItems.Previous.Slot");

						ItemStack it = plugin.getItemAPI().createItem(pl.getName(), icon);
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
						String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
								cf.getString("PageItems.Next.Display"));
						List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
								cf.getString("PageItems.Next.Lore"));
						int slot = cf.getInt("PageItems.Next.Slot");

						ItemStack it = plugin.getItemAPI().createItem(pl.getName(), icon);
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

					String icon = job.getConfig().getString("IDS." + real.toString() + "." + type + ".RewardsGUI.Icon");
					String display = sp.getLanguage().getConfig()
							.getString("Jobs." + job.getConfigID() + ".IDS." + type + ".Rewards.Display");

					ItemStack i2 = plugin.getItemAPI().createItem(pl, icon);
					ItemMeta m = i2.getItemMeta();

					if (cf.contains("Jobs." + job.getConfigID() + ".IDS." + type + ".Rewards.CustomModelData")) {
						m.setCustomModelData(
								cf.getInt("Jobs." + job.getConfigID() + ".IDS." + type + ".Rewards.CustomModelData"));
					}

					ArrayList<String> l = new ArrayList<String>();

					String used = null;

					if (display == null) {
						Bukkit.getConsoleSender().sendMessage("§4Missing Display-Name on RewardsGUI from ID " + type);
						used = "§cNot Found";
					} else {
						used = display;
					}

					m.setDisplayName(plugin.getPluginManager().toHex(used).replaceAll("&", "§"));

					double reward = job.getRewardOf(type, real);
					int chance = job.getChanceOf(type, real);
					double points = job.getPointsOf(type, real);
					double exp = job.getExpOf(type, real);

					int brokentimes = plugin.getPlayerAPI().getBrokenTimesOfID("" + pl.getUniqueId(), job, type,
							real.toString());
					double ear = plugin.getPlayerAPI().getEarnedFrom("" + pl.getUniqueId(), job, type, real.toString());

					for (String line : sp.getLanguage().getListFromLanguage(sp.getUUID(),
							"Jobs." + job.getConfigID() + ".IDS." + type + ".Rewards.Lore")) {
						l.add(plugin.getPluginManager().toHex(line).replaceAll("<exp>", "" + exp)
								.replaceAll("<points>", "" + points).replaceAll("<earned>", plugin.getAPI().Format(ear))
								.replaceAll("<action>", real.toString().toLowerCase())
								.replaceAll("<chance>", "" + chance).replaceAll("<times>", "" + brokentimes)
								.replaceAll("<money>", "" + reward).replaceAll("&", "§"));
					}

					m.setLore(l);

					i2.setItemMeta(m);

					inv.setItem(Integer.valueOf(slots.get(i3)), i2);
				}

			}

		});
	}

	public void createSelfStatsGUI(Player player, UpdateTypes t) {
		FileConfiguration cfg = plugin.getFileManager().getStatsConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Self_Name"));
		int size = cfg.getInt("Self_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.STATS_SELF, null, null);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_SELF_STATS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Self_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Self_Custom.",
				cfg.getStringList("Self_Custom.List"), name, cfg, null);
		setStatsItems(inv_view, name, cfg, UUID, player.getName(), player, "Self");
	}

	public void updateSelfUpdateGUI(Player player, String name, JobsPlayer jb) {
		FileConfiguration cfg = plugin.getFileManager().getStatsConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				plugin.getGUI().setCustomitems(player, player.getName(), inv, "Self_Custom.",
						cfg.getStringList("Self_Custom.List"), name, cfg, null);
				setStatsItems(inv, name, cfg, "" + player.getUniqueId(), player.getName(), player, "Self");
			}
		}.runTaskLater(plugin, 2);
	}

	public void createOtherStatsGUI(Player player, UpdateTypes t, String named, String ud) {
		FileConfiguration cfg = plugin.getFileManager().getStatsConfig();

		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Other_Name")).replaceAll("<name>",
				named);
		int size = cfg.getInt("Other_Size");

		plugin.getGUI().openInventory(player, size, name, GUIType.STATS_OTHER, named, null);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_OTHER_STATS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Other_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Other_Custom.",
				cfg.getStringList("Other_Custom.List"), name, cfg, null);
		setStatsItems(inv_view, name, cfg, ud, named, player, "Other");
	}

	public void updateOtherStatsGUI(Player player, String name, JobsPlayer jb, String ud) {
		FileConfiguration cfg = plugin.getFileManager().getStatsConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				plugin.getGUI().setCustomitems(player, player.getName(), inv, "Other_Custom.",
						cfg.getStringList("Other_Custom.List"), name, cfg, null);
				setStatsItems(inv, name, cfg, ud, name, player, "Other");
			}
		}.runTaskLater(plugin, 2);
	}

	public void setStatsItems(InventoryView inv, String name, FileConfiguration cf, String WATCHUUID, String NAME,
			Player pl, String prefix) {
		plugin.getExecutor().execute(() -> {
			String title = pl.getOpenInventory().getTitle();
			String need = plugin.getPluginManager().toHex(name).replaceAll("&", "§");

			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + pl.getUniqueId());

			if (title.equalsIgnoreCase(need)) {

				// informations

				double points = 0;
				int max = 0;
				String mode = cf.getString("DisplayMode").toUpperCase();

				List<String> li = null;

				String how = api.isCurrentlyInCache(WATCHUUID);

				if (how.equalsIgnoreCase("CACHE")) {
					JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(WATCHUUID);
					max = jb.getMaxJobs();
					points = jb.getPoints();

					if (mode.equalsIgnoreCase("CURRENT")) {
						li = jb.getCurrentJobs();
					} else if (mode.equalsIgnoreCase("OWNED")) {
						li = jb.getOwnJobs();
					}

				} else {
					PlayerDataAPI plm = plugin.getPlayerDataAPI();
					max = plm.getMaxJobs(WATCHUUID);
					points = plm.getPoints(WATCHUUID);

					if (mode.equalsIgnoreCase("CURRENT")) {
						li = plm.getCurrentJobs(WATCHUUID);
					} else if (mode.equalsIgnoreCase("OWNED")) {
						li = plm.getOwnedJobs(WATCHUUID);
					}
				}

				if (cf.getString(prefix + "_Skull.Material") != null) {
					String skull_item = cf.getString(prefix + "_Skull.Material");
					String skull_display = sp.getLanguage().getStringFromPath(pl.getUniqueId(),
							cf.getString(prefix + "_Skull.Display"));
					int skull_slot = cf.getInt(prefix + "_Skull.Slot");
					List<String> skull_lore = sp.getLanguage().getListFromPath(pl.getUniqueId(),
							cf.getString(prefix + "_Skull.Lore"));

					ItemStack skull = plugin.getItemAPI().createItem(NAME, skull_item);
					ItemMeta m = skull.getItemMeta();

					if (cf.contains(prefix + "_Skull.CustomModelData")) {
						m.setCustomModelData(cf.getInt(prefix + "_Skull.CustomModelData"));
					}

					ArrayList<String> l = new ArrayList<String>();

					int finalmaxjobs = max + 1;

					for (String b : skull_lore) {
						l.add(plugin.getPluginManager().toHex(b).replaceAll("<points>", api.Format(points))
								.replaceAll("<max>", "" + finalmaxjobs).replaceAll("<name>", NAME)
								.replaceAll("&", "§"));
					}

					m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
					m.addItemFlags(ItemFlag.HIDE_ENCHANTS);

					m.setLore(l);

					m.setDisplayName(plugin.getPluginManager().toHex(skull_display).replaceAll("<name>", NAME)
							.replaceAll("&", "§"));

					skull.setItemMeta(m);

					inv.setItem(skull_slot, skull);
				}

				List<String> slots = cf.getStringList(prefix + "_Slots");

				for (int i = 0; i < slots.size(); i++) {

					if (i >= li.size()) {

						String error_item = cf.getString(prefix + "_Mot_Found_Item.Icon");
						String error_display = sp.getLanguage().getStringFromPath(pl.getUniqueId(),
								cf.getString(prefix + "_Mot_Found_Item.Display"));
						List<String> errorl_lore = sp.getLanguage().getListFromPath(pl.getUniqueId(),
								cf.getString(prefix + "_Mot_Found_Item.Lore"));

						ItemStack error = plugin.getItemAPI().createItem(pl, error_item);
						ItemMeta m = error.getItemMeta();

						if (cf.contains(prefix + "_Mot_Found_Item.CustomModelData")) {
							m.setCustomModelData(cf.getInt(prefix + "_Mot_Found_Item.CustomModelData"));
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

					} else {

						Job job = plugin.getJobCache().get(li.get(i));
						String id = job.getConfigID();

						// loading stats

						int level = 0;
						double exp = 0;
						String bought = null;
						Integer broken = 0;

						if (how.equalsIgnoreCase("CACHE")) {
							JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(WATCHUUID);

							JobStats stats = jb.getStatsOf(job.getConfigID());

							level = stats.getLevel();
							exp = stats.getExp();
							bought = stats.getDate();
							broken = stats.getBrokenTimes();

						} else {

							PlayerDataAPI plm = plugin.getPlayerDataAPI();

							level = plm.getLevelOf(WATCHUUID, job.getConfigID());
							exp = plm.getExpOf(WATCHUUID, job.getConfigID());
							bought = plm.getDateOf(WATCHUUID, job.getConfigID());
							broken = plm.getBrokenOf(WATCHUUID, job.getConfigID());

						}

						String usedbuy = "";
						String lvl = job.getLevelDisplay(level, WATCHUUID);
						String usedlvl = "";

						String item = job.getIcon();
						String display = sp.getLanguage().getConfig()
								.getString("Jobs." + job.getConfigID().toUpperCase() + ".StatsGUI.Display");
						List<String> lore = sp.getLanguage().getConfig()
								.getStringList("Jobs." + job.getConfigID().toUpperCase() + ".StatsGUI.Lore." + prefix);

						ItemStack it = plugin.getItemAPI().createItem(pl, item);
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
										.replace("<earned>",
												"" + api.Format(plugin.getPlayerDataAPI().getEarnedAt(WATCHUUID, id,
														plugin.getDate())))
										.replaceAll("<stats_args_3>", "" + level)
										.replaceAll("<stats_args_2>", "" + broken)
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

						m.setDisplayName(plugin.getPluginManager().toHex(display).replaceAll("<name>", NAME)
								.replaceAll("&", "§"));

						it.setItemMeta(m);

						inv.setItem(Integer.valueOf(slots.get(i)), it);

					}

				}

			}
		});
	}

}
