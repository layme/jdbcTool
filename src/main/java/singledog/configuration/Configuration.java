package singledog.configuration;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import singledog.constant.SystemInfo;
import singledog.database.Executor;
import singledog.io.XmlReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/8.
 */
public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    private static Map<String, DataSource> dataSourceMap;  // 数据库参数
    private static Map<String, String> sqlContentMap;  // SQL语句

    private static Configuration configuration;
    private static XmlReader xmlReader;

    private Configuration() {}

    /**
     * 获取单例对象
     * @return configuration
     */
    public static Configuration getConfiguration() throws Exception {
        return getConfiguration("singledog-cfg.xml");
    }

    public static Configuration getConfiguration(String filePath) throws Exception {
        if(null == configuration) {
            configuration = new Configuration();
            xmlReader = XmlReader.getInstance();
            xmlReader.loadXml(filePath);
            //dataSourceMap = getDataMap("datasources", DataSource.class);
            sqlContentMap = getResources();
        }
        return configuration;
    }

    /**
     *
     * @param nodeName
     * @param cls
     * @return
     * @throws Exception
     */
    private static Map getDataMap(String nodeName, Class cls) throws Exception {
        Map map = null;
        try {
            map = xmlReader.getNodeTextToObj(xmlReader.getNodes(nodeName), cls);
        } catch (Exception e) {
            log.debug(SystemInfo.GET_DATA_ERROR);
            e.printStackTrace();
        }
        return map;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public static Map<String, String> getResources() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        try {
            List<Element> resources = xmlReader.getNodes("resources");
            for (Element e : resources) {
                xmlReader.loadXml(e.getText());
                map.putAll(xmlReader.getNodeTextToString(xmlReader.getNodes("procedures")));
                map.putAll(xmlReader.getNodeTextToString(xmlReader.getNodes("sqls")));
            }
        } catch (Exception e) {
            log.debug(SystemInfo.GET_RESOURCE_ERROR);
            e.printStackTrace();
        }
        return map;
    }

    public Map<String, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }

    public Map<String, String> getSqlContentMap() {
        return sqlContentMap;
    }
}
