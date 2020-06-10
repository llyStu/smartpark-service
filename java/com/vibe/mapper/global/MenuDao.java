package com.vibe.mapper.global;

import com.vibe.pojo.MenuBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuDao {

	public List<MenuBean> getMenus();
	public List<MenuBean> queryMenuByParent(Integer parent);
	public Integer queryParentMenu(Integer child);
	public MenuBean queryMenuById(@Param("id") Integer id);
	public List<String> queryPermissionBymIdAndRid(@Param("id") Integer id, @Param("rid") Integer rid);
}
