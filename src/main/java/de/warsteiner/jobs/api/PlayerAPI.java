package de.warsteiner.jobs.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.util.concurrent.AtomicDouble;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.manager.GuiOpenManager;
import de.warsteiner.jobs.utils.objects.Language;
import de.warsteiner.jobs.utils.objects.PluginColor;
import de.warsteiner.jobs.utils.objects.jobs.Job;
import de.warsteiner.jobs.utils.objects.jobs.JobAction;
import de.warsteiner.jobs.utils.objects.jobs.JobStats;
import de.warsteiner.jobs.utils.objects.jobs.JobsMultiplier;
import de.warsteiner.jobs.utils.objects.jobs.JobsPlayer;
import de.warsteiner.jobs.utils.objects.multipliers.MultiplierType;
import de.warsteiner.jobs.utils.objects.multipliers.MultiplierWeight;

/**
 * Class to manage everything about Players
 * 
 * Use this class only to access the UltimateJobs API.
 */

public class PlayerAPI {

	/**
	 * Util
	 */

	private UltimateJobs plugin;

	private HashMap<String, JobsPlayer> pllist = new HashMap<String, JobsPlayer>();
	private HashMap<String, JobsPlayer> offline_loaded = new HashMap<String, JobsPlayer>();
	private ArrayList<String> players = new ArrayList<String>();

	public PlayerAPI(UltimateJobs main) {
		plugin = main;
	}

	/**
	 * Get all Players
	 * 
	 * Key equals the UUID to String
	 * 
	 * @return
	 */

	public HashMap<String, JobsPlayer> getOfflineCachePlayers() {
		return offline_loaded;
	}

	public HashMap<String, JobsPlayer> getOnlinePlayersListed() {
		return pllist;
	}

	public String getJobsPlayerByName(String name) {

		AtomicReference<String> found = new AtomicReference<String>();

		pllist.forEach((uuid, jb) -> {

			if (jb.getName().toLowerCase().equals(name.toLowerCase())) {
				found.set(uuid);
			}

		});

		offline_loaded.forEach((uuid, jb) -> {

			if (jb.getName().toLowerCase().equals(name.toLowerCase())) {
				found.set(uuid);
			}

		});

		return found.get();
	}

	public void saveAndLoadIntoOfflineCache(Player player, String name, String uuid) {
		if (getOfflineCachePlayers().containsKey(uuid)) {
			getOfflineCachePlayers().remove(uuid);
		}

		JobsPlayer jb = getOnlinePlayersListed().get(uuid);

		getOfflineCachePlayers().put(uuid, jb);

		plugin.getPlayerOfflineAPI().savePlayerAsFinal(jb, uuid, name, jb.getDisplayName());

		if (getOnlinePlayersListed().containsKey(uuid)) {
			getOnlinePlayersListed().remove(uuid);
		}

	}

	public void checkAndLoadIntoOnlineCache(Player player, String name, String display, String uuid) {

		FileConfiguration config = UltimateJobs.getPlugin().getLocalFileManager().getConfig();

		if (getOfflineCachePlayers().containsKey(uuid)) {

			// loading from existing data

			JobsPlayer g = getOfflineCachePlayers().get(uuid);

			if (config.getBoolean("EnableMaxJobPermissions")) {

				if (config.getBoolean("UpdateOnServerJoin")) {

					for (PermissionAttachmentInfo perms : player.getEffectivePermissions()) {

						if (perms.getPermission().startsWith("ultimatejobs.max.")) {

							int real = Integer.valueOf(perms.getPermission().split("ultimatejobs.max.")[1]) - 1;

							g.updateCacheMax(real);
						}

					}
				}
			}

			getOnlinePlayersListed().put(uuid, g);
			players.add(uuid);

		} else {

			// creating a temp new user

			getOnlinePlayersListed().put(uuid, createDefaultUser(player, display, name, uuid));
			players.add(uuid);

		}

	}

	public JobsPlayer createDefaultUser(Player player, String name, String display, String uuid) {

		FileConfiguration config = UltimateJobs.getPlugin().getLocalFileManager().getConfig();

		ArrayList<String> current = new ArrayList<String>();
		ArrayList<String> owned = new ArrayList<String>();

		if (config.getBoolean("EnabledDefaultJobs")) {
			if (config.getStringList("DefaultJobs") != null) {
				for (String job : config.getStringList("DefaultJobs")) {

					if (plugin.getJobCache().get(job) != null) {

						if (!owned.contains(job)) {
							owned.add(job);

							if (config.getBoolean("AutoJoinDefaultJobs")) {
								current.add(job);
							}

						}
					}

				}
			}
		}

		int max = UltimateJobs.getPlugin().getLocalFileManager().getConfig().getInt("MaxDefaultJobs") - 1;

		if (config.getBoolean("EnableMaxJobPermissions")) {

			if (config.getBoolean("UpdateOnServerJoin")) {

				for (PermissionAttachmentInfo perms : player.getEffectivePermissions()) {

					if (perms.getPermission().startsWith("ultimatejobs.max.")) {

						int real = Integer.valueOf(perms.getPermission().split("ultimatejobs.max.")[1]) - 1;

						max = real;
					}

				}
			}
		}

		HashMap<String, String> s1 = new HashMap<String, String>();
		ArrayList<JobsMultiplier> s2 = new ArrayList<JobsMultiplier>();
		HashMap<String, JobStats> s3 = new HashMap<String, JobStats>();

		String lang = plugin.getLocalFileManager().getLanguageConfig().getString("PlayerDefaultLanguage");

		JobsPlayer jp = new JobsPlayer(name, display, current, owned, 0.0, max, uuid, UUID.fromString(uuid.toString()),
				plugin.getLanguageAPI().getLanguageFromID(lang), s3, 0.0, uuid, s2, s1);
		return jp;
	}

	public JobsPlayer getRealJobPlayer(String ID) {

		if (getOnlinePlayersListed().containsKey(ID)) {
			return getOnlinePlayersListed().get(ID);
		}

		if (getOfflineCachePlayers().containsKey(ID)) {
			return getOfflineCachePlayers().get(ID);
		}
		return null;
	}

	public JobsPlayer getRealJobPlayer(UUID ID) {

		if (getOnlinePlayersListed().containsKey("" + ID)) {
			return getOnlinePlayersListed().get("" + ID);
		}

		if (getOfflineCachePlayers().containsKey("" + ID)) {
			return getOfflineCachePlayers().get("" + ID);
		}
		return null;
	}

	public boolean existInCacheByUUID(String uuid) {
		if (getOfflineCachePlayers().containsKey(uuid)) {
			return true;
		}
		if (getOnlinePlayersListed().containsKey(uuid)) {
			return true;
		}
		return false;
	}

	/**
	 * Checking and removing old Multipliers
	 */

	public void startUtil() {

		new BukkitRunnable() {

			@Override
			public void run() {
				new BukkitRunnable() {

					@Override
					public void run() {

						Collection<? extends Player> players = Bukkit.getOnlinePlayers();

						for (Player player : players) {
							String UUID = "" + player.getUniqueId();
							if (existInCacheByUUID(UUID)) {

								if (getMultipliers(UUID) != null && !getMultipliers(UUID).isEmpty()) {
									for (JobsMultiplier multi : getMultipliers(UUID)) {

										if (!multi.getUntil().equalsIgnoreCase("X")) {
											Date today = new Date(plugin.getPluginManager().getDateTodayFromCalWith());

											Date un = new Date(multi.getUntil());

											if (today.after(un)) {
												removeMultiplier(UUID, multi.getName());
											}
										}
									}
								}
							}
						}
						cancel();
					}
				}.runTaskAsynchronously(plugin);
			}
		}.runTaskTimer(plugin, 0, 20);
	}

	public boolean existSettingData(String ID, String type) {
		if (existInCacheByUUID(ID)) {
			return getRealJobPlayer(ID).getPlayerSettings().containsKey(type.toUpperCase());
		} else {
			return plugin.getPlayerOfflineAPI().getSettingsOfPlayer(ID).containsKey(type.toUpperCase());
		}
	}

	public String getSettingData(String ID, String type) {
		if (existInCacheByUUID(ID)) {
			return getRealJobPlayer(ID).getPlayerSettings().get(type.toUpperCase());
		} else {
			return plugin.getPlayerOfflineAPI().getSettingsOfPlayer(ID).get(type.toUpperCase());
		}
	}

	public Integer getPageData(String ID, String type) {
		if (existInCacheByUUID(ID)) {

			if (!getRealJobPlayer(ID).getPlayerSettings().containsKey(type)) {
				return 1;
			}

			return Integer.valueOf(getRealJobPlayer(ID).getPlayerSettings().get(type.toUpperCase()));
		} else {

			if (!plugin.getPlayerOfflineAPI().existSettingData(ID, type)) {
				return 1;
			}

			return Integer.valueOf(plugin.getPlayerOfflineAPI().getSettingsOfPlayer(ID).get(type.toUpperCase()));
		}
	}

	public Integer getMaxJobs(String ID) {
		if (existInCacheByUUID(ID)) {
			return Integer.valueOf(getRealJobPlayer(ID).getMaxJobs());
		} else {
			return Integer.valueOf(plugin.getPlayerOfflineAPI().getMaxJobs(ID));
		}
	}

	public void createPageData(String ID, String type, int value) {
		if (existInCacheByUUID(ID)) {
			getRealJobPlayer(ID).addSetting(type, "" + value);
		} else {
			plugin.getPlayerOfflineAPI().createSettingData(ID, type, "" + value);
		}
	}

	public void addOnePage(String ID, String type) {

		Integer get = getPageData(ID, type);

		int page = get + 1;

		updateSettingData(ID, type, "" + page);
	}

	public void removeOnePage(String ID, String type) {

		Integer get = getPageData(ID, type);

		int page = get - 1;

		updateSettingData(ID, type, "" + page);
	}

	public void createSettingData(String ID, String type, String value) {
		if (existInCacheByUUID(ID)) {
			getRealJobPlayer(ID).addSetting(type, value);
		} else {
			plugin.getPlayerOfflineAPI().createSettingData(ID, type, value);
		}
	}

	public void updateSettingData(String ID, String type, String value) {
		if (existInCacheByUUID(ID)) {
			getRealJobPlayer(ID).updateSetting(type, value);
		} else {
			plugin.getPlayerOfflineAPI().updateSettingData(ID, type, value);
		}
	}

	public String getDisplayByUUID(String ID) {
		if (existInCacheByUUID(ID)) {
			return Bukkit.getPlayer(UUID.fromString(ID.toString())).getName();
		} else {
			return plugin.getPlayerOfflineAPI().getADisplayNameFromUUID(ID);
		}
	}

	public Collection<String> getEarningsList(String ID, String job) {
		if (existInCacheByUUID(ID)) {
			return getRealJobPlayer(ID).getStatsOf(job).getEarningsOnlyDates();
		} else {
			return plugin.getPlayerOfflineAPI().getAllEarnings(ID, job);
		}
	}

	public ArrayList<String> calculateSortedEarningsOf(String UUID, Job job) {
		ArrayList<String> ranked = new ArrayList<String>();

		if (getEarningsList(UUID, job.getConfigID()) != null && !getEarningsList(UUID, job.getConfigID()).isEmpty()) {

			Map<String, Double> map = new HashMap<String, Double>();

			getEarningsList(UUID, job.getConfigID()).forEach((date) -> {

				double earned = getEarnedAt(UUID, job, date);
				map.put(date, earned);

			});

			Map<String, Double> c = map.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
							LinkedHashMap::new));

			c.forEach((date, val) -> {
				ranked.add(date);
			});

		}
		return ranked;
	}

	public String getSortedEarningAsDateOf(String UUID, Job job) {

		AtomicReference<String> at = new AtomicReference<String>();

		if (calculateSortedEarningsOf(UUID, job) != null && !calculateSortedEarningsOf(UUID, job).isEmpty()) {

			ArrayList<String> listed = calculateSortedEarningsOf(UUID, job);

			return listed.get(0);
		}

		return at.get();
	}

	public void updateAverageEarnings(String UUID, Job job, String date, double val) {
		if (existInCacheByUUID(UUID)) {

			JobsPlayer jb = getRealJobPlayer(UUID);

			JobStats stats = jb.getStatsOf(job.getConfigID());

			stats.updateAverageEarnings(date, val);

		}
	}

	public void updateAverageExp(String UUID, Job job, String date, double val) {
		if (existInCacheByUUID(UUID)) {

			JobsPlayer jb = getRealJobPlayer(UUID);

			JobStats stats = jb.getStatsOf(job.getConfigID());

			stats.updateAverageExp(date, val);

		}
	}

	public double calculateAverageEarningsPerMinute(String UUID, Job job) {

		JobsPlayer jb = getRealJobPlayer(UUID);

		if (jb.getStatsOf(job.getConfigID()) != null) {
			JobStats stats = jb.getStatsOf(job.getConfigID());
			if (stats.hasAverageEarnings()) {
				HashMap<String, Double> dates = stats.getAverageEarningsList();

				if (!dates.isEmpty() && dates != null) {

					ArrayList<Double> map = new ArrayList<Double>();

					dates.forEach((date, amount) -> {

						map.add(amount);

					});

					int n = map.size();

					double total = 0;

					for (int i = 0; i < n; i++) {
						total = total + map.get(i);
					}
					;

					double avg = total / n;

					return avg;
				}
			}
		}
		return 0.0;
	}

	public double calculateAverageExpPerMinute(String UUID, Job job) {

		JobsPlayer jb = getRealJobPlayer(UUID);

		if (jb.getStatsOf(job.getConfigID()) != null) {
			JobStats stats = jb.getStatsOf(job.getConfigID());
			if (stats.hasAverageExp()) {
				HashMap<String, Double> dates = stats.getAverageExpList();

				if (!dates.isEmpty() && dates != null) {

					ArrayList<Double> map = new ArrayList<Double>();

					dates.forEach((date, amount) -> {

						map.add(amount);

					});

					int n = map.size();

					double total = 0;

					for (int i = 0; i < n; i++) {
						total = total + map.get(i);
					}
					;

					double avg = total / n;

					return avg;
				}
			}
		}
		return 0.0;
	}

	public int calculateAverageWorkPerMinute(String UUID, Job job) {

		JobsPlayer jb = getRealJobPlayer(UUID);

		if (jb.getStatsOf(job.getConfigID()) != null) {
			JobStats stats = jb.getStatsOf(job.getConfigID());
			if (stats.hasAverageWork()) {
				HashMap<String, Integer> dates = stats.getAverageWorkList();

				if (!dates.isEmpty() && dates != null) {

					ArrayList<Integer> map = new ArrayList<Integer>();

					dates.forEach((date, amount) -> {

						map.add(amount);

					});

					int n = map.size();

					int total = 0;

					for (int i = 0; i < n; i++) {
						total = total + map.get(i);
					}
					;

					int avg = total / n;

					return avg;
				}
			}
		}
		return 0;
	}

	public void updateAverageWork(String UUID, Job job, String date, int times) {
		if (existInCacheByUUID(UUID)) {

			JobsPlayer jb = getRealJobPlayer(UUID);

			JobStats stats = jb.getStatsOf(job.getConfigID());

			stats.updateAverageWork(date, times);

		}
	}

	public double calculateAverageEarnings(String UUID, Job job) {

		Collection<String> dates = getEarningsList(UUID, job.getConfigID());

		if (!dates.isEmpty() && dates != null) {

			ArrayList<Double> map = new ArrayList<Double>();

			dates.forEach((date) -> {

				double earned = getEarnedAt(UUID, job, date);
				map.add(earned);

			});

			int n = map.size();

			double total = 0;

			for (int i = 0; i < n; i++) {
				total = total + map.get(i);
			}
			;

			double avg = total / n;

			return avg;
		}
		return 0.0;
	}

	public ArrayList<String> getEveryPlayerWhichCanExist() {

		ArrayList<String> ids = new ArrayList<String>();

		getOnlinePlayersListed().forEach((uuid, jb) -> {
			ids.add(uuid);
		});

		getOfflineCachePlayers().forEach((uuid, jb) -> {
			ids.add(uuid);
		});

		return ids;
	}

	public void calculateRanking() {
		FileConfiguration cfg = plugin.getLocalFileManager().getConfig();
		if (cfg.getBoolean("CalculateRanking")) {

			int every = cfg.getInt("CalculateRankingEvery");

			new BukkitRunnable() {

				@Override
				public void run() {
					new BukkitRunnable() {

						@Override
						public void run() {

							if (getEveryPlayerWhichCanExist() != null && !getEveryPlayerWhichCanExist().isEmpty()) {

								if (getEveryPlayerWhichCanExist().size() != 0) {
									today_ranked.clear();
									blocks_ranked.clear();
									level_ranked.clear();
									ranked_points.clear();

									List<String> players = getEveryPlayerWhichCanExist();

									if (plugin.getLocalFileManager().getRankingGlobalConfig()
											.getBoolean("EnabledGlobalRanking")) {

										Map<String, Double> map = new HashMap<String, Double>();

										for (String member : players) {

											if (existInCacheByUUID(member)) {
												map.put(member, getPoints(member));
											}

										}

										if (map.size() >= 1) {
											Map<String, Double> sort = SortByValue(map);

											ArrayList<String> f = new ArrayList<String>();

											sort.forEach((k, v) -> {
												f.add(k);
											});

											Collections.reverse(f);

											ranked_points.clear();

											for (int i = 0; i != f.size(); i++) {

												ranked_points.put(i, f.get(i));

											}
										}
									}

									if (plugin.getLocalFileManager().getRankingPerJobConfig()
											.getBoolean("EnabledRankingPerJob")) {

										ArrayList<String> alljobs = plugin.getLoaded();

										for (String job : alljobs) {

											Job real = plugin.getJobCache().get(job);

											HashMap<Integer, String> today = new HashMap<Integer, String>();

											HashMap<Double, String> ma = new HashMap<Double, String>();

											HashMap<Integer, String> times = new HashMap<Integer, String>();

											HashMap<Double, String> ma2 = new HashMap<Double, String>();

											HashMap<Integer, String> level = new HashMap<Integer, String>();

											HashMap<Double, String> ma3 = new HashMap<Double, String>();

											for (String member : players) {
												if (existInCacheByUUID(member)) {
													if (plugin.getPlayerAPI().getOwnedJobs(member) != null
															&& !plugin.getPlayerAPI().getOwnedJobs(member).isEmpty()) {
														if (plugin.getPlayerAPI().getOwnedJobs(member)
																.contains(real.getConfigID())) {
															double earnings_all = plugin.getPlayerAPI()
																	.getEarningsOfToday(member, real);

															ma.put(earnings_all, member);

															double d3 = plugin.getPlayerAPI().getBrokenTimes(member,
																	real);

															ma2.put(d3, member);

															double d4 = plugin.getPlayerAPI().getLevelOF(member, real);

															ma3.put(d4, member);
														}
													}
												}
											}

											if (ma.size() != 0) {

												Map<Double, String> c = ma.entrySet().stream()
														.sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
														.collect(
																Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
																		(e1, e2) -> e1, LinkedHashMap::new));

												c.forEach((points, player) -> {
													int rank = today.size() + 1;
													today.put(rank, player);
												});

											}

											if (ma2.size() != 0) {
												Map<Double, String> time = ma2.entrySet().stream()
														.sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
														.collect(
																Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
																		(e1, e2) -> e1, LinkedHashMap::new));

												time.forEach((points, player) -> {
													int rank = times.size() + 1;
													times.put(rank, player);
												});

											}

											if (ma3.size() != 0) {
												Map<Double, String> lvl = ma3.entrySet().stream()
														.sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
														.collect(
																Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
																		(e1, e2) -> e1, LinkedHashMap::new));

												lvl.forEach((points, player) -> {
													int rank = level.size() + 1;
													level.put(rank, player);
												});

											}

											if (!today.isEmpty()) {
												today_ranked.put(real.getConfigID(), today);
											}

											if (!times.isEmpty()) {
												blocks_ranked.put(real.getConfigID(), times);
											}

											if (!level.isEmpty()) {
												level_ranked.put(real.getConfigID(), level);
											}

										}

									}
								}
							}

							cancel();
						}

					}.runTaskAsynchronously(plugin);
				}
			}.runTaskTimer(plugin, 0, every);

		}
	}

	public HashMap<Integer, String> ranked_points = new HashMap<Integer, String>();
	public HashMap<String, HashMap<Integer, String>> today_ranked = new HashMap<String, HashMap<Integer, String>>();
	public HashMap<String, HashMap<Integer, String>> blocks_ranked = new HashMap<String, HashMap<Integer, String>>();
	public HashMap<String, HashMap<Integer, String>> level_ranked = new HashMap<String, HashMap<Integer, String>>();

	public String getRankOfLevelsJob(Job job, String UUID) {

		if (!level_ranked.isEmpty() && level_ranked.containsKey(job.getConfigID())) {
			if (!level_ranked.get(job.getConfigID()).isEmpty()) {
				HashMap<Integer, String> list = level_ranked.get(job.getConfigID());

				AtomicInteger s = new AtomicInteger();

				list.forEach((rank, player) -> {

					if (player.equalsIgnoreCase(UUID)) {
						s.set(rank);
					}

				});

				return "" + s.get();
			}
		}
		return "Unknown";

	}

	public String getPlaceOfLevelsJob(Job job, int place) {
		if (!level_ranked.isEmpty() && level_ranked.containsKey(job.getConfigID())) {
			if (!level_ranked.get(job.getConfigID()).isEmpty()) {
				HashMap<Integer, String> list = level_ranked.get(job.getConfigID());

				AtomicReference<String> s = new AtomicReference<String>();

				list.forEach((rank, player) -> {

					if (rank == place) {
						s.set(player);
					}

				});

				return s.get();
			}
		}
		return "Unknown";

	}

	public String getRankOfBlocksJob(Job job, String UUID) {

		if (!blocks_ranked.isEmpty() && blocks_ranked.containsKey(job.getConfigID())) {
			if (!blocks_ranked.get(job.getConfigID()).isEmpty()) {
				HashMap<Integer, String> list = blocks_ranked.get(job.getConfigID());

				AtomicInteger s = new AtomicInteger();

				list.forEach((rank, player) -> {

					if (player.equalsIgnoreCase(UUID)) {
						s.set(rank);
					}

				});

				return "" + s.get();
			}
		}
		return "Unknown";

	}

	public String getPlaceOfBlocksJob(Job job, int place) {

		if (!blocks_ranked.isEmpty() && blocks_ranked.containsKey(job.getConfigID())) {
			if (!blocks_ranked.get(job.getConfigID()).isEmpty()) {
				HashMap<Integer, String> list = blocks_ranked.get(job.getConfigID());

				AtomicReference<String> s = new AtomicReference<String>();

				list.forEach((rank, player) -> {

					if (rank == place) {
						s.set(player);
					}

				});

				return s.get();
			}
		}
		return "Unknown";

	}

	public String getRankOfEarningsJob(Job job, String UUID) {

		if (!today_ranked.isEmpty() && today_ranked.containsKey(job.getConfigID())) {
			if (!today_ranked.get(job.getConfigID()).isEmpty()) {
				HashMap<Integer, String> list = today_ranked.get(job.getConfigID());

				AtomicInteger s = new AtomicInteger();

				list.forEach((rank, player) -> {

					if (player.equalsIgnoreCase(UUID)) {
						s.set(rank);
					}

				});

				return "" + s.get();
			}
		}
		return "Unknown";

	}

	public String getPlaceOfEarningsJob(Job job, int place) {

		if (!today_ranked.isEmpty() && today_ranked.containsKey(job.getConfigID())) {
			if (!today_ranked.get(job.getConfigID()).isEmpty()) {
				HashMap<Integer, String> list = today_ranked.get(job.getConfigID());

				AtomicReference<String> s = new AtomicReference<String>();

				list.forEach((rank, player) -> {

					if (rank == place) {
						s.set(player);
					}

				});

				return s.get();

			}
		}
		return "Unknown";

	}

	public String getRankOfGlobalPlayer(String UUID) {
		if (ranked_points != null) {
			for (int i = 0; i != ranked_points.size(); i++) {

				String uuid = ranked_points.get(i);

				if (uuid.equalsIgnoreCase(UUID)) {
					int calc = (i + 1);
					return "" + calc;
				}

			}
		}
		return "Unknown";

	}

	public String getPlaceOfGlobalPlayer(int place) {
		if (ranked_points != null) {
			for (int i = 0; i != ranked_points.size(); i++) {

				int calc = (i + 1);

				if (calc == place) {
					String uuid = ranked_points.get(i);
					return uuid;
				}

			}
		}
		return "Unknown";

	}

	public <K, V extends Comparable<? super V>> Map<K, V> SortByValue(Map<K, V> crunchifyMap) {

		Map<K, V> crunchifyResult = new LinkedHashMap<>();

		Stream<Map.Entry<K, V>> sequentialStream = crunchifyMap.entrySet().stream();

		sequentialStream.sorted(Map.Entry.comparingByValue())
				.forEachOrdered(c -> crunchifyResult.put(c.getKey(), c.getValue()));

		return crunchifyResult;
	}

	public void removePlayerFromCache(String uuid) {

		players.remove(uuid);
		pllist.remove("" + uuid);

	}

	public double calculateAmountOnAddOrRemove(String UUID, Job job, MultiplierType type, MultiplierWeight weight,
			double org) {
		if (plugin.getPlayerAPI().getMultipliers(UUID) != null) {
			if (plugin.getPlayerAPI().getMultipliersByTypeAndWeightAndJob(UUID, type, weight,
					job.getConfigID()) != null) {

				double first = org;

				for (JobsMultiplier listed : plugin.getPlayerAPI().getMultipliersByTypeAndWeightAndJob(UUID, type,
						weight, job.getConfigID())) {

					double value = listed.getValue();

					if (weight.equals(MultiplierWeight.ADD)) {
						first = first + value;
					} else if (weight.equals(MultiplierWeight.REMOVE)) {
						first = first - value;
					} else if (weight.equals(MultiplierWeight.PERCENT_ADD)) {
						first = first + (first / 100) * value;
					} else if (weight.equals(MultiplierWeight.PERCENT_REMOVE)) {
						first = first - (first / 100) * value;
					}

					return first;
				}

			}
		}
		return org;
	}

	public double getRealCalculatedAmountOfExp(String UUID, Job job, double org) {

		double od1 = org;

		od1 = plugin.getPlayerAPI().calculateAmountOnAddOrRemove(UUID, job, MultiplierType.EXP, MultiplierWeight.ADD,
				od1);
		od1 = plugin.getPlayerAPI().calculateAmountOnAddOrRemove(UUID, job, MultiplierType.EXP, MultiplierWeight.REMOVE,
				od1);

		od1 = plugin.getPlayerAPI().calculateAmountOnAddOrRemove(UUID, job, MultiplierType.EXP,
				MultiplierWeight.PERCENT_ADD, od1);
		od1 = plugin.getPlayerAPI().calculateAmountOnAddOrRemove(UUID, job, MultiplierType.EXP,
				MultiplierWeight.PERCENT_REMOVE, od1);

		return od1;
	}

	public double getRealCalculatedAmountOfMoney(String UUID, Job job, double org) {

		int lvl = 1;

		if (getCurrentJobs(UUID).contains(job.getConfigID())) {
			lvl = getLevelOF(UUID, job);
		}

		double od1 = org;

		od1 = plugin.getPlayerAPI().calculateAmountOnAddOrRemove(UUID, job, MultiplierType.MONEY, MultiplierWeight.ADD,
				od1);
		od1 = plugin.getPlayerAPI().calculateAmountOnAddOrRemove(UUID, job, MultiplierType.MONEY,
				MultiplierWeight.REMOVE, od1);

		od1 = plugin.getPlayerAPI().calculateAmountOnAddOrRemove(UUID, job, MultiplierType.MONEY,
				MultiplierWeight.PERCENT_ADD, od1);
		od1 = plugin.getPlayerAPI().calculateAmountOnAddOrRemove(UUID, job, MultiplierType.MONEY,
				MultiplierWeight.PERCENT_REMOVE, od1);

		double level = (od1 / 100) * job.getMultiOfLevel(lvl);

		return od1 + level;
	}

	public ArrayList<String> getOnlinePlayersInJob(Job job) {

		ArrayList<String> list = new ArrayList<String>();

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (getRealJobPlayer(p.getUniqueId()) != null) {

				if (getRealJobPlayer(p.getUniqueId()).getCurrentJobs().contains(job.getConfigID())) {
					list.add("" + p.getUniqueId());
				}

			}
		}

		return list;

	}

	public ArrayList<String> getAllPlayersInJob(Job job) {

		ArrayList<String> list = new ArrayList<String>();

		for (String ud : getEveryPlayerWhichCanExist()) {

			ArrayList<String> d = getCurrentJobs(ud);

			if (d != null && !d.isEmpty()) {

				if (getCurrentJobs(ud).contains(job.getConfigID())) {
					list.add(ud);
				}
			}

		}

		return list;

	}

	public double getEarnedAtDateFromAllJobs(String uuid, String date) {

		double f = 0;

		for (String job : getOwnedJobs(uuid)) {

			Job j = plugin.getJobCache().get(job);

			double earned = getEarnedAt(uuid, j, date);

			f = f + earned;
		}

		return f;

	}

	public void executeCustomEvent(String UUID, String job, boolean online) {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (online) {
					JobsPlayer f = getRealJobPlayer(UUID);

					if (Bukkit.getPlayer(f.getUUID()) != null) {

						Player player = Bukkit.getPlayer(f.getUUID());
						if (player.getOpenInventory() != null) {

							InventoryView iv = player.getOpenInventory();

							if (iv.getTitle() != null) {
								String title = iv.getTitle();

								GuiOpenManager d = plugin.getGUIOpenManager();

								if (d.isMainOpend(player, title) != null) {

									plugin.getGUI().UpdateMainInventoryItems(player, title);

								} else if (d.isLanguageOpend(player, title) != null) {
									plugin.getGUI().updateLanguageInventory(player, title, f);
								} else if (d.isEarningsAboutJob(player, title) != null) {
									plugin.getGUIAddonManager().updateEarningsGUI_Single_Job(player, title, f,
											plugin.getGUIOpenManager().isEarningsAboutJob(player, title));
								} else if (d.isEarningsALL(player, title) != null) {
									plugin.getGUIAddonManager().updateEarningsGUI_All(player, title, f);
								} else if (d.isWithdrawMenu(player, title) != null) {
									plugin.getGUIAddonManager().updateWithdrawGUI(player, title, f);
								} else if (d.isLevelsMenu(player, title) != null) {
									plugin.getGUIAddonManager().updateLevelsGUI(player, title, f,
											plugin.getGUIOpenManager().isLevelsMenu(player, title));
								} else if (d.isRewardsMenu(player, title) != null) {
									plugin.getGUIAddonManager().updateRewardsGUI(player, title, f,
											plugin.getGUIOpenManager().isRewardsMenu(player, title));
								} else if (d.isStatsMenuOpendSelf(player, title) != null) {
									plugin.getGUIAddonManager().updateSelfUpdateGUI(player, title, f);
								} else if (d.isStatsMenuOpendAboutPlayer(player, title) != null) {
									plugin.getGUIAddonManager().updateOtherStatsGUI(player, title, f,
											plugin.getGUIOpenManager().isStatsMenuOpendAboutPlayer(player, title));
								} else if (d.isSettingsMenu(player, title) != null) {
									plugin.getGUI().UpdateSettingsGUI(player, title,
											plugin.getGUIOpenManager().isSettingsMenu(player, title));
								}

							}
						}

					}

				}
				cancel();
			}
		}.runTaskLater(plugin, 2);
	}

	public void updateSalary(String UUID, double d) {
		if (existInCacheByUUID(UUID)) {
			getRealJobPlayer(UUID).updateCacheSalary(d);
			executeCustomEvent(UUID, null, true);
		} else {
			plugin.getPlayerOfflineAPI().updateSalary(UUID, d);
		}
	}

	public void updateSalaryDate(String UUID, String d) {
		if (existInCacheByUUID(UUID)) {
			getRealJobPlayer(UUID).updateCacheSalaryDate(d);
			executeCustomEvent(UUID, null, true);
		} else {
			plugin.getPlayerOfflineAPI().updateSalaryDate(UUID, d);
		}
	}

	public double getSalary(String uuid) {
		if (existInCacheByUUID(uuid)) {
			return getRealJobPlayer(uuid).getSalary();
		} else {
			return plugin.getPlayerOfflineAPI().getSalary(uuid);
		}
	}

	public void updateDateJoinedOfJob(String UUID, String job, String date) {
		if (existInCacheByUUID(UUID)) {
			getRealJobPlayer(UUID).getStatsOf(job).updateCacheJoinedDate(date);
			executeCustomEvent(UUID, job, true);
		} else {
			plugin.getPlayerOfflineAPI().updateDateJoinedOfJob(UUID, job, date);
		}
	}

	public ArrayList<JobsMultiplier> getMultipliers(String uuid) {
		if (existInCacheByUUID(uuid)) {
			return getRealJobPlayer(uuid).getMultipliers();
		} else {
			return plugin.getPlayerOfflineAPI().getMultipliers(uuid);
		}
	}

	public ArrayList<JobsMultiplier> getMultipliersByTypeAndWeightAndJob(String uuid, MultiplierType type,
			MultiplierWeight weight, String job) {

		ArrayList<JobsMultiplier> list = new ArrayList<JobsMultiplier>();

		for (JobsMultiplier m : getMultipliers(uuid)) {
			if (m.getJob().equalsIgnoreCase("NONE")) {
				if (m.getType().equals(type) && m.getWeight().equals(weight)) {
					list.add(m);
				}
			} else if (m.getType().equals(type) && m.getWeight().equals(weight) && m.getJob().equalsIgnoreCase(job)) {
				list.add(m);
			}
		}
		return list;

	}

	public JobsMultiplier getMultiplierByName(String uuid, String name) {

		for (JobsMultiplier m : getMultipliers(uuid)) {
			if (m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		return null;
	}

	public void removeMultiplier(String UUID, String name) {
		if (existInCacheByUUID(UUID)) {
			ArrayList<JobsMultiplier> listed = getRealJobPlayer(UUID).getMultipliers();

			listed.remove(getMultiplierByName(UUID, name));

			getRealJobPlayer(UUID).updateMultiList(listed);
		} else {
			plugin.getPlayerOfflineAPI().removeMultiplier(UUID, name);
		}
	}

	public boolean existMultiplier(String uuid, String name) {
		for (JobsMultiplier b : getMultipliers(uuid)) {
			if (b.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public void addMultiplier(String UUID, JobsMultiplier m) {
		if (existInCacheByUUID(UUID)) {
			ArrayList<JobsMultiplier> listed = getRealJobPlayer(UUID).getMultipliers();

			listed.add(m);

			getRealJobPlayer(UUID).updateMultiList(listed);

		} else {
			if (plugin.getPlayerOfflineAPI().existMultiplier(UUID, m.getName())) {
				plugin.getPlayerOfflineAPI().updateMultiplier(UUID, m.getName(), m.getByPlugin(), m.getType(),
						m.getUntil(), m.getWeight(), m.getValue(), m.getJob());
			} else {
				plugin.getPlayerOfflineAPI().createMultiplier(UUID, m.getName(), m.getByPlugin(), m.getType(),
						m.getUntil(), m.getWeight(), m.getValue(), m.getJob());
			}
		}
	}

	public ArrayList<String> getOwnedJobs(String uuid) {
		if (existInCacheByUUID(uuid)) {
			return getRealJobPlayer(uuid).getOwnJobs();
		} else {
			return plugin.getPlayerOfflineAPI().getOwnedJobs(uuid);
		}
	}

	public ArrayList<String> getCurrentJobs(String uuid) {
		if (existInCacheByUUID(uuid)) {
			return getRealJobPlayer(uuid).getCurrentJobs();
		} else {
			return plugin.getPlayerOfflineAPI().getCurrentJobs(uuid);
		}
	}

	public double getExpOf(String uuid, Job job) {
		if (existInCacheByUUID(uuid)) {
			return getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).getExp();
		} else {
			return plugin.getPlayerOfflineAPI().getExpOf(uuid, job.getConfigID());
		}
	}

	public double getExpOf(String uuid, String job) {
		if (existInCacheByUUID(uuid)) {
			return getRealJobPlayer(uuid).getStatsOf(job).getExp();
		} else {
			return plugin.getPlayerOfflineAPI().getExpOf(uuid, job);
		}
	}

	public int getBrokenTimesOfID(String uuid, Job job, String id, String ac) {
		if (existInCacheByUUID(uuid)) {

			if (getRealJobPlayer(uuid).getStatsOf(job.getConfigID()) == null) {
				return 0;
			}

			if (getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).getWorkedTimesOf(id) == null) {
				return 0;
			}

			return getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).getWorkedTimesOf(id);
		} else {

			return plugin.getPlayerOfflineAPI().getBrokenTimesOfBlock(uuid, job.getConfigID(), id, ac);
		}
	}

	public double getEarnedFrom(String uuid, Job job, String id, String ac) {
		if (existInCacheByUUID(uuid)) {

			if (getRealJobPlayer(uuid).getStatsOf(job.getConfigID()) == null) {
				return 0.0;
			}

			if (getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).getWorkedTimesOf(id) == null) {
				return 0.0;
			}

			return getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).getWorkedTimesOf(id);
		} else {
			return plugin.getPlayerOfflineAPI().getEarnedOfBlock(uuid, job.getConfigID(), id, ac);
		}
	}

	public void updateBrokenTimes(String uuid, Job job, int times) {
		if (existInCacheByUUID(uuid)) {
			getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).updateHowmanyTimesWorked(times);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerOfflineAPI().updateBrokenTimes(uuid, job.getConfigID(), times);
		}
	}

	public void updateBrokenTimesOf(String uuid, Job job, String id, int d, String ac) {
		if (existInCacheByUUID(uuid)) {
			getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).updateCacheBrokenTimesOf(id, d);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerOfflineAPI().updateEarningsTimesOf(uuid, job.getConfigID(), id, d, ac);
		}
	}

	public void updateBrokenMoneyOf(String uuid, Job job, String id, double d, String ac) {
		if (existInCacheByUUID(uuid)) {
			getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).updateTimesExecutedMoneyOf(id, d);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerOfflineAPI().updateEarningsAmountOf(uuid, job.getConfigID(), id, d, ac);
		}
	}

	public void updateEarningsAtDate(String uuid, Job job, double v, String date) {
		if (existInCacheByUUID(uuid)) {
			getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).updateDateEarningsOf(date, v);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerOfflineAPI().updateEarnings(uuid, job.getConfigID(), date, v);
		}
	}

	public void updateEarningsOfToday(String uuid, Job job, double v) {
		String date = plugin.getDate();
		if (existInCacheByUUID(uuid)) {
			getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).updateDateEarningsOf(date, v);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerOfflineAPI().updateEarnings(uuid, job.getConfigID(), date, v);
		}
	}

	public void updateExp(String uuid, Job job, double val) {
		if (existInCacheByUUID(uuid)) {
			getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).changeCacheExp(val);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerOfflineAPI().getExpOf(uuid, job.getConfigID());
		}
	}

	public void updateExp(String uuid, String job, double val) {
		if (existInCacheByUUID(uuid)) {
			getRealJobPlayer(uuid).getStatsOf(job).changeCacheExp(val);
			executeCustomEvent(uuid, job, true);
		} else {
			plugin.getPlayerOfflineAPI().getExpOf(uuid, job);
		}
	}

	public void updatePoints(String uuid, double val) {
		if (existInCacheByUUID(uuid)) {
			getRealJobPlayer(uuid).updateCachePoints(val);
			executeCustomEvent(uuid, null, true);
		} else {
			plugin.getPlayerOfflineAPI().updatePoints(uuid, val);
		}
	}

	public void updateMax(String uuid, int val) {
		if (existInCacheByUUID(uuid)) {
			getRealJobPlayer(uuid).updateCacheMax(val);
			executeCustomEvent(uuid, null, true);
		} else {
			plugin.getPlayerOfflineAPI().updateMax(uuid, val);
		}
	}

	public double getEarnedAt(String uuid, Job job, String date) {
		if (existInCacheByUUID(uuid)) {

			return getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).getEarningsofDate(date);

		} else {
			return plugin.getPlayerOfflineAPI().getEarnedAt(uuid, job.getConfigID(), date);
		}
	}

	public int getBrokenTimes(String uuid, Job job) {
		if (existInCacheByUUID(uuid)) {
			return getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).getHowManyTimesWorked();
		} else {
			return plugin.getPlayerOfflineAPI().getBrokenOf(uuid, job.getConfigID());
		}
	}

	public int getLevelOF(String uuid, Job job) {
		if (existInCacheByUUID(uuid)) {
			return getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).getLevel();
		} else {
			return plugin.getPlayerOfflineAPI().getLevelOf(uuid, job.getConfigID());
		}
	}

	public void updateLevelOf(String uuid, Job job, int lvl) {
		if (existInCacheByUUID(uuid)) {
			getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).changeCacheLevel(lvl);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerOfflineAPI().updateLevel(uuid, lvl, job.getConfigID());
		}
	}

	public double getPoints(String uuid) {
		if (existInCacheByUUID(uuid)) {

			return getRealJobPlayer(uuid).getPoints();

		} else {
			return plugin.getPlayerOfflineAPI().getPoints(uuid);
		}
	}

	public String getBoughtDate(String uuid, String job) {
		if (existInCacheByUUID(uuid)) {
			return getRealJobPlayer(uuid).getStatsOf(job).getDate();
		} else {
			return plugin.getPlayerOfflineAPI().getDateOf(uuid, job);
		}
	}

	public String getBoughtDate(String uuid, Job job) {
		if (existInCacheByUUID(uuid)) {
			return getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).getDate();
		} else {
			return plugin.getPlayerOfflineAPI().getDateOf(uuid, job.getConfigID());
		}
	}

	public double getEarningsOfToday(String uuid, Job job) {
		String date = plugin.getDate();
		if (existInCacheByUUID(uuid)) {
			return getRealJobPlayer(uuid).getStatsOf(job.getConfigID()).getEarningsofDate(date);
		} else {
			return plugin.getPlayerOfflineAPI().getEarnedAt(uuid, job.getConfigID(), date);
		}
	}

	public void LoadDataForServerStart(String name, String display, UUID UUID) {

		if (getOfflineCachePlayers().containsKey("" + UUID)) {
			getOfflineCachePlayers().remove("" + UUID);
		}

		OfflinePlayerAPI plm = UltimateJobs.getPlugin().getPlayerOfflineAPI();

		ArrayList<String> owned = plm.getOwnedJobs("" + UUID);
		ArrayList<String> current = plm.getCurrentJobs("" + UUID);

		ArrayList<JobsMultiplier> multi = plm.getMultipliers("" + UUID);

		double sal = plm.getSalary("" + UUID);
		String sat = plm.getSalaryDate("" + UUID);

		HashMap<String, String> settings = plm.getSettingsOfPlayer("" + UUID);

		double points = plm.getPoints("" + UUID);

		HashMap<String, JobStats> stats = new HashMap<String, JobStats>();

		for (String loading : owned) {

			if (!plugin.getJobCache().containsKey(loading)) {
				Bukkit.getConsoleSender().sendMessage(PluginColor.JOB_RELATED_ERROR.getPrefix()
						+ "Cannot load a Job for a player which does no longer eixst!");

				ArrayList<String> nl = owned;

				nl.remove(loading);

				owned = nl;

				if (current.contains(loading)) {
					ArrayList<String> n2 = current;

					n2.remove(loading);

					current = nl;
				}

			} else {
				Job j = plugin.getJobCache().get(loading);

				plugin.getPlayerChunkAPI().loadChunks("" + UUID, j);

				int level = plm.getLevelOf("" + UUID, loading);
				double exp = plm.getExpOf("" + UUID, loading);
				String date = plm.getDateOf("" + UUID, loading);
				int broken = plm.getBrokenOf("" + UUID, loading);
				String joined = plm.getJobDateJoined("" + UUID, loading);

				HashMap<String, Double> listedofearned = new HashMap<String, Double>();

				plm.getAllEarnings("" + UUID, j.getConfigID()).forEach((ddd) -> {

					double earned = plm.getEarnedAt("" + UUID, j.getConfigID(), ddd);

					listedofearned.put(ddd, earned);

				});

				HashMap<String, Double> money = new HashMap<String, Double>();
				HashMap<String, Integer> broken2 = new HashMap<String, Integer>();

				for (JobAction action : j.getActionList()) {

					j.getIDsOf(action).forEach((INT, type) -> {
						double moneyearned = plm.getEarnedOfBlock("" + UUID, j.getConfigID(), INT, "" + action);
						int brokentimes = plm.getBrokenTimesOfBlock("" + UUID, j.getConfigID(), INT, "" + action);

						money.put(INT, moneyearned);
						broken2.put(INT, brokentimes);
					});

				}

				HashMap<String, Integer> list04 = new HashMap<String, Integer>();
				HashMap<String, Double> list05 = new HashMap<String, Double>();
				HashMap<String, Double> list06 = new HashMap<String, Double>();

				JobStats sz = new JobStats(j, j.getConfigID(), exp, level, broken, date, money, broken2, listedofearned,
						joined, list04, list05, list06);

				stats.put(loading, sz);
			}

		}

		String lused = null;

		if (UltimateJobs.getPlugin().getPlayerOfflineAPI().getSettingData("" + UUID, "LANG") != null) {
			lused = UltimateJobs.getPlugin().getPlayerOfflineAPI().getSettingData("" + UUID, "LANG");
		} else {
			lused = UltimateJobs.getPlugin().getLocalFileManager().getLanguageConfig()
					.getString("PlayerDefaultLanguage");
		}

		Language langusged = plugin.getLanguageAPI().getLanguages().get(lused);

		JobsPlayer jp = new JobsPlayer(name, display, current, owned, points,

				plm.getMaxJobs("" + UUID), "" + UUID, UUID, langusged, stats, sal, sat, multi, settings);

		getOfflineCachePlayers().put("" + UUID, jp);

		Bukkit.getConsoleSender().sendMessage(
				PluginColor.INFO.getPrefix() + "Loaded " + name + " with UUID " + UUID + " into the offline cache!");

	}

}
