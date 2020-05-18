package com.lkj.onetimetreatment.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
//@Document标示映射到MongoDB文档上的领域对象(mongobd表名)
@Document
public class wb0097enterpriseDataExpand {
    //@Id标示某个域为ID域
    @Id
    private String id;
    private String normalre;//正常储量
    private String ismajorso;//是否重大危险源
    private String numbertan;//液氨储罐数量
    private String mstcla;//液氨最大单罐容量
    private String iswirtmhd;//是否涉及两类重大隐患整改
    private String remk;//备注
    private String mysqlid;//mysqlID




}
