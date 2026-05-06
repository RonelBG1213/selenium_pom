package com.automation.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("target/extent-reports/index.html");
            spark.config().setDocumentTitle("Automation Test Report");
            spark.config().setReportName("Selenium POM Framework");
            spark.config().setTheme(Theme.DARK);
            spark.config().setTimelineEnabled(true);

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Framework", "Selenium + TestNG + POM");
            extent.setSystemInfo("Java", System.getProperty("java.version"));
            extent.setSystemInfo("Browser", System.getProperty("BROWSER", "chrome"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
        }
        return extent;
    }

    public static void setTest(ExtentTest test) {
        testThread.set(test);
    }

    public static ExtentTest getTest() {
        return testThread.get();
    }

    public static void removeTest() {
        testThread.remove();
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
