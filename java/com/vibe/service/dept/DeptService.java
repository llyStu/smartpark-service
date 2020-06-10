package com.vibe.service.dept;

import java.util.List;

import com.vibe.pojo.user.Department;
import com.vibe.utils.DeptJson;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.EasyUITreeNode;
import com.vibe.utils.TreeNode;

/**
 * 部门服务类
 *
 * @author FLex3
 */
public interface DeptService {
    /**
     * 初始化部门树
     * 方法名：queryDeptTreeData
     * 参数 id
     * 返回值List<EasyUITreeNode>
     */
    public List<EasyUITreeNode> queryDeptTreeData(Integer id);

    /**
     * 查询所有部门初始化部门列表
     * 方法名：deptList
     * <p>
     * 返回值：List<DeptJson>
     */

    public List<DeptJson> deptList();

    /**
     * 查询部门信息
     *
     * @param department
     * @return
     */
    public Department queryDeptById(Integer department);

    /**
     * 分页查询列表
     *
     * @param Department
     * @param page
     * @param rows
     * @return
     */

    public EasyUIJson queryDeptListByPage(Department dept, Integer page, Integer rows);

    /**
     * 保存部门信息
     *
     * @param dept
     */
    public void addDept(Department dept);

    /**
     * 编辑用户信息
     * 方法:editUserById
     * 参数 ：userVo
     * 返回值:User
     */
    public void editDeptById(Department dept);

    /**
     * 伪删除员工信息
     * 请求：user/deleteUser
     * 方法：deleteUser
     * 参数：ids要删除的用户id串
     * 返回值:void
     */
    public void deleteDept(String ids);

    /**
     * @param id
     * @return
     */
    List<TreeNode> getAllDeptTree(int id);

    /*获取该部门的父部门列表*/
    public List<DeptJson> deptParentList(Integer id);

}
