package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.utils.TestDataLoader;
import com.automation.utils.model.RegisterData;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RegisterTest extends BaseTest {

    @DataProvider(name = "registerData")
    public Object[][] registerData() {
        return TestDataLoader.toDataProvider("register_data.json", RegisterData.class);
    }

    @Test(groups = {"smoke", "regression"}, description = "Register link is visible on the login page")
    public void testRegisterLinkVisible() {
        Assert.assertTrue(
                pages.getLoginPage().isRegisterLinkVisible(),
                "Register link should be visible on the login page.");
    }

    @Test(groups = {"smoke", "regression"}, description = "Clicking register link navigates to the registration page")
    public void testNavigateToRegisterPage() {
        pages.getLoginPage()
             .clickRegisterLink()
             .assertRegisterPageLoaded();
    }

    @Test(dataProvider = "registerData", groups = {"regression"}, description = "Verify registration form behavior")
    public void testRegistration(RegisterData data) {
        pages.getLoginPage()
             .clickRegisterLink()
             .register(data.getName(), data.getUsername(), data.getPassword(), data.getConfirmPassword())
             .assertOutcome(data.getExpectedResult(), data.getExpectedError());
    }
}
