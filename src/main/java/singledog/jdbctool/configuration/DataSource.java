package singledog.jdbctool.configuration;

/**
 * Created by admin on 2017/9/8.
 */
public class DataSource {
    private String driver;  // 驱动
    private String url;  // 地址
    private String username;  // 用户
    private String password;  // 密码

    public DataSource() {}

    public DataSource(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
