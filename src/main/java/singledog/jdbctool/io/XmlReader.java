package singledog.jdbctool.io;

import singledog.jdbctool.configuration.DataSource;
import singledog.jdbctool.constant.SystemInfo;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.annotation.ExceptionProxy;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by admin on 2017/8/29.
 */
public class XmlReader {
    private static final Logger log = LoggerFactory.getLogger(XmlReader.class);

    private Document document = null;
    private static XmlReader xmlReader = null;

    private XmlReader() {}

    /**
     * 获取单例实例
     * @return xmlReader
     */
    public static XmlReader getInstance() {
        if (null == xmlReader) {
            xmlReader = new XmlReader();
        }
        return xmlReader;
    }

    /**
     * 加载XML文件
     */
    public void loadXml(String filePath) {
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(new File(filePath));
            log.debug(SystemInfo.FILE_PARSE_SUCCESS);
        } catch(Exception e) {
            log.debug(SystemInfo.FILE_PARSE_ERROR);
            e.printStackTrace();
        }
    }

    /**
     * 获取所有子节点
     * @param nodeName
     * @return list
     */
    public List<Element> getNodes(String nodeName) {
        try {
            Element rootElement = document.getRootElement();
            List<Element> list = rootElement.element(nodeName).elements();
            log.debug(SystemInfo.GET_NODE_SUCCESS);
            return list;
        } catch (Exception e) {
            log.debug(SystemInfo.GET_NODE_ERROR);
            throw e;
        }
    }

    /**
     * 吧xml文件的数据封装成对象
     * @param list
     * @param cls
     * @return map
     * @throws Exception
     */
    public Map<String, Object> getNodeData(List<Element> list, Class cls) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fs = cls.getDeclaredFields();
        for (Element e : list) {
            Object o = cls.newInstance();
            for (int i = 0; i < fs.length; i++) {
                Field field = cls.getDeclaredField(fs[i].getName());
                field.set(o, e.element(fs[i].getName()).getText());
            }
            map.put(e.attributeValue("id"), o);
        }
        return map;
    }

    /**
     * 根据ID查找SQL
     * @param id
     * @return
     * @throws IllegalArgumentException
     */
    public String getSqlById(String id) throws IllegalArgumentException {
        String sql;
        List<Element> list = document.selectNodes("/sqls/sql[@id='" + id + "']");
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

    /**
     * 根据ID查找PROCEDURE
     * @param id
     * @return
     */
    public String getProcedureById(String id) {
        String procedure;
        List<Element> list = document.selectNodes("/sqls/procedures/procedure[@id='" + id + "']");
        if (null == list || list.size() < 1) {
            throw new IllegalArgumentException(SystemInfo.ID_NOT_FOUND);
        }
        procedure = list.get(0).getText();
        return procedure;
    }
}
