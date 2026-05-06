package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.utils.TestConstants;
import org.testng.annotations.Test;

public class NavigationTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Home page is shown at /home after successful login")
    public void testHomePageAfterLogin() {
        pages.getLoginPage().login(TestConstants.adminEmail(), TestConstants.adminPassword());
        pages.getHomePage()
             .assertUrlContains("/home")
             .assertWelcomeDisplayed();
    }

    @Test(groups = {"regression"}, description = "Dashboard page loads via sidebar link")
    public void testNavigateToDashboard() {
        pages.getLoginPage().login(TestConstants.adminEmail(), TestConstants.adminPassword());
        pages.getHomePage()
             .clickDashboardLink()
             .assertUrlContains("/dashboard");
    }

    @Test(groups = {"regression"}, description = "Home sidebar link navigates back to /home from dashboard")
    public void testNavigateBackToHome() {
        pages.getLoginPage().login(TestConstants.adminEmail(), TestConstants.adminPassword());
        pages.getHomePage()
             .clickDashboardLink()
             .clickHomeLink()
             .assertUrlContains("/home");
    }

    @Test(groups = {"smoke", "regression"}, description = "Sign out button returns user to the login page")
    public void testSignOut() {
        pages.getLoginPage().login(TestConstants.adminEmail(), TestConstants.adminPassword());
        pages.getHomePage().clickSignOut();
        pages.getLoginPage().assertLoginPageLoaded();
    }
}
