package de.warsteiner.jobs.api;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.plugins.WorldGuardManager;
import de.warsteiner.jobs.manager.PluginManager;
import de.warsteiner.jobs.utils.BossBarHandler;
import de.warsteiner.jobs.utils.objects.DataMode;
import de.warsteiner.jobs.utils.objects.Language;
import de.warsteiner.jobs.utils.objects.PluginColor;
import de.warsteiner.jobs.utils.objects.items.ItemType;
import de.warsteiner.jobs.utils.objects.jobs.Job;
import de.warsteiner.jobs.utils.objects.jobs.JobAction;
import de.warsteiner.jobs.utils.objects.jobs.JobID;
import de.warsteiner.jobs.utils.objects.jobs.JobLevel;
import de.warsteiner.jobs.utils.objects.jobs.JobsPlayer;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import static java.util.Map.entry;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Class for anything related to Jobs.
 */

public class JobAPI {

	private UltimateJobs plugin;

	private PluginManager up = UltimateJobs.getPlugin().getPluginManager();

	public HashMap<String, Date> lastworked_list;

	public JobAPI(UltimateJobs plugin) {
		this.lastworked_list = new HashMap<>();
		this.plugin = plugin;
	}

	public void playSound(String ty, Player player) {
		plugin.getEffectAPI().playSound(ty, player);
	}

	@SuppressWarnings("deprecation")
	public boolean canWithdrawMoney(Player player, JobsPlayer jb) {

		if (plugin.getLocalFileManager().getConfig().getBoolean("WithdrawCooldown")) {

			if (jb.getSalaryDate() == null) {
				return true;
			}

			String lasttime = jb.getSalaryDate();
			
			Date date_last = null;

			try {
				date_last = new Date(lasttime);
			} catch (IllegalArgumentException ex) {
				return true;
			}
			 
			if(date_last != null) {
				Date date2 = new Date(plugin.getPluginManager().getDateTodayFromCal());

				if (date2.after(date_last)) {
					return true;
				} else {
					return false;
				}
			}

		}
		return true;
	}

	public String compareData(Entity block) {

		List<MetadataValue> values = block.getMetadata("saplingby");
		if (!values.isEmpty()) {

			for (MetadataValue value : values) {
				String val = value.value().toString();

				if (val.contains("uuid;")) {

					String[] split = val.split(";");

					String player = split[1];

					return player;

				}
			}

		}
		return null;

	}

 
 
	public boolean checkIfJobIsReal(String arg, Player player) {

		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		String id = arg.toUpperCase();
		if (isJobFromConfigID(id) != null) {
			return true;
		}
		if (isJobFromDisplayID(id, "" + player.getUniqueId()) != null) {
			return true;
		}
		if (jb.getLanguage().getMessage("job_not_found") != null) {
			player.sendMessage(jb.getLanguage().getMessage("job_not_found").replaceAll("<job>", arg.toLowerCase()));
		}

		return false;
	}

	public Job checkIfJobIsRealWithResult(String arg, Player player) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		String id = arg.toUpperCase();
		if (isJobFromConfigID(id) != null) {
			return isJobFromConfigID(id);
		}
		if (isJobFromDisplayID(id, "" + player.getUniqueId()) != null) {
			return isJobFromDisplayID(id, "" + player.getUniqueId());
		}
		if (jb.getLanguage().getMessage("job_not_found") != null) {
			player.sendMessage(jb.getLanguage().getMessage("job_not_found").replaceAll("<job>", arg.toLowerCase()));
		}
		return null;
	}

	public Job checkIfJobIsRealAndGet(String arg, Player player) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());

		String id = arg.toUpperCase();
		if (isJobFromConfigID(id) != null) {
			return isJobFromConfigID(id);
		}
		if (isJobFromDisplayID(id, "" + player.getUniqueId()) != null) {
			return isJobFromDisplayID(id, "" + player.getUniqueId());
		}
		if (jb.getLanguage().getMessage("job_not_found") != null) {
			player.sendMessage(jb.getLanguage().getMessage("job_not_found").replaceAll("<job>", arg.toLowerCase()));
		}
		return null;
	}

	public void sendReward(JobsPlayer pl, Player p, Job job, double ep, double reward, String block, boolean can,
			JobAction ac, int amount) {
		new BukkitRunnable() {

			@Override
			public void run() {

				UUID UUID = p.getUniqueId();
				double all_exp = pl.getStatsOf(job.getConfigID()).getExp();
				int level = pl.getStatsOf(job.getConfigID()).getLevel();
				String disofid = job.getDisplayOf(block, "" + UUID);
				double need = plugin.getLevelAPI().getJobNeedExp(job, pl);

				Language lg = pl.getLanguage();

				Map<String, String> replacer = Map.ofEntries(entry("<amount>", "" + amount),
						entry("<prefix>", lg.getPrefix()), entry("<job>", job.getDisplayOfJob("" + UUID)),
						entry("<exp>", Format(all_exp)), entry("<exp_gained>", Format(ep)),
						entry("<exp_required>", Format(plugin.getLevelAPI().getJobNeedExp(job, pl))),
						entry("<level_name>", job.getLevelDisplay(level, "" + UUID)),
						entry("<rank_name>", job.getLevelRankDisplay(level, "" + UUID)),
						entry("<level_int>", "" + level), entry("<id>", disofid),
						entry("<action>", ac.toString().toLowerCase()), entry("<money>", Format(reward)));

				if (can) {
					String bossbar = "Jobs." + job.getConfigID() + ".BossBar";
					String actionbar = "Jobs." + job.getConfigID() + ".Actionbar";
					String message = "Jobs." + job.getConfigID() + ".Message";

					if (lg.getMessageContentOfJobs().get(bossbar) != null) {

						String bossbar_final = lg.getJobMessage(bossbar);

						Date isago5seconds = new Date((new Date()).getTime() + 3000L);
						if (lastworked_list.containsKey(p.getName()))
							lastworked_list.remove(p.getName());
						lastworked_list.put(p.getName(), isago5seconds);
						double use = BossBarHandler.calc(all_exp,
								plugin.getLevelAPI().canLevelMore("" + UUID, job, level), need);
						BarColor color = job.getBarColor();

						String whattodisplay = up.formatText(bossbar_final, replacer, p);

						if (whattodisplay != null) {
							if (!BossBarHandler.exist(p.getName())) {
								BossBarHandler.createBar(p, whattodisplay, color, p.getName(), use);
							} else {
								BossBarHandler.renameBossBar(whattodisplay, p.getName());
								BossBarHandler.recolorBossBar(color, p.getName());
								BossBarHandler.updateProgress(use, p.getName());
							}
						}
					}

					if (lg.getMessageContentOfJobs().get(actionbar) != null) {

						String actionbar_final = lg.getJobMessage(actionbar);

						String fm = up.formatText(actionbar_final, replacer, p);
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, (BaseComponent) new TextComponent(fm));
					}

					if (lg.getMessageContentOfJobs().get(message) != null) {

						String message_final = lg.getJobMessage(message);

						String fmessage = up.formatText(message_final, replacer, p);
						p.sendMessage(fmessage);
					}

				} else {

					String bossbar = "Jobs." + job.getConfigID() + ".MaxEarningsBossBar";
					String actionbar = "Jobs." + job.getConfigID() + ".MaxEarningsActionbar";
					String message = "Jobs." + job.getConfigID() + ".MaxEarningsMessage";

					if (lg.getMessageContentOfJobs().containsKey(bossbar)) {

						String bossbar_final = lg.getJobMessage(bossbar);

						Date isago5seconds = new Date((new Date()).getTime() + 3000L);
						if (lastworked_list.containsKey(p.getName()))
							lastworked_list.remove(p.getName());
						lastworked_list.put(p.getName(), isago5seconds);
						double use = BossBarHandler.calc(all_exp,
								plugin.getLevelAPI().canLevelMore("" + UUID, job, level), need);
						BarColor color = job.getBarColor();

						String whattodisplay = up.formatText(bossbar_final, replacer, p);

						if (whattodisplay != null) {
							if (!BossBarHandler.exist(p.getName())) {
								BossBarHandler.createBar(p, whattodisplay, color, p.getName(), use);
							} else {
								BossBarHandler.renameBossBar(whattodisplay, p.getName());
								BossBarHandler.recolorBossBar(color, p.getName());
								BossBarHandler.updateProgress(use, p.getName());
							}
						}
					}

					if (lg.getMessageContentOfJobs().containsKey(actionbar)) {

						String actionbar_final = lg.getJobMessage(actionbar);

						String fm = up.formatText(actionbar_final, replacer, p);
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, (BaseComponent) new TextComponent(fm));
					}

					if (lg.getMessageContentOfJobs().containsKey(message)) {

						String message_final = lg.getJobMessage(message);

						String fmessage = up.formatText(message_final, replacer, p);
						p.sendMessage(fmessage);
					}

				}

				cancel();
			}
		}.runTaskAsynchronously(plugin);
	}

	public Collection<String> fails = new ArrayList<String>();

	public void loadJobs(Logger logger) {
		File dataFolder = new File(plugin.getLocalFileManager().getConfig().getString("LoadJobsFrom"));
		File[] files = dataFolder.listFiles();

		plugin.getLoaded().clear();
		plugin.getJobCache().clear();

		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String name = files[i].getName();
				File file = files[i];

				if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Info")) {

					Bukkit.getConsoleSender().sendMessage(
							PluginColor.JOB_RELATED_INFO.getPrefix() + "Checking Job File " + name + "...");
				}
				if (file.isFile()) {
					YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

					if (!cfg.contains("ID")) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
									+ "Failed to get String ID from Job " + name + "!");
						}
						fails.add("ID");
					}

					String id = cfg.getString("ID").toUpperCase();

					if (!cfg.contains("Action")) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
									+ "Failed to get Stringlist Action from Job " + name + "!");
						}
						fails.add("Action");
					}

					List<String> actions = cfg.getStringList("Action");

					ArrayList<JobAction> realactions = new ArrayList<JobAction>();

					actions.forEach((ac) -> {

						try {
							realactions.add(JobAction.valueOf(ac));
							if (plugin.getLocalFileManager().getUtilsConfig()
									.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
								Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
										+ "Loading Job, adding Action " + JobAction.valueOf(ac).toString());
							}
						} catch (IllegalArgumentException ex) {
							if (plugin.getLocalFileManager().getUtilsConfig()
									.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
								Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
										+ "Failed to get real Action from " + ac + " of Job " + name + "!");
							}
							fails.add("ActionNotFound");
						}

					});

					if (!cfg.contains("Material")) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
									+ "Failed to get String Material from Job " + name + "!");
						}
						fails.add("Material");
					}

					String icon = cfg.getString("Material");

					String model = null;

					if (!cfg.contains("CustomModelData")) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
									+ "Loading " + id + " without Item CustomModelData...");
						}
					} else {
						model = cfg.getString("CustomModelData");
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
									+ "Loaded Job Item CustomModelData " + model + " for " + id + "...");
						}
					}

					String song_buy = null;

					if (!cfg.contains("PlaySongOnPurchase")) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
									+ "Loading " + id + " without Song on purchase...");
						}
					} else {
						song_buy = cfg.getString("PlaySongOnPurchase");
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
									+ "Loaded Job with Song on purchase " + song_buy + " for " + id + "...");
						}
					}

					String glass = null;

					if (!cfg.contains("CustomGlassPlateColor")) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
									+ "Loading " + id + " without CustomGlassPlateColor...");
						}
					} else {
						glass = cfg.getString("CustomGlassPlateColor");
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
									+ "Loaded Job with CustomGlassPlateColor " + glass + " for " + id + "...");
						}
					}

					if (!cfg.contains("Slot")) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
									+ "Failed to get int Slot from Job " + name + "!");
						}
						fails.add("Slot");
					}

					int slot = cfg.getInt("Slot");

					if (!cfg.contains("Price")) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
									+ "Failed to get double Price from Job " + name + "!");
						}
						fails.add("Price");
					}

					double price = cfg.getDouble("Price");

					String permission = null;

					if (!cfg.contains("Permission")) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
									+ "Loading " + id + " without Permission...");
						}
					} else {
						permission = cfg.getString("Permission");
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
									+ "Loaded Job with Permission " + permission + " for " + id + "...");
						}
					}

					if (!cfg.contains("Worlds")) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
									+ "Failed to get Stringlist Worlds from Job " + name + "!");
						}
						fails.add("Worlds");
					}

					List<String> worlds = cfg.getStringList("Worlds");
					if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Info")) {
						Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
								+ "Loaded Job Worlds " + worlds.toString() + " for " + id + "...");
					}

					if (!cfg.contains("ColorOfBossBar")) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
									+ "Failed to get String ColorOfBossBar from Job " + name + "!");
						}
						fails.add("ColorOfBossBar");
					}

					String bar = cfg.getString("ColorOfBossBar").toUpperCase();

					BarColor color = null;
					try {
						color = BarColor.valueOf(bar.toUpperCase());
					} catch (IllegalArgumentException ex) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
									+ "Failed to get real BarColor from " + bar + " of Job " + name + "!");
						}
						fails.add("BarColor");
					}

					String bypassperm = null;

					if (!cfg.contains("BypassPermission")) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
									+ "Loading " + id + " without Bypass Permission...");
						}
					} else {
						bypassperm = cfg.getString("BypassPermission");
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
									+ "Loaded Job with Bypass Permission " + permission + " for " + id + "...");
						}
					}

					double max = 0;

					if (!cfg.contains("MaxEarnings")) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
									+ "Loading " + id + " without max Earnings...");
						}
					} else {
						max = cfg.getDouble("MaxEarnings");
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
									+ "Loaded Job with max Earnings " + max + " for " + id + "...");
						}
					}

					List<String> customs = null;

					if (!cfg.contains("Items")) {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
									+ "Loading " + id + " without Required Items...");
						}
					} else {
						customs = cfg.getStringList("Items");
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
									+ "Loaded Job withRequired Items " + customs.toString() + " for " + id + "...");
						}
					}

					HashMap<Integer, JobLevel> levels = new HashMap<Integer, JobLevel>();

					for (int i2 = 1; i2 != 999; i2++) {

						if (cfg.contains("LEVELS." + i2 + ".Icon")) {
							if (plugin.getLocalFileManager().getUtilsConfig()
									.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
								Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
										+ "Checking " + id + " for Level " + i2 + "...");
							}
							String level_icon = cfg.getString("LEVELS." + i2 + ".Icon");

							double money = 0;

							if (!cfg.contains("LEVELS." + i2 + ".Money")) {
								if (plugin.getLocalFileManager().getUtilsConfig()
										.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
									Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
											+ "Loading Level " + i2 + " for " + id + " without Vault Reward...");
								}
							} else {
								money = cfg.getDouble("LEVELS." + i2 + ".Icon");
								if (plugin.getLocalFileManager().getUtilsConfig()
										.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
									Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
											+ "Loaded Vault Reward for Level " + i2 + " in " + id);
								}
							}

							if (!cfg.contains("LEVELS." + i2 + ".Need")) {
								if (plugin.getLocalFileManager().getUtilsConfig()
										.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
									Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
											+ "Failed to get double Need for Level " + i2 + " from Job " + name + "!");
								}
								fails.add("MissingNeed");
							}

							double need = cfg.getDouble("LEVELS." + i2 + ".Need");

							double multi = 0;

							if (!cfg.contains("LEVELS." + i2 + ".EarnMore")) {
								if (plugin.getLocalFileManager().getUtilsConfig()
										.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
									Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
											+ "Loading Level " + i2 + " for " + id + " without Level Multiplier...");
								}
							} else {
								multi = cfg.getDouble("LEVELS." + i2 + ".EarnMore");
								if (plugin.getLocalFileManager().getUtilsConfig()
										.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
									Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
											+ "Loaded Reward Multiplier for Level " + i2 + " in " + id);
								}
							}

							List<String> commands = null;

							if (!cfg.contains("LEVELS." + i2 + ".Commands")) {
								if (plugin.getLocalFileManager().getUtilsConfig()
										.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
									Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
											+ "Loading Level " + i2 + " for " + id + " without Level Commands...");
								}
							} else {
								commands = cfg.getStringList("LEVELS." + i2 + ".Commands");
								if (plugin.getLocalFileManager().getUtilsConfig()
										.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
									Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
											+ "Loaded Level Commands for Level " + i2 + " in " + id);
								}
							}

							String md = null;

							if (!cfg.contains("LEVELS." + i2 + ".CustomModelData")) {
								if (plugin.getLocalFileManager().getUtilsConfig()
										.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
									Bukkit.getConsoleSender()
											.sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix() + "Loading Level "
													+ i2 + " for " + id + " without Icon CustomModelData...");
								}
							} else {
								md = cfg.getString("LEVELS." + i2 + ".CustomModelData");
								if (plugin.getLocalFileManager().getUtilsConfig()
										.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
									Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
											+ "Loaded Level Icon CustomModelData for Level " + i2 + " in " + id);
								}
							}

							String song = null;

							if (!cfg.contains("LEVELS." + i2 + ".PlayNBSSong")) {
								if (plugin.getLocalFileManager().getUtilsConfig()
										.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
									Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix()
											+ "Loading Level " + i2 + " for " + id + " without NBS Song...");
								}
							} else {
								song = cfg.getString("LEVELS." + i2 + ".PlayNBSSong");
								if (plugin.getLocalFileManager().getUtilsConfig()
										.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
									Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
											+ "Loaded Level Song " + song + " for Level " + i2 + " in " + id);
								}
							}

							JobLevel joblevel = new JobLevel(i2, need, multi, commands, money, level_icon, md, song);

							levels.put(i2, joblevel);
						}

					}

					HashMap<JobAction, HashMap<String, JobID>> ids = new HashMap<JobAction, HashMap<String, JobID>>();

					realactions.forEach((ac) -> {

						List<String> items = cfg.getStringList("IDS." + ac.toString() + ".List");

						HashMap<String, JobID> current = new HashMap<String, JobID>();

						items.forEach((notreal) -> {

							if (!cfg.contains("IDS." + ac.toString() + "." + notreal + ".Chance")) {
								if (plugin.getLocalFileManager().getUtilsConfig()
										.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
									Bukkit.getConsoleSender()
											.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
													+ "Failed to get double Chance from ID " + notreal + " for Job "
													+ name + "!");
								}
								fails.add("ChanceOf." + notreal);
							} else {

								int chance = cfg.getInt("IDS." + ac.toString() + "." + notreal + ".Chance");

								if (!cfg.contains("IDS." + ac.toString() + "." + notreal + ".ID")) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
														+ "Failed to get String ID (real id of the item) from ID "
														+ notreal + " for Job " + name + "!");
									}
									fails.add("ID." + notreal);
								}

								String blockid = cfg.getString("IDS." + ac.toString() + "." + notreal + ".ID");

								if (!cfg.contains("IDS." + ac.toString() + "." + notreal + ".Money")) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
														+ "Failed to get double Money from ID " + notreal + " for Job "
														+ name + "!");
									}
									fails.add("Money." + notreal);
								}

								double money = cfg.getDouble("IDS." + ac.toString() + "." + notreal + ".Money");

								if (!cfg.contains("IDS." + ac.toString() + "." + notreal + ".Exp")) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
														+ "Failed to get double Exp from ID " + notreal + " for Job "
														+ name + "!");
									}
									fails.add("Exp." + notreal);
								}

								double exp = cfg.getDouble("IDS." + ac.toString() + "." + notreal + ".Exp");

								if (!cfg.contains("IDS." + ac.toString() + "." + notreal + ".Points")) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
														+ "Failed to get double Points from ID " + notreal + " for Job "
														+ name + "!");
									}
									fails.add("Points." + notreal);
								}

								double points = cfg.getDouble("IDS." + ac.toString() + "." + notreal + ".Points");

								List<String> commandlist = null;

								if (!cfg.contains("IDS." + ac.toString() + "." + notreal + ".Commands")) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix() + "Loading ID "
														+ notreal + " for " + id + " without Commands...");
									}
								} else {
									commandlist = cfg
											.getStringList("IDS." + ac.toString() + "." + notreal + ".Commands");
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
										Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
												+ "Loading ID " + notreal + " for " + id + " with Commands...");
									}
								}

								if (!cfg.contains("IDS." + ac.toString() + "." + notreal + ".RewardsGUI.Icon")) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
														+ "Failed to get String RewardsGUI.Icon from ID " + notreal
														+ " for Job " + name + "!");
									}
									fails.add("RewardsGUI.Icon." + notreal);
								}

								String iconreward = cfg
										.getString("IDS." + ac.toString() + "." + notreal + ".RewardsGUI.Icon");

								String iconmodeldata = null;

								if (!cfg.contains(
										"IDS." + ac.toString() + "." + notreal + ".RewardsGUI.CustomModelData")) {
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Warning")) {
										Bukkit.getConsoleSender()
												.sendMessage(PluginColor.JOB_RELATED_WARNING.getPrefix() + "Loading ID "
														+ notreal + " for " + id + " without CustomModelData...");
									}
								} else {
									iconmodeldata = cfg
											.getString("IDS." + ac.toString() + "." + notreal + ".RewardsGUI.Icon");
									if (plugin.getLocalFileManager().getUtilsConfig()
											.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
										Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_INFO.getPrefix()
												+ "Loading ID " + notreal + " for " + id + " with CustomModelData...");
									}
								}

								JobID jobid = new JobID(notreal, blockid, commandlist, chance, money, exp, points,
										iconreward, iconmodeldata);

								current.put(notreal, jobid);
							}

						});

						ids.put(ac, current);

					});

					HashMap<String, Boolean> options = new HashMap<String, Boolean>();

					if (cfg.contains("CheckIfThereAreOtherCanesAbove")) {

						boolean value = cfg.getBoolean("CheckIfThereAreOtherCanesAbove");

						options.put("CheckIfThereAreOtherCanesAbove", value);

					} else if (cfg.contains("GetMoneyOnlyWhenFullyGrown")) {

						boolean value = cfg.getBoolean("GetMoneyOnlyWhenFullyGrown");

						options.put("GetMoneyOnlyWhenFullyGrown", value);

					} else if (cfg.contains("CannotLeaveJob")) {

						boolean value = cfg.getBoolean("CannotLeaveJob");

						options.put("CannotLeaveJob", value);
					}

					ArrayList<String> quit = new ArrayList<String>();
					ArrayList<String> join = new ArrayList<String>();

					if (cfg.contains("Commands.Quit")) {
						quit = (ArrayList<String>) cfg.getStringList("Commands.Quit");
					}

					if (cfg.contains("Commands.Join")) {
						join = (ArrayList<String>) cfg.getStringList("Commands.Join");
					}

					if (fails.size() == 0) {
						Job job = new Job(id, realactions, icon, slot, price, permission,

								worlds, model, color, levels, bypassperm, max, ids, customs,

								options, quit, join, song_buy, glass);

						plugin.getLoaded().add(job.getConfigID());
						plugin.getJobCache().put(job.getConfigID(), job);
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Info")) {
							Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_LOADED.prefix + "§aLoaded Job "
									+ job.getConfigID() + " from File " + name + "!");
						}
					} else {
						if (plugin.getLocalFileManager().getUtilsConfig()
								.getBoolean("Plugin.DebugMessagesOnStart.Error")) {
							Bukkit.getConsoleSender()
									.sendMessage(PluginColor.JOB_RELATED_ERROR.prefix + "Failed to load Job " + name
											+ " from file " + file + ". Failed with " + fails.size() + " Issues.");
						}
					}

				} else {
					if (plugin.getLocalFileManager().getUtilsConfig().getBoolean("Plugin.DebugMessagesOnStart.Error")) {
						Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_ERROR.prefix
								+ "§cFound File in Jobs Folder which isnt a real Job! -> " + file);
					}
				}
			}
		}
	}

	public boolean checkPermissions(Player player, String text) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (player.hasPermission("ultimatejobs." + text) || player.hasPermission("ultimatejobs.admin.all"))
			return true;
		if (jb.getLanguage().getMessage("noperm") != null) {
			player.sendMessage(jb.getLanguage().getMessage("noperm"));
		}
		return false;
	}

	public List<String> canGetJobWithSubOptions(Player player, Job job) {
		String UUID = "" + player.getUniqueId();

		return null;
	}

	public boolean canBuyWithoutPermissions(Player player, Job job) {
		if (job.hasPermission() == true) {
			return player.hasPermission(job.getPermission());
		}
		return true;
	}

	public boolean checkforDailyMaxEarnings(String UUID, Job job) {

		if (job.hasMaxEarningsPerDay()) {

			double max = job.getMaxEarningsPerDay();

			double current = plugin.getPlayerAPI().getEarningsOfToday(UUID, job);

			if (current >= max) {
				return false;
			}

		}

		return true;
	}

	public boolean canByPass(Player player, Job job) {
		if (job.hasBypassPermission()) {
			return player.hasPermission(job.getByPassPermission());
		}
		// if (job.hasByPassNotQuestCon() == true &&
		// plugin.getPluginManager().isInstalled("NotQuests")) {
		// return plugin.getNotQuestManager().canBypassJob(player, job);
		// }
		return false;
	}

	public boolean canWorkThereByPlayer(String UUID, Job j, String st) {

		Location loc = null;

		if (Bukkit.getPlayer(UUID) == null) {
			loc = UltimateJobs.getPlugin().getLocationAPI().getLocation("LastLoc." + UUID);
		} else {
			loc = Bukkit.getPlayer(UUID).getLocation();
		}

		if (canWorkInWorld(loc.getWorld().getName(), j) && canWorkInRegion(loc, j, st).equalsIgnoreCase("ALLOW"))
			return true;
		return false;
	}

	public boolean canWorkThereByBlock(Location loc, Job j, String st) {

		if (canWorkInWorld(loc.getWorld().getName(), j) && canWorkInRegion(loc, j, st).equalsIgnoreCase("ALLOW"))
			return true;
		return false;
	}

	public boolean canWorkThereByEntity(Location loc, Job j, String st) {

		if (canWorkInWorld(loc.getWorld().getName(), j) && canWorkInRegion(loc, j, st).equalsIgnoreCase("ALLOW"))
			return true;
		return false;
	}

	public ArrayList<String> getJobsWithAction(String UUID, JobAction ac) {
		if (!plugin.getPlayerAPI().getCurrentJobs(UUID).isEmpty()
				&& plugin.getPlayerAPI().getCurrentJobs(UUID) != null) {
			ArrayList<String> l = new ArrayList<>();
			for (String j : plugin.getPlayerAPI().getCurrentJobs(UUID)) {
				Job jb = (Job) plugin.getJobCache().get(j);
				for (JobAction actn : jb.getActionList()) {
					if (actn.equals(ac))
						l.add(jb.getConfigID());
				}

			}
			return l;
		}
		return null;
	}

	public boolean canWorkInWorld(String world, Job job) {
		
		if(plugin.getLocalFileManager().getConfig().getStringList("GlobalJobWorlds").contains(world)) {
			return true;
		}
		
		if(job.getWorlds().contains(world)) {
			return true;
		}
		
		return false;
	}

	public boolean needCustomItemToWork(Job job) {
		return (job.getCustomitems() != null);
	}

	public boolean hasItemInhandWhichIsNeed(Job job, Player p) {
		List<String> items = job.getCustomitems();
		@SuppressWarnings("deprecation")
		ItemStack current = p.getItemInHand();
		for (String item : items) {
			ItemStack i = new ItemStack(Material.valueOf(item.toUpperCase()));
			if (current.equals(i))
				return true;
		}
		return false;
	}

	public String canWorkInRegion(Location loc, Job j, String flag) {
		if (plugin.getPluginManager().isInstalled("WorldGuard")) {

			return WorldGuardManager.checkFlag(loc, flag);

		}
		return "ALLOW";
	}

	public boolean canReward(Job j, String id, JobAction ac) {
		double chance = j.getChanceOf(id, ac);
		Random r = new Random();
		int chance2 = r.nextInt(100);
		if (chance2 < chance) {
			return true;
		}
		return false;
	}

	public ArrayList<String> getJobsInListAsID(String id) {
		ArrayList<String> list = new ArrayList<>();
		for (String l : plugin.getLoaded()) {
			Job j = plugin.getJobCache().get(l);
			list.add(j.getDisplayID(id).toLowerCase());
		}
		return list;
	}

	public Job isJobFromConfigID(String id) {
		for (String list : plugin.getLoaded()) {
			Job job = plugin.getJobCache().get(list);
			String cfg_id = job.getConfigID();
			if (cfg_id.equalsIgnoreCase(id))
				return job;
		}
		return null;
	}

	public Job isJobFromDisplayID(String id, String UUID) {
		for (String list : plugin.getLoaded()) {
			Job job = plugin.getJobCache().get(list);
			String cfg_id = job.getDisplayID(UUID);
			if (cfg_id.toUpperCase().equalsIgnoreCase(id.toUpperCase()))

				return job;
		}
		return null;
	}

	public ArrayList<String> getPlayerNameList() {
		ArrayList<String> list = new ArrayList<>();
		for (Player p : Bukkit.getOnlinePlayers())
			list.add(p.getName());
		return list;
	}

	public boolean isInt(String str) {
		try {
			Integer.parseInt(str);
		} catch (Throwable e) {
			return false;
		}
		return true;
	}

	public String Format(double i) {

		if (!plugin.getLocalFileManager().getConfig().contains("Format")) {
			Bukkit.getConsoleSender().sendMessage(PluginColor.ERROR.getPrefix() + "Missing Format in Config.yml");
		}

		DecimalFormat t = new DecimalFormat(plugin.getLocalFileManager().getConfig().getString("Format"));
		String b = t.format(i).replaceAll(",", ".");
		return b;
	}

}
