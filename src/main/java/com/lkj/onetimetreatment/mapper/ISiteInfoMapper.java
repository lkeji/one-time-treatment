package com.lkj.onetimetreatment.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author xiaohe
 */
@Mapper
public interface ISiteInfoMapper {

    /**
     * 获取要下载的数据
     *
     * @return 对应数据列表
     */
    List<Map<String, Object>> getDataOfExportExcel();

    /**
     * 添加解析出来的Excel 文件数据
     *
     * @param list 数据列表
     *
     * @return 影响行数
     */
    Integer addMultSiteInfo(List<Map<String, Object>> list);

}
