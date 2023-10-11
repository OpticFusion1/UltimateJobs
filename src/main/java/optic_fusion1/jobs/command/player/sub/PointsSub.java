package optic_fusion1.jobs.command.player.sub;

import java.util.UUID;
import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.job.JobsPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PointsSub extends SubCommand {

    public PointsSub(UltimateJobs plugin) {
        super(plugin);
    }

    @Override
    public String getName(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Points.Usage");
    }

    @Override
    public String getDescription(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Points.Description");
    }

    @Override
    public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
        final Player player = (Player) sender;
        UUID UUID = player.getUniqueId();

        if (args.length == 2) {
            String pl = args[1].toUpperCase();

            if (plugin.getPlayerAPI().getJobsPlayerByName(pl.toLowerCase()) == null) {
                plugin.getAPI().playSound("COMMAND_PLAYER_NOT_FOUND", player);
                player.sendMessage(jb.getLanguage().getMessage("command_points_not_found")
                        .replaceAll("<name>", args[1]));
                return;
            } else {
                String uuid = plugin.getPlayerAPI().getJobsPlayerByName(pl.toLowerCase());

                plugin.getAPI().playSound("COMMAND_POINTS_OTHER_SUCCES", player);

                double points = plugin.getPlayerAPI().getPoints(uuid);
                player.sendMessage(jb.getLanguage().getMessage("command_points_other")
                        .replaceAll("<points>", plugin.getAPI().Format(points)).replaceAll("<name>", args[1]));

                return;

            }
        } else if (args.length == 1) {
            plugin.getAPI().playSound("COMMAND_POINTS_SELF_SUCCES", player);
            player.sendMessage(jb.getLanguage().getMessage("command_points_self")
                    .replaceAll("<points>", plugin.getAPI().Format(jb.getPoints())));
            return;
        } else {
            plugin.getAPI().playSound("COMMAND_USAGE", player);
            player.sendMessage(jb.getLanguage().getMessage("command_usage").replaceAll("<usage>",
                    getUsage(UUID)));
        }
    }

    @Override
    public String formatTab() {
        return "command points players_online";
    }

    @Override
    public int getTabLength() {
        return 2;
    }

    @Override
    public boolean isEnabled() {
        return plugin.getLocalFileManager().getCMDSettings().getBoolean("Commands.Points.Enabled");
    }

    @Override
    public String getUsage(UUID UUID) {
        JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer("" + UUID);
        return jb.getLanguage().getMessage("Commands.Points.UsageMessage");
    }

}
