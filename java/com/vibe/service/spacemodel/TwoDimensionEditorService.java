package com.vibe.service.spacemodel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.spacemodel.TwoDimensionEditorMapper;
import com.vibe.pojo.spacemodel.TwoDimension;
import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;

@Service
public class TwoDimensionEditorService {
	private static final String UPLOAD_PATH = "/upload/twoDimensionEditor/";

	@Autowired
	private TwoDimensionEditorMapper tdem;

	public synchronized String upload(HttpServletRequest request, String josnStr) {
		// 写入txt获取相对路径
		String relative_path = "";
		File uploadPath = new File(request.getSession().getServletContext().getRealPath(UPLOAD_PATH));
		String fileName = UUID.randomUUID() + "-" + System.currentTimeMillis() + ".txt";
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		File file = new File(uploadPath, fileName);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
			fileWriter.write(josnStr);
			fileWriter.flush();
			relative_path = fileName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return relative_path;
	}

	public FormJson insertTwoDimensionMessage(HttpServletRequest request, String name, String fileName) {
		synchronized (fileName) {
			TwoDimension twoDimensionEditor = tdem.findTwoDimensionEditor(name);
			if (null == twoDimensionEditor) {
				if (tdem.insertTwoDimensionMessage(name, fileName) != 1) {
					FormJsonBulider.fail("添加失败");
				}
			} else {
				if (tdem.updateTwoDimensionMessage(name, fileName) != 1) {
					 FormJsonBulider.fail("操作失败");
				} else {
					return onDeleteFiel(request, twoDimensionEditor.getFilename());
				}
			}
			return FormJsonBulider.success();
		}
	}

	private FormJsonBulider onDeleteFiel(HttpServletRequest request, String filePath) {
		File oldFile = new File(request.getSession().getServletContext().getRealPath(UPLOAD_PATH)
				+filePath );
		if (! oldFile.delete()) {
			return FormJsonBulider.fail(oldFile.getName() + "文件删除失败！");
		} 
		return FormJsonBulider.success().withCause(oldFile.getName() + "文件删除成功！");
	}

	public List<String> findAllName() {
		return tdem.findAllName();
	}

	public String findOneFilepath(String name) {
		TwoDimension findTwoDimensionEditor = tdem.findTwoDimensionEditor(name);
		if(null ==findTwoDimensionEditor){
			return "";
		}
		String filename = findTwoDimensionEditor.getFilename();
		return filename;
	}

	public FormJson deleteTwoDimensionEditor(HttpServletRequest request,String name) {
		if (null == name) {
			FormJsonBulider.fail("name不能为空");
		} else {
			final String fileName=findOneFilepath(name);
			if (tdem.deleteTwoDimensionEditor(name) != 1) {
				FormJsonBulider.fail("删除失败");
			}else{
				return onDeleteFiel(request, fileName);
			}
		}
		return FormJsonBulider.success();
	}

}
