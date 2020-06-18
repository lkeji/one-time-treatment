package com.lkj.onetimetreatment.service;

import com.lkj.onetimetreatment.common.CommonResult;
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
     * 高德地图根据地址转换为经纬度
     * @return 统一返回
     */
    CommonResult getlocalioc(String key, String localName, String city,boolean ischage84);

    CommonResult chage84(String lan, String lon, Integer mapType);
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
