package com.example.getposition.mapper;

import com.example.getposition.entity.GetPosition;

import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface GetPositionMapper {

    @Select("select * from entity_db.entity")
    List<GetPosition> GetPositionALl();

}
