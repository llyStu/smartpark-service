package com.vibe.controller.asset;

import com.vibe.poiutil.ExportDevice;
import com.vibe.poiutil.ExportExcel;
import com.vibe.pojo.TreeAlarmData;
import com.vibe.service.device.DeviceService;
import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetException;
import com.vibe.monitor.asset.AssetKind;
import com.vibe.monitor.asset.Space;
import com.vibe.pojo.AssetVo;
import com.vibe.pojo.user.User;
import com.vibe.service.asset.AssetService;
import com.vibe.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DeviceController {
					@Autowired
					private AssetService assetService;
					@Autowired
					private UserService userService;
					@Autowired
					private DeviceService deviceService;

					/*
					 * 移动端通过扫描二维码，获取该设备的详细信息
					 *
					 */
					@RequestMapping("device/findDevice")
					public @ResponseBody AssetVo findDevice(@RequestParam(value="id") String id) throws AssetException{
						AssetVo vo = new AssetVo();
						if(id !=null){
							Integer assetId=Integer.parseInt(id);
							vo.setId(assetId);
						}
						AssetVo assetVo=assetService.findDevice(vo);
						if(assetVo != null){
							Integer location = assetVo.getLocation();
							if(location != null){
								Asset<?> asset = assetService.findAssetByID(location,AssetKind.SPACE.toString());
								Space space=(Space)asset;
				assetVo.setSpaceCaption(space.getCaption());
			}
			User user = assetVo.getKeepers();
			if(user != null){
				Integer uid = user.getId();
				User user2 = userService.queryUserById(uid);
				if(user2 !=null){
					assetVo.setUserName(user2.getName());
				}
			}
		}
		return assetVo;
	}
	//
	@GetMapping("/device/printOutDevice")
	@ResponseBody
	public ResponseEntity<byte[]> printOutDevice(String ids) {
		//test
		try {
			return deviceService.printDevice(ids);
//			ResponseEntity<byte[]> filebyte = new ResponseEntity<byte[]>(out.toByteArray(),headers1, HttpStatus.OK);
//			return ResponseModel.success(filebyte).code(ResultCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
//			return ResponseModel.failure("错误" + e.getMessage()).code(ResultCode.ERROR);
		}
		return null;
	}

	/*@GetMapping("/findAsset/printQrcode")
	@ResponseBody
	public ResponseEntity<byte[]> printOutDevice(String ids) {
		//test
		try {
			ResponseEntity<byte[]> filebyte = deviceService.printDevice(ids);
			return filebyte ;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}*/
}
