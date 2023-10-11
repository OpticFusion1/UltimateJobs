package optic_fusion1.jobs.api;

import optic_fusion1.jobs.database.statements.SQLStatementAPI;

public class DataBaseAPI {

    private static SQLStatementAPI sqlStatementAPI = null;

    public static SQLStatementAPI getSQLStatementAPI() {
        if (sqlStatementAPI == null) {
            sqlStatementAPI = new SQLStatementAPI();
        }
        return sqlStatementAPI;
    }

}
