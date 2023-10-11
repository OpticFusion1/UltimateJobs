package optic_fusion1.jobs.command.admin;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.util.JsonMessage;
import optic_fusion1.jobs.command.admin.sub.AdminSubCommand;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {

    public static String prefix = "§9UltimateJobs §8-> §7";
    public UltimateJobs plugin;

    public AdminCommand(UltimateJobs plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        int length = args.length;

        if (sender.hasPermission("ultimatejobs.admin.command")) {

            if (length == 0) {
                sendHelp(sender, 1);
                if (sender instanceof Player) {
                    Player player3 = (Player) sender;
                    player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
                }
            } else {
                String ar = args[0].toLowerCase();

                if (find(ar) == null) {
                    sender.sendMessage(prefix + "§7Error! §7Use §6/Jobsadmin help§7.");
                    return true;
                } else {

                    AdminSubCommand cmd = find(ar);

                    if (sender.hasPermission(cmd.getPermission())) {

                        cmd.perform(sender, args);

                    } else {
                        sender.sendMessage(prefix + "§cYou dont have Permissions!");
                        return true;
                    }
                }

            }

        } else {
            sender.sendMessage(prefix + "§cYou dont have Permissions!");
            return true;
        }
        return false;
    }

    public AdminSubCommand find(String given) {
        for (AdminSubCommand subCommand : plugin.getAdminSubCommandManager().getSubCommandList()) {
            if (given.equalsIgnoreCase(subCommand.getName().toLowerCase())) {
                return subCommand;
            }
        }
        return null;
    }

    public void sendHelp(CommandSender sender, int page) {

        if (page == 1 || page >= 1) {

            List<AdminSubCommand> commands = plugin.getAdminSubCommandManager().getSubCommandList();

            int pageLength = 8;

            int calc = pageLength * page + 1;
            int min = calc - 7;
            if (commands.size() >= min) {

                sender.sendMessage("§7");
                sender.sendMessage(" §8| §9UltimateJobs §8- §4Admin Help #" + page + " §8|");

                for (int i = (page - 1) * pageLength; i < (page * pageLength) && i < commands.size(); i++) {
                    AdminSubCommand which = commands.get(i);

                    if (which.showOnHelp()) {
                        String us = which.getUsage();

                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            new JsonMessage()
                                    .append("§8-> §6" + which.getUsage() + " §8| §7" + which.getDescription()).setHoverAsTooltip("§7" + which.getDescription())
                                    .setClickAsSuggestCmd(us.toLowerCase()).save().send(player);
                        } else {
                            sender.sendMessage("§8-> §6" + which.getUsage() + " §8| §7" + which.getDescription());
                        }
                    }

                }

                sender.sendMessage("§7");

                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    int c = page - 1;
                    int c2 = page + 1;

                    if (page == 1) {
                        new JsonMessage()
                                .append(ChatColor.GREEN + "§8-> §aNext Page").setHoverAsTooltip("Click here")
                                .setClickAsExecuteCmd("/jobsadmin help " + c2).save().send(player);
                    } else {

                        if (commands.size() >= calc) {
                            new JsonMessage().append(ChatColor.RED + "§8-> §cPevious Page §8|").setHoverAsTooltip("Click here")
                                    .setClickAsExecuteCmd("/jobsadmin help " + c).save()
                                    .append(ChatColor.GREEN + " §aNext Page").setHoverAsTooltip("Click here")
                                    .setClickAsExecuteCmd("/jobsadmin help " + c2).save().send(player);
                        } else {
                            new JsonMessage().append(ChatColor.RED + "§8-> §cPevious Page §8|").setHoverAsTooltip("Click here")
                                    .setClickAsExecuteCmd("/jobsadmin help " + c).save().send(player);
                        }

                    }

                    sender.sendMessage("§7");
                }
            } else {
                sender.sendMessage(prefix + "§cNo Help Page found.");
            }
        }
    }

}
