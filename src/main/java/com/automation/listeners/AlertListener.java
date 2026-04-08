package com.automation.listeners;

import com.automation.utils.EmailNotifier;
import com.automation.utils.SlackNotifier;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AlertListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMsg = result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown error";
        String message = "Test Failed: " + testName + "\nError: " + errorMsg;

        try {
            SlackNotifier.sendMessage(message);
        } catch (Exception e) {
            System.err.println("Slack notification failed: " + e.getMessage());
        }

        try {
            EmailNotifier.sendEmail("Automation Test Failure", message);
        } catch (Exception e) {
            System.err.println("Email notification failed: " + e.getMessage());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Optional: send success message (commented to reduce noise)
        // String message = "Test Passed: " + result.getMethod().getMethodName();
        // SlackNotifier.sendMessage(message);
    }
}
