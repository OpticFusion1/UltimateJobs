package de.warsteiner.jobs.inventorys;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.cevents.PlayerLanguageChangeEvent;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.objects.Language;
import de.warsteiner.jobs.utils.objects.UpdateTypes;

public class ClickAtLanguageGUI implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

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
		
		if(!plugin.getPlayerAPI().existInCacheByUUID(""+e.getWhoClicked().getUniqueId())) {
			e.getWhoClicked().sendMessage("§cError while executing the Jobs ClickEvent. (Player not found)");
			return;
		}

		String dis = e.getCurrentItem().getItemMeta().getDisplayName();
		Player player = (Player) e.getWhoClicked();

		JobsPlayer pp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());

		FileConfiguration cfg = plugin.getFileManager().getLanguageGUIConfig();

		if (plugin.getGUIOpenManager().isLanguageOpend(player, e.getView().getTitle()) != null) {

			plugin.getClickManager().executeCustomItem(null, dis, player, "Custom", cfg, null);

			if (plugin.getClickManager().isLanguageItem(dis) != null) {
				Language lang = plugin.getClickManager().isLanguageItem(dis);

				if (lang.getName().equalsIgnoreCase(
						plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId()).getLanguage().getName())) {
					player.sendMessage(
							pp.getLanguage().getMessage("LanguageChoosenMessage"));
					plugin.getAPI().playSound("LANGUAGE_ALREADY", player);
				} else {

					Language old = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId()).getLanguage();
					Language newl = lang;

					new PlayerLanguageChangeEvent(player, pp, player.getUniqueId(), old, newl);

					if (UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(pp.getUUIDAsString()) != null) {
						JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(pp.getUUIDAsString());

						jb.updateLocalLanguage(lang);
					}
					
					plugin.getPlayerAPI().updateSettingData(pp.getUUIDAsString(), "LANG", lang.getName());
					
					player.sendMessage(
							pp.getLanguage().getMessage("LanguageChangedMessage")
									.replaceAll("<lang>", lang.getID().toLowerCase()));
					plugin.getAPI().playSound("LANGUAGE_UPDATED", player);
					plugin.getGUI().openLanguageMenu(player, UpdateTypes.REOPEN);
				}
			}

			e.setCancelled(true);
		}
	}
}
