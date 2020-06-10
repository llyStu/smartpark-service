package com.vibe.mapper.emergency;

import com.vibe.pojo.emergency.EmergencyUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyUserMapper {

	List<EmergencyUser> findAllLimitUser(EmergencyUser emergencyUser);

	int deleteLead(@Param("id") String id);

	int insertLead(EmergencyUser emergencyUser);

	EmergencyUser findOneLead(@Param("eid") Integer eid, @Param("udefault") int udefault);

	List<EmergencyUser> findAllLead(@Param("udefault") int udefault);

	int updateOneEmergencyLead(EmergencyUser emergencyUser);

	List<EmergencyUser> findAllTypeGradeOfLead(@Param("etid") Integer etid, @Param("egid") Integer egid);

	int deleteLeads(@Param("ids") int[] ids);

	int updateOneLead(EmergencyUser emergencyUser);



}
