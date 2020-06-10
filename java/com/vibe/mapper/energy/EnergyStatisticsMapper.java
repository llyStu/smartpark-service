package com.vibe.mapper.energy;


import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.pojo.energy.*;
import com.vibe.scheduledtasks.statisticstask.MonitorValue;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface EnergyStatisticsMapper {

    Double queryRealTimeTotal(@Param("id") int id, @Param("tableName") String tableName,
                              @Param("subNum") int subNum, @Param("nowTime") String nowTime);


    List<CommonMonitorDataVo> queryRealTime(@Param("id") int id, @Param("tableName") String tableName,
                                            @Param("subNum") int subNum, @Param("nowTime") String nowTime);

    Object queryValueByTime(@Param("id") int id,
                            @Param("startTime") String startTime,
                            @Param("lastTime") String lastTime,
                            @Param("tableName") String tableName,
                            @Param("subNum") int subNum);

    List<CommonMonitorDataVo> queryValueList(@Param("id") int id,
                                             @Param("startTime") String startTime,
                                             @Param("lastTime") String lastTime,
                                             @Param("tableName") String tableName,
                                             @Param("subNum") int subNum);

    List<CommonMonitorDataVo> queryKindValue(
            @Param("id") int id,
            @Param("catalog") int catalog);

    MonitorValue queryValueFromEnergyDay(@Param("id") int id,
                                         @Param("startTime") String startTime,
                                         @Param("lastTime") String lastTime);

    List<MonitorValue> getRecentlyCarbon(@Param("monitorId") int monitorId,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("lastTime") LocalDateTime lastTime);

    //
    List<EnergyCount> queryFenShiEnergy(@Param("ids") int ids,
                                        @Param("startTime") String startTime,
                                        @Param("lastTime") String lastTime,
                                        @Param("tableName") String tableName,
                                        @Param("catalog") Integer catalog);

    List<EnergyCount> queryFenShiEnergyTwo(@Param("ids") int ids,
                                           @Param("startTime") String startTime,
                                           @Param("tableName") String tableName,
                                           @Param("catalog") Integer catalog);

    List<EnergyCount> queryFenShiEnergySeqShi(@Param("ids") int ids,
                                              @Param("startTime") String startTime,
                                              @Param("tableName") String tableName,
                                              @Param("catalog") Integer catalog);

    List<EnergyCount> queryFenShiEnergySeqShiOne(@Param("ids") int ids,
                                                 @Param("startTime") String startTime,
                                                 @Param("lastTime") String lastTime,
                                                 @Param("tableName") String tableName,
                                                 @Param("catalog") Integer catalog);

    List<EnergyCount> queryFenShiEnergySeqValue(@Param("ids") int ids,
                                                @Param("startTime") String startTime,
                                                @Param("tableName") String tableName,
                                                @Param("catalog") Integer catalog);

    List<EnergyCount> queryFenShiEnergySeqValueOne(@Param("ids") int ids,
                                                   @Param("startTime") String startTime,
                                                   @Param("lastTime") String lastTime,
                                                   @Param("tableName") String tableName,
                                                   @Param("catalog") Integer catalog);

    List<EnergyCountOne> queryFenXiangEnergyDuan(@Param("ids") int ids,
                                                 @Param("startTime") String startTime,
                                                 @Param("lastTime") String lastTime,
                                                 @Param("tableName") String tableName,
                                                 @Param("catalog") Integer catalog);

    List<EnergyCountOne> queryFenXiangEnergy(@Param("ids") int ids,
                                             @Param("startTime") String startTime,
                                             @Param("tableName") String tableName,
                                             @Param("catalog") Integer catalog);

    List<EnergyCountOne> queryFenXiangEnergySeqValueDuan(@Param("ids") int ids,
                                                         @Param("startTime") String startTime,
                                                         @Param("lastTime") String lastTime,
                                                         @Param("tableName") String tableName,
                                                         @Param("catalog") Integer catalog);

    List<EnergyCountOne> queryFenXiangEnergySeqValue(@Param("ids") int ids,
                                                     @Param("startTime") String startTime,
                                                     @Param("tableName") String tableName,
                                                     @Param("catalog") Integer catalog);

    Double queryEnergyZongLiang(@Param("ids") int ids,
                                @Param("startTime") String startTime,
                                @Param("tableName") String tableName,
                                @Param("catalog") Integer catalog
    );

    Double queryEnergyZongLiangDuan(@Param("ids") int ids,
                                    @Param("startTime") String startTime,
                                    @Param("lastTime") String lastTime,
                                    @Param("tableName") String tableName,
                                    @Param("catalog") Integer catalog);

    List<EnergyZong> queryEnergyZong(
            @Param("lightList") List lightList,
            @Param("startTime") String startTime,
            @Param("tableName") String tableName
    );

    List<EnergyZong> queryEnergyZongDuan(
            @Param("lightList") List lightList,
            @Param("startTime") String startTime,
            @Param("lastTime") String lastTime,
            @Param("tableName") String tableName
    );

    //	能耗排名分项
    List<EnergyCountOne> queryEnergyZongFenXiang(
            @Param("lightList") List lightList,
            @Param("startTime") String startTime,
            @Param("tableName") String tableName
    );

    List<EnergyCountOne> queryEnergyZongFenXiangDuan(
            @Param("lightList") List lightList,
            @Param("startTime") String startTime,
            @Param("lastTime") String lastTime,
            @Param("tableName") String tableName
    );

    List<Integer> queryEnergyZongId(
            @Param("catalog") Integer catalog)
            ;

    List<EnergyCountOne> queryEnergyType(
            @Param("lightList") List lightList
    );

    List<Integer> queryEnergyTypeId(
    );

    List<Energy> getEnergySumAll(
            @Param("redioType") int redioType,
            @Param("startTime") String startTime,
            @Param("lastTime") String lastTime,
            @Param("tableName") String tableName);

    Energy queryEnergyAllElec(
            @Param("id") int id,
            @Param("redioType") int redioType,
            @Param("startTime") String startTime,
            @Param("lastTime") String lastTime,
            @Param("tableName") String tableName);

    Energy queryEnergyAllWater(
            @Param("id") int id,
            @Param("redioType") int redioType,
            @Param("startTime") String startTime,
            @Param("lastTime") String lastTime,
            @Param("tableName") String tableName);

    List<EnergyCountOne> queryEnergyByCodeId(@Param("elecId") int elecId,
                                             @Param("redioType") int redioType,
                                             @Param("startTime") String startTime,
                                             @Param("lastTime") String lastTime,
                                             @Param("tableName") String tableName);

    List<EnergyCountOne> queryEnergyBySpaceId(
            @Param("parentId") int parentId,
            @Param("spaceId") int spaceId,
            @Param("redioType") int redioType,
            @Param("startTime") String startTime,
            @Param("lastTime") String lastTime,
            @Param("tableName") String tableName);

    List<EnergyType> getTypeList();

    List<Map<String, Object>> elecProportion();

    List<Map<String, Object>> waterProportion();
}

////分时能耗单个
//	List<EnergyCount> queryTimeData(@Param("ids") int ids,
//			@Param("catalog") int catalog,
//			@Param("startTime") String startTime,
//			@Param("tableName") String tableName);
//	
////分时能耗范围
//	
//	List<EnergyCount> queryTimeDataFan(@Param("ids") int ids,
//			@Param("catalog") int catalog,
//			@Param("startTime") String startTime,
//			@Param("lastTime") String lastTime,
//			@Param("tableName") String tableName);
//	
////分时能耗排序
//	List<EnergyCount> queryTimeDataShiSeq(@Param("ids") int ids,
//			@Param("catalog") int catalog,
//			@Param("startTime") String startTime,
//			@Param("tableName") String tableName);
//	List<EnergyCount> queryTimeDataAvgSeq(@Param("ids") int ids,
//			@Param("catalog") int catalog,
//			@Param("startTime") String startTime,
//			@Param("tableName") String tableName);
//	List<EnergyCount> queryTimeDataFanShiSeq(@Param("ids") int ids,
//			@Param("catalog") int catalog,
//			@Param("startTime") String startTime,
//			@Param("lastTime") String lastTime,
//			@Param("tableName") String tableName);
//	List<EnergyCount> queryTimeDataFanAvgSeq(@Param("ids") int ids,
//			@Param("catalog") int catalog,
//			@Param("startTime") String startTime,
//			@Param("lastTime") String lastTime,
//			@Param("tableName") String tableName);


