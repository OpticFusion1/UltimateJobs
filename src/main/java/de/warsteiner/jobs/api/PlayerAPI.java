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
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.manager.GuiOpenManager;
import de.warsteiner.jobs.utils.JobAction;
import de.warsteiner.jobs.utils.objects.JobStats;
import de.warsteiner.jobs.utils.objects.JobsMultiplier;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.objects.Language;
import de.warsteiner.jobs.utils.objects.MultiplierType;
import de.warsteiner.jobs.utils.objects.MultiplierWeight;

public class PlayerAPI {

	private UltimateJobs plugin;

	private HashMap<String, JobsPlayer> pllist = new HashMap<String, JobsPlayer>();
	private ArrayList<String> players = new ArrayList<String>();

	public PlayerAPI(UltimateJobs main) {
		plugin = main;
	}

	public HashMap<String, JobsPlayer> getCacheJobPlayers() {
		return pllist;
	}

	public JobsPlayer getRealJobPlayer(String ID) {
 
		return pllist.get(ID);
	}

	public JobsPlayer getRealJobPlayer(UUID ID) {
 
		return pllist.get("" + ID);
	}

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
 
								for (JobsMultiplier multi : getMultipliers(UUID)) {

									if(!multi.getUntil().equalsIgnoreCase("X")) {
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
				}.runTaskAsynchronously(plugin);
			}
		}.runTaskTimer(plugin, 0, 20);
	}

	public void calculateRanking() {
		FileConfiguration cfg = plugin.getFileManager().getConfig();
		if (cfg.getBoolean("CalculateRanking")) {

			int every = cfg.getInt("CalculateRankingEvery");

			new BukkitRunnable() {

				@Override
				public void run() {
					new BukkitRunnable() {

						@Override
						public void run() {

							if (plugin.getPlayerDataAPI().getUltimatePlayers() != null) {

								if(plugin.getPlayerDataAPI().getUltimatePlayers().size() != 0) {
									today_ranked.clear();
									blocks_ranked.clear();
									level_ranked.clear();
									ranked_points.clear();

									List<String> players = plugin.getPlayerDataAPI().getUltimatePlayers();

									if (plugin.getFileManager().getRankingGlobalConfig()
											.getBoolean("EnabledGlobalRanking")) {

										Map<String, Double> map = new HashMap<String, Double>();

										for (String member : players) {

											map.put(member, getPoints(member));

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

									if (plugin.getFileManager().getRankingPerJobConfig()
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
												if (plugin.getPlayerAPI().getOwnedJobs(member)
														.contains(real.getConfigID())) {
													double earnings_all = plugin.getPlayerAPI().getEarningsOfToday(member,
															real);

													ma.put(earnings_all, member);

													double d3 = plugin.getPlayerAPI().getBrokenTimes(member, real);

													ma2.put(d3, member);

													double d4 = plugin.getPlayerAPI().getLevelOF(member, real);

													ma3.put(d4, member);
												}
											}

											if (ma.size() != 0) {
												Map<Double, String> c = ma.entrySet().stream()
														.sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
														.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
																(e1, e2) -> e1, LinkedHashMap::new));

												c.forEach((points, player) -> {
													int rank = today.size() + 1;
													today.put(rank, player);
												});

											}

											if (ma2.size() != 0) {
												Map<Double, String> time = ma2.entrySet().stream()
														.sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
														.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
																(e1, e2) -> e1, LinkedHashMap::new));

												time.forEach((points, player) -> {
													int rank = times.size() + 1;
													times.put(rank, player);
												});

											}

											if (ma3.size() != 0) {
												Map<Double, String> lvl = ma3.entrySet().stream()
														.sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
														.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
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

		int lvl = getLevelOF(UUID, job);

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

	public double getEarnedAtDateFromAllJobs(String uuid, String date) {

		double f = 0;

		for (String job : getOwnedJobs(uuid)) {

			Job j = plugin.getJobCache().get(job);

			double earned = getEarnedAt(uuid, j, date);

			f = f + earned;
		}

		return f;

	}

	public boolean existInCacheByUUID(String uuid) {
		return getRealJobPlayer(uuid) != null;
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

			}
		}.runTaskLater(plugin, 2);
	}

	public void updateSalary(String UUID, double d) {
		if (existInCacheByUUID(UUID)) {
			getCacheJobPlayers().get(UUID).updateCacheSalary(d);
			executeCustomEvent(UUID, null, true);
		} else {
			plugin.getPlayerDataAPI().updateSalary(UUID, d);
		}
	}

	public void updateSalaryDate(String UUID, String d) {
		if (existInCacheByUUID(UUID)) {
			getCacheJobPlayers().get(UUID).updateCacheSalaryDate(d);
			executeCustomEvent(UUID, null, true);
		} else {
			plugin.getPlayerDataAPI().updateSalaryDate(UUID, d);
		}
	}

	public double getSalary(String uuid) {
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getSalary();
		} else {
			return plugin.getPlayerDataAPI().getSalary(uuid);
		}
	}

	public void updateDateJoinedOfJob(String UUID, String job, String date) {
		if (existInCacheByUUID(UUID)) {
			getCacheJobPlayers().get(UUID).getStatsOf(job).updateCacheJoinedDate(date);
			executeCustomEvent(UUID, job, true);
		} else {
			plugin.getPlayerDataAPI().updateDateJoinedOfJob(UUID, job, date);
		}
	}

	public ArrayList<JobsMultiplier> getMultipliers(String uuid) {
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getMultipliers();
		} else {
			return plugin.getPlayerDataAPI().getMultipliers(uuid);
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
			ArrayList<JobsMultiplier> listed = getCacheJobPlayers().get(UUID).getMultipliers();

			listed.remove(getMultiplierByName(UUID, name));

			getCacheJobPlayers().get(UUID).updateMultiList(listed);
		} else {
			plugin.getPlayerDataAPI().removeMultiplier(UUID, name);
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
			ArrayList<JobsMultiplier> listed = getCacheJobPlayers().get(UUID).getMultipliers();

			listed.add(m);

			getCacheJobPlayers().get(UUID).updateMultiList(listed);

		} else {
			if (plugin.getPlayerDataAPI().existMultiplier(UUID, m.getName())) {
				plugin.getPlayerDataAPI().updateMultiplier(UUID, m.getName(), m.getByPlugin(), m.getType(),
						m.getUntil(), m.getWeight(), m.getValue(), m.getJob());
			} else {
				plugin.getPlayerDataAPI().createMultiplier(UUID, m.getName(), m.getByPlugin(), m.getType(),
						m.getUntil(), m.getWeight(), m.getValue(), m.getJob());
			}
		}
	}

	public ArrayList<String> getOwnedJobs(String uuid) {
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getOwnJobs();
		} else {
			return plugin.getPlayerDataAPI().getOwnedJobs(uuid);
		}
	}

	public ArrayList<String> getCurrentJobs(String uuid) {
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getCurrentJobs();
		} else {
			return plugin.getPlayerDataAPI().getCurrentJobs(uuid);
		}
	}

	public double getExpOf(String uuid, Job job) {
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getExp();
		} else {
			return plugin.getPlayerDataAPI().getExpOf(uuid, job.getConfigID());
		}
	}

	public int getBrokenTimesOfID(String uuid, Job job, String id, String ac) {
		if (existInCacheByUUID(uuid)) {

			if (getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()) == null) {
				return 0;
			}

			return getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getBrokenTimesOf(id);
		} else {

			return plugin.getPlayerDataAPI().getBrokenTimesOfBlock(uuid, job.getConfigID(), id, ac);
		}
	}

	public double getEarnedFrom(String uuid, Job job, String id, String ac) {
		if (existInCacheByUUID(uuid)) {

			if (getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()) == null) {
				return 0.0;
			}

			return getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getBrokenOf(id);
		} else {
			return plugin.getPlayerDataAPI().getEarnedOfBlock(uuid, job.getConfigID(), id, ac);
		}
	}

	public void updateBrokenTimes(String uuid, Job job, int times) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).updateCacheBrokenTimes(times);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerDataAPI().updateBrokenTimes(uuid, job.getConfigID(), times);
		}
	}

	public void updateBrokenTimesOf(String uuid, Job job, String id, int d, String ac) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).updateCacheBrokenTimesOf(id, d);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerDataAPI().updateEarningsTimesOf(uuid, job.getConfigID(), id, d, ac);
		}
	}

	public void updateBrokenMoneyOf(String uuid, Job job, String id, double d, String ac) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getBrokenList().put(id, d);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerDataAPI().updateEarningsAmountOf(uuid, job.getConfigID(), id, d, ac);
		}
	}

	public void updateEarningsAtDate(String uuid, Job job, double v, String date) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).updateCacheEarnings(date, v);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerDataAPI().updateEarnings(uuid, job.getConfigID(), date, v);
		}
	}

	public void updateEarningsOfToday(String uuid, Job job, double v) {
		String date = plugin.getDate();
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).updateCacheEarnings(date, v);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerDataAPI().updateEarnings(uuid, job.getConfigID(), date, v);
		}
	}

	public void updateExp(String uuid, Job job, double val) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).changeCacheExp(val);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerDataAPI().getExpOf(uuid, job.getConfigID());
		}
	}

	public void updatePoints(String uuid, double val) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).updateCachePoints(val);
			executeCustomEvent(uuid, null, true);
		} else {
			plugin.getPlayerDataAPI().updatePoints(uuid, val);
		}
	}

	public void updateMax(String uuid, int val) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).updateCacheMax(val);
			executeCustomEvent(uuid, null, true);
		} else {
			plugin.getPlayerDataAPI().updateMax(uuid, val);
		}
	}

	public double getEarnedAt(String uuid, Job job, String date) {
		if (existInCacheByUUID(uuid)) {

			return getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getEarningsofDate(date);

		} else {
			return plugin.getPlayerDataAPI().getEarnedAt(uuid, job.getConfigID(), date);
		}
	}

	public int getBrokenTimes(String uuid, Job job) {
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getBrokenTimes();
		} else {
			return plugin.getPlayerDataAPI().getBrokenOf(uuid, job.getConfigID());
		}
	}

	public int getLevelOF(String uuid, Job job) {
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getLevel();
		} else {
			return plugin.getPlayerDataAPI().getLevelOf(uuid, job.getConfigID());
		}
	}

	public void updateLevelOf(String uuid, Job job, int lvl) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).changeCacheLevel(lvl);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerDataAPI().updateLevel(uuid, lvl, job.getConfigID());
		}
	}

	public double getPoints(String uuid) {
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getPoints();
		} else {
			return plugin.getPlayerDataAPI().getPoints(uuid);
		}
	}

	public double getEarningsOfToday(String uuid, Job job) {
		String date = plugin.getDate();
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getEarningsofDate(date);
		} else {
			return plugin.getPlayerDataAPI().getEarnedAt(uuid, job.getConfigID(), date);
		}
	}

	public JobStats loadSingleJobData(UUID UUID, String job) {

		PlayerDataAPI plm = UltimateJobs.getPlugin().getPlayerDataAPI();

		Job j = plugin.getJobCache().get(job);

		int level = plm.getLevelOf("" + UUID, job);
		double exp = plm.getExpOf("" + UUID, job);
		String date = plm.getDateOf("" + UUID, job);
		int broken = plm.getBrokenOf("" + UUID, job);
		String joined = plm.getJobDateJoined("" + UUID, job);

		HashMap<String, Double> listedofearned = new HashMap<String, Double>();
		for (int i = 0; i != plugin.getFileManager().getConfig().getInt("LoadEarningsDataOfDays"); i++) {
			DateFormat format = new SimpleDateFormat(plugin.getFileManager().getConfig().getString("Date"));
			Date data = new Date();

			Calendar c1 = Calendar.getInstance();
			c1.setTime(data);

			c1.add(Calendar.DATE, -i);

			Date newdate = c1.getTime();

			String d = "" + format.format(newdate);

			double earned = plm.getEarnedAt("" + UUID, job, d);

			listedofearned.put(d, earned);

		}

		plugin.getPlayerChunkAPI().loadChunks("" + UUID, j);

		HashMap<String, Double> money = new HashMap<String, Double>();
		HashMap<String, Integer> broken2 = new HashMap<String, Integer>();

		for (JobAction action : j.getActionList()) {
			for (String id : j.getNotRealIDSListByAction(action)) {

				double moneyearned = plm.getEarnedOfBlock("" + UUID, job, id, "" + action);
				int brokentimes = plm.getBrokenTimesOfBlock("" + UUID, job, id, "" + action);

				money.put(id, moneyearned);
				broken2.put(id, brokentimes);

			}

		}

		JobStats sz = new JobStats(j, j.getConfigID(), exp, level, broken, date, money, broken2, listedofearned,
				joined);

		plugin.getPlayerAPI().getRealJobPlayer("" + UUID).getStatsList().put(date, sz);

		return sz;
	}

	public JobsPlayer loadData(String name, UUID UUID) {

		if (existInCacheByUUID("" + UUID)) {
			removePlayerFromCache("" + UUID);
		}

		PlayerDataAPI plm = UltimateJobs.getPlugin().getPlayerDataAPI();

		ArrayList<String> owned = plm.getOwnedJobs("" + UUID);
		ArrayList<String> current = plm.getCurrentJobs("" + UUID);

		ArrayList<JobsMultiplier> multi = plm.getMultipliers("" + UUID);

		double sal = plm.getSalary("" + UUID);
		String sat = plm.getSalaryDate("" + UUID);

		double points = plm.getPoints("" + UUID);

		HashMap<String, JobStats> stats = new HashMap<String, JobStats>();

		for (String loading : owned) {

			Job j = plugin.getJobCache().get(loading);

			plugin.getPlayerChunkAPI().loadChunks("" + UUID, j);

			int level = plm.getLevelOf("" + UUID, loading);
			double exp = plm.getExpOf("" + UUID, loading);
			String date = plm.getDateOf("" + UUID, loading);
			int broken = plm.getBrokenOf("" + UUID, loading);
			String joined = plm.getJobDateJoined("" + UUID, loading);

			HashMap<String, Double> listedofearned = new HashMap<String, Double>();

			for (int i = 0; i != plugin.getFileManager().getConfig().getInt("LoadEarningsDataOfDays"); i++) {
				DateFormat format = new SimpleDateFormat(plugin.getFileManager().getConfig().getString("Date"));
				Date data = new Date();

				Calendar c1 = Calendar.getInstance();
				c1.setTime(data);

				c1.add(Calendar.DATE, -i);

				Date newdate = c1.getTime();

				String d = "" + format.format(newdate);

				double earned = plm.getEarnedAt("" + UUID, j.getConfigID(), d);

				listedofearned.put(d, earned);

			}

			HashMap<String, Double> money = new HashMap<String, Double>();
			HashMap<String, Integer> broken2 = new HashMap<String, Integer>();

			for (JobAction action : j.getActionList()) {
				for (String id : j.getNotRealIDSListByAction(action)) {

					double moneyearned = plm.getEarnedOfBlock("" + UUID, loading, id, "" + action);
					int brokentimes = plm.getBrokenTimesOfBlock("" + UUID, loading, id, "" + action);

					money.put(id, moneyearned);
					broken2.put(id, brokentimes);

				}

			}

			JobStats sz = new JobStats(j, j.getConfigID(), exp, level, broken, date, money, broken2, listedofearned,
					joined);

			stats.put(loading, sz);
		}

		String lused = null;

		if (UltimateJobs.getPlugin().getPlayerDataAPI().getSettingData("" + UUID, "LANG") != null) {
			lused = UltimateJobs.getPlugin().getPlayerDataAPI().getSettingData("" + UUID, "LANG");
		} else {
			lused = UltimateJobs.getPlugin().getFileManager().getLanguageConfig().getString("PlayerDefaultLanguage");
		}

		Language langusged = plugin.getLanguageAPI().getLanguages().get(lused);

		JobsPlayer jp = new JobsPlayer(name, current, owned, points,

				plm.getMaxJobs("" + UUID), "" + UUID, UUID, langusged, stats, sal, sat, multi);

		pllist.put("" + UUID, jp);
		players.add("" + UUID);

		return jp;
	}

	public void updateDataOfJob(String j, JobsPlayer pl, String UUID) {

		PlayerDataAPI plm = UltimateJobs.getPlugin().getPlayerDataAPI();

		if (!plm.ExistJobData(UUID, j)) {
			plm.createJobData(UUID, j);

			Job real = plugin.getJobCache().get(j);

			plugin.getPlayerChunkAPI().loadChunks("" + UUID, real);

			int level = plm.getLevelOf("" + UUID, j);
			double exp = plm.getExpOf("" + UUID, j);
			String date = plm.getDateOf("" + UUID, j);
			int broken = plm.getBrokenOf("" + UUID, j);
			String joined = plm.getJobDateJoined("" + UUID, j);

			HashMap<String, Double> listedofearned = new HashMap<String, Double>();
			for (int i = 0; i != plugin.getFileManager().getConfig().getInt("LoadEarningsDataOfDays"); i++) {
				DateFormat format = new SimpleDateFormat(plugin.getFileManager().getConfig().getString("Date"));
				Date data = new Date();

				Calendar c1 = Calendar.getInstance();
				c1.setTime(data);

				c1.add(Calendar.DATE, -i);

				Date newdate = c1.getTime();

				String d = "" + format.format(newdate);

				double earned = plm.getEarnedAt("" + UUID, real.getConfigID(), d);

				listedofearned.put(d, earned);

			}

			HashMap<String, Double> money = new HashMap<String, Double>();
			HashMap<String, Integer> broken2 = new HashMap<String, Integer>();

			for (JobAction action : real.getActionList()) {
				for (String id : real.getNotRealIDSListByAction(action)) {

					double moneyearned = plm.getEarnedOfBlock("" + UUID, j, id, "" + action);
					int brokentimes = plm.getBrokenTimesOfBlock("" + UUID, j, id, "" + action);

					money.put(id, moneyearned);
					broken2.put(id, brokentimes);

				}

			}

			JobStats sz = new JobStats(real, real.getConfigID(), exp, level, broken, date, money, broken2,
					listedofearned, joined);

			pl.getStatsList().put(real.getConfigID(), sz);

		}

	}

}
