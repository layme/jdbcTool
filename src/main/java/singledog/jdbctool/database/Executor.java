package singledog.jdbctool.database;

import singledog.jdbctool.configuration.Configuration;
import singledog.jdbctool.constant.SystemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/8.
 */
public class Executor {
    private static final Logger log = LoggerFactory.getLogger(Executor.class);

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private CallableStatement callableStatement = null;

    protected Executor(Connection connection) {
        this.connection = connection;
    }

    /**
     * 根据ID查找SQL
     * @param id
     * @return
     */
    public String getSqlContent(String id) throws Exception {
        return Configuration.getConfiguration().getSqlContentMap().get(id);
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

    public void setProperties(PreparedStatement preparedStatement, List<String> properties) throws SQLException {
        if(null != properties) {
            int i = 1;
            for (String property : properties) {
                preparedStatement.setString(i++,property);
                log.debug("[" + property + "]");
            }
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
            setProperties(preparedStatement, properties);
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
     * @param id
     * @param properties
     * @return
     * @throws SQLException
     */
    public List<String> queryOneColumn(String id, List<String> properties) throws Exception {
        List<String> list = new ArrayList<String>();
        try {
            log.debug("sql>> " + getSqlContent(id));
            preparedStatement = connection.prepareStatement(getSqlContent(id));
            setProperties(preparedStatement, properties);
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
            setProperties(preparedStatement, properties);
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
            setProperties(callableStatement, properties);
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
