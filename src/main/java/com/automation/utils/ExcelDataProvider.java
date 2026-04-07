package com.automation.utils;

import org.apache.poi.ss.usermodel.*;
import org.testng.annotations.DataProvider;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ExcelDataProvider {
    @DataProvider(name = "excelData")
    public static Object[][] getExcelData(Method m) throws IOException {
        String filePath = "src/test/resources/testdata/" + m.getName() + ".xlsx";
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

            String[] headers = new String[colCount];
            for (int i = 0; i < colCount; i++) {
                headers[i] = sheet.getRow(0).getCell(i).getStringCellValue();
            }

            Object[][] data = new Object[rowCount - 1][1];
            for (int i = 1; i < rowCount; i++) {
                Map<String, String> rowMap = new HashMap<>();
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    String value = (cell != null) ? cell.toString() : "";
                    rowMap.put(headers[j], value);
                }
                data[i - 1][0] = rowMap;
            }
            return data;
        }
    }
}