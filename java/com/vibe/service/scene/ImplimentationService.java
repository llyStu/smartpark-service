package com.vibe.service.scene;

import java.util.List;
import com.vibe.pojo.Implimentation;

public interface ImplimentationService {
	
	public void insertImplimentation(Implimentation implimentation);

	public void deleteImplimentation(int parseInt);

	public Implimentation queryImplimentation(int id);

	public List<Implimentation> queryImplimentationList();

	public void updateImplimentation(Implimentation implimentation);
	
}
