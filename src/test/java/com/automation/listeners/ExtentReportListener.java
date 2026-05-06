package com.automation.listeners;

import com.automation.base.BaseTest;
import com.automation.reports.ExtentReportManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.Base64;

public class ExtentReportListener implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        ExtentReportManager.getInstance();
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentReportManager.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String name = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        String[] groups = result.getMethod().getGroups();

        ExtentTest test;
        synchronized (ExtentReportListener.class) {
            test = ExtentReportManager.getInstance().createTest(name, description);
        }

        for (String group : groups) {
            test.assignCategory(group);
        }
        test.assignDevice(System.getProperty("BROWSER", "chrome"));

        ExtentReportManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().pass("Test passed");
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        test.fail(result.getThrowable());

        String base64 = captureBase64Screenshot(result);
        if (base64 != null) {
            test.fail("Screenshot on failure",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
        }

        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test == null) {
            synchronized (ExtentReportListener.class) {
                test = ExtentReportManager.getInstance()
                        .createTest(result.getMethod().getMethodName());
            }
            ExtentReportManager.setTest(test);
        }
        Throwable cause = result.getThrowable();
        test.skip(cause != null ? cause.getMessage() : "Test skipped");
        ExtentReportManager.removeTest();
    }

    private String captureBase64Screenshot(ITestResult result) {
        WebDriver driver = extractDriver(result);
        if (driver == null) return null;
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    private WebDriver extractDriver(ITestResult result) {
        Object instance = result.getInstance();
        if (!(instance instanceof BaseTest)) return null;
        try {
            Method m = BaseTest.class.getDeclaredMethod("getDriver");
            m.setAccessible(true);
            return (WebDriver) m.invoke(instance);
        } catch (Exception e) {
            return null;
        }
    }
}
