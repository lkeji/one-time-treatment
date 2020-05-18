package com.lkj.onetimetreatment.mapper;


import com.lkj.onetimetreatment.po.t_chemical_enterprises;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface tchemicalenterprisesMapper {
    int deleteByPrimaryKey(String id);

    int insert(t_chemical_enterprises record);

    int insertSelective(t_chemical_enterprises record);

    t_chemical_enterprises selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(t_chemical_enterprises record);

    int updateByPrimaryKeyWithBLOBs(t_chemical_enterprises record);

    int updateByPrimaryKey(t_chemical_enterprises record);

    int selectonly22(@Param("companyName") String companyName);

    Integer addMultSiteInfo(List<Map<String, Object>> list);
}