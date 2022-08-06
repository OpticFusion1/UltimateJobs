package de.warsteiner.jobs.inventorys;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;

public class ClickAtUpdateMenuEvent implements Listener {

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

		Player p = (Player) e.getWhoClicked();

		String display = plugin.getPluginManager()
				.toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String title = plugin.getPluginManager().toHex(e.getView().getTitle().replaceAll("&", "§"));

		if (title.equalsIgnoreCase("§aUpdate Found")) {

			if (display.equalsIgnoreCase("§e☆ §7Download Update §e☆")) {

				plugin.getWebManager().downloadUpdate(p);

			} else if (display.equalsIgnoreCase("§c✘ §7Close §c✘")) {

				p.closeInventory();

			} else if (display.equalsIgnoreCase("§a◇ §7Documentation Site §a◇")) {

				p.sendMessage(AdminCommand.prefix
						+ "§7You can visit our Documentation Site here: §b§lhttps://dto.mc-plugins.org");

			} else if (display.equalsIgnoreCase("§5☆ §7Our Discord Server §5☆")) {

				p.sendMessage(AdminCommand.prefix
						+ "§7You can join our Discord with this Invite: §a§lhttps://dcto.mc-plugins.org");

			}

			e.setCancelled(true);

		} else if (title.equalsIgnoreCase("§cNo Update Found")) {

			if (display.equalsIgnoreCase("§e☆ §7Check for Updates §e☆")) {
				p.closeInventory();
				p.sendMessage(AdminCommand.prefix + "§cChecking for Updates...");
				plugin.getWebManager().checkVersionWithPlayer(p);
			} else if (display.equalsIgnoreCase("§c✘ §7Close §c✘")) {

				p.closeInventory();

			} else if (display.equalsIgnoreCase("§a◇ §7Documentation Site §a◇")) {

				p.sendMessage(AdminCommand.prefix
						+ "§7You can visit our Documentation Site here: §b§lhttps://dto.mc-plugins.org");

			} else if (display.equalsIgnoreCase("§5☆ §7Our Discord Server §5☆")) {

				p.sendMessage(AdminCommand.prefix
						+ "§7You can join our Discord with this Invite: §a§lhttps://dcto.mc-plugins.org");

			}

			e.setCancelled(true);
		}
	}
}