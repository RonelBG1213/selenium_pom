package com.automation.pages;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Central accessor for all page objects.
 * Inject this into test classes instead of individual Provider<PageX> fields.
 *
 * Each getter calls provider.get(), which triggers Guice to construct the page
 * with the WebDriver currently stored in the thread-local slot — safe for
 * parallel test execution.
 *
 * To add a new page:
 *   1. Add a Provider<YourPage> field and inject it in the constructor.
 *   2. Expose a getYourPage() method.
 */
@Singleton
public class PageManager {

    private final Provider<LoginPage>     loginPageProvider;
    private final Provider<RegisterPage>  registerPageProvider;
    private final Provider<HomePage>      homePageProvider;
    private final Provider<DashboardPage> dashboardPageProvider;

    @Inject
    public PageManager(
            Provider<LoginPage>     loginPageProvider,
            Provider<RegisterPage>  registerPageProvider,
            Provider<HomePage>      homePageProvider,
            Provider<DashboardPage> dashboardPageProvider) {
        this.loginPageProvider     = loginPageProvider;
        this.registerPageProvider  = registerPageProvider;
        this.homePageProvider      = homePageProvider;
        this.dashboardPageProvider = dashboardPageProvider;
    }

    public LoginPage     getLoginPage()     { return loginPageProvider.get(); }
    public RegisterPage  getRegisterPage()  { return registerPageProvider.get(); }
    public HomePage      getHomePage()      { return homePageProvider.get(); }
    public DashboardPage getDashboardPage() { return dashboardPageProvider.get(); }
}
