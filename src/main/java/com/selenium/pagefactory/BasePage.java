package com.selenium.pagefactory;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;


public abstract class BasePage {

    private static final Logger LOG = LoggerFactory.getLogger(BasePage.class);

    private static String url = "https://wheelsup.com/";
    private static WebDriver driver;
    private static WebDriverWait wait;

    BasePage() {

        driver = com.util.WebDriverManager.getWebDriver();

        PageFactory.initElements(driver, this);
        if (wait == null) {
            wait = new WebDriverWait(driver, 20);
        }
    }

    @Step("Open Url")
    public void openUrl(){
        LOG.info("Opening the following url: " + url);
        driver.get(url);
    }

    @Step("Tear Down")
    public void tearDown(){
        LOG.info("Starting tear down method and closing all browsers");
        driver.quit();
    }

    WebElement waitForElementToBeVisible(WebElement element){
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    WebElement waitForElementToBePresent(By by){
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }


    void moveToElement(WebElement element){
        new Actions(driver).moveToElement(element).perform();
    }

    void scrollToElement(WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    void clickWithJS(WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {view: window, bubbles:true, cancelable: true}))", element);
    }
}
