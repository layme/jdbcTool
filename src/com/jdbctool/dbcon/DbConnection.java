package com.jdbctool.dbcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class DbConnection {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private String driver;
    private String url;
    private String username;
    private String password;

    private static final Logger log = LoggerFactory.getLogger(DbConnection.class);

    public Connection getConnection() {
        ResourceBundle resource = ResourceBundle.getBundle("config");
        String key = resource.getString("profile");
        driver = resource.getString("driver");
        if("produce".equals(key)) {
            log.info("数据库环境：生产");
            url = resource.getString("database.url.pro");
            username = resource.getString("database.username.pro");
            password = resource.getString("database.password.pro");
        } else {
            log.info("数据库环境：测试");
            url = resource.getString("database.url.dev");
            username = resource.getString("database.username.dev");
            password = resource.getString("database.password.dev");
        }
        log.debug("url = " + url + ", username = " + username + ", password = " + password);


        return connection;
    }
}
