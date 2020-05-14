package com.lkj.onetimetreatment.controller;

import com.lkj.onetimetreatment.common.CommonResult;
import com.lkj.onetimetreatment.service.ISiteInfoServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author: likj
 * @create: 2020-05-14 13:12
 * @description: mongoDB的CRUD
 * @program: one-time-treatment
 */
@RestController
@Api(tags={"mongoDBCURD"})
@RequestMapping("/MongoDBController")
public class MongoDBController {
    private static Logger logger = LoggerFactory.getLogger(MongoDBController.class);
    @Autowired
    private ISiteInfoServiceI siteInfoService;

    @ApiOperation(value = "excel导入到mongoDB")
    @PostMapping(value = "/excelToMongoDB")
    public CommonResult ExcelToMongoDB (@RequestParam("file") MultipartFile file) {
//        CommonResult commonResult = siteInfoService.ExceltoMaogoDb(file);
        CommonResult commonResult = siteInfoService.ExceltoMaogoDb2(file);
        return commonResult;
    }
    @ApiOperation(value = "excel导入")
    @PostMapping(value = "/importExcel")
    public CommonResult importExcel (@RequestParam("file") MultipartFile file) {
        CommonResult commonResult = siteInfoService.importExcel(file);
        return commonResult;
    }

    @ApiOperation(value = "excel导出")
    @GetMapping(value = "/exportExcel", produces = "application/force-download;charset=utf-8")
    public CommonResult exportExcel (Integer myid, String fileName, String excelFormat, HttpServletResponse response) {
        CommonResult commonResult=null;
        // 测试下载文件名为中文名
        // 设置下载框
        response.setContentType("application/force-download");
        try {
            fileName = fileName+"." + excelFormat;
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            // response.addHeader("Content-Disposition","attachment;filename="+URLEncoder.encode(fileName+"." + excelFormat, "UTF-8"));// 设置下载文件名（*+fileName这个值可以定死，下载时会引用这个名字如：”aa.xml“）
            // 处理火狐和Safari浏览器 中文文件名乱码
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            //拿到用户选择的路径
            OutputStream out = response.getOutputStream();
             commonResult = this.siteInfoService.exportExcel(fileName, excelFormat, out);
        } catch (IOException e) {
            logger.error("下载excel后台错误 : " + e.getMessage());
        }
        return commonResult;
    }
}
