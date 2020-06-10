package com.vibe.service.seawater;


import java.util.List;

import com.vibe.pojo.seawater.WorkPattern;
import com.vibe.pojo.seawater.WorkPeriod;
import com.vibe.pojo.user.User;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.FormJson;

public interface WorkSimulatorService {

	FormJson operationSimulatorWork(WorkPattern workpattern);

	EasyUIJson findAllSimulatorWork(int page, int rows, WorkPattern workpattern);

	FormJson delSimulatorWork(int[] ids);

	EasyUIJson findAllWorkPeriod(int page, int rows, WorkPeriod workPeriod);

	FormJson delWorkPeriod(int[] ids, int falg);

	List<WorkPattern> findAllWorkPatternName(User user);

	List<WorkPattern> findWorkPatternCheckId(int[] id);

	List<WorkPattern> findSimulatorWork(Integer[] ids);

	FormJson operationWorkPeriod(WorkPeriod period);





}
