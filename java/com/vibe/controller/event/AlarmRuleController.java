package com.vibe.controller.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vibe.pojo.event.AlarmRuleMessage;
import com.vibe.pojo.event.EventRank;
import com.vibe.pojo.event.RuleRankRelation;
import com.vibe.service.alarm.AlarmRuleService;
import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;
import com.vibe.utils.EasyUIJson;

@RestController
public class AlarmRuleController {

	@Autowired
	private AlarmRuleService alarmRuleService;
	/**
	 * 查询报警规则列表
	 * @param page
	 * @param rows
	 * @param alarmRule
	 * @return
	 */
	@RequestMapping("/alarm/queryAlarmRules")
	public ResponseModel<EasyUIJson> queryAlarmRules(@RequestParam(defaultValue = "1") Integer page,
													 @RequestParam(defaultValue = "10") Integer rows, @RequestBody AlarmRuleMessage alarmRule) {
		EasyUIJson queryAlarmRules = alarmRuleService.queryAlarmRules(page,rows,alarmRule);
		return ResponseModel.success(queryAlarmRules).code(ResultCode.SUCCESS);
	}
	/**
	 * 查询事件等级
	 * @param rankId
	 * @return
	 */
	@RequestMapping("/alarm/eventRanks")
	public ResponseModel<List<EventRank>> queryEventRanks(Integer rankId) {
		List<EventRank> queryEventRanks = alarmRuleService.queryEventRanks(rankId);
		return ResponseModel.success(queryEventRanks).code(ResultCode.SUCCESS);
	}
	/**
	 * 更新事件等级
	 * @param ids
	 * @param eventId
	 * @return
	 */
	@RequestMapping("/alarm/updateAssetEventRank")
	public ResponseModel<String> updateAssetEventRank(Integer[] ids,Integer rankId) {
		return alarmRuleService.updateAssetEventRank(ids,rankId);
	}
	/**
	 * 删除事件规则
	 * @param ruleRankRelations
	 * @return
	 */
	@RequestMapping("/alarm/deleteEventRank")
	public ResponseModel<String> deleteEventRank(@RequestBody List<RuleRankRelation> ruleRankRelations) {
		return alarmRuleService.deleteEventRank(ruleRankRelations);
	}



}
