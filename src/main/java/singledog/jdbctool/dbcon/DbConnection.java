package singledog.jdbctool.dbcon;

import singledog.jdbctool.constant.SystemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DbConnection {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private CallableStatement callableStatement = null;

    private String driver;
    private String url;
    private String username;
    private String password;

    private static final Logger log = LoggerFactory.getLogger(DbConnection.class);

    private static DbConnection dbConnection = null;

    private DbConnection() {}

    /**
     * 获取单例实例
     * @return
     */
    public static DbConnection getInstance() {
        if(null == dbConnection) {
            dbConnection = new DbConnection();
        }
        return dbConnection;
    }

    /**
     * 获取连接 默认关闭自动提交
     * @throws Exception
     */
    public void getConnection() throws Exception {
        this.getConnection(false);
    }

    /**
     * 获取连接并指定提交方式
     * @param autoCommit
     * @throws Exception
     */
    public void getConnection(boolean autoCommit) throws Exception {
        if(null == connection) {
            ResourceBundle resource = ResourceBundle.getBundle("config");
            String key = resource.getString("profile");
            driver = resource.getString("driver");
            if ("produce".equals(key)) {
                log.debug(SystemInfo.DATABASE_ENVIRONMENT_PRODUCE);
                url = resource.getString("database.url.pro");
                username = resource.getString("database.username.pro");
                password = resource.getString("database.password.pro");
            } else {
                log.info(SystemInfo.DATABASE_ENVIRONMENT_DEVELOP);
                url = resource.getString("database.url.dev");
                username = resource.getString("database.username.dev");
                password = resource.getString("database.password.dev");
            }
            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url, username, password);
                connection.setAutoCommit(autoCommit);
                log.debug(SystemInfo.DATABASE_CONNECTION_SUCCESS);
            } catch (SQLException e) {
                log.debug(SystemInfo.DATABASE_CONNECTION_ERROR);
                throw e;
            } catch (Exception e) {
                log.debug(SystemInfo.UNKNOWN_ERROR);
                throw e;
            }
        }
    }

    /**
     * 提交事务
     * @throws SQLException
     */
    public void commit() throws SQLException {
        try {
            connection.commit();
            log.debug(SystemInfo.DATABASE_COMMIT_SUCCESS);
        } catch (SQLException e) {
            log.debug(SystemInfo.DATABASE_COMMIT_ERROR);
            throw e;
        }
    }

    /**
     * 回滚事务
     * @throws SQLException
     */
    public void rollback() throws SQLException {
        try {
            connection.rollback();
            log.debug(SystemInfo.DATABASE_ROLLBACK_SUCCESS);
        } catch (SQLException e) {
            log.debug(SystemInfo.DATABASE_ROLLBACK_ERROR);
            throw e;
        }
    }

    /**
     * 关闭数据库连接
     * @throws SQLException
     */
    public void close() throws SQLException {
        try {
            if(null != connection) {
                connection.close();
            }
            if(null != preparedStatement) {
                preparedStatement.close();
            }
            if(null != resultSet) {
                resultSet.close();
            }
            log.debug(SystemInfo.DATABASE_CLOSE_SUCCESS);
        } catch (SQLException e) {
            log.debug(SystemInfo.DATABASE_CLOSE_ERROR);
            throw e;
        }
    }

    /**
     * 查询总数
     * @param sql
     * @param properties
     * @return
     * @throws SQLException
     */
    public int queryCount(String sql, List<String> properties) throws SQLException {
        int count = 0;
        try {
            log.debug("sql>> " + sql);
            preparedStatement = connection.prepareStatement(sql);
            if(null != properties) {
                int i = 1;
                for (String property : properties) {
                    preparedStatement.setString(i++,property);
                }
            }
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }
            log.debug(SystemInfo.DATABASE_SELECT_SUCCESS);
            log.debug("count = " + count);
        } catch (SQLException e) {
            log.debug(SystemInfo.DATABASE_SELECT_ERROR);
            throw e;
        }
        return count;
    }

    /**
     * 查询单列
     * @param sql
     * @param properties
     * @return
     * @throws SQLException
     */
    public List<String> queryOneColumn(String sql, List<String> properties) throws SQLException {
        List<String> list = new ArrayList<String>();
        try {
            log.debug("sql>> " + sql);
            preparedStatement = connection.prepareStatement(sql);
            if(null != properties) {
                int i = 1;
                for (String property : properties) {
                    preparedStatement.setString(i++,property);
                }
            }
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                do{
                    list.add(resultSet.getString(1));
                } while(resultSet.next());
            }
            log.debug(SystemInfo.DATABASE_SELECT_SUCCESS);
        } catch (SQLException e) {
            log.debug(SystemInfo.DATABASE_SELECT_ERROR);
            throw e;
        }
        return list;
    }

    /**
     * 查询全部
     * @param sql
     * @return
     * @throws SQLException
     */
    public ResultSet queryAll(String sql) throws SQLException {
        try {
            log.debug("sql>> " + sql);
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            log.debug(SystemInfo.DATABASE_SELECT_SUCCESS);
        } catch (SQLException e) {
            log.debug(SystemInfo.DATABASE_SELECT_ERROR);
            throw e;
        }
        return resultSet;
    }

    /**
     * 更新/删除数据
     * @param sql
     * @param properties
     * @return
     * @throws SQLException
     */
    public int update(String sql, List<String> properties) throws SQLException {
        int num = 0;
        try {
            log.debug("sql>> " + sql);
            preparedStatement = connection.prepareStatement(sql);
            if(null != properties) {
                int i = 1;
                for (String property : properties) {
                    preparedStatement.setString(i++,property);
                }
            }
            num = preparedStatement.executeUpdate();
            log.debug(SystemInfo.DATABASE_UPDATE_SUCCESS);
        } catch (SQLException e) {
            log.debug(SystemInfo.DATABASE_UPDATE_ERROR);
            throw e;
        }
        return num;
    }

    /**
     * 执行带参无返回值的存储过程
     * @param procedure
     * @param properties
     * @return
     * @throws SQLException
     */
    public boolean callProcedureWithoutResult(String procedure, List<String> properties) throws SQLException {
        boolean result = false;
        try {
            log.debug("procedure>> " + procedure);
            callableStatement = connection.prepareCall(procedure);
            if(null != properties) {
                int i = 1;
                for (String property : properties) {
                    callableStatement.setString(i++,property);
                }
            }
            callableStatement.execute();
            log.debug(SystemInfo.DATABASE_PROCEDURE_SUCCESS);
            result = true;
        } catch (SQLException e) {
            log.debug(SystemInfo.DATABASE_PROCEDURE_ERROR);
            throw e;
        }
        return result;
    }
}
