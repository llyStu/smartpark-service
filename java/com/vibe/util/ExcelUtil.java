package com.vibe.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.vibe.parse.ExcelSheetPO;
import com.vibe.parse.ExcelVersion;

public class ExcelUtil {
	public static List<ExcelSheetPO> readExcel(MultipartFile file) {
		  String fileName = file.getOriginalFilename();  
	
		String extName = fileName.substring(fileName.lastIndexOf("."));
		Workbook wb = null;
		try {
			if (ExcelVersion.V2003.getSuffix().equals(extName)) {
				wb = new HSSFWorkbook(file.getInputStream());

			} else if (ExcelVersion.V2007.getSuffix().equals(extName)) {
				wb = new XSSFWorkbook(file.getInputStream());
			} else {
				// 无效后缀名称，这里之能保证excel的后缀名称，不能保证文件类型正确，不过没关系，在创建Workbook的时候会校验文件格式
				throw new IllegalArgumentException("Invalid excel version");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 开始读取数据
		List<ExcelSheetPO> sheetPOs = new ArrayList<>();
		// 解析sheet
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			Sheet sheet = wb.getSheetAt(i);
			List<List<String>> dataList = new ArrayList<>();
			ExcelSheetPO sheetPO = new ExcelSheetPO();
			sheetPO.setSheetName(sheet.getSheetName());
			sheetPO.setDataList(dataList);
			// 解析sheet 的行
			for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
				Row row = sheet.getRow(j);
				if (row == null) {
					continue;
				}
				if (row.getFirstCellNum() < 0) {
					continue;
				}
				List<String> rowValue = new ArrayList<String>();
				// 解析sheet 的列
				for (int k = row.getFirstCellNum(); k < row.getLastCellNum(); k++) {
					Cell cell = row.getCell(k);
					if (cell != null) {
						if("".equals(cell.toString())){
							rowValue.add(null);
						}else {
							rowValue.add(cell.toString().trim());
						}
					} else {
						rowValue.add(null);
					}
				}
				dataList.add(rowValue);
			}
			sheetPOs.add(sheetPO);
		}
		return sheetPOs;
	}

	public static String rvZeroAndDot(String s) {
		if(s == null){
			return s;
		}
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}
}