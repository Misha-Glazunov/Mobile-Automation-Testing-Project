package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found!");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading config.properties", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getWebBaseUrl() {
        return getProperty("web.base.url");
    }

    public static String getBrowser() {
        return getProperty("web.browser");
    }

    public static int getTimeout() {
        return Integer.parseInt(getProperty("web.timeout"));
    }

    public static String getAppiumUrl() {
        return getProperty("mobile.appium.url");
    }

    public static String getAppPath() {
        return getProperty("mobile.app.path");
    }

    public static String getAppPackage() {
        return getProperty("mobile.app.package");
    }

    public static String getAppActivity() {
        return getProperty("mobile.app.activity");
    }

    public static String getPlatformName() {
        return getProperty("mobile.platform.name");
    }

    public static String getDeviceName() {
        return getProperty("mobile.device.name");
    }

    public static String getPlatformVersion() {
        return getProperty("mobile.platform.version");
    }
}