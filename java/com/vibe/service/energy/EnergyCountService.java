package com.vibe.service.energy;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.vibe.pojo.energy.*;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.PageInfo;
import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.pojo.Data;

public interface EnergyCountService {
    List<List<Object>> queryEnergyCountByProbeId(@Param("type") Integer type, @Param("date") String date, @Param("idParentList") List idParentList, @Param("standard") Integer standard);

    String queryEnergyCountByProbeIdUnit(@Param("lightList") List lightList, @Param("date") String date, @Param("idParentList") List idParentList);

    String queryEnergyCountByProbeIdFloor(@Param("lightList") List lightList, @Param("date") String date, @Param("idParentList") List idParentList);

    //	List<EnergyCount> queryEnergyCountByProbeId(@Param("lightList")List lightList, @Param("date")String date);
    PageInfo<EnergyCountOne> queryNightEnergyCountByProbeId(@Param("lightList") List lightList, @Param("sTime") String sTime, @Param("eTime") String eTime, @Param("parent") Integer parent);

    //	Double queryEnergyCountByProbeIdAvg(@Param("lightList")List lightList, @Param("date")String date,@Param("idParentList")List idParentList);
    List<Integer> queryEnergyCountByMonitorId(@Param("type") Integer type);

    List<Integer> queryEnergyCountByProbeIdParent(@Param("parent") Integer parent, @Param("lou") Integer lou);

    List<EnergyCountOne> queryNightEnergyCountByProbeIdAvg(@Param("lightList") List lightList, @Param("sTime") String sTime, @Param("eTime") String eTime, @Param("parent") Integer parent);

    List<List<Object>> queryEnergyCountById(@Param("type") Integer type, @Param("date") String date, @Param("idParentList") List idParentList, @Param("standard") Integer standard);

    List<List<Energy>> getEnergySpaceTu(CommonMonitorDataVo vo, String ids);

    Map<String, Object> getEnergySpaceBiao(CommonMonitorDataVo vo, String ids, int size, int page);

    Map<String, Object> getEnergySpaceExcle(CommonMonitorDataVo vo, String ids);

    List<Energy> getEnergyA3Floorid();

    Map<String, Object> getEnergyComAndSeq(CommonMonitorDataVo vo, Integer floorId);

    List<List<Energy>> getEnergyType(CommonMonitorDataVo vo, String ids, Integer floorId);

    Map<String, Object> getEnergyTypeSort(CommonMonitorDataVo vo, String ids, Integer floorId, int page, int size);

    List<List<Energy>> getEnergyTypeSortExcel(CommonMonitorDataVo vo, String ids, Integer floorId);

    List<Energy> getEnergyFenXiangTu(CommonMonitorDataVo vo, Integer floor);

    Map<String, Object> getEnergyFenXiangBiao(CommonMonitorDataVo vo, Integer floor, int page, int size);

    List<EnergySub> getEnergyFenXiangBiaoExcel(CommonMonitorDataVo vo, Integer floor);

    List<EnergyCount> getEnergyFenShiTu(CommonMonitorDataVo vo, Integer floor);

    Map<String, Object> getEnergyFenShiBiao(CommonMonitorDataVo vo, Integer floor, int page, int size, int seq, String rank);

    List<EnergyBiao> getEnergyFenShiBiaoExcel(CommonMonitorDataVo vo, Integer floor, int seq, String rank);

    Map<String, Object> getBaseRepresentationNumber(int page, int size, int catalog, String endTime, String startTime);

    List<BaseRepresentationNumber> getBaseRepresentationNumberExcel(int catalog, String endTime, String startTime);

    Map<String, Object> getLastNumber(int page, int size, int catalog);

    List<BaseNumber> getLastNumberExcel(int catalog);
}
