package com.lkj.onetimetreatment.utils;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author: likj
 * @create: 2020-05-14 09:54
 * @description: excel导出工具类
 * @program: one-time-treatment
 */
public class ExcelExportTools {

    /**
     * 功能描述:基础的excel导出
     * @param
     * @return
     * @author likj
     * @date 2020/5/14 10:23
     */
    public static void excelExport(Map<Integer, List<String>> data, String path, String sheetName) throws Exception {
        WritableWorkbook book = null;
        try {
            book = Workbook.createWorkbook(new File(path));
            WritableSheet sheet = book.createSheet(StringUtils.isBlank(sheetName) ? "sheet1" : sheetName, 0);
            for (int i = 0; i < data.size(); i++) {
                for (int j = 0; j < data.get(i).size(); j++) {
                    System.out.println();
                    sheet.addCell(new Label(j, i, data.get(i).get(j)));
                }
            }
            book.write();
        } finally {
            book.close();
        }
    }


}
