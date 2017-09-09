import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import singledog.configuration.Configuration;
import singledog.configuration.DataSource;
import singledog.database.DbConnection;
import singledog.database.DbConnectionFactory;
import singledog.database.Executor;

/**
 * Created by admin on 2017/9/9.
 */
public class AppTest {
    private static final Logger log = LoggerFactory.getLogger(AppTest.class);

    public static void main(String[] args) {
        try {
            Configuration configuration = Configuration.getConfiguration("target/classes/singledog-cfg.xml");
//            DbConnection dbConnection = DbConnectionFactory.build("mac");
//            Executor executor = dbConnection.buildExecutor();
//            executor.queryAll("queryCount");
            System.out.println(configuration.getSqlContentMap().get("beife"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
