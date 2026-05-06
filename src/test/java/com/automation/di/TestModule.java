package com.automation.di;

import com.google.inject.AbstractModule;

/**
 * Single entry-point module for all tests.
 * BaseTest references only this class so individual test classes
 * never need to import WebDriverModule, AppModule, or any Guice type.
 */
public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new WebDriverModule());
        install(new AppModule());
    }
}
