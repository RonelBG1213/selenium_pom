package com.automation.locators;

import org.openqa.selenium.By;

public final class LoginPageLocators {
    private LoginPageLocators() {}

    public static final By USERNAME_INPUT = By.xpath("//*[@id='username']");
    public static final By PASSWORD_INPUT = By.xpath("//*[@id='password']");
    public static final By LOGIN_BUTTON   = By.xpath("//*[@id='login-btn']");
    public static final By ERROR_MESSAGE  = By.xpath("//*[contains(@class,'error-message')]");
    public static final By WELCOME_HEADER = By.xpath("//*[contains(@class,'welcome-header')]");
    public static final By REGISTER_LINK  = By.xpath("//a[@href='/register']");
}
