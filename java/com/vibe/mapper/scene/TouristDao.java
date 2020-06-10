package com.vibe.mapper.scene;

import com.vibe.pojo.Tourist;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TouristDao {

	public void insertTourist(Tourist tourist);

	public void deleteTourist(int id);

	public Tourist queryTourist(int id);

	public List<Tourist> queryTouristList();

	public void updateTourist(Tourist tourist);
	
}
