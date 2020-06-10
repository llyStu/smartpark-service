package com.vibe.mapper.user;

import com.vibe.pojo.Module;

import java.util.List;

/**
 * 权限的接口
 *
 * @author FLex3
 */
public interface ModuleMapper {

    /*
     * 查询所有模块信息
     * 方法名：queryModuleList
     * 参数：上级模块的id
     * 返回值：List<Module>
     */
    public List<Module> queryModuleList(Integer parent);


}
