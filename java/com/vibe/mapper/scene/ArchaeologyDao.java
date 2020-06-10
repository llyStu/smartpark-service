package com.vibe.mapper.scene;

import com.vibe.pojo.Archaeology;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchaeologyDao {

	public void insertArchaeology(Archaeology archaeology);

	public void deleteArchaeology(int id);

	public Archaeology queryArchaeology(int id);

	public List<Archaeology> queryArchaeologyList();

	public void updateArchaeology(Archaeology archaeology);
	
}
