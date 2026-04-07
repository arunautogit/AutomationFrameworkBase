package com.automation.utils;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReportAggregator {
    public static void main(String[] args) throws Exception {
        String testOutputDir = args.length > 0 ? args[0] : "test-output";
        String outputDir = args.length > 1 ? args[1] : "test-output/weekly_monthly_reports";
        new File(outputDir).mkdirs();

        // For real aggregation, you would read multiple historical XML files.
        // Here we read the latest testng-results.xml for demo.
        File resultsFile = new File(testOutputDir, "testng-results.xml");
        if (!resultsFile.exists()) {
            System.out.println("No testng-results.xml found");
            return;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(resultsFile);
        NodeList testNodes = doc.getElementsByTagName("test-method");

        Map<String, Integer> statusCounts = new HashMap<>();
        statusCounts.put("PASS", 0);
        statusCounts.put("FAIL", 0);
        statusCounts.put("SKIP", 0);

        for (int i = 0; i < testNodes.getLength(); i++) {
            Element method = (Element) testNodes.item(i);
            String status = method.getAttribute("status");
            if (status.equals("PASS")) statusCounts.put("PASS", statusCounts.get("PASS") + 1);
            else if (status.equals("FAIL")) statusCounts.put("FAIL", statusCounts.get("FAIL") + 1);
            else if (status.equals("SKIP")) statusCounts.put("SKIP", statusCounts.get("SKIP") + 1);
        }

        String weeklyHtml = generateHtmlReport("Weekly Report (Last 7 days)", statusCounts);
        Files.write(Paths.get(outputDir, "weekly_report.html"), weeklyHtml.getBytes());

        String monthlyHtml = generateHtmlReport("Monthly Report (Last 30 days)", statusCounts);
        Files.write(Paths.get(outputDir, "monthly_report.html"), monthlyHtml.getBytes());

        System.out.println("Reports generated at " + outputDir);
    }

    private static String generateHtmlReport(String title, Map<String, Integer> counts) {
        int total = counts.values().stream().mapToInt(Integer::intValue).sum();
        double passRate = total > 0 ? (counts.get("PASS") * 100.0 / total) : 0;
        return "<html><head><title>" + title + "</title></head><body>" +
                "<h1>" + title + "</h1>" +
                "<p>Total Tests: " + total + "</p>" +
                "<p>Passed: " + counts.get("PASS") + " (" + String.format("%.2f", passRate) + "%)</p>" +
                "<p>Failed: " + counts.get("FAIL") + "</p>" +
                "<p>Skipped: " + counts.get("SKIP") + "</p>" +
                "</body></html>";
    }
}