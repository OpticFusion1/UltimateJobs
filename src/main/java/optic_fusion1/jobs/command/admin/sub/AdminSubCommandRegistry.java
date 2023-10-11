package optic_fusion1.jobs.command.admin.sub;

import optic_fusion1.jobs.command.admin.sub.AdminSubCommand;
import java.util.ArrayList;
import java.util.List;

public class AdminSubCommandRegistry {

    private final List<AdminSubCommand> subCommandList = new ArrayList<AdminSubCommand>();

    public List<AdminSubCommand> getSubCommandList() {
        return subCommandList;
    }

}
