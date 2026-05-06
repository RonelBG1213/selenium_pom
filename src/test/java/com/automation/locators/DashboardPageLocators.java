package com.automation.locators;

import org.openqa.selenium.By;

public final class DashboardPageLocators {
    private DashboardPageLocators() {}

    public static final By DASHBOARD_HEADER = By.xpath("//*[contains(@class,'welcome-header')]");
    public static final By NAV_HOME         = By.xpath("//*[@id='nav-home']");
    public static final By NAV_DASHBOARD    = By.xpath("//*[@id='nav-dashboard']");
}
