package de.warsteiner.jobs.api.plugins;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.JobAction;
import dev.lone.itemsadder.api.CustomStack;

public class ItemsAdderManager {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public ItemStack checkIfItemStackExist(String ID) {
		CustomStack stack = CustomStack.getInstance(ID);
		if (stack != null) {
			ItemStack itemStack = stack.getItemStack();

			return itemStack;
		}
		return null;

	}
	
	public void executeKillWork(Player player, Entity e, String id) {  
 
		UUID UUID = player.getUniqueId();
		 
		if (plugin.getJobWorkManager().getJobOnWork("" + UUID, JobAction.IA_KILL, "" + id) != null) {

			Job job = plugin.getJobWorkManager().getJobOnWork("" + UUID, JobAction.IA_KILL, "" + id);

			plugin.getJobWorkManager().finalWork("" + id, UUID, JobAction.IA_KILL, "ia-kill-action", 1,  null, e, true, false, true,
					job);

			return;
		}
	}
	
	public void executeBlockBreakWork(Player player, Block block, String id) {  

		if (block.hasMetadata("placed-by-player")) {
			return;
		}
		 
		UUID UUID = player.getUniqueId();
		 
		if (plugin.getJobWorkManager().getJobOnWork("" + UUID, JobAction.IA_BREAK, "" + id) != null) {

			Job job = plugin.getJobWorkManager().getJobOnWork("" + UUID, JobAction.IA_BREAK, "" + id);

			plugin.getJobWorkManager().finalWork("" + id, UUID, JobAction.IA_BREAK, "ia-break-action", 1,  block, null, true, true, false,
					job);

			return;
		}
	}

}
