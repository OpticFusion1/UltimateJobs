package de.warsteiner.jobs.manager;

import java.util.ArrayList; 
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player; 
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.ItemAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.datax.managers.GUIManager;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.JobsPlayer; 

public class GuiManager {

	private UltimateJobs plugin;
	private JobAPI api = UltimateJobs.getPlugin().getAPI(); 
	private PluginAPI up = SimpleAPI.getInstance().getAPI();
	private ItemAPI im = SimpleAPI.getInstance().getItemAPI();
	private GUIManager gm = SimpleAPI.getInstance().getGUIManager();
	private YamlConfiguration cfg = UltimateJobs.getPlugin().getMainConfig().getConfig();

	public GuiManager(UltimateJobs plugin) {
		this.plugin = plugin;
	}
	
	public void EditJobMenu(Player player, Job job) {  
		String name = "§6Job Settings";
		gm.openInventory(player, 5, name);
		
		InventoryView inv = player.getOpenInventory();
		
		plugin.getExecutor().execute(() -> { 
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "GRAY_STAINED_GLASS_PANE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8-/-");
				  
				it.setItemMeta(meta);
				  
				inv.setItem(0, it); 
				inv.setItem(1, it); 
				inv.setItem(2, it); 
				inv.setItem(3, it); 
				inv.setItem(4, it); 
				inv.setItem(5, it); 
				inv.setItem(6, it); 
				inv.setItem(7, it); 
				inv.setItem(8, it); 
				inv.setItem(36, it); 
				inv.setItem(37, it); 
				inv.setItem(38, it); 
				inv.setItem(39, it);  
				inv.setItem(41, it);
				inv.setItem(42, it); 
				inv.setItem(43, it); 
				inv.setItem(44, it); 
	 
			}
			
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();

			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "BEACON");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7"+job.getID()+" §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7You edit the Job "+job.getDisplay());
				
				meta.setLore(lore);
				  
				it.setItemMeta(meta);
				   
				inv.setItem(4, it); 
	 
			}
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "RED_DYE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §cGo Back §8>");
				  
				it.setItemMeta(meta); 
				
				inv.setItem(40, it); 
			}
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "PAPER");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Change Job Action §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7Current§8: §b"+job.getAction());
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), job.getIcon());
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Change Job Icon §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7Current§8: §b"+job.getIcon());
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "CHEST");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Job Description §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				
				lore.add("§8> §7Current§8: §b");
				lore.add("§7");
				
				for(String line : job.getLore()) {
					lore.add(up.toHex(line).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§"));
				}
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "NAME_TAG");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Change Job Display §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7Current§8: §b"+job.getDisplay());
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), job.getBarColor()+"_DYE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Change BossBar Color §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7Current§8: §b"+job.getBarColor());
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "LEVER");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Change Job Slot §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7Current§8: §b"+job.getSlot());
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "EMERALD");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Change Job Price §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7Current§8: §b"+job.getPrice());
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "NETHER_STAR");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Bypass Permission §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				boolean has = job.hasByoassPermission();
				
				String wh = null;
				
				if(has) {
					wh = "§aYes";
				} else {
					wh = "§cNone";
				}
				
				lore.add("§8> §7Has§8: §b"+wh);
				if(has) {
					lore.add("§8> §7Current§8: §b"+job.getByPassPermission());
				}
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "ENDER_PEARL");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Job Permission §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				boolean has = job.hasPermission();
				
				String wh = null;
				
				if(has) {
					wh = "§aYes";
				} else {
					wh = "§cNone";
				}
				
				lore.add("§8> §7Has§8: §b"+wh);
				if(has) {
					lore.add("§8> §7Current§8: §b"+job.getPermission());
				}
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			} 
			if(job.hasPermission()) {
				if(inv.getTitle().equalsIgnoreCase(name)) {
					ItemStack it = im.createItemStack(player.getName(), "BOOK");
					ItemMeta meta = it.getItemMeta();
					
					meta.setDisplayName("§8< §7Permissions Lore §8>");
					
					ArrayList<String> lore = new ArrayList<String>(); 
					lore.add("§8> §7Current§8:");
					lore.add("§8");
					for(String line : job.getPermissionsLore()) {
						lore.add(up.toHex(line).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§"));
					}
					
					meta.setLore(lore);
					
					it.setItemMeta(meta); 
					
					items.add(it);
				}
				if(inv.getTitle().equalsIgnoreCase(name)) {
					ItemStack it = im.createItemStack(player.getName(), "TORCH");
					ItemMeta meta = it.getItemMeta();
					
					meta.setDisplayName("§8< §7Permissions Message §8>");
					
					ArrayList<String> lore = new ArrayList<String>(); 
					lore.add("§8> §7Current§8:"); 
					lore.add("§7");
					lore.add(up.toHex(job.getPermMessage()).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§"));
				  
					meta.setLore(lore);
					
					it.setItemMeta(meta); 
					
					items.add(it);
				}
			}
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "GRASS_BLOCK");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Worlds §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				
				lore.add("§8> §7List§8: §b");
				lore.add("§7");
				
				for(String line : job.getWorlds()) {
					lore.add(up.toHex(line).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§"));
				}
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			
			int size = items.size();
			
			for (int i = 0; i < size; i++) {

				if (size >= i) {
					
					ItemStack item = items.get(i);
					
					int slot = 9 + i;
					
					inv.setItem(slot, item);
					
				}
			}
			
		});
		 
	}
	
	public void EditorChooseJob(Player player, boolean sound) {  
		String name = "§6Jobs Editor";
		gm.openInventory(player, 5, name);
		
		if(sound) {
			player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
		}
		
		InventoryView inv = player.getOpenInventory();
		
		plugin.getExecutor().execute(() -> { 
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "GRAY_STAINED_GLASS_PANE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8-/-");
				  
				it.setItemMeta(meta);
				  
				inv.setItem(0, it); 
				inv.setItem(1, it); 
				inv.setItem(2, it); 
				inv.setItem(3, it); 
				inv.setItem(4, it); 
				inv.setItem(5, it); 
				inv.setItem(6, it); 
				inv.setItem(7, it); 
				inv.setItem(8, it); 
				inv.setItem(36, it); 
				inv.setItem(37, it); 
				inv.setItem(38, it); 
				inv.setItem(39, it);  
				inv.setItem(41, it);
				inv.setItem(42, it); 
				inv.setItem(43, it); 
				inv.setItem(44, it); 
	 
			}
	
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "RED_DYE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §cClose §8>");
				  
				it.setItemMeta(meta); 
				
				inv.setItem(40, it); 
			}
			
			ArrayList<String> jobs = plugin.getLoaded();
			int size = jobs.size();
			
			if(size == 0) {
				
				ItemStack it = im.createItemStack(player.getName(), "BARRIER");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§cNo Jobs Found");
				  
				it.setItemMeta(meta);
				  
					inv.setItem(22, it); 
					
					return;
				
			} else {
				for (int i = 0; i < size; i++) {

					if (size >= i) {
						
						Job job = plugin.getJobCache().get(jobs.get(i));
						
						ItemStack it = im.createItemStack(player.getName(), job.getIcon());
						ItemMeta meta = it.getItemMeta();
						
						meta.setDisplayName(job.getDisplay());
						
						ArrayList<String> lore = new ArrayList<String>();
						  
						lore.add("§8< Click to edit this Job §8>");
						
						meta.setLore(lore);
						
						meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
						
						it.setItemMeta(meta);
						
						int slot = i + 9;
						 
						if(inv.getItem(slot) == null) {
							inv.setItem(slot, it); 
						}
						
					}
				}
			}
			
		});
	}

	public void createAreYouSureGUI(Player player, Job job) {
		String name = cfg.getString("AreYouSureGUI_Name").replaceAll("<job>", job.getDisplay());
		int size = cfg.getInt("AreYouSureGUI_Size");

		gm.openInventory(player, size, name);
 
		api.playSound("OPEN_SURE_GUI", player);
		InventoryView inv_view = player.getOpenInventory();

		setPlaceHolders(player, inv_view, cfg.getStringList("AreYouSureGUI_Place"), name);
		setCustomitems(player, player.getName(), inv_view, "AreYouSureGUI_Custom.",
				cfg.getStringList("AreYouSureGUI_Custom.List"), name, cfg);
		setAreYouSureItems(player, job, name, inv_view);
	}
	
	public void setAreYouSureItems(Player player, Job job, String tit, InventoryView inv) { 
		plugin.getExecutor().execute(() -> { 
			 
			if(player != null) { 
			 
				ItemStack item = im.createItemStack(player, cfg.getString("AreYouSureItems.Button_YES.Icon"));
				
				String dis =up.toHex( cfg.getString("AreYouSureItems.Button_YES.Display")).replaceAll("<job>", job.getDisplay()).replaceAll("&", "§");
				int slot = cfg.getInt("AreYouSureItems.Button_YES.Slot");
				List<String> lore = cfg.getStringList("AreYouSureItems.Button_YES.Lore");
				ArrayList<String> l = new ArrayList<String>();
				
				ItemMeta meta = item.getItemMeta();
				
				for(String line : lore) {
					l.add(up.toHex(line).replaceAll("<job>", job.getDisplay()).replaceAll("&", "§"));
				}
				
				meta.setDisplayName(dis);
				
				meta.setLore(l);
				
				item.setItemMeta(meta);
				
				inv.setItem(slot, item);
				
			}
			
			if(player != null) {
				
				ItemStack item = im.createItemStack(player, cfg.getString("AreYouSureItems.Button_NO.Icon"));
				
				String dis = up.toHex(cfg.getString("AreYouSureItems.Button_NO.Display")).replaceAll("<job>", job.getDisplay()).replaceAll("&", "§");
				int slot = cfg.getInt("AreYouSureItems.Button_NO.Slot");
				List<String> lore = cfg.getStringList("AreYouSureItems.Button_NO.Lore");
				ArrayList<String> l = new ArrayList<String>();
				
				ItemMeta meta = item.getItemMeta();
				
				for(String line : lore) {
					l.add(up.toHex(line).replaceAll("<job>", job.getDisplay()).replaceAll("&", "§"));
				}
				
				meta.setDisplayName(dis);
				
				meta.setLore(l);
				
				item.setItemMeta(meta);
				
				inv.setItem(slot, item);
				
			}
			
		});
	}

	public void createMainGUIOfJobs(Player player) {
		String name = cfg.getString("Main_Name");
		int size = cfg.getInt("Main_Size");

		gm.openInventory(player, size, name);
 
		api.playSound("OPEN_MAIN", player);
		InventoryView inv_view = player.getOpenInventory();

		setPlaceHolders(player, inv_view, cfg.getStringList("Main_Place"), name);
		UpdateMainInventory(player, name);
	}

	public void UpdateMainInventory(Player player, String name) {
		new BukkitRunnable() {
			public void run() {
				setCustomitems(player, player.getName(), player.getOpenInventory(), "Main_Custom.",
						cfg.getStringList("Main_Custom.List"), name, cfg);
				setMainInventoryJobItems(player.getOpenInventory(), player, name);
			}
		}.runTaskLater(plugin, 1);
	}

	public void createSettingsGUI(Player player, Job job) {

		String dis = job.getDisplay();
		String name = cfg.getString("Settings_Name").replaceAll("<job>", dis);
		int size = cfg.getInt("Settings_Size");

		gm.openInventory(player, size, name);
 
		api.playSound("OPEN_SETTINGS", player);
		InventoryView inv_view = player.getOpenInventory();

		setPlaceHolders(player, inv_view, cfg.getStringList("Settings_Place"), name);
		setCustomitems(player, player.getName(), inv_view, "Settings_Custom.",
				cfg.getStringList("Settings_Custom.List"), name, cfg);

	}

	public void setMainInventoryJobItems(InventoryView inv, Player player, String name) {

		plugin.getExecutor().execute(() -> {
			String title = player.getOpenInventory().getTitle(); 
			JobsPlayer jb = plugin.getPlayerManager().getOnlineJobPlayers().get(""+player.getUniqueId()); 
 
			String need = up.toHex(name).replaceAll("&", "§");
			if (title.equalsIgnoreCase(need)) {

				ArrayList<String> jobs = plugin.getLoaded();

				for (String li : jobs) {

					Job j = plugin.getJobCache().get(li);;
					
					String display = up.toHex(j.getDisplay().replaceAll("&", "§"));
					int slot = j.getSlot();
					List<String> lore = j.getLore();
					String mat = j.getIcon();
					double price = j.getPrice();
					String id = j.getID();

					inv.setItem(slot, null);

					ItemStack item = im.createItemStack(player, mat);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(display.replaceAll("&", "§"));

					List<String> see = null;

					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

					if (api.canBuyWithoutPermissions(player, j)) {
						List<String> d = api.canGetJobWithSubOptions(player, j);
						if(d == null) {
							if (jb.ownJob(id) == true || api.canByPass(player, j) == true) {
	
								if (jb.isInJob(id)) {
									meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
	
									see = cfg.getStringList("Jobs.Lore.In");
								} else {
									see = cfg.getStringList("Jobs.Lore.Bought");
								}
	
							} else {
								see = cfg.getStringList("Jobs.Lore.Price");
							}

						} else { 
							see = d;
							 
						}
						
					} else {
						see = j.getPermissionsLore();
					}

					List<String> filore = new ArrayList<String>();
					for (String l : lore) {
						filore.add(up.toHex(l).replaceAll("&", "§"));
					}
					if (jb.isInJob(id)) {

						int level =  jb.getLevelOf(id);
						double exp = jb.getExpOf(id);
						String bought = jb.getDateOfJob(id);
						String lvl = j.getLevelDisplay(level);
						Integer broken = jb.getBrokenOf(id);
						
						for (String l : j.getStatsMessage()) {

							filore.add(up.toHex(l).replaceAll("<stats_args_4>", lvl)
									.replaceAll("<stats_args_3>", "" + level)
									.replaceAll("<stats_args_2>", "" + broken)
									.replaceAll("<stats_args_6>",
											"" + api.Format(plugin.getLevelAPI().getJobNeedExp(j, jb)))
									.replaceAll("<stats_args_5>", "" + api.Format(exp))
									.replaceAll("<stats_args_1>",""+ bought).replaceAll("&", "§"));
						}
					}
					for (String l : see) {
						filore.add(up.toHex(l).replaceAll("<price>", "" + price).replaceAll("&", "§"));
					}
					meta.setLore(filore);

					item.setItemMeta(meta);

					inv.setItem(slot, item);

				}
			}
		});

	}

	public void setCustomitems(Player player, String pname, InventoryView inv, String prefix, List<String> list,
			String name, YamlConfiguration cf) {

		plugin.getExecutor().execute(() -> {
			
			JobsPlayer jb =  plugin.getPlayerManager().getOnlineJobPlayers().get(""+player.getUniqueId());
		 
			String title = player.getOpenInventory().getTitle();
			String need = up.toHex(name).replaceAll("&", "§");
			if (title.equalsIgnoreCase(need)) {
				for (String pl : list) {
					if (cf.contains(prefix + pl + ".Display")) {
						String display = cf.getString(prefix + pl + ".Display");
						String mat = cf.getString(prefix + pl + ".Material").toUpperCase();
						int slot = cf.getInt(prefix + pl + ".Slot");

						ItemStack item = im.createItemStack(player, mat);
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName(display.replaceAll("&", "§"));

						int max = jb.getMaxJobs() + 1;
						
						if (cf.contains(prefix + pl + ".Lore")) {
							List<String> lore = cf.getStringList(prefix + pl + ".Lore");
							List<String> filore = new ArrayList<String>();
							for (String l : lore) {
								filore.add(up.toHex(l)
										.replaceAll("<points>", ""+api.Format(jb.getPoints())).replaceAll("<max>", ""+max).replaceAll("&", "§"));
							}
							meta.setLore(filore);
						}
						item.setItemMeta(meta);

						inv.setItem(slot, item);
					} else {
						plugin.getLogger().warning("§c§lMissing Element in " + need + " §4§lCustom Item: §b§l" + pl);
					}
				}
			}
		});

	}

	public void setPlaceHolders(Player player, InventoryView inv_view, List<String> list, String name) {
		plugin.getExecutor().execute(() -> {
			String title = player.getOpenInventory().getTitle();
			String need = up.toHex(name).replaceAll("&", "§");
			if (title.equalsIgnoreCase(need)) {
				for (String pl : list) {
					String[] t = pl.split(":");

					String mat = t[0].toUpperCase();
					int slot = Integer.valueOf(t[1]).intValue();
					String display = t[2];

					ItemStack item = im.createItemStack(player, mat);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(display.replaceAll("&", "§"));
					item.setItemMeta(meta);

					inv_view.setItem(slot, item);
				}

			}

		});

	}

}
