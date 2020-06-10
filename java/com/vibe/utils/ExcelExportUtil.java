package com.vibe.utils;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.write.Alignment;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelExportUtil {
     public String exportExcel(List<List<Object>> order,String[] headerlist, 
             String name,HttpServletResponse response ) {  
            String result = "系统提示：Excel文件导出成功！";  
            // 以下开始输出到EXCEL  
            try {  
                 //定义输出流，以便打开保存对话框______________________begin  
                 OutputStream os = response.getOutputStream();// 取得输出流  
                 response.reset();// 清空输出流                     
                 String fileName = name + ".xls";
                 response.setContentType("application/x-msdownload");// 设定输出文件类型
                 response.setHeader("Content-Disposition",
                         //"attachment;filename=" + new String( fileName.getBytes("gb2312"), "ISO8859-1" )); //设定文件输出头
                         URLEncoder.encode(fileName, "UTF-8"));//设定文件输出头
                 //定义输出流，以便打开保存对话框_______________________end  
                /** **********创建工作簿************ */  
                WritableWorkbook workbook = Workbook.createWorkbook(os);        
                /** **********创建工作表************ */       
                WritableSheet sheet = workbook.createSheet("Sheet1", 0);        
                /** **********设置纵横打印（默认为纵打）、打印纸***************** */  
                SheetSettings sheetset = sheet.getSettings();  
                sheetset.setProtected(false);  
                sheetset.setDefaultColumnWidth(20); 
                /** ************设置单元格字体************** */  
                WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);  
                WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,  
                        WritableFont.BOLD);  
                WritableFont TitleFont = new WritableFont(WritableFont.ARIAL, 20,  
                        WritableFont.BOLD);        
                /** ************以下设置三种单元格样式，灵活备用************ */  
                // 用于表名
                WritableCellFormat title_center = new WritableCellFormat(TitleFont);  
                title_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条  
                title_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐  
                title_center.setAlignment(Alignment.CENTRE); // 文字水平对齐  
                title_center.setWrap(false); // 文字是否换行  
                  
                // 用于标题居中  
                WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);  
                wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条  
                wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐  
                wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐  
                wcf_center.setWrap(false); // 文字是否换行  
      
                // 用于正文居左  
                WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);  
                wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条  
                wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐  
                wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐  
                wcf_left.setWrap(true); // 文字是否换行                
                /** ***************以下是EXCEL第一行列标题********************* */  
                for (int i = 0; i < headerlist.length; i++) {  
                    System.out.println(headerlist[i]);
                    sheet.addCell(new Label(i, 0, headerlist[i], wcf_center));  
                }  
                /** ***************以下是EXCEL正文数据********************* */  
                int i = 1;  
                for (List<Object> orderlist : order) {                                 
                    int j = 0;  
//                    System.out.println(orderlist);
                    for (Object obj : orderlist) {
                        System.out.println(obj);
//                        if (obj instanceof Date) {
//     	                   Date date = (Date) obj;
//     	                   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//     	                   obj = sdf.format(date);
//     	                } 
                        sheet.addCell(new Label(j, i, obj.toString(), wcf_left));//j表示列，i表示行，va.toString是数据，wcf_left是样式
                        j++;                               
                }  
                i++;  
                }
                /** **********将以上缓存中的内容写到EXCEL文件中******** */  
                
                workbook.write();  
                /** *********关闭文件************* */  
                workbook.close();  
                  
                System.out.println(result);  
            
            } catch (Exception e) {  
                result = "系统提示：Excel文件导出失败，原因：" + e.toString();  
                System.out.println(result);  
                e.printStackTrace();  
            }  
            return result;  
        }  
}