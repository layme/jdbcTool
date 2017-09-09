import singledog.jdbctool.configuration.Configuration;
import singledog.jdbctool.database.DbConnection;
import singledog.jdbctool.database.DbConnectionFactory;
import singledog.jdbctool.database.Executor;

import java.sql.SQLException;

/**
 * Created by admin on 2017/9/9.
 */
public class AppTest {
    public static void main(String[] args) {
        try {
            Configuration configuration = Configuration.getConfiguration("target\\classes\\singledog-cfg.xml");
            DbConnection dbConnection = DbConnectionFactory.build();
            Executor executor = dbConnection.buildExecutor();
            //executor.queryAll("");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
