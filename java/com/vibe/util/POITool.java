package com.vibe.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class POITool {

    public static boolean mapToExcel2(Map<String, Object> map, String path, String fileName) {
        WritableWorkbook book;
        File upload_file = new File(path);
        if (!upload_file.exists()) {
            upload_file.mkdirs();
        }
        try {
            book = Workbook.createWorkbook(new File(path, fileName));
            WritableSheet sheet = book.createSheet("firstSeet", 0);
            int row = 0, col = 0;
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                Label head = new Label(col, row, entry.getKey());
                sheet.addCell(head);
                row = 1;
                //excel的单元格中 第一行显示标题，第二行开始显示数值，每次遍历生成一列数据
                if (entry.getValue().getClass().toString().contains("List")) {
                    List<Object> values = (List<Object>) entry.getValue();
                    for (Object value : values) {
                        Label body = new Label(col, row, value.toString());
                        sheet.addCell(body);
                        row++;
                    }
                    col++;
                    row = 0;
                } else {
                    Label body = new Label(col, row, entry.getValue().toString());
                    sheet.addCell(body);
                    col++;
                    row = 0;
                }
            }
            book.write();
            book.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void exportToDisk(String fileName) {

        OutputStream out = null;
        FileInputStream in = null;

        File down_path = new File("C:/Export");
        if (!down_path.exists()) {
            down_path.mkdirs();
        }

        //导出到 C:/Export
        try {
            out = new FileOutputStream(new File("C:/Export", fileName + ".xls"));
            in = new FileInputStream(new File("C:/download", fileName + ".xls"));
            byte[] by = new byte[1024];
            int len;
            while ((len = in.read(by)) != -1) {
                out.write(by, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {

                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean mapToExcelMonitor(Map<String, List<Object>> map, String name, HttpServletResponse response) {
        WritableWorkbook book;
        try {
            //定义输出流，以便打开保存对话框______________________begin
            OutputStream os = response.getOutputStream();// 取得输出流  
            response.reset();// 清空输出流                     
            String fileName = name + ".xls";
            response.setContentType("application/x-msdownload");// 设定输出文件类型
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1")); //设定文件输出头
            //定义输出流，以便打开保存对话框_______________________end  
            book = Workbook.createWorkbook(os);
            WritableSheet sheet = book.createSheet(name, 0);
            int row = 0, col = 0;
            Iterator<Map.Entry<String, List<Object>>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<Object>> entry = iterator.next();
                Label head = new Label(col, row, entry.getKey());
                sheet.addCell(head);
                row = 1;
                //excel的单元格中 第一行显示标题，第二行开始显示数值，每次遍历生成一列数据
                if (entry.getValue().getClass().toString().contains("List")) {
                    List<Object> values = (List<Object>) entry.getValue();
                    for (Object value : values) {
                        Label body = new Label(col, row, value.toString());
                        sheet.addCell(body);
                        row++;
                    }
                    col++;
                    row = 0;
                } else {
                    Label body = new Label(col, row, entry.getValue().toString());
                    sheet.addCell(body);
                    col++;
                    row = 0;
                }
            }
            book.write();
            book.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
