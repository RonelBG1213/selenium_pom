package com.automation.locators;

import org.openqa.selenium.By;

public final class RegisterPageLocators {
    private RegisterPageLocators() {}

    public static final By NAME_INPUT      = By.xpath("//*[@id='name']");
    public static final By EMAIL_INPUT     = By.xpath("//*[@id='reg-email']");
    public static final By PASSWORD_INPUT  = By.xpath("//*[@id='reg-password']");
    public static final By CONFIRM_INPUT   = By.xpath("//*[@id='confirm']");
    public static final By REGISTER_BUTTON = By.xpath("//*[@id='register-btn']");
    public static final By ERROR_MESSAGE   = By.xpath("//*[contains(@class,'error-message')]");
    public static final By SIGNIN_LINK     = By.xpath("//a[@href='/']");
}
