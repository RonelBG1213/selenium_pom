package com.automation.di;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.openqa.selenium.WebDriver;

public class WebDriverModule extends AbstractModule {

    @Override
    protected void configure() {
        // Let Guice manage the WebDriverProvider as a true singleton so that
        // @Inject WebDriverProvider in BaseTest receives the same instance
        // as the one backing the WebDriver binding.
        bind(WebDriverProvider.class).in(Scopes.SINGLETON);
        bind(WebDriver.class).toProvider(WebDriverProvider.class);
    }
}
