package com.vibe.mapper.spacemodel;

import com.vibe.pojo.spacemodel.TwoDimension;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TwoDimensionEditorMapper {

    int insertTwoDimensionMessage(@Param("name") String name, @Param("filename") String filename);


    TwoDimension findTwoDimensionEditor(@Param("name") String name);


    int updateTwoDimensionMessage(@Param("name") String name, @Param("filename") String filename);

    List<String> findAllName();


    int deleteTwoDimensionEditor(@Param("name") String name);

}
