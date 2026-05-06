package com.automation.base;

import com.automation.config.ConfigLoader;
import com.automation.di.TestModule;
import com.automation.di.WebDriverProvider;
import com.automation.driver.DriverFactory;
import com.automation.pages.PageManager;
import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;

import java.time.Duration;

@Guice(modules = TestModule.class)
public abstract class BaseTest {

    @Inject private WebDriverProvider driverProvider;
    @Inject private ConfigLoader config;

    /** Available to every test class — no @Inject or DI imports needed there. */
    @Inject protected PageManager pages;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriver driver = DriverFactory.createDriver(config.getBrowser(), config.isHeadless());
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWaitSeconds()));
        driver.manage().window().maximize();
        driver.get(config.getBaseUrl());
        driverProvider.set(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driverProvider.quit();
    }

    protected WebDriver getDriver() {
        return driverProvider.get();
    }
}
