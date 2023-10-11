package optic_fusion1.jobs.command.player.sub;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.gui.UpdateTypes;
import optic_fusion1.jobs.job.JobsPlayer;
import java.util.UUID;
import optic_fusion1.jobs.command.player.sub.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LangSub extends SubCommand {

    public LangSub(UltimateJobs plugin) {
        super(plugin);
    }

    @Override
    public String getName(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Language.Usage");
    }

    @Override
    public String getDescription(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Language.Description");
    }

    @Override
    public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
        final Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        if (args.length == 1) {

            plugin.getGUI().openLanguageMenu(player, UpdateTypes.OPEN);

        } else {
            plugin.getAPI().playSound("COMMAND_USAGE", player);
            player.sendMessage(
                    jb.getLanguage().getMessage("command_usage").replaceAll("<usage>", getUsage(UUID)));
        }
    }

    @Override
    public String formatTab() {
        return "command help languages";
    }

    @Override
    public int getTabLength() {
        return 1;
    }

    @Override
    public boolean isEnabled() {
        return plugin.getLocalFileManager().getCMDSettings().getBoolean("Commands.Language.Enabled");
    }

    @Override
    public String getUsage(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Language.UsageMessage");
    }
}
