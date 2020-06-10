package com.vibe.mapper.general;

import com.vibe.pojo.GeneralForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneralFormAddDeleteUpdateQueryDao {
	public List<GeneralForm> queryForms(@Param("business") String business);
	public void deleteForm(@Param("id") int id, @Param("business") String business);
	public int insertForm(GeneralForm generalFrom);
	public void updateForm(@Param("data") String data, @Param("id") int id);
}
