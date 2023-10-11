package optic_fusion1.jobs.command.admin.sub;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.util.JsonMessage;
import optic_fusion1.jobs.command.admin.AdminCommand;
import optic_fusion1.jobs.command.admin.sub.AdminSubCommand;
import java.util.List;
import static optic_fusion1.jobs.command.admin.AdminCommand.prefix;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpSub extends AdminSubCommand {

    public HelpSub(UltimateJobs plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "See the Plugin's Commands";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sendHelp(sender, 1);
            if (sender instanceof Player) {
                Player player3 = (Player) sender;
                player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
            }
        } else if (args.length == 2) {

            if (!plugin.getAPI().isInt(args[1])) {
                sender.sendMessage(AdminCommand.prefix + "Error! The value must be a Integer");
                return;
            }

            if (Integer.valueOf(args[1]) == 0) {
                sender.sendMessage(AdminCommand.prefix + "Error! Page cannot be 0.");
                return;
            }

            sendHelp(sender, Integer.valueOf(args[1]));
            if (sender instanceof Player) {
                Player player3 = (Player) sender;
                player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
            }

        } else {
            if (sender instanceof Player) {
                Player player3 = (Player) sender;
                player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
            }
            sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6" + getUsage());
        }
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

    @Override
    public int getTabLength() {
        return 1;
    }

    @Override
    public String formatTab() {
        return "command help";
    }

    @Override
    public String getUsage() {
        return "/JobsAdmin help";
    }

    @Override
    public String getPermission() {
        return "ultimatejobs.admin.help";
    }

    @Override
    public boolean showOnHelp() {
        return true;
    }

}
