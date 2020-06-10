package com.vibe.mapper.energy;

import com.vibe.pojo.HandInputProbe;
import com.vibe.pojo.energy.CatalogEnergyReportData;
import com.vibe.pojo.energy.CatalogTreeNode;
import com.vibe.pojo.energy.EnergyReportVo;
import com.vibe.pojo.energy.SpaceEnergyReportData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnergyReportMapper {
	String getUnit(@Param("catalog") int catalog);
	
	List<SpaceEnergyReportData> getPerCapitaEnergyReport1(EnergyReportVo vo);

	List<SpaceEnergyReportData> getUnitAreaEnergyReport1(EnergyReportVo vo);
	List<SpaceEnergyReportData> getUnitAreaEnergyReport2(EnergyReportVo vo);
	
	List<CatalogTreeNode> getAllCatalog();
	List<HandInputProbe> getProbeByCatalogs(@Param("catalogIds") Integer[] catalogIds);
	List<SpaceEnergyReportData> getEquiEnergyReport(EnergyReportVo vo);
	
	List<SpaceEnergyReportData> getIdleEnergyReport(EnergyReportVo vo);
	
	List<SpaceEnergyReportData> getSpaceEnergyReport(EnergyReportVo vo);
	List<CatalogEnergyReportData> getCatalogEnergyReport(EnergyReportVo vo);
}
