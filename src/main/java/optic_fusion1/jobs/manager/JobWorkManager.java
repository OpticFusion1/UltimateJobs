package optic_fusion1.jobs.manager;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.api.JobAPI;
import optic_fusion1.jobs.event.PlayerFinishedWorkEvent;
import optic_fusion1.jobs.job.Job;
import optic_fusion1.jobs.job.JobType;
import optic_fusion1.jobs.job.JobsPlayer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JobWorkManager {

    private UltimateJobs plugin;
    private JobAPI api;

    public JobWorkManager(UltimateJobs plugin, JobAPI ap) {
        this.api = ap;
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public void executeHoneyAction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.isCancelled()) {
            if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {
                event.setCancelled(true);
            }
            return;
        }
        Material item = player.getItemInHand().getType();
        if (item == null) {
            return;
        }
        if (item != Material.GLASS_BOTTLE) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                UUID UUID = player.getUniqueId();
                String type = "" + event.getClickedBlock().getType();
                if (getJobOnWork("" + UUID, JobType.HONEY, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.HONEY, "" + type);
                    finalWork(type, player.getUniqueId(), JobType.HONEY, "honey-action", 1, event.getClickedBlock(),
                            null, false, true, false, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void executeEatAction(FoodLevelChangeEvent event) {
        if (event.isCancelled()) {
            if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {
                event.setCancelled(true);
            }
            return;
        }
        if (event.getItem() == null) {
            return;
        }
        Material item = event.getItem().getType();
        Player player = ((Player) event.getEntity());
        UUID UUID = player.getUniqueId();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getJobOnWork("" + UUID, JobType.EAT, "" + item) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.EAT, "" + item);
                    finalWork("" + item, ((Player) event.getEntity()).getUniqueId(), JobType.EAT, "eat-action", 1,
                            null, null, true, false, false, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void executeSaplingGrowAction(String type, UUID UUID, Block block) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getJobOnWork("" + UUID, JobType.GROWSAPLINGS, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.GROWSAPLINGS, "" + type);
                    finalWork(type, UUID, JobType.GROWSAPLINGS, "grow-saplings-action", 1, block, null, false, true,
                            false, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void executeBerrysEvent(Player player, String id, Block block) {
        new BukkitRunnable() {
            @Override
            public void run() {
                UUID UUID = player.getUniqueId();
                if (getJobOnWork("" + UUID, JobType.COLLECTBERRYS, "" + id.toUpperCase()) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.COLLECTBERRYS, "" + id.toUpperCase());
                    finalWork(id.toUpperCase(), player.getUniqueId(), JobType.COLLECTBERRYS, "collectberrys-action",
                            1, block, null, false, true, false, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void executeKillByBowEvent(Player player, String id, Entity et) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String type = id.toUpperCase();
                UUID UUID = player.getUniqueId();
                if (getJobOnWork("" + UUID, JobType.KILL_BY_BOW, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.KILL_BY_BOW, "" + type);
                    finalWork(type, player.getUniqueId(), JobType.KILL_BY_BOW, "killbybow-action", 1, null, et, true,
                            false, true, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void executeAchWork(PlayerAdvancementDoneEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String type = event.getAdvancement().getKey().getKey().replaceAll("story/", "   ").replaceAll(" ", "")
                        .toUpperCase();
                UUID UUID = event.getPlayer().getUniqueId();
                if (getJobOnWork("" + UUID, JobType.ADVANCEMENT, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.ADVANCEMENT, "" + type);
                    finalWork(type, event.getPlayer().getUniqueId(), JobType.ADVANCEMENT, "advancement-action", 1,
                            null, null, true, false, false, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void executeShearWork(PlayerShearEntityEvent event) {
        if (event.getEntity() instanceof Sheep) {
            Sheep sheep = (Sheep) event.getEntity();
            DyeColor color = sheep.getColor();
            if (event.isCancelled()) {
                if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {
                    event.setCancelled(true);
                }
                return;
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    UUID UUID = event.getPlayer().getUniqueId();
                    if (getJobOnWork("" + UUID, JobType.SHEAR, "" + color) != null) {
                        Job job = getJobOnWork("" + UUID, JobType.SHEAR, "" + color);
                        finalWork("" + color, event.getPlayer().getUniqueId(), JobType.SHEAR, "shear-action", 1, null,
                                event.getEntity(), true, false, true, job);
                        return;
                    }
                    cancel();
                }
            }.runTaskAsynchronously(plugin);
        }
    }

    public void executeCraftWork(CraftItemEvent event) {
        final Material type = event.getInventory().getResult().getType();
        final int amount = event.getInventory().getResult().getAmount();
        if (event.isCancelled()) {
            if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {
                event.setCancelled(true);
            }
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                UUID UUID = event.getWhoClicked().getUniqueId();
                if (getJobOnWork("" + UUID, JobType.CRAFT, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.CRAFT, "" + type);
                    finalWork("" + type, ((Player) event.getWhoClicked()).getUniqueId(), JobType.CRAFT,
                            "craft-action", amount, null, null, true, false, false, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void executeStripLogWork(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {
                event.setCancelled(true);
            }
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        Action action = event.getAction();
        Material item = event.getMaterial();
        Block block = event.getClickedBlock();
        Material mat = block.getType();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (action == Action.RIGHT_CLICK_BLOCK && item.toString().contains("_AXE")) {
                    if (block.toString().contains("LOG")) {
                        UUID UUID = event.getPlayer().getUniqueId();
                        if (getJobOnWork("" + UUID, JobType.STRIPLOG, "" + mat) != null) {
                            Job job = getJobOnWork("" + UUID, JobType.STRIPLOG, "" + mat);
                            finalWork(mat.toString(), event.getPlayer().getUniqueId(), JobType.STRIPLOG,
                                    "strip-action", 1, event.getClickedBlock(), null, true, true, false, job);
                            return;
                        }

                    }
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void executeCarveWork(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {
                event.setCancelled(true);
            }
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        Action action = event.getAction();
        Material item = event.getMaterial();
        Block block = event.getClickedBlock();
        Material mat = block.getType();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (action == Action.RIGHT_CLICK_BLOCK && item.equals(Material.SHEARS)) {
                    if (mat.equals(Material.PUMPKIN)) {
                        UUID UUID = event.getPlayer().getUniqueId();
                        if (getJobOnWork("" + UUID, JobType.CARVE, "" + mat) != null) {
                            Job job = getJobOnWork("" + UUID, JobType.CARVE, "" + mat);
                            finalWork(mat.toString(), event.getPlayer().getUniqueId(), JobType.CARVE,
                                    "carve-action", 1, event.getClickedBlock(), null, true, true, false, job);
                            return;
                        }

                    }
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void executePickUpWork(Player player, String item) {
        new BukkitRunnable() {
            @Override
            public void run() {
                UUID UUID = player.getUniqueId();
                if (getJobOnWork("" + UUID, JobType.PICKUP, item) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.PICKUP, "" + item);
                    finalWork(item, UUID, JobType.PICKUP, "pickup-action", 1, null, null, true, false,
                            false, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);

    }

    public void executeBlockBreakWork(BlockBreakEvent event) {
        final Block block = event.getBlock();
        final Material type = event.getBlock().getType();
        if (plugin.getBlockAPI().getPlacedBy(block) != null) {
            return;
        }
        if (event.isCancelled()) {
            if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {
                event.setCancelled(true);
            }
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                UUID UUID = event.getPlayer().getUniqueId();
                if (getJobOnWork("" + UUID, JobType.BREAK, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.BREAK, "" + type);
                    finalWork("" + type, UUID, JobType.BREAK, "break-action", 1, event.getBlock(), null, true, true,
                            false, job);
                    plugin.getBlockAPI().removeBlock(block);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);

    }

    public void executeTreasureEvent(String type, Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                UUID UUID = player.getUniqueId();
                if (getJobOnWork("" + UUID, JobType.FIND_TREASURE, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.FIND_TREASURE, "" + type);
                    finalWork("" + type, UUID, JobType.FIND_TREASURE, "find-treasure-action", 1, null, null, true,
                            false, false, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);

    }

    public void executeBlockPlaceWork(BlockPlaceEvent event) {
        final Material type = event.getBlock().getType();
        if (event.isCancelled()) {
            if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {
                event.setCancelled(true);
            }
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                UUID UUID = event.getPlayer().getUniqueId();
                if (getJobOnWork("" + UUID, JobType.PLACE, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.PLACE, "" + type);
                    finalWork("" + type, UUID, JobType.PLACE, "place-action", 1, event.getBlock(), null, true, true,
                            false, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void executeFishWork(Entity cought, Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String id = cought.getName().toUpperCase().replaceAll(" ", "_");
                UUID UUID = player.getUniqueId();
                if (getJobOnWork("" + UUID, JobType.FISH, "" + id) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.FISH, "" + id);
                    finalWork("" + id, UUID, JobType.FISH, "fish-action", 1, null, null, true, false, false, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);

    }

    public void executeBreedWork(EntityBreedEvent event) {
        final EntityType type = event.getEntity().getType();
        if (event.isCancelled()) {
            if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {
                event.setCancelled(true);
            }
            return;
        }
        if (event.getEntity() == null) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                UUID UUID = ((Player) event.getBreeder()).getUniqueId();
                if (getJobOnWork("" + UUID, JobType.BREED, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.BREED, "" + type);
                    finalWork("" + type, UUID, JobType.BREED, "breed-action", 1, null, event.getEntity(), true, false,
                            true, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);

    }

    public void executeTameWork(EntityTameEvent event) {
        final EntityType type = event.getEntity().getType();
        if (event.isCancelled()) {
            if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {
                event.setCancelled(true);
            }
            return;
        }
        if (event.getEntity() == null) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                UUID UUID = ((Player) event.getOwner()).getUniqueId();
                if (getJobOnWork("" + UUID, JobType.TAME, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.TAME, "" + type);
                    finalWork("" + type, UUID, JobType.TAME, "tame-action", 1, null, event.getEntity(), true, false,
                            true, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);

    }

    public void executeKillWork(Entity ent, Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                UUID UUID = player.getUniqueId();
                String type = "" + ent.getType();
                if (getJobOnWork("" + UUID, JobType.KILL_MOB, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.KILL_MOB, "" + type);
                    finalWork(type, player.getUniqueId(), JobType.KILL_MOB, "kill-action", 1, null, ent,
                            true, false, true, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);

    }

    public void executeMilkWork(PlayerInteractAtEntityEvent event) {
        String type = "" + event.getRightClicked().getType();
        if (event.getPlayer() == null) {
            return;
        }
        if (event.getPlayer().getItemInHand() == null) {
            return;
        }
        if (event.getPlayer().getItemInHand().getType() != Material.BUCKET) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                UUID UUID = event.getPlayer().getUniqueId();
                if (getJobOnWork("" + UUID, JobType.MILK, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.MILK, "" + type);
                    finalWork(type, UUID, JobType.MILK, "milk-action", 1, null, event.getRightClicked(), true, false,
                            true, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);

    }

    private List<Material> breakingMaterials = List.of(Material.SUGAR_CANE, Material.CACTUS, Material.BAMBOO);

    public void executeFarmWork(BlockBreakEvent event) {
        final Block block = event.getBlock();
        final Material type = event.getBlock().getType();
        List<Block> blocks = new ArrayList<>();
        List<Block> blocksingle = new ArrayList<>();
        List<Block> blockstocheck = new ArrayList<>();
        if (!plugin.getPluginManager().isFullyGrown(block)) {
            return;
        }
        if (event.isCancelled()) {
            if (plugin.getLocalFileManager().getConfig().getBoolean("CancelEvents")) {
                event.setCancelled(true);
            }
            return;
        }
        blocksingle.add(block);
        if (breakingMaterials.contains(type)) {
            for (int i = 0; i <= 16; i++) {
                Block bl = block.getLocation().add(0, i, 0).getBlock();
                Material d = bl.getType();
                if (breakingMaterials.contains(d)) {
                    if (plugin.getBlockAPI().getPlacedBy(block) == null) {
                        blocks.add(bl);
                    }
                    blockstocheck.add(bl);
                }
            }
        } else {
            blocks.add(block);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                UUID UUID = event.getPlayer().getUniqueId();
                if (getJobOnWork("" + UUID, JobType.FARM_BREAK, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.FARM_BREAK, "" + type);
                    if (job.getOptionValue("CheckIfThereAreOtherCanesAbove")) {
                        if (blocks.size() != 0) {
                            finalWork("" + type, UUID, JobType.FARM_BREAK, "farm-break-action", blocks.size(),
                                    event.getBlock(), null, true, true, false, job);
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                blockstocheck.forEach(b -> {
                                    plugin.getBlockAPI().removeBlock(b);
                                });
                            });
                        }
                    } else {
                        if (blocksingle.size() != 0) {
                            finalWork("" + type, UUID, JobType.FARM_BREAK, "farm-break-action", blocksingle.size(),
                                    event.getBlock(), null, true, true, false, job);
                            plugin.getBlockAPI().removeBlock(blocksingle.get(0));
                        }
                    }
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);

    }

    public void executeFarmGrowWork(Block block, UUID UUID) {
        new BukkitRunnable() {
            @Override
            public void run() {
                final Material type = block.getType();
                if (getJobOnWork("" + UUID, JobType.FARM_GROW, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.FARM_GROW, "" + type);
                    if (job.getOptionValue("GetMoneyOnlyWhenFullyGrown")) {
                        if (!plugin.getPluginManager().isFullyGrown(block)) {
                            return;
                        }
                    }
                    finalWork("" + type, UUID, JobType.FARM_GROW, "farm-grow-action", 1, block, null, true, true,
                            false, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);

    }

    public void executeSmelt(String type, UUID UUID, int amount) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getJobOnWork("" + UUID, JobType.SMELT, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.SMELT, "" + type);
                    finalWork("" + type, UUID, JobType.SMELT, "smelt-action", amount, null, null, true, false, false,
                            job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);

    }

    public void executeMoveAction(UUID UUID, PlayerMoveEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getJobOnWork("" + UUID, JobType.EXPLORE_CHUNK, "CHUNK") != null) {
                    Job job = getJobOnWork("" + UUID, JobType.EXPLORE_CHUNK, "CHUNK");
                    List<String> playerChunks = null;
                    if (plugin.getPlayerChunkAPI().players.containsKey("" + UUID)) {
                        if (!plugin.getPlayerChunkAPI().players.get("" + UUID).containsKey(job)) {
                            playerChunks = new ArrayList<String>();
                        } else {
                            HashMap<Job, List<String>> l = plugin.getPlayerChunkAPI().players.get("" + UUID);
                            playerChunks = l.get(job);
                        }
                    } else {
                        playerChunks = new ArrayList<String>();
                    }
                    Chunk from = e.getFrom().getChunk();
                    Chunk to = e.getTo().getChunk();
                    if (!from.equals(to)) {
                        int x = to.getX(), z = to.getZ();
                        for (String s : playerChunks) {
                            if (s.equals(x + ";" + z)) {
                                return;
                            }
                        }
                        plugin.getPlayerChunkAPI().addChunk("" + UUID, job, x + ";" + z);
                        finalWork("CHUNK", UUID, JobType.EXPLORE_CHUNK, "explore-action", 1, null, null, true, false,
                                false, job);
                    }
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);

    }

    public void executeEnchant(String type, UUID UUID) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getJobOnWork("" + UUID, JobType.ENCHANT, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.ENCHANT, "" + type);
                    finalWork("" + type, UUID, JobType.ENCHANT, "enchant-action", 1, null, null, true, false, false,
                            job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);

    }

    public void executePotionDrink(String type, UUID UUID) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getJobOnWork("" + UUID, JobType.DRINK_POTION, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.DRINK_POTION, "" + type);
                    finalWork("" + type, UUID, JobType.DRINK_POTION, "drink-action", 1, null, null, true, false,
                            false, job);

                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);

    }

    public void executeVilBuyTrade(String type, UUID UUID, int amount) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getJobOnWork("" + UUID, JobType.VILLAGER_TRADE, "" + type) != null) {
                    Job job = getJobOnWork("" + UUID, JobType.VILLAGER_TRADE, "" + type);
                    finalWork("" + type, UUID, JobType.VILLAGER_TRADE, "villager-trade-action", amount, null, null,
                            true, false, false, job);
                    return;
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);

    }

    public Job getJobOnWork(String id, JobType ac, String real) {
        if (api.getJobsWithAction(id, ac) == null) {
            return null;
        }
        ArrayList<String> jobs = api.getJobsWithAction(id, ac);
        for (String job : jobs) {
            Job jub = plugin.getJobCache().get(job);
            if (jub.getConfigIDOfRealID(ac, real, jub) == null) {
                continue;
            }
            if (jub.getListOfRealIDS(ac).contains(real.toUpperCase())) {
                return jub;
            }
        }
        return null;
    }

    public void finalWork(String real, UUID ID, JobType ac, String flag, int amount, Block block, Entity ent,
            boolean checkplayer, boolean checkblock, boolean checkentity, Job job) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String PUID = "" + ID;
                FileConfiguration cfg = plugin.getLocalFileManager().getConfig();
                if (plugin.getPlayerAPI().getCurrentJobs(PUID).contains(job.getConfigID().toUpperCase())) {
                    String iD = job.getConfigIDOfRealID(ac, real, job);
                    if (checkplayer) {
                        if (!api.canWorkThereByPlayer(PUID, job, flag)) {
                            return;
                        }
                    }
                    if (checkblock) {
                        if (block != null) {
                            if (!api.canWorkThereByBlock(block.getLocation(), job, flag)) {
                                return;
                            }
                        }
                    }
                    if (checkentity) {
                        if (ent != null) {
                            if (ent.getLocation() != null) {
                                if (!api.canWorkThereByEntity(ent.getLocation(), job, flag)) {
                                    return;
                                }
                            }
                        }
                    }
                    if (api.canReward(job, iD, ac)) {
                        String usedid = job.getNotRealIDByRealOne(real.toUpperCase(), ac);
                        if (usedid != null) {
                            boolean can = api.checkforDailyMaxEarnings(PUID, job);
                            String date = plugin.getDate();
                            double reward = job.getRewardOf(iD, ac);
                            double exp_old = plugin.getPlayerAPI().getExpOf(PUID, job);
                            double EPC1 = plugin.getPlayerAPI().getRealCalculatedAmountOfExp(PUID, job,
                                    job.getExpOf(iD, ac) * amount);
                            Integer broken = plugin.getPlayerAPI().getBrokenTimes(PUID, job) + amount;
                            double points = job.getPointsOf(iD, ac) * amount;
                            double old_points = plugin.getPlayerAPI().getPoints(PUID);
                            double fixed = reward * amount;
                            double od1 = plugin.getPlayerAPI().getRealCalculatedAmountOfMoney(PUID, job, fixed);
                            String date_average = plugin.getPluginManager().getDateTodayFromCalWithOutSeconds();
                            plugin.getPlayerAPI().updateAverageEarnings(PUID, job, date_average, od1);
                            plugin.getPlayerAPI().updateAverageWork(PUID, job, date_average, amount);
                            plugin.getPlayerAPI().updateAverageExp(PUID, job, date_average, EPC1);
                            double earnedcalc = plugin.getPlayerAPI().getEarnedAt(PUID, job, date) + od1;
                            if (job.hasVaultReward(iD, ac)) {
                                if (can) {
                                    if (plugin.getLocalFileManager().getConfig().getString("PayMentMode").toUpperCase()
                                            .equalsIgnoreCase("INSTANT")) {
                                        if (Bukkit.getPlayer(ID).isOnline()) {
                                            plugin.getEco().depositPlayer(Bukkit.getPlayer(ID), od1);
                                        } else {
                                            plugin.getEco().depositPlayer(Bukkit.getOfflinePlayer(ID),
                                                    od1);
                                        }
                                    } else {
                                        if (Bukkit.getPlayer(ID).isOnline()) {
                                            double old = plugin.getPlayerAPI().getSalary("" + ID);
                                            plugin.getPlayerAPI().updateSalary("" + ID, old + od1);
                                        } else {
                                            if (plugin.getLocalFileManager().getConfig()
                                                    .getInt("MaxDefaultJobs") != 0) {
                                                return;
                                            }
                                            double old = plugin.getPlayerAPI().getSalary("" + ID);
                                            plugin.getPlayerAPI().updateSalary("" + ID, old + od1);

                                        }
                                    }
                                }

                            }
                            plugin.getPlayerAPI().updateBrokenTimes(PUID, job, broken);
                            int ol = plugin.getPlayerAPI().getBrokenTimesOfID(PUID, job, usedid, "" + ac);
                            double earned_old = plugin.getPlayerAPI().getEarnedFrom(PUID, job, usedid, "" + ac);
                            plugin.getPlayerAPI().updateBrokenTimesOf(PUID, job, usedid, ol + amount, "" + ac);
                            if (can == false) {
                                if (cfg.getBoolean("Jobs.MaxEarnings.IfReached_Can_Earn_Exp")) {
                                    plugin.getPlayerAPI().updateExp(PUID, job, exp_old + EPC1);
                                }
                                if (cfg.getBoolean("Jobs.MaxEarnings.IfReached_Can_Earn_Points")) {
                                    plugin.getPlayerAPI().updatePoints(PUID, points + old_points);
                                }
                                if (cfg.getBoolean("Jobs.MaxEarnings.IfReached_Can_Stats")) {
                                    plugin.getPlayerAPI().updateEarningsOfToday(PUID, job, earnedcalc);
                                    plugin.getPlayerAPI().updateBrokenMoneyOf(PUID, job, usedid, earned_old + od1,
                                            "" + ac);
                                }
                            } else {
                                plugin.getPlayerAPI().updateExp(PUID, job, exp_old + EPC1);
                                plugin.getPlayerAPI().updatePoints(PUID, points + old_points);
                                plugin.getPlayerAPI().updateEarningsOfToday(PUID, job, earnedcalc);
                                double done = earned_old + od1;
                                plugin.getPlayerAPI().updateBrokenMoneyOf(PUID, job, usedid, done, "" + ac);

                            }

                            if (Bukkit.getPlayer(ID).isOnline()) {
                                Player player = Bukkit.getPlayer(ID);
                                JobsPlayer d = plugin.getPlayerAPI().getRealJobPlayer(PUID);
                                api.playSound("FINISHED_WORK", player);
                                new BukkitRunnable() {
                                    public void run() {
                                        new PlayerFinishedWorkEvent(player, d, job, iD, ac);
                                        cancel();
                                    }
                                }.runTaskLater(plugin, 1);
                                if (cfg.getBoolean("Enable_Levels")) {
                                    plugin.getLevelAPI().check(player, job, d, iD);
                                }
                                plugin.getAPI().sendReward(d, player, job, EPC1, od1, iD, can, ac,
                                        amount);
                            }
                            return;
                        }
                    }
                }
                cancel();
            }
        }.runTaskAsynchronously(plugin);
    }

}
