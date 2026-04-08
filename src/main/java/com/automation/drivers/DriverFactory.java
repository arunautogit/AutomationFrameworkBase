package com.automation.drivers;

import com.automation.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Factory class to create WebDriver instances.
 * Supports Chrome, Firefox, Edge with headless mode and configurable timeouts.
 * Thread-safe for parallel execution.
 * Optimised for Docker/headless environments.
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Initializes WebDriver based on configuration properties.
     * Uses system-installed browser binaries when available (e.g., in Docker).
     *
     * @return WebDriver instance
     */
    public static WebDriver initDriver() {
        String browser = ConfigReader.getBrowser().toLowerCase();
        boolean headless = ConfigReader.isHeadless();
        WebDriver driver;

        if (ConfigReader.isRemoteExecution()) {
            driver = initRemoteDriver(browser, headless);
        } else {
            switch (browser) {
                case "chrome":
                    driver = initChromeDriver(headless);
                    break;
                case "firefox":
                    driver = initFirefoxDriver(headless);
                    break;
                case "edge":
                    driver = initEdgeDriver(headless);
                    break;
                default:
                    throw new IllegalArgumentException("Browser not supported: " + browser);
            }
        }

        configureWindow(driver, headless);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getTimeout()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getTimeout()));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(ConfigReader.getTimeout()));

        driverThreadLocal.set(driver);
        return driver;
    }

    private static WebDriver initChromeDriver(boolean headless) {
        ChromeOptions options = buildChromeOptions(headless);

        // Try to use system ChromeDriver first, else fallback to WebDriverManager
        String systemChromeDriver = findSystemChromeDriver();
        if (systemChromeDriver != null) {
            System.setProperty("webdriver.chrome.driver", systemChromeDriver);
            System.out.println("Using system ChromeDriver: " + systemChromeDriver);
        } else {
            System.out.println("System ChromeDriver not found, using WebDriverManager...");
            WebDriverManager.chromedriver().setup();
        }

        return new ChromeDriver(options);
    }

    private static WebDriver initFirefoxDriver(boolean headless) {
        FirefoxOptions options = buildFirefoxOptions(headless);
        // Try system geckodriver first
        String systemGecko = findSystemGeckoDriver();
        if (systemGecko != null) {
            System.setProperty("webdriver.gecko.driver", systemGecko);
        } else {
            WebDriverManager.firefoxdriver().setup();
        }
        return new FirefoxDriver(options);
    }

    private static WebDriver initEdgeDriver(boolean headless) {
        EdgeOptions options = buildEdgeOptions(headless);
        WebDriverManager.edgedriver().setup();
        return new EdgeDriver(options);
    }

    private static WebDriver initRemoteDriver(String browser, boolean headless) {
        MutableCapabilities capabilities;
        switch (browser) {
            case "chrome":
                capabilities = buildChromeOptions(headless);
                break;
            case "firefox":
                capabilities = buildFirefoxOptions(headless);
                break;
            case "edge":
                capabilities = buildEdgeOptions(headless);
                break;
            default:
                throw new IllegalArgumentException("Browser not supported for remote execution: " + browser);
        }

        try {
            return new RemoteWebDriver(new URL(ConfigReader.getRemoteUrl()), capabilities);
        } catch (MalformedURLException exception) {
            throw new IllegalArgumentException("Invalid remote Selenium URL: " + ConfigReader.getRemoteUrl(), exception);
        }
    }

    private static ChromeOptions buildChromeOptions(boolean headless) {
        String chromeBinary = findChromeBinary();
        ChromeOptions options = new ChromeOptions();

        if (chromeBinary != null) {
            options.setBinary(chromeBinary);
            System.out.println("Using Chrome binary: " + chromeBinary);
        }

        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("--disable-extensions");
        return options;
    }

    private static FirefoxOptions buildFirefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        return options;
    }

    private static EdgeOptions buildEdgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        return options;
    }

    private static void configureWindow(WebDriver driver, boolean headless) {
        if (headless) {
            return;
        }
        try {
            driver.manage().window().maximize();
        } catch (WebDriverException exception) {
            System.out.println("Window maximize skipped: " + exception.getMessage());
        }
    }

    private static String findChromeBinary() {
        // Common paths for Chromium/Chrome in Alpine/Docker
        String[] possiblePaths = {
            "/usr/bin/chromium-browser",
            "/usr/bin/chromium",
            "/usr/bin/google-chrome",
            "/opt/google/chrome/chrome"
        };
        for (String path : possiblePaths) {
            if (new File(path).exists()) {
                return path;
            }
        }
        return null;
    }

    private static String findSystemChromeDriver() {
        String[] possiblePaths = {
            "/usr/lib/chromium/chromedriver",
            "/usr/bin/chromedriver",
            "/usr/local/bin/chromedriver"
        };
        for (String path : possiblePaths) {
            if (new File(path).exists()) {
                return path;
            }
        }
        return null;
    }

    private static String findSystemGeckoDriver() {
        String[] possiblePaths = {
            "/usr/bin/geckodriver",
            "/usr/local/bin/geckodriver"
        };
        for (String path : possiblePaths) {
            if (new File(path).exists()) {
                return path;
            }
        }
        return null;
    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
