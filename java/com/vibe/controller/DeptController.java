package com.vibe.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.vibe.pojo.user.Department;
import com.vibe.service.dept.DeptService;
import com.vibe.service.logAop.MethodLog;
import com.vibe.utils.DeptJson;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.EasyUITreeNode;
import com.vibe.utils.FormJson;
import com.vibe.utils.TreeNode;
import com.vibe.common.Application;
import com.vibe.common.id.IdGenerator;

@Controller
public class DeptController {

    @Autowired
    DeptService deptService;
    @Autowired
    Application application;

    /*异步初始化部门树的数据*/
    @RequestMapping("/dept/treeData")
    public @ResponseBody
    List<EasyUITreeNode> treeData(@RequestParam(defaultValue = "0", value = "id") Integer parent) {
        List<EasyUITreeNode> treeList = deptService.queryDeptTreeData(parent);
        return treeList;

    }

    /*初始化整棵树*/
    @RequestMapping("/dept/treeAllData")
    public @ResponseBody
    List<TreeNode> treeAllData(@RequestParam(defaultValue = "0", value = "id") Integer parent) {
        List<TreeNode> treeList = deptService.getAllDeptTree(parent);
        return treeList;

    }

    /*加载修改页面的部门下拉列表*/
    @RequestMapping("/dept/deptList")
    public @ResponseBody
    List<DeptJson> deptList() {
        List<DeptJson> deptList = deptService.deptList();
        return deptList;
    }

    /*加载修改页面的部门下拉列表,不包括本身以及子部门*/
    @RequestMapping("/dept/deptParentList")
    public @ResponseBody
    List<DeptJson> deptParentList(Integer id) {
        List<DeptJson> deptList = deptService.deptParentList(id);
        return deptList;
    }

    /*
     * 员工列表 请求：dept/deptList/{id}; 方法：querydeptList 参数：id 父部门id,分页组建1,10
     * 返回值：EasyUIJson 返回值page
     *
     */
    @RequestMapping("/dept/queryDeptList")
    public @ResponseBody
    EasyUIJson queryDeptList(Department dept, @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "20") Integer rows) {
        // 远程调用Service服务对象，获取员工列表数据

        EasyUIJson easyUIJson = deptService.queryDeptListByPage(dept, page, rows);
        return easyUIJson;
    }

    /*
     * 新增员工 请求：dept/deptAdd 方法：deptAdd 参数：表单参数 返回值：String 到emloyeeList
     */
	/*@RequestMapping("/dept/deptAdd")
	public String deptAdd(Department dept, Model model) {
		IdGenerator<Integer> gen = application.getIntIdGenerator("dept");
		dept.setDid(gen.next());
		deptService.addDept(dept);

		model.addAttribute("id", dept.getDid());

		return "/system/dept/deptList";

	}
*/
    @RequestMapping("/dept/deptAdd")
    @MethodLog(remark = "add", option = "添加部门信息")
    public @ResponseBody
    String deptAdd(Department dept) {
        try {
//			IdGenerator<Integer> gen = application.getIntIdGenerator("dept");
//			dept.setId(gen.next());
            deptService.addDept(dept);
            return "200";
        } catch (Exception e) {
            e.printStackTrace();
            return "500";
        }
    }

    /*
     * 跳到修改页面，加载部门信息 请求路径：/dept/todeptEdit/${id} 方法：todeptEdit 参数：部门id,用户id
     * 返回值：String 到修改页面deptEdits
     *
     */
    @RequestMapping("/dept/toDeptEdit/{id}")
    public String toDepatEdit(@PathVariable Integer id, Model model) {
        Department dept = deptService.queryDeptById(id);
        model.addAttribute("dept", dept);
        return "/system/dept/deptEdit";
    }

    @RequestMapping("/dept/toDeptEdit")
    public @ResponseBody
    Department toDeptEdit(Integer id) {

        Department dept = deptService.queryDeptById(id);
        return dept;
    }

    // 跳转到列表
    @RequestMapping("/dept/toList/{id}")
    public String toDeptList(@PathVariable Integer id, Model model) {
        model.addAttribute("id", id);
        return "/system/dept/deptList";
    }

    /*
     * 查询用户详情信息 请求路径：/dept/deptDetail/{ids} 方法：deptDetail 参数：用户ids 返回值：String
     * 到详情页
     *
     */
    @RequestMapping("/dept/deptDetail/{ids}")
    public @ResponseBody
    List<Department> deptDetail(@PathVariable String ids, Model model) {
        String[] split = ids.split(",");
        String id = split[0];
        // 查询用户信息根据用户id
        List<Department> list = new ArrayList<Department>();
        Department dept = deptService.queryDeptById(Integer.parseInt(id));
        /*
         * Department dept = deptService.queryDeptById(dept.getDepartment());
         * model.addAttribute("dept",dept); model.addAttribute("dept",dept);
         */
        list.add(dept);
        return list;

    }

    /*
     * 修改员工信息 请求：dept/deptEdit 方法：deptEdit 参数：dept 返回值:String
     */
    @RequestMapping("/dept/deptEdit")
    @MethodLog(remark = "edit", option = "编辑部门信息")
    public @ResponseBody
    FormJson deptEdit(Department dept) {
        FormJson json = new FormJson();
        try {
            deptService.editDeptById(dept);
            json.setSuccess(true);
            json.setMessage("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMessage("操作失败");
        }
        return json;
    }

    /*
     * 删除员工信息 请求：dept/deletedept 方法：deletedept 参数：ids要删除的用户id,该部门id 返回值:String
     */
    @RequestMapping("/dept/deleteDept")
    @MethodLog(remark = "delete", option = "删除部门信息")
    public @ResponseBody
    String deleteDept(String ids) {
        deptService.deleteDept(ids);
        return "200";
    }

}
