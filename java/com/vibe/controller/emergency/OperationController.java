package com.vibe.controller.emergency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibe.pojo.emergency.Operation;
import com.vibe.pojo.emergency.OperationVo;
import com.vibe.service.emergency.OperationService;
import com.vibe.service.logAop.MethodLog;
import com.vibe.utils.FormJson;
import com.vibe.utils.Page;

@RestController
public class OperationController {
	@Autowired
	private OperationService os;

	@RequestMapping("/emergency/insertOperation")
	@MethodLog(remark="add",option="添加演练信息")
	public FormJson insertOperation(Operation oper) {
		return os.insertOperation(oper);
	}

	@RequestMapping("/emergency/deleteOperation")
	@MethodLog(remark="detete",option="删除演练信息")
	public FormJson deleteOperation(int[] oid) {
		return os.deleteOperation(oid);
	}

	@RequestMapping("/emergency/updateOperation")
	@MethodLog(remark="edit",option="编辑演练信息")
	public FormJson updateOperation(Operation oper) {
		return os.updateOperation(oper);
	}

	@RequestMapping("/emergency/queryOperation")
	public Page<Operation> queryOperation(OperationVo oper) {
		return os.queryOperation(oper, oper.getPageNum(), oper.getPageSize());
	}
}
