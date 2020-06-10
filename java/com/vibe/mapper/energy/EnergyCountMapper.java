package com.vibe.mapper.energy;

import com.vibe.pojo.energy.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface EnergyCountMapper {
    List<EnergyCountOne> queryEnergyCountByProbeId(@Param("type") Integer type, @Param("date") String date, @Param("parent") Integer parent);

    String queryEnergyCountByProbeIdUnit(@Param("lightList") List lightList, @Param("date") String date, @Param("idParentList") List idParentList);

    String queryEnergyCountByProbeIdFloor(@Param("lightList") List lightList, @Param("date") String date, @Param("idParentList") List idParentList);

    List<EnergyCountOne> queryNightEnergyCountByProbeId(@Param("lightList") List lightList, @Param("sTime") String sTime, @Param("eTime") String eTime, @Param("parent") Integer parent);

    Double queryEnergyCountByProbeIdAvg(@Param("type") Integer type, @Param("date") String date, @Param("parent") Integer parent);

    List<Integer> queryEnergyCountByMonitorId(@Param("type") Integer type);

    List<Integer> queryEnergyCountByProbeIdParent(@Param("parent") Integer parent, @Param("lou") Integer lou);

    List<EnergyCountOne> queryNightEnergyCountByProbeIdAvg(@Param("lightList") List lightList, @Param("sTime") String sTime, @Param("eTime") String eTime, @Param("parent") Integer parent);


    List<EnergyCountOne> queryEnergyCountByIdOfHour(@Param("type") Integer type, @Param("startTime") String startTime, @Param("parent") Integer parent, @Param("firstDayNext") String firstDayNext);

    Double queryEnergyCountByProbeIdAvgOfHour(@Param("lightList") List lightList, @Param("date") String date, @Param("parent") Integer parent);


    List<Energy> getEnergyAllRank(
            @Param("redioType") int redioType,
            @Param("catalog") int catalog,
            @Param("startTime") String startTime,
            @Param("lastTime") String lastTime,
            @Param("tableName") String tableName
    );

    List<Energy> getEnergySpaceBiao(
            @Param("redioType") int redioType,
            @Param("catalog") int catalog,
            @Param("startTime") String startTime,
            @Param("lastTime") String lastTime,
            @Param("tableName") String tableName,
            @Param("id") Integer id
    );

    int getParentCatalog(@Param("catalog") int catalog);

    Double getEnergySum(@Param("parentCatalog") int parentCatalog,
                        @Param("redioType") int redioType,
                        @Param("startTime") String startTime,
                        @Param("lastTime") String lastTime,
                        @Param("tableName") String tableName);

    List<Energy> getEnergyA3Floorid();

    List<ComSeqEnergy> getEnergyComAndSeq(
            @Param("redioType") int redioType,
            @Param("catalog") int catalog,
            @Param("startTime") String startTime,
            @Param("lastTime") String lastTime,
            @Param("tableName") String tableName,
            @Param("floorId") Integer floorId
    );

    String getUnit(@Param("catalog") int catalog);

    List<Energy> getEnergyType(
            @Param("redioType") int redioType,
            @Param("catalog") int catalog,
            @Param("startTime") String startTime,
            @Param("lastTime") String lastTime,
            @Param("tableName") String tableName,
            @Param("floorId") Integer floorId
    );

    List<Double> getEnergyTypeSum(
            @Param("redioType") int redioType,
            @Param("itemize_type") int itemize_type,
            @Param("startTime") String startTime,
            @Param("lastTime") String lastTime,
            @Param("tableName") String tableName,
            @Param("floorId") Integer floorId
    );

    List<Energy> getEnergyFenXiangTu(
            @Param("redioType") int redioType,
            @Param("catalog") int catalog,
            @Param("startTime") String startTime,
            @Param("lastTime") String lastTime,
            @Param("tableName") String tableName,
            @Param("floor") Integer floor
    );

    List<EnergySub> getEnergyFenXiangBiao(
            @Param("redioType") int redioType,
            @Param("catalog") int catalog,
            @Param("startTime") String startTime,
            @Param("lastTime") String lastTime,
            @Param("tableName") String tableName,
            @Param("floor") Integer floor
    );

    List<EnergyCount> getEnergyFenShiTu(
            @Param("redioType") int redioType,
            @Param("catalog") int catalog,
            @Param("startTime") String startTime,
            @Param("lastTime") String lastTime,
            @Param("tableName") String tableName,
            @Param("floor") Integer floor
    );

    List<EnergyBiao> getEnergyFenShiBiao(
            @Param("redioType") int redioType,
            @Param("catalog") int catalog,
            @Param("startTime") String startTime,
            @Param("lastTime") String lastTime,
            @Param("tableName") String tableName,
            @Param("floor") Integer floor,
            @Param("seq") Integer seq,
            @Param("rank") String rank
    );

    List<BaseRepresentationNumber> getBaseRepresentationNumberElec(
            @Param("catalog") int catalog,
            @Param("endTime") String endTime,
            @Param("startTime") String startTime
    );

    List<BaseRepresentationNumber> getBaseRepresentationNumberWater(
            @Param("catalog") int catalog,
            @Param("endTime") String endTime,
            @Param("startTime") String startTime
    );

    List<BaseNumber> getLastNumber(
            @Param("catalog") int catalog,
            @Param("year") String year,
            @Param("month") String month
    );

    Map getValue(
            @Param("monitor") int monitor,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );
}
