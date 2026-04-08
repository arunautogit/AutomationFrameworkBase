package com.automation.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class JsonDataProvider {
    @DataProvider(name = "jsonData")
    public static Object[][] getJsonData(Method m) throws IOException {
        String filePath = resolveFilePath(m, "json");
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> testData = mapper.readValue(new File(filePath), List.class);
        Object[][] data = new Object[testData.size()][1];
        for (int i = 0; i < testData.size(); i++) {
            data[i][0] = testData.get(i);
        }
        return data;
    }

    private static String resolveFilePath(Method method, String extension) {
        String basePath = "src/test/resources/testdata/";
        String[] candidates = {
                basePath + method.getName() + "." + extension,
                basePath + method.getDeclaringClass().getSimpleName() + "." + extension,
                basePath + "loginTest." + extension
        };

        for (String candidate : candidates) {
            if (new File(candidate).exists()) {
                return candidate;
            }
        }

        throw new IllegalArgumentException("No test data file found for method: " + method.getName());
    }
}
