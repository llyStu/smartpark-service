package com.vibe.service.energy;

import java.util.List;

import com.vibe.monitor.asset.AssetException;
import com.vibe.parse.ExcelSheetPO;
import com.vibe.pojo.energy.MeterRelation;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.TreeNode;

public interface EnergyMeterService {


    /*
     * 新增表
     */
    /*
     * 查询表的信息
     */
    /*
     * 修改表信息
     */
    /*
     * 生成实表的tree
     */
    List<TreeNode> getMeterTree(Meter meter);

    /*
     * 生成虚表的树
     */
    List<TreeNode> queryDummyMeter(Integer energyKind);

    /*
     * 分页查询列表
     */
    EasyUIJson meterList(Integer rows, Integer page, Meter meter);

    /*
     * 添加能源probe
     * @param meter
     */
    void addEnergyProbe(Meter meter) throws AssetException;

    /*
     * 查询一个表的信息
     */
    Meter queryEnergyMeterByProbeId(int probeId);

    Integer queryItemize(int probeId);

    void updateEnergyMeter(Meter meter) throws AssetException;

    void deleteMeter(String ids) throws AssetException;

    /*
     * 查询空间下的表按类型分类
     */
    List<Meter> queryChildMeter(Meter meter);

    void addMeterGrade(Meter meter);

    void imEnergyMeter(List<ExcelSheetPO> excelSheet) throws AssetException;

    EasyUIJson relationMeterList(Integer rows, Integer page, MeterRelation meterRelation);
}
