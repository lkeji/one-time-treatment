package com.lkj.onetimetreatment.service;

import com.lkj.onetimetreatment.common.CommonResult;
import com.lkj.onetimetreatment.entity.wb0097EarthquakeMonitoring;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

/**
 * @author xiaohe
 */
public interface ISiteInfoServiceI  {

    /**
     * 导入 Excel
     *
     * @param file 文件流
     * @return 统一返回
     */
    CommonResult importExcel(MultipartFile file);
    /**
     * excel导入mysql
     * */
    CommonResult ExceltoMysql(MultipartFile file);
    /**
     * excel导入到mongoDB
     * */
    CommonResult ExceltoMaogoDb(MultipartFile file);
    /**
     * 去除一行和第一列
     * */
    CommonResult ExceltoMaogoDb2(MultipartFile file);

    /**
     * 导出 Excel
     *
     * @param fileName    文件名称
     * @param excelFormat Excel 格式
     * @param fileOut     输出流
     * @return 统一返回
     */
    CommonResult exportExcel( String fileName, String excelFormat, OutputStream fileOut);

}
