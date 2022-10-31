package de.warsteiner.jobs.api;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.util.concurrent.AtomicDouble;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.database.statements.SQLStatementAPI;
import de.warsteiner.jobs.utils.objects.DataMode;
import de.warsteiner.jobs.utils.objects.jobs.Job;
import de.warsteiner.jobs.utils.objects.jobs.JobAction;
import de.warsteiner.jobs.utils.objects.jobs.JobStats;
import de.warsteiner.jobs.utils.objects.jobs.JobsMultiplier;
import de.warsteiner.jobs.utils.objects.jobs.JobsPlayer;
import de.warsteiner.jobs.utils.objects.multipliers.MultiplierType;
import de.warsteiner.jobs.utils.objects.multipliers.MultiplierWeight;

/**
 * Class to manage ALL Offline Player Data
 * 
 * DO NOT USE THIS CLASS AS API IN ANY PLUGIN.
*/

public class OfflinePlayerAPI {

	private SQLStatementAPI mg = UltimateJobs.getPlugin().getSQLStatementAPI();
	private UltimateJobs plugin = UltimateJobs.getPlugin();

	public void createtables() {
		SQLStatementAPI s = UltimateJobs.getPlugin().getSQLStatementAPI();
		new BukkitRunnable() {

			@Override
			public void run() {

				s.executeUpdate(
						"CREATE TABLE IF NOT EXISTS playerlist (UUID varchar(200), NAME varchar(200), DISPLAY varchar(200))");
				s.executeUpdate(
						"CREATE TABLE IF NOT EXISTS playersettings (UUID varchar(200), TYPE varchar(200), MODE varchar(200))");

				s.executeUpdate(
						"CREATE TABLE IF NOT EXISTS job_stats (UUID varchar(200), JOB varchar(200), DATE varchar(200), LEVEL int, EXP double, BROKEN int)");

				s.executeUpdate("CREATE TABLE IF NOT EXISTS job_current (UUID varchar(200), JOB varchar(200))");
				s.executeUpdate(
						"CREATE TABLE IF NOT EXISTS job_players (UUID varchar(200), DATE varchar(200), POINTS int, MAX int)");

				s.executeUpdate(
						"CREATE TABLE IF NOT EXISTS earnings_all (UUID varchar(200), JOB varchar(200), DATE varchar(200), MONEY double)");

				s.executeUpdate(
						"CREATE TABLE IF NOT EXISTS earnings_stats_per_action (UUID varchar(200),IDACTION varchar(200), JOB varchar(200), ID varchar(200), TIMES int, MONEY double)");

				s.executeUpdate("CREATE TABLE IF NOT EXISTS jobs_plugin (DATE varchar(200), PLY varchar(200))");

				s.executeUpdate(
						"CREATE TABLE IF NOT EXISTS job_dates_joined (UUID varchar(200), JOBID varchar(200), DATE varchar(200))");

				s.executeUpdate("CREATE TABLE IF NOT EXISTS jobs_earnings_storage (UUID varchar(200),  AMOUNT double)");

				s.executeUpdate(
						"CREATE TABLE IF NOT EXISTS jobs_earnings_storage_dates (UUID varchar(200), CDATE varchar(200))");

				s.executeUpdate(
						"CREATE TABLE IF NOT EXISTS job_player_multipliers (UUID varchar(200), NAME varchar(200), PLUGIN varchar(200), TYPE varchar(200), UNTIL varchar(200), WEIGHT varchar(200), VAL double, JOB varchar(200))");

				cancel();
			}

		}.runTaskAsynchronously(plugin);
	}

	public boolean existMultiplier(String UUID, String name) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM job_player_multipliers WHERE UUID= '" + UUID + "' AND NAME='" + name + "'",
					rs -> {
						if (rs.next()) {
							a.set(rs.getString("PLUGIN"));
						}
						return 1;
					});
			return a.get() != null;

	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("Multipliers." + UUID + "." + name + ".Plugin") != null;

		}
		return false;
	}

	public void updateMultiplier(String uuid, String name, String by_plugin, MultiplierType type_of, String until,
			MultiplierWeight weight, double value, String job) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			final String insertQuery = "UPDATE `job_player_multipliers` SET PLUGIN='" + by_plugin + "' AND SET TYPE='"
					+ type_of.toString() + "' AND SET JOB='" + job + "' AND SET VAL='" + value + "' AND SET WEIGHT='"
					+ weight.toString() + "' AND SET UNTIL='" + until + "' WHERE UUID='" + uuid + "' AND NAME='" + name
					+ "'";
			mg.executeUpdate(insertQuery);

	} else if (mode.equals(DataMode.FILE)) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("Multipliers." + uuid + "." + name + ".Plugin", by_plugin);
			cfg.set("Multipliers." + uuid + "." + name + ".Type", type_of.toString());
			cfg.set("Multipliers." + uuid + "." + name + ".Until", until);
			cfg.set("Multipliers." + uuid + "." + name + ".Weight", weight.toString());
			cfg.set("Multipliers." + uuid + "." + name + ".Value", value);
			cfg.set("Multipliers." + uuid + "." + name + ".Job", job);

			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public ArrayList<JobsMultiplier> getMultipliers(String UUID) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		Collection<JobsMultiplier> ms = new ArrayList<JobsMultiplier>();
		if (mode.equals(DataMode.SQL)) {

			mg.executeQuery("SELECT * FROM job_player_multipliers WHERE UUID= '" + UUID + "'", rs -> {

				while (rs.next()) {

					String name = rs.getString("NAME");
					String by = rs.getString("PLUGIN");
					String type = rs.getString("TYPE");
					String until = rs.getString("UNTIL");
					String weight = rs.getString("WEIGHT");
					double value = rs.getDouble("VAL");
					String job = rs.getString("JOB");

					JobsMultiplier nw = new JobsMultiplier(name, by, MultiplierType.valueOf(type), until,
							MultiplierWeight.valueOf(weight), value, job);

					ms.add(nw);

				}

				return 1;
			});

			return (ArrayList<JobsMultiplier>) ms;
	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			List<String> listed = cfg.getStringList("MultipliersList." + UUID);

			for (String v : listed) {
				String name = v;
				String by = cfg.getString("Multipliers." + UUID + "." + name + ".Plugin");
				String type = cfg.getString("Multipliers." + UUID + "." + name + ".Type");
				String until = cfg.getString("Multipliers." + UUID + "." + name + ".Until");
				String weight = cfg.getString("Multipliers." + UUID + "." + name + ".Weight");
				double value = cfg.getDouble("Multipliers." + UUID + "." + name + ".Value");
				String job = cfg.getString("Multipliers." + UUID + "." + name + ".Job");

				JobsMultiplier nw = new JobsMultiplier(name, by, MultiplierType.valueOf(type), until,
						MultiplierWeight.valueOf(weight), value, job);

				ms.add(nw);

			}

			return (ArrayList<JobsMultiplier>) ms;
		}
		return null;
	}

	public void removeMultiplier(String UUID, String name) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			mg.executeUpdate("DELETE FROM job_player_multipliers WHERE UUID='" + UUID + "' AND NAME='" + name + "';");
		} else {
			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			List<String> listed = cfg.getStringList("MultipliersList." + UUID);

			cfg.set("Multipliers." + UUID + "." + name, null);
			listed.remove(name);
			cfg.set("MultipliersList." + UUID, listed);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void createMultiplier(String uuid, String name, String by_plugin, MultiplierType type_of, String until,
			MultiplierWeight weight, double value, String job) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			final String insertQuery = "INSERT INTO job_player_multipliers(UUID,NAME,PLUGIN,TYPE,UNTIL,WEIGHT,VAL, JOB) VALUES(?,?,?,?,?,?,?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, uuid);
				ps.setString(2, name);
				ps.setString(3, by_plugin);
				ps.setString(4, type_of.toString());
				ps.setString(5, until);
				ps.setString(6, weight.toString());
				ps.setDouble(7, value);
				if (job != null) {
					ps.setString(8, job);
				} else {
					ps.setString(8, "NONE");
				}
			});

	} else if (mode.equals(DataMode.FILE)) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			List<String> listed = cfg.getStringList("MultipliersList." + uuid);

			cfg.set("Multipliers." + uuid + "." + name + ".Plugin", by_plugin);
			cfg.set("Multipliers." + uuid + "." + name + ".Type", type_of.toString());
			cfg.set("Multipliers." + uuid + "." + name + ".Until", until);
			cfg.set("Multipliers." + uuid + "." + name + ".Weight", weight.toString());
			cfg.set("Multipliers." + uuid + "." + name + ".Value", value);
			cfg.set("Multipliers." + uuid + "." + name + ".Job", job);
			listed.add(name);
			cfg.set("MultipliersList." + uuid, listed);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void updateSalaryDate(String UUID, String date) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			if (existSalary(UUID)) {

				final String insertQuery = "UPDATE `jobs_earnings_storage_dates` SET `CDATE`='" + date
						+ "' WHERE UUID='" + UUID + "'";
				mg.executeUpdate(insertQuery);

			} else {
				final String insertQuery = "INSERT INTO jobs_earnings_storage_dates(UUID,CDATE) VALUES(?,?)";
				mg.executeUpdate(insertQuery, ps -> {
					ps.setString(1, UUID);
					ps.setString(2, date);
				});
			}

	} else if (mode.equals(DataMode.FILE)) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("CDATE." + UUID, date);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public String getSalaryDate(String UUID) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM jobs_earnings_storage_dates WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("CDATE"));
				}
				return 0;
			});
			return a.get();

	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("CDATE." + UUID);

		}
		return null;
	}

	public boolean existSalaryDate(String UUID) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM jobs_earnings_storage_dates WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("CDATE"));
				}
				return 1;
			});
			return a.get() != null;

	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.contains("CDATE." + UUID);
		}
		return false;
	}

	public double getSalary(String UUID) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (!existSalary(UUID)) {
			updateSalary(UUID, 0.0);
		}
		if (mode.equals(DataMode.SQL)) {

			AtomicDouble a = new AtomicDouble();

			mg.executeQuery("SELECT * FROM jobs_earnings_storage WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getDouble("AMOUNT"));
				}
				return 0;
			});
			return a.get();

	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getDouble("Salary." + UUID);

		}
		return 0.0;
	}

	public void updateSalary(String UUID, double sal) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			if (existSalary(UUID)) {

				final String insertQuery = "UPDATE `jobs_earnings_storage` SET `AMOUNT`='" + sal + "' WHERE UUID='"
						+ UUID + "'";
				mg.executeUpdate(insertQuery);

			} else {
				final String insertQuery = "INSERT INTO jobs_earnings_storage(UUID,AMOUNT) VALUES(?,?)";
				mg.executeUpdate(insertQuery, ps -> {
					ps.setString(1, UUID);
					ps.setDouble(2, 0.0);
				});
			}

	} else if (mode.equals(DataMode.FILE)) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("Salary." + UUID, sal);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public boolean existSalary(String UUID) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM jobs_earnings_storage WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("UUID"));
				}
				return 1;
			});
			return a.get() != null;

	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.contains("Salary." + UUID);
		}
		return false;
	}

	public String getJobDateJoined(String UUID, String job) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (!existDateJoined(UUID, job)) {
			updateDateJoinedOfJob(UUID, job, plugin.getPluginManager().getDateTodayFromCal());
		}
		if (mode.equals(DataMode.SQL)) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM job_dates_joined WHERE UUID= '" + UUID + "' AND JOBID= '" + job + "'",
					rs -> {
						if (rs.next()) {
							a.set(rs.getString("DATE"));
						}
						return 0;
					});
			return a.get();

	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("JobDates." + UUID + ".Job." + job);

		}
		return null;
	}

	public void updateDateJoinedOfJob(String UUID, String job, String date) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			if (existDateJoined(UUID, job)) {

				final String insertQuery = "UPDATE `job_dates_joined` SET `DATE`='" + date + "' WHERE UUID='" + UUID
						+ "' AND JOBID= '" + job + "'";
				mg.executeUpdate(insertQuery);

			} else {
				final String insertQuery = "INSERT INTO job_dates_joined(UUID,JOBID,DATE) VALUES(?,?,?)";
				mg.executeUpdate(insertQuery, ps -> {
					ps.setString(1, UUID);
					ps.setString(2, job);
					ps.setString(3, plugin.getPluginManager().getDateTodayFromCal());
				});
			}

	} else if (mode.equals(DataMode.FILE)) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("JobDates." + UUID + ".Job." + job, date);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public boolean existDateJoined(String UUID, String job) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM job_dates_joined WHERE UUID= '" + UUID + "' AND JOBID= '" + job + "'",
					rs -> {
						if (rs.next()) {
							a.set(rs.getString("DATE"));
						}
						return 1;
					});
			return a.get() != null;

	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.contains("JobDates." + UUID + ".Job." + job);
		}
		return false;
	}

	public void createFirstPluginStart(String date) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			final String insertQuery = "INSERT INTO jobs_plugin(DATE,PLY) VALUES(?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, date);
				ps.setString(2, "null");
			});

	} else if (mode.equals(DataMode.FILE)) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("Plugin.Data.Date", date);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public boolean isFirstPluginStart() {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			AtomicBoolean a = new AtomicBoolean(false);

			mg.executeQuery("SELECT * FROM jobs_plugin", rs -> {

				if (rs.next()) {
					if (rs.getString("DATE") != null) {
						a.set(true);
					}
				}
				return 1;
			});

			return a.get();

	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("Plugin.Data.Date") != null;

		}
		return false;
	}

	public HashMap<String, String> getSettingsOfPlayer(String uuid) {

		DataMode mode = UltimateJobs.getPlugin().getPluginMode();

		HashMap<String, String> data = new HashMap<String, String>();

		if (mode.equals(DataMode.SQL)) {

			Collection<String> settings = new ArrayList<String>();
			mg.executeQuery("SELECT * FROM playersettings WHERE UUID='" + uuid + "'", rs -> {

				while (rs.next()) {
					settings.add(rs.getString("TYPE"));
				}

				return 1;
			});

			settings.forEach((type) -> {
				data.put(type, getSettingData(uuid, type));
			});

		} else {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			List<String> listed = cfg.getStringList("SettingsList." + uuid);

			listed.forEach((type) -> {
				data.put(type, getSettingData(uuid, type));
			});

		}

		return data;
	}

	public boolean existSettingData(String UUID, String id) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM playersettings WHERE UUID= '" + UUID + "' AND TYPE= '" + id + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("TYPE"));
				}
				return 1;
			});
			return a.get() != null;

		}
		return false;
	}

	public boolean existPlayer(String UUID) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM playerlist WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("NAME"));
				}
				return 1;
			});
			return a.get() != null;

	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("Fetcher." + UUID + ".Name") != null;

		}
		return false;
	}

	public void createSettingData(String UUID, String type, String value) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			final String insertQuery = "INSERT INTO playersettings(UUID,TYPE,MODE) VALUES(?,?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, type);
				ps.setString(3, value);
			});

	} else if (mode.equals(DataMode.FILE)) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			List<String> listed = cfg.getStringList("SettingsList." + UUID);

			listed.add(type);

			cfg.set("Settings." + UUID + "." + type, value);

			cfg.set("SettingsList." + UUID, listed);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void updateSettingData(String UUID, String type, String value) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			final String insertQuery = "UPDATE `playersettings` SET `MODE`='" + value + "' WHERE UUID='" + UUID
					+ "' AND TYPE= '" + type + "'";
			mg.executeUpdate(insertQuery);

	} else if (mode.equals(DataMode.FILE)) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("Settings." + UUID + "." + type, value);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public String getSettingData(String UUID, String type) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM playersettings WHERE UUID= '" + UUID + "' AND TYPE= '" + type + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("MODE"));
				}
				return 0;
			});
			return a.get();

	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("Settings." + UUID + "." + type);

		}
		return null;
	}

	public void updateName(String UUID, String name) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			final String insertQuery = "UPDATE `playerlist` SET `NAME`='" + name + "' WHERE UUID='" + UUID + "'";
			mg.executeUpdate(insertQuery);

	} else if (mode.equals(DataMode.FILE)) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("Fetcher." + UUID + ".Name", name);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void updateDisplay(String UUID, String name) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			final String insertQuery = "UPDATE `playerlist` SET `DISPLAY`='" + name + "' WHERE UUID='" + UUID + "'";
			mg.executeUpdate(insertQuery);

	} else if (mode.equals(DataMode.FILE)) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("Fetcher." + UUID + ".Name", name);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void addAPlayerToList(String UUID, String name, String dis) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			final String insertQuery = "INSERT INTO playerlist(UUID,NAME,DISPLAY) VALUES(?,?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, name);
				ps.setString(3, dis);
			});

	} else if (mode.equals(DataMode.FILE)) {

			List<String> list = getUltimatePlayers();

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			list.add(UUID);

			cfg.set("Players", list);
			cfg.set("Fetcher." + name.toUpperCase() + ".UUID", UUID);
			cfg.set("Fetcher." + UUID + ".Name", name.toUpperCase());
			cfg.set("Fetcher." + UUID + ".Display", dis);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public boolean existInPlayersList(String uuid) {
		return getUltimatePlayers().contains(uuid);
	}

	public List<String> getUltimatePlayers() {

		DataMode mode = UltimateJobs.getPlugin().getPluginMode();

		if (mode.equals(DataMode.SQL)) {

			Collection<String> jobs = new ArrayList<String>();
			mg.executeQuery("SELECT * FROM playerlist", rs -> {

				while (rs.next()) {
					jobs.add(rs.getString("UUID"));
				}

				return 1;
			});

			return (List<String>) jobs;

		} else {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.getStringList("Players");

		}
	}

	public String getUUIDByName(String name) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM playerlist WHERE NAME= '" + name + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("UUID"));
				}
				return 0;
			});
			return a.get();

	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.getString("Fetcher." + name.toUpperCase() + ".UUID");
		}
		return null;
	}

	public String getDisplayByUUID(String UUID) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM playerlist WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("DISPLAY"));
				}
				return 0;
			});
			return a.get();

	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.getString("Fetcher." + UUID + ".Display");
		}
		return null;
	}

	public String getNameByUUID(String UUID) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM playerlist WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("NAME"));
				}
				return 0;
			});
			return a.get();

	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.getString("Fetcher." + UUID + ".Name");
		}
		return null;
	}

	public void updateEarningsTimesOf(String UUID, String job, String id, int time, String action) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			final String insertQuery = "UPDATE `earnings_stats_per_action` SET `TIMES`='" + time + "' WHERE UUID='"
					+ UUID + "' AND JOB= '" + job + "' AND ID= '" + id + "' AND IDACTION= '" + action + "'";
			mg.executeUpdate(insertQuery);
	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			cfg.set("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Times", time);
			save(cfg, file);

		}
	}

	public void updateEarningsAmountOf(String UUID, String job, String id, double money, String action) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			final String insertQuery = "UPDATE `earnings_stats_per_action` SET `MONEY`='" + money + "' WHERE UUID='"
					+ UUID + "' AND JOB= '" + job + "' AND ID= '" + id + "' AND IDACTION= '" + action + "'";
			mg.executeUpdate(insertQuery);
	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();
			cfg.set("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Money", money);
			save(cfg, file);

		}
	}

	public boolean ExistEarningsOfBlock(String UUID, String job, String id, String action) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM earnings_stats_per_action WHERE UUID= '" + UUID + "' AND JOB= '" + job
					+ "' AND ID= '" + id + "' AND IDACTION= '" + action + "'", rs -> {
						if (rs.next()) {
							a.set(rs.getString("ID"));
						}
						return 1;
					});
			return a.get() != null;
	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.contains("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Times");
		}
		return false;
	}

	public int getBrokenTimesOfBlock(String UUID, String job, String id, String action) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			if (ExistEarningsOfBlock(UUID, job, id, action)) {
				AtomicInteger a = new AtomicInteger();

				mg.executeQuery("SELECT * FROM earnings_stats_per_action WHERE UUID= '" + UUID + "' AND JOB= '" + job
						+ "' AND ID= '" + id + "' AND IDACTION= '" + action + "'", rs -> {
							if (rs.next()) {
								a.set(rs.getInt("TIMES"));
							}
							return 0;
						});
				return a.get();
			} else {
				createEarningsOfBlock(UUID, job, id, action);
			}
	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			if (cfg.contains("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Times")) {
				return cfg.getInt("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Times");
			} else {
				cfg.set("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Times", 0);
				save(cfg, file);
			}
		}
		return 0;
	}

	public double getEarnedOfBlock(String UUID, String job, String id, String action) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			if (ExistEarningsOfBlock(UUID, job, id, action)) {
				AtomicDouble a = new AtomicDouble();

				mg.executeQuery("SELECT * FROM earnings_stats_per_action WHERE UUID= '" + UUID + "' AND JOB= '" + job
						+ "' AND ID= '" + id + "' AND IDACTION= '" + action + "'", rs -> {
							if (rs.next()) {
								a.set(rs.getDouble("MONEY"));
							}
							return 0;
						});
				return a.get();
			} else {
				createEarningsOfBlock(UUID, job, id, action);
			}

	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			if (cfg.contains("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Money")) {
				return cfg.getDouble("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Money");
			} else {
				cfg.set("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Money", 0);
				save(cfg, file);
			}

		}
		return 0;
	}

	public void createEarningsOfBlock(String UUID, String job, String id, String action) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			final String insertQuery = "INSERT INTO earnings_stats_per_action(UUID,IDACTION,JOB,ID,TIMES,MONEY) VALUES(?,?,?,?,?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, action);
				ps.setString(3, job);
				ps.setString(4, id);
				ps.setInt(5, 0);
				ps.setDouble(6, 0);
			});
		}
	}

	public boolean ExistEarningsDataToday(String UUID, String job, String date) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM earnings_all WHERE UUID= '" + UUID + "' AND JOB= '" + job + "' AND DATE= '"
					+ date + "'", rs -> {
						if (rs.next()) {
							a.set(rs.getString("DATE"));
						}
						return 1;
					});
			return a.get() != null;
	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.getString("Earnings." + UUID + "." + date + "." + job) != null;
		}
		return false;
	}

	public void createEarningsData(String UUID, String job, String date) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			final String insertQuery = "INSERT INTO earnings_all(UUID,JOB,DATE,MONEY) VALUES(?,?,?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, job);
				ps.setString(3, date);
				ps.setInt(4, 0);

			});
		}
	}

	public void updatePoints(String UUID, double value) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			final String insertQuery = "UPDATE `job_players` SET `POINTS`='" + value + "' WHERE UUID='" + UUID + "'";
			mg.executeUpdate(insertQuery);
	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			cfg.set("Player." + UUID + ".Points", value);
			save(cfg, file);
		}
	}

	public void updateLevel(String UUID, int value, String job) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			final String insertQuery = "UPDATE `job_stats` SET `LEVEL`='" + value + "' WHERE UUID='" + UUID
					+ "' AND JOB='" + job + "'";
			mg.executeUpdate(insertQuery);
	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			cfg.set("Jobs." + UUID + "." + job + ".Level", value);
			save(cfg, file);

		}
	}

	public void updateMax(String UUID, int value) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			final String insertQuery = "UPDATE `job_players` SET `MAX`='" + value + "' WHERE UUID='" + UUID + "'";
			mg.executeUpdate(insertQuery);
	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			cfg.set("Player." + UUID + ".Max", value);
			save(cfg, file);

		}
	}

	public void updateExp(String UUID, double d, String job) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			final String insertQuery = "UPDATE `job_stats` SET `EXP`='" + d + "' WHERE UUID='" + UUID + "' AND JOB`='"
					+ job + "'";
			mg.executeUpdate(insertQuery);
	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			cfg.set("Jobs." + UUID + "." + job + ".Exp", d);
			save(cfg, file);

		}
	}

	public void savePlayer(JobsPlayer pl, String UUID) {

		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			mg.executeUpdate("DELETE FROM job_current WHERE UUID='" + UUID + "';");

			Collection<String> current = pl.getCurrentJobs();
			Collection<String> owned = pl.getOwnJobs();
			int max = pl.getMaxJobs();
			double points = pl.getPoints();
			
			final String insertQuery_owned = "INSERT INTO job_current(UUID,JOB) VALUES(?,?)";

			if (current != null) {
				for (String job : current) {

					mg.executeUpdate(insertQuery_owned, ps -> {
						ps.setString(1, UUID);
						ps.setString(2, job);

					});
				}
			}

			HashMap<String, String> settings = pl.getPlayerSettings();

			if (!settings.isEmpty()) {
				settings.forEach((type, value) -> {
					if (existSettingData(UUID, type)) {

						final String insertQuery = "UPDATE `playersettings` SET `MODE`='" + value + "' WHERE UUID='"
								+ UUID + "' AND TYPE= '" + type + "'";
						mg.executeUpdate(insertQuery);

					} else {

						final String insertQuery = "INSERT INTO playersettings(UUID,TYPE,MODE) VALUES(?,?,?)";
						mg.executeUpdate(insertQuery, ps -> {
							ps.setString(1, UUID);
							ps.setString(2, type);
							ps.setString(3, value);
						});

					}
				});
			}

			String dated = UltimateJobs.getPlugin().getDate();
 
			if (dated != null) {
				final String insertQuery = "UPDATE `job_players` SET `DATE`='" + dated + "' WHERE UUID='" + UUID + "'";
				mg.executeUpdate(insertQuery);
			}

			final String insertQuery_points = "UPDATE `job_players` SET `POINTS`='" + points + "' WHERE UUID='" + UUID
					+ "'";
			mg.executeUpdate(insertQuery_points);

			final String insertQuery_max = "UPDATE `job_players` SET `MAX`='" + max + "' WHERE UUID='" + UUID + "'";
			mg.executeUpdate(insertQuery_max);
 
			double sal = pl.getSalary();

			String sat = pl.getSalaryDate();

			final String insertQuery_sal = "UPDATE `jobs_earnings_storage_dates` SET `CDATE`='" + sat + "' WHERE UUID='"
					+ UUID + "'";
			mg.executeUpdate(insertQuery_sal);

			if (existSalary(UUID)) {

				final String insertQuery = "UPDATE `jobs_earnings_storage` SET `AMOUNT`='" + sal + "' WHERE UUID='"
						+ UUID + "'";
				mg.executeUpdate(insertQuery);

			} else {
				final String insertQuery = "INSERT INTO jobs_earnings_storage(UUID,AMOUNT) VALUES(?,?)";
				mg.executeUpdate(insertQuery, ps -> {
					ps.setString(1, UUID);
					ps.setDouble(2, 0.0);
				});
			}

			for (JobsMultiplier s : pl.getMultipliers()) {
				String name = s.getName();
				String by = s.getByPlugin();
				MultiplierType type = s.getType();
				String until = s.getUntil();
				MultiplierWeight weight = s.getWeight();
				double value = s.getValue();
				String job = s.getJob();

				if (existMultiplier(UUID, name)) {

					final String insertQuery = "UPDATE `job_player_multipliers` SET PLUGIN='" + by + "' AND SET TYPE='"
							+ type.toString() + "' AND SET JOB='" + job + "' AND SET VAL='" + value
							+ "' AND SET WEIGHT='" + weight.toString() + "' AND SET UNTIL='" + until + "' WHERE UUID='"
							+ UUID + "' AND NAME='" + name + "'";
					mg.executeUpdate(insertQuery);

				} else {

					final String insertQuery = "INSERT INTO job_player_multipliers(UUID,NAME,PLUGIN,TYPE,UNTIL,WEIGHT,VAL, JOB) VALUES(?,?,?,?,?,?,?,?)";
					mg.executeUpdate(insertQuery, ps -> {
						ps.setString(1, UUID);
						ps.setString(2, name);
						ps.setString(3, by);
						ps.setString(4, type.toString());
						ps.setString(5, until);
						ps.setString(6, weight.toString());
						ps.setDouble(7, value);
						if (job != null) {
							ps.setString(8, job);
						} else {
							ps.setString(8, "NONE");
						}
					});

					;
				}

			}

			for (String job : owned) {
 
				Job j = plugin.getJobCache().get(job);
				
				JobStats stats = pl.getStatsOf(job);
				
				int level = stats.getLevel();
				double exp = stats.getExp();
				int broken = stats.getHowManyTimesWorked();
				String date = stats.getDate();

				String jd = stats.getJoinedDate();
			 
				if(! ExistJobData(UUID, job)) {
				 
					final String insertQuery = "INSERT INTO job_stats(UUID,JOB,DATE,LEVEL,EXP,BROKEN) VALUES(?,?,?,?,?,?)";
					mg.executeUpdate(insertQuery, ps -> {
						ps.setString(1, UUID);
						ps.setString(2, job);
						ps.setString(3, date);
						ps.setInt(4, level);
						ps.setDouble(5, exp);
						ps.setInt(6, broken);
					});
					
				}
				 
				stats.getEarningDatesList().forEach((key, value) -> {

					final String insertQuery = "UPDATE `earnings_all` SET `MONEY`='" + value + "' WHERE UUID='" + UUID
							+ "' AND JOB= '" + job + "' AND DATE= '" + key + "' ";
					mg.executeUpdate(insertQuery);

				});

				plugin.getPlayerChunkAPI().savePlayerChunks(UUID, j);

			 

				final String insertQuery = "UPDATE `job_dates_joined` SET `DATE`='" + jd + "' WHERE UUID='" + UUID
						+ "' AND JOBID= '" + job + "'";
				mg.executeUpdate(insertQuery);

				final String insertQuery_date = "UPDATE `job_stats` SET `DATE`='" + date + "' WHERE UUID='" + UUID
						+ "' AND JOB='" + j.getConfigID() + "'";
				mg.executeUpdate(insertQuery_date);

				final String insertQuery_level = "UPDATE `job_stats` SET `LEVEL`='" + level + "' WHERE UUID='" + UUID
						+ "' AND JOB='" + j.getConfigID() + "'";
				mg.executeUpdate(insertQuery_level);

				final String insertQuery_exp = "UPDATE `job_stats` SET `EXP`='" + exp + "' WHERE UUID='" + UUID
						+ "' AND JOB='" + j.getConfigID() + "'";
				mg.executeUpdate(insertQuery_exp);

				final String insertQuery_br = "UPDATE `job_stats` SET `BROKEN`='" + broken + "' WHERE UUID='" + UUID
						+ "' AND JOB='" + j.getConfigID() + "'";
				mg.executeUpdate(insertQuery_br);

				for (JobAction action : j.getActionList()) {

					stats.getTimesExecutedMoneyList().forEach((key, value) -> {

						final String insert_earnings = "UPDATE `earnings_stats_per_action` SET `MONEY`='" + value
								+ "' WHERE UUID='" + UUID + "' AND JOB= '" + job + "' AND ID= '" + key
								+ "' AND IDACTION= '" + action.toString() + "'";
						mg.executeUpdate(insert_earnings);

					});

					stats.getWorkedTimesOfIDList().forEach((key, value) -> {

						final String insert_times = "UPDATE `earnings_stats_per_action` SET `TIMES`='" + value
								+ "' WHERE UUID='" + UUID + "' AND JOB= '" + job + "' AND ID= '" + key
								+ "' AND IDACTION= '" + action.toString() + "'";
						mg.executeUpdate(insert_times);

					});

				}

			}

			 
	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			Collection<String> current = pl.getCurrentJobs();
			Collection<String> owned = pl.getOwnJobs();
			int max = pl.getMaxJobs();
			double points = pl.getPoints();
			double sal = pl.getSalary();

			String sat = pl.getSalaryDate();

			HashMap<String, String> settings = pl.getPlayerSettings();

			if (!settings.isEmpty()) {
				settings.forEach((type, value) -> {
					if (existSettingData(UUID, type)) {
						cfg.set("Settings." + UUID + "." + type, value);
					} else {
						List<String> listed = cfg.getStringList("SettingsList." + UUID);

						listed.add(type);

						cfg.set("Settings." + UUID + "." + type, value);

						cfg.set("SettingsList." + UUID, listed);
					}
				});
			}

			cfg.set("Player." + UUID + ".Points", points);
			cfg.set("CDATE." + UUID, sat);
			cfg.set("Player." + UUID + ".Date", plugin.getDate());
			cfg.set("Player." + UUID + ".Max", max);
			cfg.set("Salary." + UUID, sal);

			ArrayList<String> newowned = new ArrayList<String>();

			for (JobsMultiplier s : pl.getMultipliers()) {
				String name = s.getName();

				String by = s.getByPlugin();
				MultiplierType type = s.getType();
				String until = s.getUntil();
				MultiplierWeight weight = s.getWeight();
				double value = s.getValue();
				String job = s.getJob();

				if (existMultiplier(UUID, name)) {

					cfg.set("Multipliers." + UUID + "." + name + ".Plugin", by);
					cfg.set("Multipliers." + UUID + "." + name + ".Type", type.toString());
					cfg.set("Multipliers." + UUID + "." + name + ".Until", until);
					cfg.set("Multipliers." + UUID + "." + name + ".Weight", weight.toString());
					cfg.set("Multipliers." + UUID + "." + name + ".Value", value);
					cfg.set("Multipliers." + UUID + "." + name + ".Job", job);

				} else {
					List<String> listed = cfg.getStringList("MultipliersList." + UUID);

					cfg.set("Multipliers." + UUID + "." + name + ".Plugin", by);
					cfg.set("Multipliers." + UUID + "." + name + ".Type", type.toString());
					cfg.set("Multipliers." + UUID + "." + name + ".Until", until);
					cfg.set("Multipliers." + UUID + "." + name + ".Weight", weight.toString());
					cfg.set("Multipliers." + UUID + "." + name + ".Value", value);
					cfg.set("Multipliers." + UUID + "." + name + ".Job", job);
					listed.add(name);
					cfg.set("MultipliersList." + UUID, listed);

				}

			}

			for (String job : owned) {

				Job j = plugin.getJobCache().get(job);

				JobStats stats = pl.getStatsOf(job);

				List<String> listed = cfg.getStringList("EarningsList." + UUID + "." + job);
				
				
				stats.getEarningDatesList().forEach((key, value) -> {
					listed.add(key);
					cfg.set("EarnedDate." + UUID + "." + key + "." + job, value);
				});
				
				cfg.set("EarningsList." + UUID + "." + job, listed);

				int level = stats.getLevel();
				double exp = stats.getExp();
				int broken = stats.getHowManyTimesWorked();
				String date = stats.getDate();

				cfg.set("Jobs." + UUID + "." + job + ".Level", level);
				cfg.set("Jobs." + UUID + "." + job + ".Date", date);
				cfg.set("Jobs." + UUID + "." + job + ".Broken", broken);
				cfg.set("Jobs." + UUID + "." + job + ".Exp", exp);

				newowned.add(job);

				plugin.getPlayerChunkAPI().savePlayerChunks(UUID, j);

				for (JobAction action : j.getActionList()) {

					stats.getTimesExecutedMoneyList().forEach((key, value) -> {

						cfg.set("Earnings." + UUID + "." + job + "." + key + ".Action." + action.toString() + ".Money",
								value);

					});

					stats.getWorkedTimesOfIDList().forEach((key, value) -> {

						cfg.set("Earnings." + UUID + "." + job + "." + key + ".Action." + action.toString() + ".Times",
								value);

					});

				}

			}

			cfg.set("Player." + UUID + ".Owned", newowned);
			cfg.set("Player." + UUID + ".Current", current);

			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void updateEarnings(String UUID, String job, String date, double money) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			final String insertQuery = "UPDATE `earnings_all` SET `MONEY`='" + money + "' WHERE UUID='" + UUID
					+ "' AND JOB= '" + job + "' AND DATE= '" + date + "' ";
			mg.executeUpdate(insertQuery);
	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();
			cfg.set("EarnedDate." + UUID + "." + date + "." + job, money);
			save(cfg, file);

		}
	}

	public double getEarnedAt(String UUID, String job, String date) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			if (ExistEarningsDataToday(UUID, job, date)) {
				AtomicDouble a = new AtomicDouble();

				mg.executeQuery("SELECT * FROM earnings_all WHERE UUID= '" + UUID + "' AND JOB= '" + job
						+ "' AND DATE= '" + date + "'", rs -> {
							if (rs.next()) {
								a.set(rs.getDouble("MONEY"));
							}
							return 0;
						});
				return a.get();
			} else {
				createEarningsData(UUID, job, date);
			}

	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getDouble("EarnedDate." + UUID + "." + date + "." + job);

		}
		return 0.1;
	}
	
	public ArrayList<String> getAllEarnings(String UUID, String job) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
		 
				Collection<String> jobs = new ArrayList<String>();
				mg.executeQuery("SELECT * FROM earnings_all WHERE UUID= '" + UUID + "' AND JOB='"+job+"'", rs -> {

					while (rs.next()) {
						jobs.add(rs.getString("DATE"));
					}

					return 1;
				});

				return (ArrayList<String>) jobs;
		
	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return (ArrayList<String>) cfg.getStringList("EarningsList." + UUID + "." + job);

		}
		return null; 
	}

	public void updateBrokenTimes(String UUID, String job, int val) {

		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			final String insertQuery = "UPDATE `job_stats` SET `BROKEN`='" + val + "' WHERE UUID='" + UUID
					+ "' AND JOB='" + job + "'";
			mg.executeUpdate(insertQuery);
	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			cfg.set("Jobs." + UUID + "." + job + ".Broken", val);
			save(cfg, file);
		}
	}
 
	public boolean ExistJobData(String UUID, String job) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("DATE"));
				}
				return 1;
			});
			return a.get() != null;
	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("Jobs." + UUID + "." + job + ".Date") != null;
		}
		return false;
	}

	public int getLevelOf(String UUID, String job) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			AtomicInteger a = new AtomicInteger(0);

			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getInt("LEVEL"));
				}
				return 1;
			});
			return a.get();
	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getInt("Jobs." + UUID + "." + job + ".Level");
		}
		return 1;
	}

	public double getExpOf(String UUID, String job) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			AtomicDouble a = new AtomicDouble();

			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getDouble("EXP"));
				}
				return 0;
			});
			return a.get();
	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getDouble("Jobs." + UUID + "." + job + ".Exp");
		}
		return 0;
	}

	public int getBrokenOf(String UUID, String job) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			AtomicInteger a = new AtomicInteger(0);

			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getInt("BROKEN"));
				}
				return 0;
			});
			return a.get();
	} else if (mode.equals(DataMode.FILE)) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getInt("Jobs." + UUID + "." + job + ".Broken");

		}
		return 0;
	}

	public String getDateOf(String UUID, String job) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("DATE"));
				}
				return 0;
			});
			return a.get();
	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("Jobs." + UUID + "." + job + ".Date");
		}
		return null;
	}

	public ArrayList<String> getOwnedJobs(String UUID) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			Collection<String> jobs = new ArrayList<String>();
			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "'", rs -> {

				while (rs.next()) {
					jobs.add(rs.getString("JOB"));
				}

				return 1;
			});

			return (ArrayList<String>) jobs;
	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return (ArrayList<String>) cfg.getStringList("Player." + UUID + ".Owned");
		}
		return null;
	}

	public ArrayList<String> getCurrentJobs(String UUID) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			Collection<String> jobs = new ArrayList<String>();
			mg.executeQuery("SELECT * FROM job_current WHERE UUID= '" + UUID + "'", rs -> {

				while (rs.next()) {
					jobs.add(rs.getString("JOB"));
				}

				return 1;
			});

			return (ArrayList<String>) jobs;
	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return (ArrayList<String>) cfg.getStringList("Player." + UUID + ".Current");
		}
		return null;
	}

	public double getPoints(String UUID) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			AtomicDouble a = new AtomicDouble();

			mg.executeQuery("SELECT * FROM job_players WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getDouble("POINTS"));
				}
				return 0.0;
			});
			return a.get();
	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getDouble("Player." + UUID + ".Points");
		}
		return 0.0;
	}

	public int getMaxJobs(String UUID) {
		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {
			AtomicInteger a = new AtomicInteger(0);

			mg.executeQuery("SELECT * FROM job_players WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getInt("MAX"));
				}
				return 0;
			});
			return a.get();
	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getInt("Player." + UUID + ".Max");
		}
		return 0;
	}

	public void createPlayer(String UUID, String name) {

		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			String date = UltimateJobs.getPlugin().getDate();
			createPlayerDetails(UUID, date);

	} else if (mode.equals(DataMode.FILE)) {
			String date = plugin.getDate();
			createPlayerDetails(UUID, date);
		}
	}

	public void createPlayerDetails(String UUID, String date) {

		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			int max = UltimateJobs.getPlugin().getLocalFileManager().getConfig().getInt("MaxDefaultJobs") - 1;
			final String insertQuery = "INSERT INTO job_players(UUID,DATE,POINTS,MAX) VALUES(?,?,?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, "" + date);
				ps.setInt(3, 0);
				ps.setInt(4, max);

			});

	} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();
			int max = UltimateJobs.getPlugin().getLocalFileManager().getConfig().getInt("MaxDefaultJobs") - 1;

			ArrayList<String> list = new ArrayList<String>();
			cfg.set("Player." + UUID + ".Points", 0);
			cfg.set("Player." + UUID + ".Max", max);
			cfg.set("Player." + UUID + ".Date", date);
			cfg.set("Player." + UUID + ".Owned", list);
			cfg.set("Player." + UUID + ".Current", list);

			save(cfg, file);
		}
	}

	public boolean ExistPlayer(String UUID) {

		DataMode mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equals(DataMode.SQL)) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM job_players WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("DATE"));
				}
				return 1;
			});
			return a.get() != null;

		} else if (mode.equals(DataMode.FILE)) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.getString("Player." + UUID + ".Date") != null;
		}
		return false;
	}

	public void save(FileConfiguration cfg, File file) {
		if (file != null) {
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
		}
	}

}
