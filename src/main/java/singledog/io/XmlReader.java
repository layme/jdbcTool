package singledog.io;

import singledog.constant.SystemInfo;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            log.debug(SystemInfo.FILE_PARSE_SUCCESS + filePath);
        } catch(Exception e) {
            log.debug(SystemInfo.FILE_PARSE_ERROR + filePath);
            e.printStackTrace();
        }
    }

    /**
     * 获取所有子节点
     * @param nodeName
     * @return list
     */
    public List<Element> getNodes(String nodeName) {
        List<Element> list = null;
        try {
            Element rootElement = document.getRootElement();
            list = rootElement.element(nodeName).elements();
            log.debug(SystemInfo.GET_NODE_SUCCESS + nodeName);
        } catch (Exception e) {
            log.debug(SystemInfo.GET_NODE_ERROR + nodeName);
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 把xml文件的数据封装成对象
     * @param list
     * @param cls
     * @return map
     * @throws Exception
     */
    public Map<String, Object> getNodeTextToObj(List<Element> list, Class cls) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fs = cls.getDeclaredFields();
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

    /**
     *
     * @param list
     * @return map
     * @throws Exception
     */
    public Map<String, String> getNodeTextToString(List<Element> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        for (Element e : list) {
            map.put(e.attributeValue("id"), e.getText());
        }
        return map;
    }
}