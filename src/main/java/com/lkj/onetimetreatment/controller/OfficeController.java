package com.lkj.onetimetreatment.controller;

import com.lkj.onetimetreatment.common.CommonResult;
import com.lkj.onetimetreatment.service.impl.UploadWordtoExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private UploadWordtoExcelService uploadWordtoExcelI;

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

}
