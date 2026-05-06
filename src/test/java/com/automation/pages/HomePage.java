package com.automation.pages;

import com.automation.locators.HomePageLocators;
import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class HomePage extends BasePage {

    @Inject
    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isWelcomeDisplayed() {
        try {
            return waitForVisible(HomePageLocators.WELCOME_HEADER).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public DashboardPage clickDashboardLink() {
        waitForClickable(HomePageLocators.NAV_DASHBOARD).click();
        return new DashboardPage(driver);
    }

    public HomePage clickHomeLink() {
        waitForClickable(HomePageLocators.NAV_HOME).click();
        return this;
    }

    public void clickSignOut() {
        waitForClickable(HomePageLocators.NAVBAR_LOGOUT).click();
    }

    public HomePage assertUrlContains(String path) {
        wait.until(d -> d.getCurrentUrl().contains(path));
        Assert.assertTrue(driver.getCurrentUrl().contains(path),
                "Expected URL to contain '" + path + "' but was: " + driver.getCurrentUrl());
        return this;
    }

    public HomePage assertWelcomeDisplayed() {
        Assert.assertTrue(isWelcomeDisplayed(), "Welcome header was not displayed.");
        return this;
    }
}
