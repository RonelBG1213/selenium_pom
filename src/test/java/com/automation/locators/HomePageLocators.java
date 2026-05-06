package com.automation.locators;

import org.openqa.selenium.By;

public final class HomePageLocators {
    private HomePageLocators() {}

    public static final By WELCOME_HEADER = By.xpath("//*[contains(@class,'welcome-header')]");
    public static final By NAV_HOME       = By.xpath("//*[@id='nav-home']");
    public static final By NAV_DASHBOARD  = By.xpath("//*[@id='nav-dashboard']");
    public static final By NAVBAR_LOGOUT  = By.xpath("//button[contains(@class,'navbar-logout')]");
}
