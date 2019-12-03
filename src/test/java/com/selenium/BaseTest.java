package com.selenium;

import com.selenium.pagefactory.HomePage;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public abstract class BaseTest {

    static HomePage homePage;
    @Rule
    public TestName testName = new TestName();


    @BeforeClass
    public static void setUpBeforeClass(){
        homePage = new com.selenium.pagefactory.HomePage();
    }

    void openUrl(){
        homePage.openUrl();
    }

    @AfterClass
    public static void tearDownAfterClass(){
        //captureScreenshot(null);
        homePage.tearDown();
    }

    @Step("Screenshot")
    @Attachment(value = "Page screenshot", type = "image/png")
    private static byte[] captureScreenshot(byte[] screenShot) {
        screenShot = ((TakesScreenshot) com.util.WebDriverManager.getWebDriver()).getScreenshotAs(OutputType.BYTES);

        return  screenShot;
    }
}
