package com.lkj.onetimetreatment.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
//@Document标示映射到MongoDB文档上的领域对象(mongobd表名)
@Document
public class wb0097EarthquakeMonitoring {
    //@Id标示某个域为ID域
    @Id
    private String id;
    //@Indexed标示某个字段为MongoDB的索引字段
    @Indexed
    private String stationname;//台站名称
    private String adrs;//详细地址
    private String prna;//负责人
    private String erar;//占地面积
    private String cede;//所属单位
    private String numbering;//编号
    private String loc;//经纬度
    private String stationtype;//站台类型
}
