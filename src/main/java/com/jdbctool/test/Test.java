package com.jdbctool.test;

import com.jdbctool.dbcon.DbConnection;
import com.jdbctool.untils.XmlParse;
import org.apache.log4j.lf5.util.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Test {
    private static final Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        XmlParse xmlParse = XmlParse.getInstance();
        xmlParse.loadXml();
//        String sql = xmlParse.getSqlById("allCount");
        List<String> sqls = xmlParse.getSqlByGroupId("backup");
        for (String sql : sqls) {
            log.debug(sql + "\n");
        }
        log.debug("" + sqls.size());
//        List<String> list = new ArrayList<String>();
//        list.add("20170825");
//        list.add("20170828");
//        DbConnection c = DbConnection.getInstance();
//        try {
//            c.getConnection();
//            int count = c.queryCount(sql, list);
//            log.debug("count = " + count);
//            c.commit();
//        } catch (Exception e) {
//            c.rollback();
//            e.printStackTrace();
//        } finally {
//            c.close();
//        }
    }
}
