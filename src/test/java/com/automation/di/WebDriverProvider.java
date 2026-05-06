package com.automation.di;

import com.google.inject.Provider;
import org.openqa.selenium.WebDriver;

/**
 * Thread-local WebDriver provider.
 * Scope (singleton) is declared in WebDriverModule, not on this class,
 * so Guice controls the lifecycle and @Inject fields are honoured.
 */
public class WebDriverProvider implements Provider<WebDriver> {

    private static final ThreadLocal<WebDriver> holder = new ThreadLocal<>();

    @Override
    public WebDriver get() {
        WebDriver driver = holder.get();
        if (driver == null) {
            throw new IllegalStateException(
                "WebDriver not initialised for this thread. " +
                "Ensure BaseTest.setUp() has been called before accessing the driver."
            );
        }
        return driver;
    }

    public void set(WebDriver driver) {
        holder.set(driver);
    }

    public void quit() {
        WebDriver driver = holder.get();
        if (driver != null) {
            driver.quit();
            holder.remove();
        }
    }
}
