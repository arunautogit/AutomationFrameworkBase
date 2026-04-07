package com.automation.utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("WARNING: config.properties not found – using defaults.");
                setDefaults();
            } else {
                properties.load(input);
                System.out.println("Loaded config.properties from classpath.");
            }
        } catch (Exception e) {
            System.err.println("Failed to load config.properties – using defaults.");
            setDefaults();
        }
    }

    private static void setDefaults() {
        properties.setProperty("browser", "chrome");
        properties.setProperty("url.qa", "https://www.google.com");
        properties.setProperty("url.uat", "https://www.google.com");
        properties.setProperty("url.prod", "https://www.google.com");
        properties.setProperty("timeout", "10");
        properties.setProperty("headless", "true");
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static String getEnvironment() {
        String env = System.getProperty("env");
        if (env == null || env.isEmpty()) {
            env = "qa";
        }
        return env;
    }

    public static String getUrl() {
        String url = getProperty("url." + getEnvironment());
        if (url == null || url.isEmpty()) {
            url = "https://www.google.com";
            System.err.println("URL not configured, falling back to: " + url);
        }
        return url;
    }

    public static int getTimeout() {
        String timeout = getProperty("timeout");
        if (timeout == null) return 10;
        return Integer.parseInt(timeout);
    }

    public static boolean isHeadless() {
        String headless = getProperty("headless");
        return headless != null && headless.equalsIgnoreCase("true");
    }
}