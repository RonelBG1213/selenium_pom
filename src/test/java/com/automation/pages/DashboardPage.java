package com.automation.pages;

import com.automation.locators.DashboardPageLocators;
import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class DashboardPage extends BasePage {

    @Inject
    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public DashboardPage assertUrlContains(String path) {
        wait.until(d -> d.getCurrentUrl().contains(path));
        Assert.assertTrue(driver.getCurrentUrl().contains(path),
                "Expected URL to contain '" + path + "' but was: " + driver.getCurrentUrl());
        return this;
    }

    public HomePage clickHomeLink() {
        waitForClickable(DashboardPageLocators.NAV_HOME).click();
        return new HomePage(driver);
    }
}
