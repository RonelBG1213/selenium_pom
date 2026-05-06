package com.automation.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRIES = 2;
    private int count = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (count < MAX_RETRIES) {
            count++;
            System.out.printf("[Retry] %s — attempt %d of %d%n",
                    result.getName(), count, MAX_RETRIES);
            return true;
        }
        return false;
    }
}
