package optic_fusion1.jobs.database;

import com.zaxxer.hikari.HikariDataSource;
import optic_fusion1.jobs.UltimateJobs;
import optic_fusion1.jobs.database.hikari.HikariAuthentication;
import optic_fusion1.jobs.database.hikari.HikariSetup;
import optic_fusion1.jobs.database.hikari.SQLTypes;
import java.sql.Connection;

public class DatabaseInit extends HikariSetup {

    private HikariAuthentication authentication;

    public void initDatabase(UltimateJobs plugin, HikariAuthentication authentication, String type, int timeout, int ppolsize) {
        this.authentication = authentication;
        init(plugin, SQLTypes.fromName(type), authentication, timeout, ppolsize);
    }

    @Override
    public HikariDataSource getDataSource() {
        return super.getDataSource();
    }

    @Override
    public Connection getConnection() {
        return super.getConnection();
    }

    public HikariAuthentication getAuthentication() {
        return authentication;
    }
}
