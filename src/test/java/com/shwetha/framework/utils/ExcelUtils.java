
package com.shwetha.framework.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    public static Object[][] readSheet(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new RuntimeException("Sheet not found: " + sheetName);

            Iterator<Row> rowIterator = sheet.iterator();
            if (!rowIterator.hasNext()) return new Object[0][0];
            Row headerRow = rowIterator.next();

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }

            List<Object[]> data = new ArrayList<>();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row == null) continue;
                Object[] rowData = new Object[headers.size()];
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData[i] = getCellValue(cell);
                }
                data.add(rowData);
            }
            Object[][] arr = new Object[data.size()][];
            for (int i = 0; i < data.size(); i++) {
                arr[i] = data.get(i);
            }
            return arr;
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel: " + e.getMessage(), e);
        }
    }

    // ---- NEW: header-based reader returning Object[][] where each row is Map<String,String> ----
    public static Object[][] readSheetAsMap(String filePath, String sheetName, boolean onlyExecuteY) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new RuntimeException("Sheet not found: " + sheetName);

            DataFormatter fmt = new DataFormatter();
            FormulaEvaluator eval = workbook.getCreationHelper().createFormulaEvaluator();

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) return new Object[0][0];

            int colCount = headerRow.getLastCellNum();
            List<String> headers = new ArrayList<>(colCount);
            for (int c = 0; c < colCount; c++) {
                String key = fmt.formatCellValue(headerRow.getCell(c));
                headers.add(key == null ? "" : key.trim());
            }

            List<Object[]> out = new ArrayList<>();
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                // skip completely blank rows
                boolean allBlank = true;
                for (int c = 0; c < colCount; c++) {
                    Cell cell = row.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell != null && cell.getCellType() != CellType.BLANK) { allBlank = false; break; }
                }
                if (allBlank) continue;

                Map<String,String> map = new LinkedHashMap<>();
                for (int c = 0; c < colCount; c++) {
                    String key = headers.get(c);
                    Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String val = fmt.formatCellValue(cell, eval);
                    map.put(key, val == null ? "" : val.trim());
                }

                if (onlyExecuteY) {
                    String exe = map.getOrDefault("Execute", "");
                    if (!"Y".equalsIgnoreCase(exe)) continue;
                }
                out.add(new Object[]{ map });
            }
            return out.toArray(new Object[0][]);
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel: " + e.getMessage(), e);
        }
    }

    private static Object getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue();
                } else {
                    yield (int) cell.getNumericCellValue();
                }
            }
            case BOOLEAN -> cell.getBooleanCellValue();
            case FORMULA -> cell.getCellFormula();
            case BLANK, _NONE, ERROR -> "";
        };
    }
}