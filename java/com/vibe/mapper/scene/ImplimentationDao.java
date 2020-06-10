package com.vibe.mapper.scene;

import com.vibe.pojo.Implimentation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImplimentationDao {

	public void insertImplimentation(Implimentation implimentation);

	public void deleteImplimentation(int id);

	public Implimentation queryImplimentation(int id);

	public List<Implimentation> queryImplimentationList();

	public void updateImplimentation(Implimentation implimentation);
	
}
