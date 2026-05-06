package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.utils.TestConstants;
import com.automation.utils.TestDataLoader;
import com.automation.utils.model.LoginData;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @DataProvider(name = "loginData", parallel = true)
    public Object[][] loginData() {
        return TestDataLoader.toDataProvider("login_data.json", LoginData.class);
    }

    @Test(groups = {"smoke", "regression"},
          description = "Successful login with credentials from .env navigates to home page")
    public void testLoginSuccess() {
        pages.getLoginPage()
             .login(TestConstants.adminEmail(), TestConstants.adminPassword())
             .assertOutcome("success");
    }

    @Test(dataProvider = "loginData", groups = {"regression"},
          description = "Invalid credentials show an error and stay on login page")
    public void testLoginFailure(LoginData loginData) {
        pages.getLoginPage()
             .login(loginData.getUsername(), loginData.getPassword())
             .assertOutcome(loginData.getExpectedResult());
    }
}
