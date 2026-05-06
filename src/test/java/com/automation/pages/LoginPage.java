package com.automation.pages;

import com.automation.locators.LoginPageLocators;
import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class LoginPage extends BasePage {

    @Inject
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage enterUsername(String username) {
        waitForVisible(LoginPageLocators.USERNAME_INPUT).clear();
        driver.findElement(LoginPageLocators.USERNAME_INPUT).sendKeys(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        waitForVisible(LoginPageLocators.PASSWORD_INPUT).clear();
        driver.findElement(LoginPageLocators.PASSWORD_INPUT).sendKeys(password);
        return this;
    }

    public LoginPage clickLogin() {
        waitForClickable(LoginPageLocators.LOGIN_BUTTON).click();
        return this;
    }

    public String getErrorMessage() {
        return waitForVisible(LoginPageLocators.ERROR_MESSAGE).getText();
    }

    public boolean isWelcomeDisplayed() {
        try {
            return waitForVisible(LoginPageLocators.WELCOME_HEADER).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public LoginPage login(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }

    public LoginPage assertOutcome(String expectedResult) {
        if ("success".equalsIgnoreCase(expectedResult)) {
            wait.until(d -> d.getCurrentUrl().contains("/home"));
            Assert.assertTrue(driver.getCurrentUrl().contains("/home"),
                    "Expected URL to contain '/home' after login but was: " + driver.getCurrentUrl());
            Assert.assertTrue(isWelcomeDisplayed(),
                    "Expected welcome header on home page but it was not displayed.");
        } else {
            Assert.assertTrue(driver.getCurrentUrl().endsWith("/") || driver.getCurrentUrl().endsWith("3000"),
                    "Expected to stay on login page but URL was: " + driver.getCurrentUrl());
            Assert.assertFalse(getErrorMessage().isEmpty(),
                    "Expected an error message on failed login but none was displayed.");
        }
        return this;
    }

    public boolean isRegisterLinkVisible() {
        try {
            return waitForVisible(LoginPageLocators.REGISTER_LINK).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public RegisterPage clickRegisterLink() {
        waitForClickable(LoginPageLocators.REGISTER_LINK).click();
        return new RegisterPage(driver);
    }

    public LoginPage assertLoginPageLoaded() {
        waitForVisible(LoginPageLocators.USERNAME_INPUT);
        return this;
    }
}
