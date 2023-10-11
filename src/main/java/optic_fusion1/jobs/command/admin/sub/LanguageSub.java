package optic_fusion1.jobs.command.admin.sub;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.command.admin.AdminCommand;
import optic_fusion1.jobs.util.JsonMessage;
import optic_fusion1.jobs.command.admin.sub.AdminSubCommand;
import optic_fusion1.jobs.util.Language;
import optic_fusion1.jobs.job.JobsPlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LanguageSub extends AdminSubCommand {

    public LanguageSub(UltimateJobs plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "language";
    }

    @Override
    public String getDescription() {
        return "Update Player's Language";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length == 1) {

            sender.sendMessage("§7");
            sender.sendMessage(" §8| §9UltimateJobs §8- §aPlayer Languages §8|");

            if (sender instanceof Player) {

                Player player = (Player) sender;

                new JsonMessage()
                        .append("§8-> §6/JobsAdmin language §8| §7View all arguments")
                        .setClickAsSuggestCmd("/jobsadmin language").save().send(player);

                new JsonMessage()
                        .append("§8-> §6/JobsAdmin language set <player_name> <lang> §8| §7Set a Language")
                        .setClickAsSuggestCmd("/jobsadmin language set").save().send(player);

            } else {
                sender.sendMessage("§8-> §6/JobsAdmin language §8| §7View all arguments");
                sender.sendMessage("§8-> §6/JobsAdmin language set <player_name> <lang> §8| §7Set a Language");
            }

            sender.sendMessage("§7");

            if (sender instanceof Player) {
                Player player3 = (Player) sender;
                player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
            }

        } else if (args.length == 4 && args[1].toLowerCase().equalsIgnoreCase("set")) {

            String player = args[2];
            String value = args[3];

            if (plugin.getPlayerAPI().getJobsPlayerByName(player.toLowerCase()) == null) {
                sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
                if (sender instanceof Player) {
                    Player player3 = (Player) sender;
                    player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
                }
                return;
            }

            String uuid = plugin.getPlayerAPI().getJobsPlayerByName(player.toLowerCase());

            if (plugin.getLanguageAPI().getLanguageFromName(value.toUpperCase()) != null) {

                if (sender instanceof Player) {
                    Player player3 = (Player) sender;
                    player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
                }

                Language lang = plugin.getLanguageAPI().getLanguageFromName(value.toUpperCase());

                if (plugin.getPlayerAPI().getRealJobPlayer(uuid) != null) {
                    JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(uuid);

                    jb.updateLocalLanguage(lang);
                }

                plugin.getPlayerAPI().updateSettingData(uuid, "LANG", lang.getName());

                sender.sendMessage(AdminCommand.prefix + "Changed §c" + player + "'s §7Language to §a" + value
                        + "§7.");
                return;

            } else {
                sender.sendMessage(AdminCommand.prefix + "Error! Language not found!");
                if (sender instanceof Player) {
                    Player player3 = (Player) sender;
                    player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
                }
                return;
            }

        } else {
            sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6" + getUsage());
            if (sender instanceof Player) {
                Player player3 = (Player) sender;
                player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
            }
        }
    }

    @Override
    public int getTabLength() {
        return 1;
    }

    @Override
    public String formatTab() {
        return "command language language_options players_online languages";
    }

    @Override
    public String getUsage() {
        return "/JobsAdmin language";
    }

    @Override
    public String getPermission() {
        return "ultimatejobs.admin.language";
    }

    @Override
    public boolean showOnHelp() {
        return true;
    }

}
