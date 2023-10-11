package optic_fusion1.jobs.command.player;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.api.PlayerAPI;
import optic_fusion1.jobs.manager.GuiManager;
import optic_fusion1.jobs.util.PluginColor;
import optic_fusion1.jobs.gui.UpdateTypes;
import java.util.UUID;
import optic_fusion1.jobs.command.AbstractJobCommand;
import optic_fusion1.jobs.command.player.sub.SubCommand;
import optic_fusion1.jobs.job.JobsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class JobsCommand extends AbstractJobCommand {

    public JobsCommand(UltimateJobs plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c§lThis Command is only for players!");
            return true;
        }
        String playerUUID = player.getUniqueId().toString();
        PlayerAPI playerAPI = plugin.getPlayerAPI();
        if (!playerAPI.existInCacheByUUID(playerUUID)) {
            Bukkit.getConsoleSender().sendMessage(
                    PluginColor.ERROR.getPrefix() + "Seems like a player was not found... (" + playerUUID + ")");
            return true;
        }
        JobsPlayer jobsPlayer = playerAPI.getRealJobPlayer(playerUUID);
        GuiManager guiManager = plugin.getGUI();
        if (args.length != 0) {
            String arg = args[0].toLowerCase();
            if (find(arg, jobsPlayer.getUUID()) == null) {
                player.sendMessage(jobsPlayer.getLanguage().getMessage("command_notfound").replaceAll("<cmd>", arg));
                return true;
            }
            SubCommand cmd = find(arg, jobsPlayer.getUUID());
            if (cmd.isEnabled()) {
                cmd.perform(player, args, jobsPlayer);
                return true;
            }
            player.sendMessage(jobsPlayer.getLanguage().getMessage("command_notfound").replaceAll("<cmd>", arg));
            return true;
        }
        FileConfiguration config = plugin.getLocalFileManager().getConfig();
        guiManager.createMainGUIOfJobs(player, UpdateTypes.OPEN);
        // Updating max permission
        // TODO: Clean this up
        if (config.getBoolean("EnableMaxJobPermissions")) {
            if (config.getBoolean("UpdateOnGuiOpen")) {
                for (PermissionAttachmentInfo perms : player.getEffectivePermissions()) {
                    if (perms.getPermission().startsWith("ultimatejobs.max.")) {
                        int real = Integer
                                .valueOf(perms.getPermission().split("ultimatejobs.max.")[1]) - 1;
                        jobsPlayer.updateCacheMax(real);
                    }
                }
            }
        }
        return true;
    }

    public SubCommand find(String given, UUID UUID) {
        for (SubCommand subCommand : plugin.getSubCommandManager().getSubCommandList()) {
            if (given.equalsIgnoreCase(subCommand.getName(UUID).toLowerCase())) {
                return subCommand;
            }
        }
        return null;
    }

}
