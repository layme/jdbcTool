package com.jdbctool.untils;

import com.jdbctool.constant.SystemInfo;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/29.
 */
public class XmlParse {
    private static final Logger log = LoggerFactory.getLogger(XmlParse.class);

    private Document document = null;
    private static XmlParse xmlParse = null;

    private XmlParse() {}

    public static XmlParse getInstance() {
        if (null == xmlParse) {
            xmlParse = new XmlParse();
        }
        return xmlParse;
    }

    /**
     * 解析XML文件
     */
    public void loadXml() {
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(new File("target/classes/SqlPool.xml"));
            log.debug(SystemInfo.FILE_PARSE_SUCCESS);
        } catch(Exception e) {
            log.debug(SystemInfo.FILE_PARSE_ERROR);
            e.printStackTrace();
        }
    }

    /**
     * 根据ID查找SQL
     * @param id
     * @return
     * @throws IllegalArgumentException
     */
    public String getSqlById(String id) throws IllegalArgumentException {
        String sql;
        List<org.dom4j.Element> list = document.selectNodes("/sqls/sql[@id='" + id + "']");
        if (null == list || list.size() < 1) {
            throw new IllegalArgumentException(SystemInfo.ID_NOT_FOUND);
        }
        sql = list.get(0).getText();
        return sql;
    }

    /**
     * 根据GROUP ID查找SQlS
     * @param id
     * @return
     */
    public List<String> getSqlByGroupId(String id) {
        List<Element> list = document.selectNodes("/sqls/groups/group[@id='" + id + "']");
        if (null == list || list.size() < 1) {
            throw new IllegalArgumentException(SystemInfo.ID_NOT_FOUND);
        }
        list = list.get(0).elements();
        List<String> sqls = new ArrayList<String>();
        for (Element element : list) {
            sqls.add(element.getText());
        }
        return sqls;
    }
}
