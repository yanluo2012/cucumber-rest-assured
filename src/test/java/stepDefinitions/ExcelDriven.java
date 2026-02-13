package stepDefinitions;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelDriven {
    public List<String> getData(String testcaseName, String tabName, String fileName) throws IOException {
        DataFormatter formatter = new DataFormatter();
        String excelPath = "/Users/yan.lai/workspace_atlas_v4/cucumber_rest_assured/src/resources/" + fileName;
        String headerName = "Testcases";

        FileInputStream fis = new FileInputStream(excelPath);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        XSSFSheet sheet = workbook.getSheet(tabName);
        if (sheet == null) {
            throw new RuntimeException("Sheet not found: " + tabName);
        }

        System.out.println("Sheet name is " + sheet.getSheetName());

        // 1️⃣ Find column index from header row
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new RuntimeException("Header row is missing");
        }

        int columnIndex = -1;
        for (Cell cell : headerRow) {
            if (headerName.equalsIgnoreCase(formatter.formatCellValue(cell))) {
                columnIndex = cell.getColumnIndex();
                break;
            }
        }

        if (columnIndex == -1) {
            throw new RuntimeException("Column not found: " + headerName);
        }

        System.out.println(headerName + " located at column " + columnIndex);

        // 2️⃣ Find row index directly (no column list needed)
        int targetRowIndex = -1;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Cell cell = row.getCell(columnIndex);
            if (testcaseName.equalsIgnoreCase(formatter.formatCellValue(cell))) {
                targetRowIndex = i;
                break;
            }
        }

        if (targetRowIndex == -1) {
            throw new RuntimeException("Row not found for value: " + testcaseName);
        }

        System.out.println(testcaseName + " row located at " + targetRowIndex);

        // 3️⃣ Read full row data
        Row targetRow = sheet.getRow(targetRowIndex);
        List<String> rowData = new ArrayList<>();

        for (int i = 0; i < targetRow.getLastCellNum(); i++) {
            Cell cell = targetRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            rowData.add(formatter.formatCellValue(cell));
        }

        System.out.println("All data in " + testcaseName + " row: " + rowData);
        return rowData;
    }
}
