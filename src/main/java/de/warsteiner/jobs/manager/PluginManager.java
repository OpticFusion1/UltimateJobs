package de.warsteiner.jobs.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.objects.DataMode;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;

public class PluginManager {

	private UltimateJobs plugin = UltimateJobs.getPlugin();

	public static final Pattern HEX_PATTERN = Pattern.compile("#(\\w{5}[0-9a-f])#");

	public boolean isInstalled(String plugin) {
		Plugin Plugin = Bukkit.getServer().getPluginManager().getPlugin(plugin);
		if (Plugin != null) {
			return true;
		}
		return false;
	}

	public String getDateTodayFromCal() {
		DateFormat format = new SimpleDateFormat(plugin.getFileManager().getConfig().getString("Date"));
		Date data = new Date();
		return format.format(data);
	}

	public String getDateTodayFromCalWith() {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date data = new Date();
		return format.format(data);
	}

	public String formatText(String text, Map<String, String> replacer, OfflinePlayer player) {
		text = toHex(text).replaceAll("&", "§");
		for (String key : replacer.keySet()) {
			text = text.replaceAll(key, replacer.get(key));
		}

		if (plugin.getPluginManager().isInstalled("PlaceHolderAPI")) {
			return PlaceholderAPI.setPlaceholders(player, text);
		}

		return text;
	}

	public void startCheck() {
		new BukkitRunnable() {

			@Override
			public void run() {

				new BukkitRunnable() {

					@Override
					public void run() {

						if (plugin.getPluginMode().equals(DataMode.SQL)) {
							if (plugin.getInit().isClosed()) {

								plugin.connect();

							}
						}  
						cancel();
					}
				}.runTaskAsynchronously(plugin);
			}

		}.runTaskTimer(plugin, 0, 20 * plugin.getFileManager().getDataConfig().getInt("CheckConnectionEvery"));
	}

	public boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public String toHex(String textToTranslate) {

		Matcher matcher = HEX_PATTERN.matcher(textToTranslate);
		StringBuffer buffer = new StringBuffer();

		while (matcher.find()) {
			matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
		}

		return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());

	}

	private List<Material> breakingMaterials = List.of(Material.SUGAR_CANE, Material.CACTUS, Material.BAMBOO,
			Material.CRIMSON_FUNGUS, Material.WARPED_FUNGUS, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM,
			Material.MELON, Material.PUMPKIN);

	private List<Material> bypassmeta = List.of(Material.CRIMSON_FUNGUS, Material.WARPED_FUNGUS,
			Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.CARROTS, Material.WHEAT, Material.POTATOES,
			Material.BEETROOTS, Material.BAMBOO, Material.COCOA, Material.NETHER_WART);

	public boolean isFullyGrown(Block block) { 
		if (breakingMaterials.contains(block.getType())) {
			return true;
		}

		if (block.hasMetadata("placed-by-player") && !bypassmeta.contains(block.getType())) {
			return false;
		} 
		if (block.getBlockData() == null) {
			return false;
		}  
		BlockData bdata = block.getBlockData();
		if (bdata instanceof Ageable) {
			Ageable age = (Ageable) bdata;
			if (age.getAge() == age.getMaximumAge()) {
				return true;
			}
		}
		return false;
	}

}
