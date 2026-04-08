package com.automation.listeners;

import com.automation.utils.ConfigReader;
import com.automation.utils.VideoRecorderUtil;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class VideoListener implements ITestListener {
    @Override
    public void onTestStart(ITestResult result) {
        if (!ConfigReader.isHeadless()) {
            try {
                VideoRecorderUtil.startRecording(result.getMethod().getMethodName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Headless mode - video recording disabled.");
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (!ConfigReader.isHeadless()) {
            try {
                VideoRecorderUtil.stopRecording();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (!ConfigReader.isHeadless()) {
            try {
                VideoRecorderUtil.stopRecording();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
