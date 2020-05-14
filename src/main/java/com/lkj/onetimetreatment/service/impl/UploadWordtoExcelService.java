package com.lkj.onetimetreatment.service.impl;

import com.lkj.onetimetreatment.common.CommonResult;
import com.lkj.onetimetreatment.service.UploadWordtoExcelServiceI;
import com.lkj.onetimetreatment.utils.ExcelExportTools;
import com.lkj.onetimetreatment.utils.FormatConversionTools;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: likj
 * @create: 2020-05-14 09:36
 * @description:
 * @program: one-time-treatment
 */
@Service
public class UploadWordtoExcelService implements UploadWordtoExcelServiceI {

    @Override
    public CommonResult uploadWordtoExcel(String fileUrl,String url) {
        //创建文件
        File file = new File(fileUrl);;
        Map<Integer, List<String>> maps = new HashMap();
        String filename = file.getName();
        try {
            FileInputStream in = new FileInputStream(file);//载入文档 //如果是office2007  docx格式
            if (filename.toLowerCase().endsWith("doc")) {
                POIFSFileSystem pfs = new POIFSFileSystem(in);
                HWPFDocument hwpf = new HWPFDocument(pfs);
                Range range = hwpf.getRange();
                TableIterator it = new TableIterator(range);
                String[] item = {};
                //迭代文档中的表格
                while (it.hasNext()) {
                    Table tb = (Table) it.next();
                    //迭代行，默认从0开始
                    for (int i = 0; i < tb.numRows(); i++) {
                        TableRow tr = tb.getRow(i);
                        List<String> list = new ArrayList<>();
                        //迭代列，默认从0开始
                        for (int j = 0; j < tr.numCells(); j++) {
                            if (FormatConversionTools.formatText(tb.getRow(i).getCell(j)).contains("；")){
                                list.add(FormatConversionTools.formatText(tb.getRow(i).getCell(j)).replace("；", ";"));
                            }else{
                                list.add(FormatConversionTools.formatText(tb.getRow(i).getCell(j)));
                            }
                        }
                        maps.put(i, list);
                    }
                }
            }
            ExcelExportTools.excelExport(maps, url, "");
        } catch (Exception e) {
            System.err.println(filename);
            e.printStackTrace();
        }
        return CommonResult.success(null);
    }
}
