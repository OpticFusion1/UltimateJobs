package optic_fusion1.jobs.command.admin;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.command.admin.sub.AdminSubCommand;
import optic_fusion1.jobs.util.Language;
import optic_fusion1.jobs.gui.GUIType;
import optic_fusion1.jobs.multiplier.MultiplierType;
import optic_fusion1.jobs.multiplier.MultiplierWeight;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class AdminTabComplete implements TabCompleter {

    public UltimateJobs plugin;

    public AdminTabComplete(UltimateJobs plugin) {
        this.plugin = plugin;
    }

    public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {

        ArrayList<String> l = new ArrayList<String>();

        if (s.hasPermission("ultimatejobs.complete")) {

            if (args.length == 1) {

                for (AdminSubCommand found : plugin.getAdminSubCommandManager().getSubCommandList()) {
                    l.add(found.getName());
                }

            } else if (args.length != 1) {

                for (AdminSubCommand c : plugin.getAdminSubCommandManager().getSubCommandList()) {

                    if (c.getTabLength() <= args.length) {

                        if (args[0].toLowerCase().equalsIgnoreCase(c.getName().toLowerCase())) {
                            if (!getFromFormat(args.length, c).equalsIgnoreCase("NOT_FOUND")) {

                                String type = getFromFormat(args.length, c).toUpperCase();

                                if (type.equalsIgnoreCase("PLAYERS_ONLINE")) {
                                    for (Player b : Bukkit.getOnlinePlayers()) {
                                        l.add(b.getName());
                                    }
                                } else if (type.equalsIgnoreCase("JOBS_LISTED")) {
                                    for (String b : plugin.getLoaded()) {
                                        String id = plugin.getJobCache().get(b).getConfigID();
                                        l.add(id);
                                    }
                                } else if (type.equalsIgnoreCase("GUI_TYPES")) {
                                    for (GUIType k : GUIType.values()) {
                                        l.add(k.toString().toLowerCase());
                                    }
                                    ;
                                } else if (type.equalsIgnoreCase("LANGUAGES")) {
                                    for (Language lang : plugin.getLanguageAPI().getLoadedLanguagesAsArray()) {
                                        l.add(lang.getName());
                                    }
                                    ;
                                } else if (type.equalsIgnoreCase("OPTIONS")) {
                                    for (AdminCommandOptions k : AdminCommandOptions.values()) {
                                        l.add(k.toString().toLowerCase());
                                    }
                                    ;
                                } else if (type.equalsIgnoreCase("ADMIN_GEN")) {
                                    l.add("set");
                                    l.add("add");
                                    l.add("remove");
                                } else if (type.equalsIgnoreCase("LEVEL_OPTIONS")) {
                                    l.add("set");
                                } else if (type.equalsIgnoreCase("LANGUAGE_OPTIONS")) {
                                    l.add("set");
                                } else if (type.equalsIgnoreCase("BOOST_TYPES")) {
                                    for (MultiplierType k : MultiplierType.values()) {
                                        l.add(k.toString().toLowerCase());
                                    }
                                    ;
                                } else if (type.equalsIgnoreCase("BOOST_UNTIL")) {
                                    l.add("1m");
                                    l.add("1h");
                                    l.add("1d");
                                    l.add("X");
                                } else if (type.equalsIgnoreCase("BOOST_WEIGHT")) {
                                    for (MultiplierWeight k : MultiplierWeight.values()) {
                                        l.add(k.toString().toLowerCase());
                                    }
                                    ;
                                }
                            }
                        }

                    }

                }

            }
        } else {
            l.add("none");
        }
        return l;

    }

    public String getFromFormat(int length, AdminSubCommand c) {
        String[] format = c.formatTab().split(" ");
        if (format.length <= length) {
            return "NOT_FOUND";
        }
        if (format[length] == null) {
            return "NOT_FOUND";
        }
        return format[length];

    }

}
