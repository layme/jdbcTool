package com.jdbctool.dbcon;

import com.jdbctool.constant.SystemInfo;
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

    private String driver = null;
    private String url = null;
    private String username = null;
    private String password = null;

    private static final Logger log = LoggerFactory.getLogger(DbConnection.class);

    public void getConnection() {
        this.getConnection(false);
    }

    public void getConnection(boolean autoCommit) {
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
                e.printStackTrace();
            } catch (Exception e) {
                log.debug(SystemInfo.UNKNOWN_ERROR);
                e.printStackTrace();
            }
        }
    }

    public void commit() {
        try {
            connection.commit();
            log.debug(SystemInfo.DATABASE_COMMIT_SUCCESS);
        } catch (SQLException e) {
            log.debug(SystemInfo.DATABASE_COMMIT_ERROR);
            e.printStackTrace();
        }
    }

    public void rollback() {
        try {
            connection.rollback();
            log.debug(SystemInfo.DATABASE_ROLLBACK_SUCCESS);
        } catch (SQLException e) {
            log.debug(SystemInfo.DATABASE_ROLLBACK_ERROR);
            e.printStackTrace();
        }
    }

    public void close() {
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
            e.printStackTrace();
        }
    }

    /**
     * 查询总数
     * @param sql
     * @param properties
     * @return
     */
    public int queryCount(String sql, List<String> properties) throws SQLException {
        int count = 0;
        try {
            preparedStatement = connection.prepareStatement(sql);
            if(null != properties) {
                int i = 1;
                for (String property : properties) {
                    preparedStatement.setString(i++,property);
                }
            }
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                count = resultSet.getInt("count");
            }
            log.debug(SystemInfo.DATABASE_SELECT_SUCCESS);
            log.debug("sql>> " + sql);
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
     */
    public List<String> QueryOneColumn(String sql, List<String> properties) throws SQLException {
        List<String> list = new ArrayList<String>();
        try {
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
                    list.add(resultSet.getString("shenqingh"));
                } while(resultSet.next());
            }
            log.debug(SystemInfo.DATABASE_SELECT_SUCCESS);
            log.debug("sql>> " + sql);
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
     */
    public ResultSet queryAll(String sql) throws SQLException {
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            log.debug(SystemInfo.DATABASE_SELECT_SUCCESS);
            log.debug("sql>> " + sql);
        } catch (SQLException e) {
            log.debug(SystemInfo.DATABASE_SELECT_ERROR);
            throw e;
        }
        return resultSet;
    }

    /**
     * 更新数据
     * @param sql
     * @param properties
     * @return
     */
    public int executeUpdate(String sql, List<String> properties) throws SQLException {
        int num = 0;
        try {
            preparedStatement = connection.prepareStatement(sql);
            if(null != properties) {
                int i = 1;
                for (String property : properties) {
                    preparedStatement.setString(i++,property);
                }
            }
            num = preparedStatement.executeUpdate();
            log.debug(SystemInfo.DATABASE_UPDATE_SUCCESS);
            log.debug("sql>> " + sql);
        } catch (SQLException e) {
            log.debug(SystemInfo.DATABASE_UPDATE_ERROR);
            throw e;
        }
        return num;
    }

}
