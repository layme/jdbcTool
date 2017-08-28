package com.jdbctool.dbcon;

import com.jdbctool.constant.SystemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
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

    public DbConnection() {
        this.getConnection(false);
    }

    public Connection getConnection(boolean autoCommit) {
        if(null == connection) {
            ResourceBundle resource = ResourceBundle.getBundle("config");
            String key = resource.getString("profile");
            driver = resource.getString("driver");
            if ("produce".equals(key)) {
                log.info(SystemInfo.DATABASE_ENVIRONMENT_PRODUCE);
                url = resource.getString("database.url.pro");
                username = resource.getString("database.username.pro");
                password = resource.getString("database.password.pro");
            } else {
                log.info(SystemInfo.DATABASE_ENVIRONMENT_DEVELOP);
                url = resource.getString("database.url.dev");
                username = resource.getString("database.username.dev");
                password = resource.getString("database.password.dev");
            }
            log.debug("url = " + url + ", username = " + username + ", password = " + password);
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
        return connection;
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

    public int executeCount(String sql, String date) {
        int num = 0;
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            resultSet.last();
            num = resultSet.getRow();
            log.debug(SystemInfo.DATABASE_SELECT_SUCCESS + " >> " + sql);
        } catch (SQLException e) {
            log.debug(SystemInfo.DATABASE_SELECT_ERROR);
        }
        return num;
    }
}
