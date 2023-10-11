package optic_fusion1.jobs.command.player.sub;

import optic_fusion1.jobs.gui.UpdateTypes;
import java.util.UUID;
import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.job.JobsPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpSub extends SubCommand {

    public HelpSub(UltimateJobs plugin) {
        super(plugin);
    }

    @Override
    public String getName(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Help.Usage");
    }

    @Override
    public String getDescription(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Help.Description");
    }

    @Override
    public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
        final Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        if (args.length == 1) {
            String mode = plugin.getLocalFileManager().getHelpSettings().getString("Help_Mode").toUpperCase();
            if (mode.equalsIgnoreCase("GUI")) {
                plugin.getGUI().createHelpGUI(player, UpdateTypes.OPEN);
            } else {

                plugin.getAPI().playSound("SEND_HELP", player);

                for (String m : jb.getLanguage().getList("Commands.Help.List")) {
                    player.sendMessage(plugin.getPluginManager().toHex(m).replaceAll("&", "§"));
                }
            }
        } else {
            plugin.getAPI().playSound("COMMAND_USAGE", player);
            player.sendMessage(jb.getLanguage().getMessage("command_usage").replaceAll("<usage>", getUsage(UUID)));
        }
    }

    @Override
    public String formatTab() {
        return "command help";
    }

    @Override
    public int getTabLength() {
        return 1;
    }

    @Override
    public boolean isEnabled() {
        return plugin.getLocalFileManager().getCMDSettings().getBoolean("Commands.Help.Enabled");
    }

    @Override
    public String getUsage(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Help.UsageMessage");
    }

}
