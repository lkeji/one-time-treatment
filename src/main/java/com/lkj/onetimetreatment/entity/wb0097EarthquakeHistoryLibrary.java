package com.lkj.onetimetreatment.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
//@Document标示映射到MongoDB文档上的领域对象(mongobd表名)
@Document
public class wb0097EarthquakeHistoryLibrary {
    //@Id标示某个域为ID域
    @Id
    private String id;
    private String timeOfOnset;//发震时间年
    private String onsetTime;//发震时间月
    private String timeOfEarthquake;//发震时间日
    private String epicenterlat;//震中位置北纬
    private String epicenterlon;//震中位置东经
    private String magnitude;//震级
    //@Indexed标示某个字段为MongoDB的索引字段
    @Indexed
    private String referencePlaceName;   //参考地名
    private String epicentertype;   //地震历史类型:0厦门周边地区 1厦门市历史地震史料 2同安县历史地震史料 3金门县历史地震史料 4历史上厦门邻区主要强震活动对厦门的影响烈度
    private String timeofoccurrence;   //发生时间
    private String earthquakesituation;   //地震情况
    private String historicalsources;   //史料来源



}
