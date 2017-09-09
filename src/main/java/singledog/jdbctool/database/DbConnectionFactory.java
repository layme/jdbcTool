package singledog.jdbctool.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import singledog.jdbctool.configuration.Configuration;
import singledog.jdbctool.configuration.DataSource;
import singledog.jdbctool.constant.SystemInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by admin on 2017/9/8.
 */
public class DbConnectionFactory {
    private static final Logger log = LoggerFactory.getLogger(DbConnectionFactory.class);

    private DbConnectionFactory() {}

    /**
     * 获取连接 默认关闭自动提交
     * @throws Exception
     */
    public static DbConnection build() throws Exception {
        return build("develop");
    }

    /**
     * 获取连接并指定提交方式
     * @param profile
     * @throws Exception
     */
    public static DbConnection build(String profile) throws Exception {
        return new DbConnection(Configuration.getConfiguration().getDataSourceMap().get(profile), profile);
    }
}
