package com.vibe.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.ibm.icu.text.SimpleDateFormat;
import com.vibe.common.Application;
import com.vibe.common.id.IdGenerator;
import com.vibe.pojo.Archaeology;
import com.vibe.pojo.BuildingsPhoto;
import com.vibe.pojo.Catalog;
import com.vibe.pojo.DailyCheck;
import com.vibe.pojo.Damage;
import com.vibe.pojo.Implimentation;
import com.vibe.pojo.Others;
import com.vibe.pojo.Tourist;
import com.vibe.pojo.UnionPolling;
import com.vibe.pojo.user.User;
import com.vibe.service.classification.SelectOptionService;
import com.vibe.service.dailycheck.DailyCheckService;
import com.vibe.service.fault.FaultService;
import com.vibe.service.scene.ArchaeologyService;
import com.vibe.service.scene.BuildingsPhotoService;
import com.vibe.service.scene.DamageService;
import com.vibe.service.scene.ImplimentationService;
import com.vibe.service.scene.OthersService;
import com.vibe.service.scene.TouristService;
import com.vibe.util.CatalogIds;
import com.vibe.util.Msg;
import com.vibe.util.PathTool;


/*
 * 处理手机端请求，1 返回所有实测录入的分页后的列表
 * 2 接收手机端参数type,id，根据type的不同，把请求转发给不同的controller
 * */
@Controller
public class UnionPollingController {

	@Autowired
	private BuildingsPhotoService buildingsPhotoService;
	@Autowired
	private ImplimentationService implimentationService;
	@Autowired
	private ArchaeologyService archaeologyService;
	@Autowired
	private TouristService touristService;
	@Autowired
	private DailyCheckService dailyCheckService;
	@Autowired
	private DamageService damageService;
	@Autowired
	private OthersService othersService;
	@Autowired
	private SelectOptionService selectOptionService;
	@Autowired
	private Application application;
	@Autowired
	private FaultService faultService;
	//手机端查询详情
	@RequestMapping("/android_detail_items")
	public String dispatcherForDtail(int id,
			int type,
			HttpServletRequest request,
			HttpSession session){
		
		if(type==1){
			return "redirect:/android_query_dailyCheck?id="+ id;
		}
		else if(type==2){
			return "redirect:/android_detailBuildingsPhoto?id="+ id;
		}
		else if(type==3){
			return "redirect:/android_query_damage?id="+ id;
		}
		else if(type==5){
			return "redirect:/android_query_archaeology?id="+ id;
		}
		else if(type==4){
			return "redirect:/android_query_implimentation?id="+ id;
		}
		else if(type==6){
			return "redirect:/android_query_tourist?id="+ id;
		}
		else if(type==8 || type==9){
			return "redirect:/android_query_fault?id="+ id;
		}
		else{
			return "redirect:/android_query_others?id="+ id;
		}
		
	}
	
	@RequestMapping("/android_list_all")
	@ResponseBody
	public List<UnionPolling> listUnionPolling(int pageNum,
                                               @RequestParam(value="type",defaultValue="0",required=false) int type,
                                               int pageCount, HttpServletRequest request,
                                               HttpSession session){
		//记录分页信息，下一次请求分类列表的时候用
		session.setAttribute("pageNum", pageNum);
		session.setAttribute("pageCount", pageCount);
		
		PageHelper.startPage(pageNum, pageCount);
		
		List<UnionPolling>list = buildingsPhotoService.queryUnionPollingList();
		//设置图片属性值为绝对路径，单张图片保存在photo属性，多张保存在photos集合
		for(UnionPolling item:list){
			//item.getPhoto()报空指针异常
			if(item.getPhoto()!=null){	
				
			if(item.getPhoto().contains(",")){
				List<String>path = PathTool.getAbsolutePathList(item.getPhoto(), request);
				//item.setPhotos(path);
				item.setPhoto(path.get(0));
			}
			else{
				String absolutePath = PathTool.getAbsolutePath(item.getPhoto(), request);
				item.setPhoto(absolutePath);
			}
			}
		}
		return list;
	}
	
	//手机端显示分类列表
	@RequestMapping("/android_list_items")
	public String dispatcherForList(int pageNum, int pageCount, int type, HttpServletRequest request,
			HttpSession session) {
		// 记录分页信息，下一次请求分类列表的时候用
		session.setAttribute("pageNum", pageNum);
		session.setAttribute("pageCount", pageCount);

		// 类型0代表所有实测录入的列表，1代表日常巡查，2代表本体情况...
		if (type == 0) {
			return "redirect:/android_list_all?pageNum=" + pageNum + "&pageCount=" + pageCount;
		} else {
			return "redirect:/android_list_catalog?type=" + type;
		}
	}
	//收到手机端请求，根据type进行判断，把请求转发给不同的controller
	@RequestMapping("/android_list_catalog")
	public String listPollingCatalog(int type,HttpSession session){
		int pageNum = (int) session.getAttribute("pageNum");
		int pageCount = (int) session.getAttribute("pageCount");
		
		if(type==1 ){
			return "redirect:/android_list_dailyCheck?pageNum="
					+ pageNum + "&pageCount=" + pageCount;
		}
		if(type==2){
			return "redirect:/android_listBuildingsPhoto?pageNum="
					+ pageNum + "&pageCount=" + pageCount;
		}
		else if(type==3){
			return "redirect:/android_list_damage?pageNum="
					+ pageNum + "&pageCount=" + pageCount;
		}
		else if(type==4){
			return "redirect:/android_list_implimentation?pageNum="
					+ pageNum + "&pageCount=" + pageCount;
		}
		else if(type==5){
			return "redirect:/android_list_archaeology?pageNum="
					+ pageNum + "&pageCount=" + pageCount;
		}
		else if(type==6){
			return "redirect:/android_list_tourist?pageNum="
					+ pageNum + "&pageCount=" + pageCount;
		}
		else if(type==8 ){
			return "redirect:/android_list_fault?pageNum="
					+ pageNum + "&pageCount=" + pageCount;
		}else if(type==9 ){
			return "redirect:/android_listDespatchFault?pageNum="
					+ pageNum + "&pageCount=" + pageCount;
		}
		//如果选择type为“其他”返回所有的列表
		else{
			return "redirect:/android_list_tourist?pageNum="
					+ pageNum + "&pageCount=" + pageCount;
		}
	}
	
	//网页端点击 实测录入 跳转至所有实测录入界面
	@RequestMapping("/list_unionPolling")
	public String listUnionPolling(){
		return "polling/unionPolling/listUnionPolling";
	}
	
	//手机端删除
	@RequestMapping("/android_delete_items")
	public String dispatcherForDelete(String ids, int type, HttpServletRequest request, HttpSession session) {

		if (type == 1 ) {
			return "redirect:/android_delete_dailyCheck?ids=" + ids;
		} else if (type == 2) {
			return "redirect:/android_deleteBuildingsPhoto?ids=" + ids;
		} else if (type == 3) {
			return "redirect:/android_delete_damage?ids=" + ids;
		} else if (type == 5) {
			return "redirect:/android_delete_archaeology?ids=" + ids;
		} else if (type == 4) {
			return "redirect:/android_delete_implimentation?ids=" + ids;
		} else if (type == 6) {
			return "redirect:/android_delete_tourist?ids=" + ids;
		} else if (type == 8) {
			return "redirect:/android_delete_fault?ids=" + ids;
		}else {
			return "redirect:/android_delete_others?ids=" + ids;
		}
	}
	
	//手机端录入	
	@RequestMapping("/android_insert_items")
	@ResponseBody
	public Msg dispatcherForInsert(
			MultipartFile[] photoFile,
			String jsonStr,
			int type,			
			HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, JSONException{
		//过滤掉上传过来对象自带的id属性??
		JSONObject j = new JSONObject(jsonStr);
		j.remove("id");
		jsonStr = j.toString();
		ObjectMapper objectMapper = new ObjectMapper();
		if(type==1 || type==8){	
			
			DailyCheck dailyCheck = objectMapper.readValue(jsonStr,DailyCheck.class);
			IdGenerator<Integer> gen = application.getIntIdGenerator("task");
			dailyCheck.setId(gen.next());
			if(photoFile!=null && photoFile.length > 0 && photoFile[0].getSize()>0){
				
				String pics = PathTool.getRelativePath(photoFile, request);
				dailyCheck.setPhoto(pics);
				}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			dailyCheck.setDate(simpleDateFormat.format(new Date()));
			User user=(User)request.getSession().getAttribute("loginUser");
			if(user !=null){
				dailyCheck.setPerson(user.getId());
			}

			try{
				if(type ==1){
					dailyCheckService.insertDailyCheck(dailyCheck);
				}else{
					
					
					faultService.insertFault(dailyCheck);
					faultService.insertDeviceFault(dailyCheck);
				}
			return Msg.success();
			}catch(Exception e){
				e.printStackTrace();
				return Msg.fail();
			}
		}
		else if(type==2){
			BuildingsPhoto buildingsPhoto = objectMapper.readValue(jsonStr, BuildingsPhoto.class);
			if(photoFile!=null){							
			String pics = PathTool.getRelativePath(photoFile, request);
			buildingsPhoto.setPhoto(pics);
			}
			buildingsPhoto.setDate(LocalDate.now().toString());
			try{
				buildingsPhotoService.insertBuildingsPhoto(buildingsPhoto);
				return Msg.success();
			}catch(Exception e){
				e.printStackTrace();
				return Msg.fail();
			}
		}
		else if(type==4){
			Implimentation implimentation = objectMapper.readValue(jsonStr, Implimentation.class);
			if(photoFile!=null&&photoFile[0].getSize()>0){			
				String path = PathTool.getRelativePath(photoFile, request);
				implimentation.setPhoto(path);
				}			
			try{
				implimentationService.insertImplimentation(implimentation);
				return Msg.success();
			}catch(Exception e){
				e.printStackTrace();
				return Msg.fail();
			}
		}
		else if(type==3){
			Damage damage = objectMapper.readValue(jsonStr, Damage.class);
			if(photoFile!=null&&photoFile[0].getSize()>0){			
				String path = PathTool.getRelativePath(photoFile, request);
				damage.setPhoto(path);
				}			
			damage.setDate(LocalDate.now().toString());
//			int damageType2 = damage.getDamageType2();
//			int damageType1 = selectOptionService.getParentId(damageType2,16);
//			
			try{
			damageService.insertDamage(damage);
			return Msg.success();
			}catch(Exception e){
				e.printStackTrace();
				return Msg.fail();
			}
		}
		else if(type==5){		 
		  Archaeology archaeology = objectMapper.readValue(jsonStr, Archaeology.class);	
		  if(photoFile!=null&&photoFile[0].getSize()>0){			
				String path = PathTool.getRelativePath(photoFile, request);
				archaeology.setPhoto(path);
				}
		 
			archaeology.setDate(LocalDate.now().toString());
			try{
			archaeologyService.insertArchaeology(archaeology);
			return Msg.success();
			}catch(Exception e){
				e.printStackTrace();
				return Msg.fail();
			}
		}
		else if(type==6){
			Tourist tourist = objectMapper.readValue(jsonStr, Tourist.class);
			if(photoFile!=null&&photoFile[0].getSize()>0){			
				String path = PathTool.getRelativePath(photoFile, request);
				tourist.setPhoto(path);
				}
			tourist.setDate(LocalDate.now().toString());
			try{
			touristService.insertTourist(tourist);
			return Msg.success();
			}catch(Exception e){
				e.printStackTrace();
				return Msg.fail();
			}
		}
		else if(type==7){
			Others others = objectMapper.readValue(jsonStr, Others.class);
			if(photoFile!=null&&photoFile[0].getSize()>0){			
				String path = PathTool.getRelativePath(photoFile, request);
				others.setPhoto(path);
				}
			others.setDate(LocalDate.now().toString());
			try{
			othersService.insertOthers(others);
			return Msg.success();
			}catch(Exception e){
				e.printStackTrace();
				return Msg.fail();
			}
		}
	  return Msg.fail();
	 
	  }
	  
	// 手机端点击“修改”，返回对象用于前段回显，等待修改完后提交update
	@RequestMapping("/android_before_update_items")
	@ResponseBody
	public Object dispatcherForUpdate(
			int type, 
			int id,
			HttpServletRequest request)
			throws JsonParseException, JsonMappingException, IOException {
		
		if(type==1){
			DailyCheck dailyCheck = dailyCheckService.queryDailyCheck(id);
			List<String> pics = PathTool.getAbsolutePathList(dailyCheck.getPhoto(), request);
			dailyCheck.setPhotos(pics);
			request.setAttribute("dailyCheck", dailyCheck);
			return dailyCheck;
		}
		else if(type==2){
			BuildingsPhoto buildingsPhoto = buildingsPhotoService.queryBuildingsPhoto(id);
			List<String> pics = PathTool.getAbsolutePathList(buildingsPhoto.getPhoto(), request);
			buildingsPhoto.setPhotos(pics);
			request.setAttribute("buildingsPhoto", buildingsPhoto);
			return buildingsPhoto;
		}
		else if(type==3){
			Damage damage = damageService.queryDamage(id);
			List<String> pics = PathTool.getAbsolutePathList(damage.getPhoto(), request);
			damage.setPhotos(pics);
			request.setAttribute("damage", damage);
			return damage;
		}
		else if(type==4){
			Implimentation implimentation = implimentationService.queryImplimentation(id);
			List<String> pics = PathTool.getAbsolutePathList(implimentation.getPhoto(), request);
			implimentation.setPhotos(pics);
			request.setAttribute("implimentation", implimentation);
			return implimentation;
		}
		else if(type==5){
			Archaeology archaeology = archaeologyService.queryArchaeology(id);
			List<String> pics = PathTool.getAbsolutePathList(archaeology.getPhoto(), request);
			archaeology.setPhotos(pics);
			request.setAttribute("archaeology", archaeology);
			return archaeology;
		}
		else if(type==6){
			Tourist tourist = touristService.queryTourist(id);
			List<String> pics = PathTool.getAbsolutePathList(tourist.getPhoto(), request);
			tourist.setPhotos(pics);
			request.setAttribute("tourist", tourist);
			return tourist;
		}
		else if(type==7){
			Others others = othersService.queryOthers(id);
			List<String> pics = PathTool.getAbsolutePathList(others.getPhoto(), request);
			others.setPhotos(pics);
			request.setAttribute("others", others);
			return others;
		}
		return Msg.fail();
	}

	// 手机端修改提交
	@RequestMapping("/android_after_update_items")
	@ResponseBody
	public Msg dispatcherForUpdate2(
			String delphotos,
			MultipartFile[] photoFile, 
			String jsonStr, 
			int type, 
			HttpServletRequest request)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		if(type==1){
			DailyCheck dailyCheck = objectMapper.readValue(jsonStr, DailyCheck.class);
			int id = dailyCheck.getId();
			DailyCheck dailyCheck2 = dailyCheckService.queryDailyCheck(id);
			String fileNames = dailyCheck2.getPhoto();
			if (null != photoFile && photoFile.length > 0){
				String pics = PathTool.alterFile(delphotos, photoFile, fileNames, request);
				dailyCheck.setPhoto(pics);
			}else{
				dailyCheck.setPhoto(fileNames);
			}
			try{
				dailyCheckService.updateDailyCheck(dailyCheck);
			return Msg.success();
			}catch(Exception e){
				e.printStackTrace();
				return Msg.fail();
			}
		}
		else if(type==2){
			BuildingsPhoto buildingsPhoto = objectMapper.readValue(jsonStr, BuildingsPhoto.class);
			int id = buildingsPhoto.getId();
			BuildingsPhoto buildingsPhoto2 = buildingsPhotoService.queryBuildingsPhoto(id);
			String fileNames = buildingsPhoto2.getPhoto();
			String pics = PathTool.alterFile(delphotos, photoFile, fileNames, request);
			buildingsPhoto.setPhoto(pics);
			try{
			buildingsPhotoService.updateBuildingsPhoto(buildingsPhoto);
			return Msg.success();
			}catch(Exception e){
				e.printStackTrace();
				return Msg.fail();
			}
		}
		else if(type==3){
			Damage damage = objectMapper.readValue(jsonStr, Damage.class);
			int id = damage.getId();
			Damage damage2 = damageService.queryDamage(id);
			String fileNames = damage2.getPhoto();
			String pics = PathTool.alterFile(delphotos, photoFile, fileNames, request);
			damage.setPhoto(pics);
			try{
			damageService.updateDamage(damage);
			return Msg.success();
			}catch(Exception e){
				e.printStackTrace();
				return Msg.fail();
			}
		}
		else if(type==4){
			Implimentation implimentation = objectMapper.readValue(jsonStr, Implimentation.class);
			int id = implimentation.getId();
			Implimentation implimentation2 = implimentationService.queryImplimentation(id);
			String fileNames = implimentation2.getPhoto();
			String pics = PathTool.alterFile(delphotos, photoFile, fileNames, request);
			implimentation.setPhoto(pics);
			try{
				implimentationService.updateImplimentation(implimentation);
			return Msg.success();
			}catch(Exception e){
				e.printStackTrace();
				return Msg.fail();
			}
		}
		else if(type==5){
			Archaeology archaeology = objectMapper.readValue(jsonStr, Archaeology.class);
			int id = archaeology.getId();
			Archaeology archaeology2 = archaeologyService.queryArchaeology(id);
			String fileNames = archaeology2.getPhoto();
			String pics = PathTool.alterFile(delphotos, photoFile, fileNames, request);
			archaeology.setPhoto(pics);
			try{
				archaeologyService.updateArchaeology(archaeology);
			return Msg.success();
			}catch(Exception e){
				e.printStackTrace();
				return Msg.fail();
			}
		}
		else if(type==6){
			Tourist tourist = objectMapper.readValue(jsonStr, Tourist.class);
			int id = tourist.getId();
			Tourist tourist2 = touristService.queryTourist(id);
			String fileNames = tourist2.getPhoto();
			String pics = PathTool.alterFile(delphotos, photoFile, fileNames, request);
			tourist.setPhoto(pics);
			try{
			touristService.updateTourist(tourist);
			return Msg.success();
			}catch(Exception e){
				e.printStackTrace();
				return Msg.fail();
			}
		}
		else if(type==7){
			Others others = objectMapper.readValue(jsonStr, Others.class);
			int id = others.getId();
			Others others2 = othersService.queryOthers(id);
			String fileNames = others2.getPhoto();
			String pics = PathTool.alterFile(delphotos, photoFile, fileNames, request);
			others.setPhoto(pics);
			try{
			othersService.updateOthers(others);
			return Msg.success();
			}catch(Exception e){
				e.printStackTrace();
				return Msg.fail();
			}
		}
		else if(type==8){
			DailyCheck dailyCheck = objectMapper.readValue(jsonStr, DailyCheck.class);
			int id = dailyCheck.getId();
			DailyCheck dailyCheck2 = faultService.queryFault(id);
			String fileNames = dailyCheck2.getPhoto();

			String pics = PathTool.alterFile(delphotos, photoFile, fileNames, request);
			dailyCheck.setPhoto(pics);
			try{
			faultService.updateFault(dailyCheck);
			return Msg.success();
			}catch(Exception e){
				e.printStackTrace();
				return Msg.fail();
			}
		}
		return Msg.fail();
	}

	/*//手机端录入前获得选择框选项	
		@RequestMapping("/android_catalog_items")
		@ResponseBody
		public List<List<SelectOption>> getSelectOption(int type){
			List<List<SelectOption>> result = new ArrayList<List<SelectOption>>();
			if(type==1){
				List<SelectOption> list = selectOptionService.querySelectOptionList(0,18);				
				result.add(list);
			}
			if(type==2){
				List<SelectOption> list1 = selectOptionService.querySelectOptionList(0,19);
				List<SelectOption> list2 = selectOptionService.querySelectOptionList(0,20);
				List<SelectOption> list3 = selectOptionService.querySelectOptionList(0,21);
				result.add(list1);result.add(list2);result.add(list3);
			}
			return result;
		}*/
			
		//手机端登录，返回给手机端所有类型对应的选择框catalogId
		@RequestMapping("/android_catalog_ids")
		@ResponseBody
		public List<Catalog> getCatalogIds(){
			
			List<Catalog>list = new ArrayList<Catalog>();
			list.add(new Catalog(1,"日常巡查",Arrays.asList(CatalogIds.DAILYCHECK_CATALOGIDS)));
			list.add(new Catalog(2,"本体情况",Arrays.asList(CatalogIds.BUILDINGSPHOTO_CATALOGIDS)));
			list.add(new Catalog(3,"病害分析",Arrays.asList(CatalogIds.DAMAGE_CATALOGIDS)));
			list.add(new Catalog(4,"施工情况",Arrays.asList(CatalogIds.IMPLIMENTATION_CATALOGIDS)));
			list.add(new Catalog(5,"考古发掘",new ArrayList<Integer>()));
			list.add(new Catalog(6,"游客情况",new ArrayList<Integer>()));
			list.add(new Catalog(8,"故障处理",Arrays.asList(CatalogIds.DEVICE_FAULT)));
			list.add(new Catalog(9,"我的任务",Arrays.asList(CatalogIds.DEVICE_FAULT)));
			list.add(new Catalog(7,"其他",new ArrayList<Integer>()));
			/*map.put("1", Arrays.asList(CatalogIds.DAILYCHECK_CATALOGIDS));
			map.put("2", Arrays.asList(CatalogIds.BUILDINGSPHOTO_CATALOGIDS));
			map.put("3", Arrays.asList(CatalogIds.DAMAGE_CATALOGIDS));
			map.put("4", Arrays.asList(CatalogIds.IMPLIMENTATION_CATALOGIDS));*/
			return list;
		}

}




