package com.jdbctool.untils;

import com.jdbctool.constant.SystemInfo;
import com.jdbctool.dbcon.DbConnection;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by admin on 2017/8/29.
 */
public class XmlParse {
    private static final Logger log = LoggerFactory.getLogger(XmlParse.class);

    private Document document = null;
    private static XmlParse xmlParse = null;

    public static XmlParse getInstance() {
        if (null == xmlParse) {
            xmlParse = new XmlParse();
        }
        return xmlParse;
    }

    public void loadXml() {
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(new File("F:\\GitHub\\jdbcTool\\target\\classes\\SqlPool.xml"));
            log.debug(SystemInfo.FILE_PARSE_SUCCESS);
        } catch(Exception e) {
            log.debug(SystemInfo.FILE_PARSE_ERROR);
            e.printStackTrace();
        }
    }

    public String getSql(String id) throws IllegalArgumentException {
        String sql = null;
        List<Element> list = document.selectNodes("/sqls/sql[@id='" + id + "']");
        if (null == list || list.size() < 1) {
            throw new IllegalArgumentException(SystemInfo.ID_NOT_FOUND);
        }
        sql = list.get(0).getText();
        return sql;
    }


}
