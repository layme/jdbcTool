import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import singledog.configuration.Configuration;
import singledog.configuration.DataSource;
import singledog.database.DbConnection;
import singledog.database.DbConnectionFactory;
import singledog.database.Executor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/9.
 */
public class AppTest {
    private static final Logger log = LoggerFactory.getLogger(AppTest.class);

    public static void main(String[] args) {
        Executor executor = null;
        try {
            Configuration configuration = Configuration.getConfiguration("target/classes/singledog-cfg.xml");
            DbConnection dbConnection = DbConnectionFactory.build("mac");
            executor = dbConnection.buildExecutor();
            List<String> properties = new ArrayList<String>();
            properties.add("张星辰");
            properties.add("02");
            properties.add("北京");
            properties.add("11");
            log.debug("update Result = " + executor.update("update",properties));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.close();
        }
    }
}
