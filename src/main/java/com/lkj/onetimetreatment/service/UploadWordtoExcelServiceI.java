package com.lkj.onetimetreatment.service;

import com.lkj.onetimetreatment.common.CommonResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author: likj
 * @create: 2020-05-14 09:35
 * @description:
 * @program: one-time-treatment
 */
public interface UploadWordtoExcelServiceI {

    public CommonResult uploadWordtoExcel(String fileUrl,String url);
}
