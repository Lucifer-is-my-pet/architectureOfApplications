package main.java.modules.HSSFWorkbookGenerator;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/*
* Класс для работы с Excel-файлами
*/
public class HSSFWorkbookGenerator {

    private HSSFWorkbook workbook;
    private ArrayList<HSSFSheet> sheets;
    private int rowsCount = 0;

    public HSSFWorkbookGenerator(String[] sheetsNames) {
        this.workbook = new HSSFWorkbook();
        this.sheets = new ArrayList<>();
        for (String sheetsName : sheetsNames) {
            this.sheets.add(workbook.createSheet(sheetsName));
        }
    }

    public void createRow(String[] cells, int sheetNumber) {
        HSSFRow row = this.sheets.get(sheetNumber).createRow((short)this.rowsCount);
        this.rowsCount += 1;

        for (int i = 0; i < cells.length; i++) {
            row.createCell(i).setCellValue(cells[i]);
        }
    }

    public void write(OutputStream stream) throws IOException {
        workbook.write(stream);
    }

    public void close() throws IOException {
        workbook.close();
    }
}
