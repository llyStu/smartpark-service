package com.vibe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.pojo.Archaeology;
import com.vibe.pojo.BuildingsPhoto;
import com.vibe.pojo.DailyCheck;
import com.vibe.pojo.Damage;
import com.vibe.pojo.Implimentation;
import com.vibe.pojo.Others;
import com.vibe.pojo.ProtectProject;
import com.vibe.pojo.Tourist;
import com.vibe.pojo.UnionPolling;
import com.vibe.pojo.user.User;
import com.vibe.service.dailycheck.DailyCheckService;
import com.vibe.service.fault.OldFaultService;
import com.vibe.service.scene.ArchaeologyService;
import com.vibe.service.scene.BuildingsPhotoService;
import com.vibe.service.scene.DamageService;
import com.vibe.service.scene.ImplimentationService;
import com.vibe.service.scene.OthersService;
import com.vibe.service.scene.ProtectProjectService;
import com.vibe.service.scene.TouristService;
import com.vibe.service.user.UserService;
import com.vibe.util.Msg;

@Controller
public class PageController2 {
	@Autowired
	private UserService userService;

	@Autowired
	private BuildingsPhotoService buildingsPhotoService;
	@Autowired
	private ProtectProjectService protectProjectService;
	@Autowired
	private DailyCheckService dailyCheckService;
	@Autowired
	private TouristService touristService;
	@Autowired
	private ArchaeologyService archaeologyService;
	@Autowired
	private ImplimentationService implimentationService;
	@Autowired
	private DamageService damageService;
	@Autowired
	private OthersService othersService;
	@Autowired
	private OldFaultService faultService;
	
	/*时间有限，本该新建Damage对应的dao和service，但暂时在BuildingsPhotoService
	 * 中添加queryDamage方法查询所有实测录入列表,后期改过来
	 * @Autowired
	private DamageService DamageService;*/
	
	
	//给文物档案的分页方法
	/*@RequestMapping("/page_div")
	@ResponseBody
	public Msg pageDivWithJson(@RequestParam("pageNum") int pageNum){
		PageHelper.startPage(pageNum, 10);
		List<Relic>relics = relicService.listRelic();
		PageInfo pageInfo = new PageInfo(relics);		
		return Msg.success().add("pageInfo", pageInfo);
	}*/
	//给本体照片的分页方法
	@RequestMapping("/page_div_building")
	@ResponseBody
	public Msg pageDiv_building(@RequestParam("pageNum") int pageNum){
		PageHelper.startPage(pageNum, 10);
		List<BuildingsPhoto>buildingsPhotos = buildingsPhotoService.listBuildingsPhoto();
		PageInfo pageInfo = new PageInfo(buildingsPhotos);		
		return Msg.success().add("pageInfo", pageInfo);
	}
	
	//给保护工程的分页方法
	@RequestMapping("/page_div_protect")
	@ResponseBody
	public Msg pageDiv_protect(@RequestParam("pageNum") int pageNum) {
		PageHelper.startPage(pageNum, 10);
		List<ProtectProject> protectProjects = protectProjectService.listProtectProject();
		PageInfo pageInfo = new PageInfo(protectProjects);
		return Msg.success().add("pageInfo", pageInfo);
	}
	
	//给日常巡检的分页方法
		@RequestMapping("/page_div_dailyCheck")
		@ResponseBody
		public Msg pageDiv_dailyCheck(@RequestParam("pageNum") int pageNum) {
			PageHelper.startPage(pageNum, 10);
			List<DailyCheck> dailyChecks = dailyCheckService.queryDailyCheckList();
			if(dailyChecks !=null){
				for (DailyCheck dailyCheck : dailyChecks) {
					if(dailyCheck != null){
						dailyCheck.setType(1);
						int person = dailyCheck.getPerson();
						User user = userService.queryUserById(person);
						if(user != null){
							dailyCheck.setPersonName(user.getName());
						}
					}
				}
			}
			PageInfo pageInfo = new PageInfo(dailyChecks);
			return Msg.success().add("pageInfo", pageInfo);
		}
	//给日常巡检的分页方法
		@RequestMapping("/page_div_fault")
		@ResponseBody
		public Msg pageDiv_fault(@RequestParam("pageNum") int pageNum) {
			PageHelper.startPage(pageNum, 10);
			List<DailyCheck> dailyChecks = faultService.queryFaultList();
			if(dailyChecks !=null){
				for (DailyCheck dailyCheck : dailyChecks) {
					if(dailyCheck != null){
						dailyCheck.setType(8);
						int person = dailyCheck.getPerson();
						User user = userService.queryUserById(person);
						if(user != null){
							dailyCheck.setPersonName(user.getName());
						}
					}
				}
			}
			PageInfo pageInfo = new PageInfo(dailyChecks);
			return Msg.success().add("pageInfo", pageInfo);
		}
		// 给考古挖掘的分页方法
		@RequestMapping("/page_div_archaeology")
		@ResponseBody
		public Msg pageDiv_archaeology(@RequestParam("pageNum") int pageNum) {
			PageHelper.startPage(pageNum, 10);
			List<Archaeology> archaeologys = archaeologyService.queryArchaeologyList();
			PageInfo pageInfo = new PageInfo(archaeologys);
			return Msg.success().add("pageInfo", pageInfo);
		}
		
	// 给游客情况的分页方法
	@RequestMapping("/page_div_tourist")
	@ResponseBody
	public Msg pageDiv_tourist(@RequestParam("pageNum") int pageNum) {
		PageHelper.startPage(pageNum, 10);
		List<Tourist> tourists = touristService.queryTouristList();
		PageInfo pageInfo = new PageInfo(tourists);
		return Msg.success().add("pageInfo", pageInfo);
	}
	// 给施工情况的分页方法
		@RequestMapping("/page_div_implimentation")
		@ResponseBody
		public Msg pageDiv_implimentation(@RequestParam("pageNum") int pageNum) {
			PageHelper.startPage(pageNum, 10);
			List<Implimentation> implimentations = implimentationService.queryImplimentationList();
			PageInfo pageInfo = new PageInfo(implimentations);
			return Msg.success().add("pageInfo", pageInfo);
		}
	// 给实测录入总列表的分页方法
	@RequestMapping("/page_div_unionPolling")
	@ResponseBody
	public Msg pageDiv_unionPolling(@RequestParam("pageNum") int pageNum) {
		PageHelper.startPage(pageNum, 10);
		List<UnionPolling>unionPollings = buildingsPhotoService.queryUnionPollingList();		
		PageInfo pageInfo = new PageInfo(unionPollings);
		return Msg.success().add("pageInfo", pageInfo);
	}
	
	// 给实测录入总列表的分页方法
		@RequestMapping("/page_div_damage")
		@ResponseBody
		public Msg pageDiv_damage(@RequestParam("pageNum") int pageNum) {
			PageHelper.startPage(pageNum, 10);
			List<Damage>damages = damageService.queryDamageList();		
			PageInfo pageInfo = new PageInfo(damages);
			return Msg.success().add("pageInfo", pageInfo);
		}
		// 给实测录入总列表的分页方法
	@RequestMapping("/page_div_others")
	@ResponseBody
	public Msg pageDiv_others(@RequestParam("pageNum") int pageNum) {
		PageHelper.startPage(pageNum, 10);
		List<Others> otherss = othersService.queryOthersList();
		PageInfo pageInfo = new PageInfo(otherss);
		return Msg.success().add("pageInfo", pageInfo);
	}
}
