package com.automation.pages;

import com.automation.locators.RegisterPageLocators;
import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

public class RegisterPage extends BasePage {

    @Inject
    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public RegisterPage enterName(String name) {
        waitForVisible(RegisterPageLocators.NAME_INPUT).clear();
        driver.findElement(RegisterPageLocators.NAME_INPUT).sendKeys(name);
        return this;
    }

    public RegisterPage enterEmail(String email) {
        waitForVisible(RegisterPageLocators.EMAIL_INPUT).clear();
        driver.findElement(RegisterPageLocators.EMAIL_INPUT).sendKeys(email);
        return this;
    }

    public RegisterPage enterPassword(String password) {
        waitForVisible(RegisterPageLocators.PASSWORD_INPUT).clear();
        driver.findElement(RegisterPageLocators.PASSWORD_INPUT).sendKeys(password);
        return this;
    }

    public RegisterPage enterConfirmPassword(String confirm) {
        waitForVisible(RegisterPageLocators.CONFIRM_INPUT).clear();
        driver.findElement(RegisterPageLocators.CONFIRM_INPUT).sendKeys(confirm);
        return this;
    }

    public RegisterPage clickRegister() {
        waitForClickable(RegisterPageLocators.REGISTER_BUTTON).click();
        return this;
    }

    public boolean isErrorDisplayed() {
        try {
            return waitForVisible(RegisterPageLocators.ERROR_MESSAGE).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        return waitForVisible(RegisterPageLocators.ERROR_MESSAGE).getText();
    }

    public RegisterPage assertRegisterPageLoaded() {
        waitForVisible(RegisterPageLocators.REGISTER_BUTTON);
        return this;
    }

    public RegisterPage register(String name, String email, String password, String confirm) {
        return enterName(name)
                .enterEmail(email)
                .enterPassword(password)
                .enterConfirmPassword(confirm)
                .clickRegister();
    }

    public RegisterPage assertOutcome(String expectedResult, String expectedError) {
        if ("success".equalsIgnoreCase(expectedResult)) {
            wait.until(d -> !d.getCurrentUrl().contains("/register"));
        } else {
            SoftAssert soft = new SoftAssert();
            boolean errorShown = isErrorDisplayed();
            soft.assertTrue(errorShown, "Expected an error message but none was displayed.");
            if (errorShown && expectedError != null && !expectedError.isEmpty()) {
                soft.assertTrue(getErrorMessage().contains(expectedError),
                        "Expected error '" + expectedError + "' but got: " + getErrorMessage());
            }
            soft.assertAll();
        }
        return this;
    }
}
