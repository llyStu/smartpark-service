package com.vibe.service.scene;

import java.util.List;
import com.vibe.pojo.Others;


public interface OthersService {
	
	public void insertOthers(Others others);

	public void deleteOthers(int i);

	public Others queryOthers(int id);

	public List<Others> queryOthersList();

	public void updateOthers(Others others);
	
}
