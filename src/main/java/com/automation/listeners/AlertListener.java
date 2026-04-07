package com.automation.listeners;

import com.automation.utils.EmailNotifier;
import com.automation.utils.SlackNotifier;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AlertListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMsg = result.getThrowable().getMessage();
        String message = "❌ Test Failed: " + testName + "\nError: " + errorMsg;
        SlackNotifier.sendMessage(message);
        EmailNotifier.sendEmail("Automation Test Failure", message);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String message = "✅ Test Passed: " + result.getMethod().getMethodName();
        SlackNotifier.sendMessage(message); // optional
    }
}