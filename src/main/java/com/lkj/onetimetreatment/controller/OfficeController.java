package com.lkj.onetimetreatment.controller;

import com.lkj.onetimetreatment.common.CommonResult;
import com.lkj.onetimetreatment.service.ISiteInfoServiceI;
import com.lkj.onetimetreatment.service.impl.UploadWordtoExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * @create: 2020-05-14 09:23
 * @description: 单个word转换为excel表格
 * @program: one-time-treatment
 */
@RestController
@Api(tags={"word和excel之间的转换"})
@RequestMapping("/OfficeController")
public class OfficeController {
        private static Logger logger = LoggerFactory.getLogger(OfficeController.class);
    @Autowired
    private UploadWordtoExcelService uploadWordtoExcelI;

        @Autowired
    private ISiteInfoServiceI siteInfoService;

    @ApiOperation("单个word转换为excel表格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileUrl",value = "要转换的文件路径+名称",paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "excelUrl",value = "excel的路径+名称",paramType = "query",dataType = "string")
    })
    @RequestMapping(value = "/uploadWordtoExcel", method = RequestMethod.GET)
    public CommonResult uploadWordtoExcel( @RequestParam(value = "fileUrl")  String fileUrl,  @RequestParam(value = "excelUrl")  String excelUrl) {
        CommonResult commonResult = uploadWordtoExcelI.uploadWordtoExcel(fileUrl,excelUrl);
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
    @ApiOperation(value = "高德地图根据地址转换为经纬度")
    @GetMapping(value = "/getlocalioc")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "高德KEY", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "localName", value = "要转换的名字或者地址", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "city", value = "所在城市", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "ischage84", value = "是否要转成天地图（84坐标）", required = true, dataType = "string", paramType = "query")
    })
    public CommonResult getlocalioc (String key, String localName, String city,boolean ischage84) {
        CommonResult commonResult = siteInfoService.getlocalioc(key,localName,city,ischage84);
        return commonResult;
    }
    @ApiOperation(value = "地图坐标转为天地图（84坐标）")
    @GetMapping(value = "/chage84")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lan", value = "经度", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "lon", value = "纬度", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "lanAndlon", value = "格式：经度，纬度", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "mapType", value = "地图类型1高德，2百度", required = true, dataType = "int", paramType = "query")
    })
    public CommonResult chage84 (String lan, String lon,String lanAndlon, Integer mapType) {
        String [] ss=lanAndlon.split(",");
        lan=ss[0];
        lon=ss[1];
        CommonResult commonResult = siteInfoService.chage84(lan,lon,mapType);
        return commonResult;
    }




}
