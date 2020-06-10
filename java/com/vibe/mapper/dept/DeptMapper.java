package com.vibe.mapper.dept;

import com.vibe.pojo.user.Department;

import java.util.List;

/**
 * 部门的接口
 * @author FLex3
 *
 */
public interface DeptMapper {
	/*
	 * 初始化部门树
	 * 方法名queryDeptTreeData
	 * 参数 id
	 * 返回值List<Department>
	 */
	public List<Department> queryDeptTreeData(Integer id);
	/*
	 * 查询所有部门初始化部门列表
	 * 方法名：queryDeptList
	 * 参数：
	 * 返回值：List<Department>
	 */
	public List<Department> queryDeptList(Department dept);
	/*
	 * 查询部门信息
	 * @param department
	 * @return
	 */
		public Department queryDeptById(Integer department);
		public void saveDepttoDB(Department dept);
		public void editDeptById(Department dept);
		/*
		 * 查询所有ids的数据
		 */
		public List<Department> queryDeptByIds(List<Integer> deptIds);
	
}
