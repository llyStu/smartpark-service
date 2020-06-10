package com.vibe.service.scene;

import java.util.List;
import com.vibe.pojo.Tourist;


public interface TouristService {
	
	public void insertTourist(Tourist tourist);

	public void deleteTourist(int i);

	public Tourist queryTourist(int id);

	public List<Tourist> queryTouristList();

	public void updateTourist(Tourist tourist);
	
}
