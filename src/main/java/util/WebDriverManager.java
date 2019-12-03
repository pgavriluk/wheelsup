package com.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public final class WebDriverManager {

    private static final Logger LOG = LoggerFactory.getLogger(WebDriverManager.class);
    private static WebDriver driver;
    private static final String BROWSER = "Browser";
    private static final String EXECUTION_MODE = "execution.mode";
    private static final String HUB_HOST = "hub.host";
    private static String browserName;
    private static String executionMode;
    private static String host = "localhost";

    public static WebDriver getWebDriver() {
        if (driver == null) {

            browserName = System.getProperty(BROWSER);
            executionMode = System.getProperty(EXECUTION_MODE);

            Browser browser = getBrowserFromString(browserName);

            DesiredCapabilities desiredCapabilities;

            if(executionMode != null && executionMode.equalsIgnoreCase("grid")){
                if(browser.getName().equalsIgnoreCase("firefox")){
                    desiredCapabilities = DesiredCapabilities.firefox();

                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("no-sandbox");
                    desiredCapabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
                }else{
                    desiredCapabilities = DesiredCapabilities.chrome();

                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("no-sandbox");
                    desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                }

                if(System.getProperty(HUB_HOST) != null){
                    host = System.getProperty(HUB_HOST);
                }

                String fullUrl = "http://" + host + ":4444/wd/hub";

                desiredCapabilities.setCapability("name", "Test name");

                try {
                    driver = new RemoteWebDriver(new URL(fullUrl), desiredCapabilities);

                    driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

                    if(browser.getName().equalsIgnoreCase("firefox")){
                        driver.manage().window().maximize();
                    }else{
                        driver.manage().window().fullscreen();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }else {
                switch (browser) {
                    case FIREFOX:
                        System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/drivers/geckodriver");
                        break;
                    case CHROME:
                        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver");
                        break;
                    case IE:
                        // implementation for IE
                        break;
                    default:
                        break;
                }
                driver = browser.getDriver();
                driver.manage().window().maximize();
            }
        }

        return driver;

    }

    private static Browser getBrowserFromString(String value) {
        for (Browser browser : Browser.values()) {
            if (value != null && value.equalsIgnoreCase(browser.getName())) {
                return browser;
            }
        }
        LOG.error("Invalid driver name passed as 'browser' property. "
                + "One of: " + Arrays.toString(Browser.values()) + " is expected.");
        return Browser.CHROME;
    }

    private enum Browser {
        FIREFOX("firefox", FirefoxDriver::new),
        CHROME("chrome", ChromeDriver::new),
        IE("ie", InternetExplorerDriver::new);

        private String name;
        private Supplier<WebDriver> driverSupplier;

        Browser(String name, Supplier<WebDriver> driverSupplier) {
            this.name = name;
            this.driverSupplier = driverSupplier;
        }

        public String getName() {
            return name;
        }

        public WebDriver getDriver() {
            return driverSupplier.get();
        }

    }
}
