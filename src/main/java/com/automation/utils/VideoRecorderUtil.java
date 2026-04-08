package com.automation.utils;

import java.io.File;

public class VideoRecorderUtil {
    private static boolean recording;
    private static String currentTestName;

    public static void startRecording(String testName) {
        File directory = new File("test-output/videos");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        currentTestName = testName;
        recording = true;
        System.out.println("Video recording placeholder started for: " + currentTestName);
    }

    public static void stopRecording() {
        if (recording) {
            System.out.println("Video recording placeholder stopped for: " + currentTestName);
        }
        recording = false;
        currentTestName = null;
    }
}
