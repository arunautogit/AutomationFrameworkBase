package com.automation.utils;

import okhttp3.*;
import java.io.IOException;

public class SlackNotifier {
    private static String webhookUrl = ConfigReader.getSlackWebhook();

    public static void sendMessage(String message) {
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            System.err.println("Slack webhook URL not configured");
            return;
        }
        OkHttpClient client = new OkHttpClient();
        String json = "{\"text\":\"" + message + "\"}";
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(webhookUrl)
                .post(body)
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}