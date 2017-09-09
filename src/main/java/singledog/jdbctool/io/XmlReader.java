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
     * 把xml文件的数据封装成对象
     * @param list
     * @param cls
     * @return map
     * @throws Exception
     */
    public Map<String, Object> getNodeData(List<Element> list, Class cls) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fs = cls.getDeclaredFields();
        for (Field f : fs) {
            log.debug("f = " + f.getName());
        }
        for (Element e : list) {
            Object o = cls.newInstance();
            for (int i = 0; i < fs.length; i++) {
                Field field = cls.getDeclaredField(fs[i].getName());
                field.setAccessible(true);
                field.set(o, e.element(fs[i].getName()).getText());
            }
            map.put(e.attributeValue("id"), o);
        }
        return map;
    }
}
