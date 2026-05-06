package com.automation.utils;

import com.automation.config.ConfigLoader;

public final class TestConstants {
    private TestConstants() {}

    public static String adminEmail()    { return ConfigLoader.getInstance().getAdminEmail(); }
    public static String adminPassword() { return ConfigLoader.getInstance().getAdminPassword(); }
}
