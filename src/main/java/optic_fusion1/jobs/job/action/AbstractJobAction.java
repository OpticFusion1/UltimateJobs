package optic_fusion1.jobs.job.action;

import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.manager.JobWorkManager;
import org.bukkit.event.Listener;

public abstract class AbstractJobAction implements Listener {

    private UltimateJobs ultimateJobs;
    private JobWorkManager jobWorkManager;

    public AbstractJobAction(UltimateJobs ultimateJobs) {
        this.ultimateJobs = ultimateJobs;
        jobWorkManager = ultimateJobs.getJobWorkManager();
    }

    public UltimateJobs getUltimateJobs() {
        return ultimateJobs;
    }
    
    public JobWorkManager getJobWorkManager() {
        return jobWorkManager;
    }

}
