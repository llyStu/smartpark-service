package com.vibe.controller.asset;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.vibe.poiutil.ExportDevice;
import com.vibe.poiutil.ExportExcel;
import com.vibe.pojo.AssetVo;
import com.vibe.pojo.DeviceCatalogSpace;
import com.vibe.service.asset.AssetService;
import com.vibe.utils.EasyUIJson;
import com.vibe.monitor.asset.AssetStore;

@Controller
public class FindAssetController {
	@Autowired
	private AssetService assetService;
	
	@Autowired
	private AssetStore assetStore;
	
	@RequestMapping("/findAsset/searchAssetList")
	public @ResponseBody EasyUIJson serachAssetList(AssetVo assetVo,@RequestParam(defaultValue="1") Integer page,@RequestParam(defaultValue="10")Integer rows,Model model){
		//调用内存对象数据
		EasyUIJson uiJson = assetService.findAssetList(assetStore,assetVo,rows, page);
		 return uiJson;
	}

	@RequestMapping("/findAsset/searchDevices")
	public @ResponseBody EasyUIJson searchDevices(@RequestBody DeviceCatalogSpace dcs,
			@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer rows,
			Model model) {
		EasyUIJson uiJson = assetService.findDeviceList(assetStore, dcs, rows, page);
		return uiJson;
	}
	@RequestMapping(value="/findAsset/searchDeviceList")
	public @ResponseBody Map<String,Object> getRecentLineValues(AssetVo assetVo,@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "20") Integer rows){
		Map<String,Object> lines = assetService.queryQrcodeListByPage(assetVo,page, rows);
		return lines;
	}
	@RequestMapping(value="/findAsset/addQrcode")
	public @ResponseBody String  addQrcode(String ids,HttpServletRequest request) {
		try {  
		String contextPath = request.getContextPath();
		  String remoteAddr = request.getRemoteAddr();
		  int serverPort = request.getServerPort();
		  String scheme = request.getScheme();
		
			String savePath=scheme+"://"+remoteAddr+":"+serverPort+contextPath+"/";
	
	String realPath = request.getSession().getServletContext().getRealPath("/");

		// 用，号切成数组
		   String[] ides = ids.split(",");
		//遍历数组
				for (String id : ides) {
						assetService.addDeviceQrcode(id,savePath,realPath);
				}
		      return "200";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "500";
				}
	}
	@RequestMapping(value="/findAsset/printQrcode")
	public @ResponseBody ResponseEntity<byte[]> printQrcode(String ids,HttpServletRequest request,HttpServletResponse response) {
		try {
			List<AssetVo> devices = new ArrayList<AssetVo>();
			if (ids != null && ids != "") {
				// 用，号切成数组
				String[] ides = ids.split(",");
				// 遍历数组
				List<Integer> deviceIds = new ArrayList<Integer>();
				for (String id : ides) {
					deviceIds.add(Integer.parseInt(id));
				}
				devices = assetService.findDeviceByIds(deviceIds);
			} else {
				devices = assetService.findAllDevice();
			}
			List<ExportDevice> dataset = new ArrayList<ExportDevice>();
			if (devices != null) {
				for (AssetVo device : devices) {
					ExportDevice exportDevice = new ExportDevice();
					exportDevice.setCaption(device.getCaption());
					exportDevice.setId(device.getId());
					exportDevice.setVendor(device.getVendor());
					String enabing_date = device.getEnabing_date();
					if (enabing_date != null) {
						exportDevice.setDate(enabing_date);
					}
					// exportDevice.setQrcode(space.getId());
					dataset.add(exportDevice);
				}
			}

			/* 生成xlsx 直接写到响应体下载 */
			ExportExcel<ExportDevice> ex = new ExportExcel<ExportDevice>();
//			String[] headers = { "设备编号", "设备名称", "生产厂家", "启用日期" };
			String[] headers = { "设备编号", "标题", "分类", "名称" ,"存放地点","保管人","启用时间","使用年限"};
			HSSFWorkbook workbook = ex.exportExcel("设备二维码", headers, dataset);
			/*String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + "设备.xls";
			String realPath = "C:/space/" + fileName;
			System.out.println(realPath);
			File file = new File(realPath);*/
			/* runExcelFileExport(response,workbook,fileName); */
			/*OutputStream out;

			out = new FileOutputStream(file);*/

			/*workbook.write(new BufferedOutputStream(response.getOutputStream()));*/
			/*String filename = "file.xls";//设置下载时客户端Excel的名称  
			response.setContentType("application/force-download");// 设置强制下载不打开 
		      response.setHeader("Content-disposition", "attachment;filename=" + filename);   
		      OutputStream ouputStream = response.getOutputStream();   
		      workbook.write(ouputStream);   
		      ouputStream.flush();   
		      ouputStream.close();   */
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();  
	        try {  
	        	workbook.write(out);  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        HttpHeaders headers1 = new HttpHeaders();   
	        //headers1.setContentDispositionFormData("attachment", "file.xls");  
	        headers1.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"设备信息.xls\"");
	        headers1.setContentType(MediaType.APPLICATION_OCTET_STREAM);  
	        ResponseEntity<byte[]> filebyte = new ResponseEntity<byte[]>(out.toByteArray(),headers1, HttpStatus.OK);  
	        try {  
	            out.close();  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        return filebyte;  

			/* download(realPath,response); */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		/* print(response,request,"data.xls");*/
		 
		/* String    mimetype = "application/x-msdownload";
		   response.setContentType(mimetype);
		   String inlineType = "attachment"; // 是否内联附件
		   response.setHeader("Content-Disposition", inlineType
		    + ";filename=\"" + downloadname + "\"");
		   OutputStream out=response.getOutputStream();
	
	    
	     
	      out.flush();
	      out.close();*/
		     
				
	}
	/*private void runExcelFileExport(HttpServletResponse response, HSSFWorkbook workbook, String fileName) {
		 try {  
	          OutputStream sos = response.getOutputStream();  
	           ByteArrayOutputStream buffer = new ByteArrayOutputStream();  
	           workbook.write(buffer);  
	           response.setContentType("application/vnd.ms-excel"); //设置下载文件类型为 Excel  
	           response.setContentLength(buffer.size());  
	           response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(fileName,"utf-8")); //防止中文乱码  
	           response.setHeader("Pragma", "public");  
	           response.setHeader("Cache-Control", "max-age=0");  
	           sos.write(buffer.toByteArray());  
	           buffer.flush();  
	           sos.flush();  
	           buffer.close();
	       } catch (IOException e){  
	           e.printStackTrace();  
	       }  
		
	}
	@RequestMapping(value="/findAsset/print")
	public void print(HttpServletResponse response,HttpServletRequest request){
		//1、获取参数filrname
		String filename = System.currentTimeMillis()+"qrcode";
				try{
				//获取中文    get方式解决乱码   首先进行iso8859-1编，再进行UTF-8解
				filename=new String(filename.getBytes("iso8859-1"),"utf-8");
				
				//解决   下载名乱码
				String downloadname = new String(filename.getBytes("gbk"),"iso8859-1");
				
				//2、添加  强制下载的响应头
				response.setHeader("content-disposition", "attachment;filename="+downloadname);
				
				//3、首先获取硬盘资源文件
				 String realPath = request.getSession().getServletContext().getRealPath("/")+"qrcode/";
				String path = realPath+filename;
				
				
				InputStream is = new FileInputStream(path);
				
				
				//4、向响应体输出字节流
				OutputStream out = response.getOutputStream();
				
				//3、写响应体   复制
				
				int len = -1;
				byte[] b = new byte[1024];
				while((len=is.read(b))!=-1){
					out.write(b, 0, len);
				}
				out.flush();
				//并不是必须的，因为服务器会自动帮我关
				out.close();
				is.close();
				}catch (Exception e) {
					// TODO: handle exception
				}
	}*/
	@SuppressWarnings("unused")
	private void download(String path, HttpServletResponse response) {  
        try {  
            // path是指欲下载的文件的路径。  
            File file = new File(path);  
            // 取得文件名。  
            String filename = file.getName();  
            // 以流的形式下载文件。  
            InputStream fis = new BufferedInputStream(new FileInputStream(path));  
            byte[] buffer = new byte[fis.available()];  
            fis.read(buffer);  
            fis.close();  
            // 清空response  
            response.reset();  
            // 设置response的Header  
            response.addHeader("Content-Disposition", "attachment;filename="  
                    + new String(filename.getBytes()));  
            response.addHeader("Content-Length", "" + file.length());  
            OutputStream toClient = new BufferedOutputStream(  
                    response.getOutputStream());  
            response.setContentType("application/vnd.ms-excel;charset=gb2312");  
            toClient.write(buffer);  
            toClient.flush();  
            toClient.close();  
        } catch (IOException ex) {  
            ex.printStackTrace();  
        }  
    }  
}
