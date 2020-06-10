package com.vibe.mapper.information;

import com.vibe.pojo.infomation.ShowTask;
import com.vibe.pojo.infomation.ShowTaskVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ShowTaskMapper {
	public Integer add(ShowTask st);

	public List<ShowTask> getByStartAfter(@Param("startTime") Date startTime);
	public Date getAfterStart(@Param("startTime") Date startTime);

	public ShowTask getById(Integer id);

	public void setType(@Param("ids") Integer[] ids, @Param("type") String type);

	public List<ShowTask> getByIds(@Param("ids") Integer[] ids);

	public List<ShowTask> find(ShowTaskVo stv);

	public void update(ShowTask st);
}
