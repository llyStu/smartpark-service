package com.vibe.mapper.energy;

import com.vibe.pojo.energy.MeterRelation;
import com.vibe.service.energy.Meter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EnergyMeterMapper {
    /*
     * 查询实际监测能源表的列表
     */
    List<Meter> queryEnergyMeter(Meter meter);

    List<Meter> queryEnergyMeterId(Meter meter);

    List<Meter> queryEnergyMeterByParentId(Meter meter);

    void addEnergyMeter(Meter meter);

    void updateEnergyMeterById(Meter meter);

    Meter queryEnergyMeterByProbeId(@Param("probeId") Integer probeId);

    List<MeterRelation> queryMeterRelation(MeterRelation meterRelation);

    void addMeterRelation(MeterRelation meterRelation);

}
