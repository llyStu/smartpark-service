package com.vibe.service.scene;

import java.util.List;
import com.vibe.pojo.ProtectProject;


public interface ProtectProjectService {

	public void insertProtectProject(ProtectProject protectProject);
	
	public void deleteProtectProject(int id);
	
	public void updateProtectProject(ProtectProject protectProject);
	
	public List<ProtectProject> listProtectProject();
	
	public ProtectProject queryProtectProject(int id);
}
