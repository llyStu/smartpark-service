package com.vibe.mapper.auty;

import com.vibe.pojo.auty.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutyMapper {

	int insertArrangInfo(ArrangInfo info);
	int deleteArrangInfo(@Param("ids") int[] ids);
	int deleteArrangInfoByConf(@Param("ids") int[] ids);
	int updateArrangInfo(ArrangInfo info);
	List<ArrangInfo> findArrangInfo(ArrangInfo info);

	int insertAuty(Auty info);
	int deleteAuty(@Param("ids") int[] ids);
	int updateAuty(Auty info);
	List<Auty> findAuty(Auty info);
	
	int insertAbnormality(Abnormality info);
	int deleteAbnormality(@Param("ids") int[] ids);
	int updateAbnormality(Abnormality info);
	List<Abnormality> findAbnormality(Abnormality info);
	
	int insertChangeShifts(ChangeShifts info);
	int deleteChangeShifts(@Param("ids") int[] ids);
	int updateChangeShifts(ChangeShifts info);
	List<ChangeShifts> findChangeShifts(ChangeShifts info);
	
	int insertChangeShiftsGood(ChangeShiftsGood info);
	int deleteChangeShiftsGood(@Param("ids") int[] ids);
	int updateChangeShiftsGood(ChangeShiftsGood info);
	List<ChangeShiftsGood> findChangeShiftsGood(ChangeShiftsGood info);
	
	int insertArrangInfoConf(ArrangInfoConf conf);
	int deleteArrangInfoConf(@Param("ids") int[] ids);
	int updateArrangInfoConf(ArrangInfoConf info);
	List<ArrangInfo> findArrangInfoConf(ArrangInfoConf info);
	
	int insertArrangInfoConfType1(@Param("id") int id, @Param("type") String type, @Param("startTime") String[] startime, @Param("stopTime") String[] endtime, @Param("memo") String memo);
	int deleteArrangInfoConfType1(@Param("ids") int[] ids);
	int updateArrangInfoConfType1(ArrangInfoConf info);
	List<ArrangInfo> findArrangInfoConfType1(ArrangInfoConf info);

	/**
	 * 查询值班人员集合
	 * @param staff
	 * @return
	 */
	List<String> selectDutyPeopleListByIds(@Param("staffIds") String staff);
}
