package com.vibe.service.dept;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.dept.DeptMapper;
import com.vibe.pojo.user.Department;
import com.vibe.utils.DeptJson;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.EasyUITreeNode;
import com.vibe.utils.TreeNode;

@Service
public class DeptServiceImpl implements DeptService {
	//注入代理类的接口
	@Autowired
	private DeptMapper deptMapper;
	/**
	 * 
	 * 初始化部门树
	 * 方法名deptTreeData
	 * 参数 id
	 * 返回值List<EasyUITreeNode>
	 */
	@Override
	public List<EasyUITreeNode> queryDeptTreeData(Integer id) {
		//创建要封装返回值
		List<EasyUITreeNode>  treeList=new ArrayList<EasyUITreeNode>();
		//获取查询数据
		List<Department> queryDeptTreeData = deptMapper.queryDeptTreeData(id);
		//遍历数据封装节点
		if(queryDeptTreeData !=null &&queryDeptTreeData.size()>0){
			for (Department department : queryDeptTreeData) {
				EasyUITreeNode treeNode = new EasyUITreeNode();
				// 封装id
				treeNode.setId(department.getId());
				//封装text,节点的名称
				treeNode.setText(department.getName());
				//封装state，是否是叶子节点
				treeNode.setState("closed");
				treeList.add(treeNode);
			}
		}
	
		return treeList;
	}
	//获取分类的树
		@Override
		public List<TreeNode> getAllDeptTree(int id) {
			List<TreeNode> list = new ArrayList<TreeNode>();
			List<Department> deptList = deptMapper.queryDeptTreeData(id);;
			if(deptList != null && deptList.size()>0){
				for (Department dept : deptList) {
					TreeNode node = printDeptTree(dept);
					list.add(node);
				}
			}
			return list;
		}
		//获取树的递归方法
		public TreeNode printDeptTree(Department dept){
			TreeNode treeNode = new TreeNode();
			int id = dept.getId();
			treeNode.setId(id);
		    treeNode.setText(dept.getName());
			List<Department> deptList = deptMapper.queryDeptTreeData(id);
			if(deptList != null && deptList.size()>0){
				for (Department child : deptList) {
					 TreeNode childNode = printDeptTree(child);
					 if(childNode != null){
						 treeNode.addChild(childNode);
					 }
				}
			}		
			return treeNode;
		}
	/**
	 * 查询所有部门初始化部门列表
	 * 方法名：deptList
	 * 参数：本身部门的id
	 * 返回值：List<DeptJson>
	 */
	
	@Override
	public List<DeptJson> deptList() {
		//遍历查询结果，封装对象
		List<Department> queryDeptList = deptMapper.queryDeptList(null);
		return setDeptJsonList(queryDeptList);
	}
	/**
	 * 封装下拉列表的数据
	 * @param queryDeptList
	 * @return
	 */
	private List<DeptJson> setDeptJsonList(List<Department> queryDeptList) {
		List<DeptJson> jsonList=new ArrayList<DeptJson>();
		if(queryDeptList !=null &&queryDeptList.size()>0){
			for (Department dept : queryDeptList) {
				DeptJson deptJson = new DeptJson();
				deptJson.setId(dept.getId());
				deptJson.setName(dept.getName());
				jsonList.add(deptJson);
			}
		}
		return jsonList;
	}
	/**
	 * 查询部门信息，根据部门id
	 */
	@Override
	public Department queryDeptById(Integer department) {
		// TODO Auto-generated method stub
		return deptMapper.queryDeptById(department);
	}
	
	/**
	 * 根据部门id，分页查询员工列表
	 */
	@Override
	public EasyUIJson queryDeptListByPage(Department dept, Integer page, Integer rows) {
		
				//设置分页参数
				PageHelper.startPage(page, rows);		
				//调用接口查询数据
				List<Department> deptList =deptMapper.queryDeptList(dept) ;
				//创建PageInfo对象，获取分页信息
				PageInfo<Department> pageInfo = new PageInfo<Department>(deptList);
				//创建EasyUIJson对象，封装查询结果
				EasyUIJson uiJson = new EasyUIJson();
				//设置查询总记录数
				uiJson.setTotal(pageInfo.getTotal());
				//设置查询记录
				uiJson.setRows(deptList);
				
		return uiJson;
	}
	
	/**
	 * 
	 * 添加用户
	 * 方法名称：adddept
	 * 参数 类型:dept dept
	 * 返回值类型: void
	 */
	@Override
	public void addDept(Department dept) {
		Department deptById = deptMapper.queryDeptById(dept.getParent());
		if (null != deptById && null != deptById.getLevel()){
			dept.setLevel(deptById.getLevel() + 1);
		}else{
			dept.setLevel(0);
		}
		deptMapper.saveDepttoDB(dept);
	}
	/**
	 * 编辑用户信息
	 * 方法:editDeptById
	 * 参数 ：用户id
	 * 
	 */
	@Override
	public void editDeptById(Department dept) {
		Integer did=dept.getId();
		Department deptById = deptMapper.queryDeptById(did);
		deptById.setName(dept.getName());
		deptById.setAbbr(dept.getAbbr());
		deptById.setParent(dept.getParent());
		deptMapper.editDeptById(deptById);
	}
	
	/**
	 *伪删除部门信息
	 *请求：dept/deleteDept
	 *方法：deleteDept
	 *参数：ids要删除的用户id串
	 *返回值:void
	 */
	
	@Override
	public void deleteDept(String ids) {
		// 用，号切成数组
		String[] split = ids.split(",");
		//遍历数组
		for (String string : split) {
			int id = Integer.parseInt(string);
			Department deptById = deptMapper.queryDeptById(id);
			//修改状态
			deptById.setValid(0);
			deptMapper.editDeptById(deptById);
		}
		
	}
	/**
	 * 获取父部门列表
	 */
	@Override
	public List<DeptJson> deptParentList(Integer id) {
		List<DeptJson> deptJsonList = null;
		List<Integer> list = new ArrayList<Integer>();
		list.add(id);
		if(id != null && id != 0){
			 List<Integer> deptIds = queryDeptIds(id,list);
			 if(deptIds != null && deptIds.size()>0 ){
				List<Department> deptList = deptMapper.queryDeptByIds(deptIds);
				 deptJsonList = setDeptJsonList(deptList);
			 }
		}
		return  deptJsonList;
	}
	/**
	 * 获取选中节点的所有后代节点的id对应的实例
	 * @param id
	 * @param list
	 * @return
	 */
	public List<Integer>  queryDeptIds(Integer id,List<Integer> list){
		Department dept = new Department();
		dept.setParent(id);
		  List<Department> queryDeptList = deptMapper.queryDeptList(dept);
		if(queryDeptList != null){
			for (Department department : queryDeptList) {
				Integer parentId = department.getId();
				list.add(parentId);
				 queryDeptIds(parentId,list);
			}
		}
		return list;
	}
}
