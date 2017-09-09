package singledog.database;

import singledog.configuration.DataSource;
import singledog.constant.SystemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DbConnection {
    private static final Logger log = LoggerFactory.getLogger(DbConnection.class);

    private DataSource dataSource;  // 数据库参数
    private String profile;

    /**
     * 构造函数
     * @param dataSource
     */
    protected DbConnection(DataSource dataSource, String profile) {
        this.dataSource = dataSource;
        this.profile = profile;
    }

    /**
     * 构建执行器 默认关闭自动提交
     * @return executor
     * @throws Exception
     */
    public Executor buildExecutor() throws Exception {
        return this.buildExecutor(false);
    }

    /**
     * 构建执行器
     * @param autoCommit
     * @return executor
     * @throws Exception
     */
    public Executor buildExecutor(boolean autoCommit) throws Exception {
        log.debug(SystemInfo.DATABASE_ENVIRONMENT + profile);
        try {
            Class.forName(dataSource.getDriver());
            Connection connection = DriverManager.getConnection(dataSource.getUrl(),
                    dataSource.getUrl(),
                    dataSource.getPassword());
            connection.setAutoCommit(autoCommit);
            log.debug(SystemInfo.DATABASE_CONNECTION_SUCCESS);
            return new Executor(connection);
        } catch (SQLException e) {
            log.debug(SystemInfo.DATABASE_CONNECTION_ERROR);
            throw e;
        } catch (Exception e) {
            log.debug(SystemInfo.UNKNOWN_ERROR);
            throw e;
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
