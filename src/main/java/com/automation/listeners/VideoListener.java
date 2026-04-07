package com.automation.listeners;

import com.automation.utils.VideoRecorderUtil;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class VideoListener implements ITestListener {
    @Override
    public void onTestStart(ITestResult result) {
        try {
            VideoRecorderUtil.startRecording(result.getMethod().getMethodName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        try {
            VideoRecorderUtil.stopRecording();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            VideoRecorderUtil.stopRecording();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}