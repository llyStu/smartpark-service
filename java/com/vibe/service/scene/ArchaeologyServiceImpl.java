package com.vibe.service.scene;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vibe.mapper.scene.ArchaeologyDao;
import com.vibe.pojo.Archaeology;

@Service
public class ArchaeologyServiceImpl implements ArchaeologyService {
	
	@Autowired
	private ArchaeologyDao archaeologyDao;
	
	public void insertArchaeology(Archaeology archaeology) {		

		archaeologyDao.insertArchaeology(archaeology);
		
	}

	public void deleteArchaeology(int id) {
		
		archaeologyDao.deleteArchaeology(id);
	}

	public Archaeology queryArchaeology(int id) {
		
		Archaeology d = archaeologyDao.queryArchaeology(id);
		//return archaeologyDao.queryArchaeology(id);
		return d;
	}

	@Override
	public List<Archaeology> queryArchaeologyList() {
		
		return archaeologyDao.queryArchaeologyList();
	}

	@Override
	public void updateArchaeology(Archaeology archaeology) {

		archaeologyDao.updateArchaeology(archaeology);
	}

}
