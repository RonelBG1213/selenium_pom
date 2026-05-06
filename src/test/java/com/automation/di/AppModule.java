package com.automation.di;

import com.automation.config.ConfigLoader;
import com.automation.pages.PageManager;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        // ConfigLoader manages its own singleton; expose the existing instance to Guice.
        bind(ConfigLoader.class).toInstance(ConfigLoader.getInstance());

        // PageManager is singleton — one per injector, providers inside are lazily called.
        bind(PageManager.class).in(Scopes.SINGLETON);

        // Note: TestDataLoader has only static methods and a private constructor.
        // It is NOT bound here — call TestDataLoader.loadList(...) directly in tests.
    }
}
