package com.automation.listeners;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotListener implements ITestListener {

    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String SCREENSHOTS_DIR = "target/screenshots";

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = extractDriver(result);
        if (driver == null) return;

        try {
            Path dir = Paths.get(SCREENSHOTS_DIR);
            Files.createDirectories(dir);

            String filename = String.format("%s_%s.png",
                    LocalDateTime.now().format(TIMESTAMP),
                    sanitize(result.getName()));

            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(dir.resolve(filename), bytes);

            System.out.printf("[Screenshot] %s/%s%n", SCREENSHOTS_DIR, filename);
        } catch (IOException e) {
            System.err.println("[Screenshot] Failed to save: " + e.getMessage());
        }
    }

    private WebDriver extractDriver(ITestResult result) {
        Object instance = result.getInstance();
        if (!(instance instanceof com.automation.base.BaseTest)) return null;
        try {
            java.lang.reflect.Method m = com.automation.base.BaseTest.class
                    .getDeclaredMethod("getDriver");
            m.setAccessible(true);
            return (WebDriver) m.invoke(instance);
        } catch (Exception e) {
            return null;
        }
    }

    private String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9_\\-]", "_");
    }
}
