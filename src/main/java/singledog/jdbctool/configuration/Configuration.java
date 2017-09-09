package singledog.jdbctool.configuration;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import singledog.jdbctool.constant.SystemInfo;
import singledog.jdbctool.database.Executor;
import singledog.jdbctool.io.XmlReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/8.
 */
public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Executor.class);

    private static Map<String, DataSource> dataSourceMap;  // 数据库参数
    private static Map<String, String> sqlContentMap;

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
            dataSourceMap = getDataMap("datasources", DataSource.class);
            //sqlContentMap = getDataMap("sqls", String.class);
            //sqlContentMap.putAll(getDataMap("procedures", String.class));
        }
        return configuration;
    }

    private static Map getDataMap(String nodeName, Class cls) throws Exception {
        try {
            return xmlReader.getNodeData(xmlReader.getNodes(nodeName), cls);
        } catch (Exception e) {
            log.debug(SystemInfo.GET_DATA_ERROR);
            throw e;
        }
    }

    public Map<String, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }

    public Map<String, String> getSqlContentMap() {
        return sqlContentMap;
    }
}
