package com.vibe.controller;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.common.code.MonitorCodes;
import com.vibe.pojo.CommonSelectOption;
import com.vibe.service.classification.Code;
import com.vibe.service.classification.SelectOptionService;
import com.vibe.utils.TreeNode;

/*用于向前端页面显示联动选择框的选项
 * */

@Controller
public class CommonSelectOptionController {


    @Autowired
    private SelectOptionService selectOptionService;

    @RequestMapping("/getSelectOptionOther")
    @ResponseBody
    public List<CommonSelectOption> getSelectOptionOther(
            @RequestParam(value = "parentId", defaultValue = "0") int parentId,
            @RequestParam("catalogId") int catalogId) {
        List<CommonSelectOption> list = selectOptionService.querySelectOptionListOther(parentId, catalogId);
        return list;

    }

    @RequestMapping("/getSelectOption")
    @ResponseBody
    public List<CommonSelectOption> getItems(
            @RequestParam(value = "parentId", defaultValue = "0") int parentId,
            @RequestParam("catalogId") int catalogId) {
        List<CommonSelectOption> list = selectOptionService.querySelectOptionList(parentId, catalogId);
        return list;

    }

    @RequestMapping("/getSelectOptionByArray")
    @ResponseBody
    public List<CommonSelectOption> getSelectOptionByArray(
            @RequestParam("catalogId") int catalogId) {
        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(5);
        list1.add(34);
        List<CommonSelectOption> list = selectOptionService.querySelectOptionListbyArray(list1, catalogId);
        return list;

    }

    //手机端获取选择框选项
    @RequestMapping("/android_getSelectOption")
    @ResponseBody
    public List<CommonSelectOption> anGetItems(
            @RequestParam("catalogId") int catalogId) {
        List<CommonSelectOption> list = selectOptionService.anQuerySelectOptionList(catalogId);
        return list;
    }

    //构建分类模块的tree结构
    @RequestMapping("/getItemsTree")
    public @ResponseBody
    List<TreeNode> getItemsTree(
            @RequestParam(required = false) Integer catalogId) {
        if (catalogId == null) {
            catalogId = (int) MonitorCodes.MONITOR_KIND;
        }
        List<TreeNode> list = selectOptionService.getItemsTree(Code.ROOT_ID, catalogId);
        return list;
    }

    @RequestMapping("/getControlItemsTree")
    public @ResponseBody
    List<TreeNode> getControlItemsTree() {
        List<TreeNode> list = selectOptionService.getControlItemsTree(Code.ROOT_ID, MonitorCodes.MONITOR_KIND);
        return list;
    }

    @RequestMapping("/catalog/getItems")
    @ResponseBody
    public List<CommonSelectOption> getItem(
            @RequestParam("catalogId") int catalogId) {
        List<CommonSelectOption> list = selectOptionService.anQuerySelectOptionList(catalogId);
        return list;
    }


}
