package com.vibe.mapper.seawater;

import com.vibe.pojo.seawater.WorkPattern;
import com.vibe.pojo.seawater.WorkPeriod;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkSimulatorMapper {

	int insertSimulatorWork(WorkPattern workpattern);

	List<WorkPattern> findAllSimulatorWork(WorkPattern workpattern);

	int updateSimulatorWork(WorkPattern workpattern);

	int delSimulatorWork(@Param("ids") int[] ids);

	List<WorkPattern> findSimulatorWork(@Param("ids") Integer[] ids);

	List<WorkPeriod> findAllWorkPeriod(WorkPeriod workPeriod);

	int delWorkPeriod(@Param("ids") int[] ids, @Param("falg") int falg);

	List<WorkPattern> findAllWorkPatternName(@Param("userId") Integer userId, @Param("delFalg") int delFalg);

	int delWorkTemplateId(@Param("ids") int[] ids);

	List<WorkPattern> findNameSimulatorWork(@Param("userId") Integer userId, @Param("name") String name, @Param("delFalg") int delFalg);

	int insertWorkPeriod(WorkPeriod period);

	int updateWorkPeriod(WorkPeriod period);

	List<WorkPattern> findWorkPatternCheckId(@Param("ids") int[] ids);

}
