package com.automation.utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static String getEnvironment() {
        return System.getProperty("env", "qa");
    }

    public static String getUrl() {
        return getProperty("url." + getEnvironment());
    }

    public static int getTimeout() {
        return Integer.parseInt(getProperty("timeout"));
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless"));
    }

    public static String getSlackWebhook() {
        String webhook = System.getenv("SLACK_WEBHOOK_URL");
        return webhook != null ? webhook : getProperty("slack.webhook");
    }
}