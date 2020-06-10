package com.vibe.controller.backup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.service.backup.BackupService;
import com.vibe.utils.FormJson;

@Controller
public class Backup {
	@Autowired
	private BackupService backup;
	
	@RequestMapping("/backupAll")
	@ResponseBody
	public FormJson backupAll(){
		return backup.backupAll();
	}
	
	@RequestMapping("/recoverAll")
	@ResponseBody
	public FormJson recoverAll(){
		return backup.recoverAll();
	}
}
