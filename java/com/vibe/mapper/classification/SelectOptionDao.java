package com.vibe.mapper.classification;

import com.vibe.pojo.CommonSelectOption;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectOptionDao {

    public Integer getParentId(
            @Param("id") int id,
            @Param("type") int type
    );

    public List<CommonSelectOption> querySelectOptionList(@Param(value = "parentId") Integer parentId, @Param(value = "catalogId") Integer catalogId);

    public List<Integer> queryIdListByParent(int parentId);

    public List<CommonSelectOption> anQuerySelectOptionList(int catalogId);

    public CommonSelectOption getSelectOption(int id, int catalogId);

    public CommonSelectOption getSelectListData(int id);

    public List<Integer> getSelectIdList(@Param(value = "catalogId") Integer catalogId, @Param(value = "catalog") Integer catalog);

    public List<CommonSelectOption> querySelectOptionListbyArray(
            @Param(value = "list") List<Integer> list,
            @Param(value = "catalogId") int catalogId);

    public List<CommonSelectOption> getWorkItems(@Param(value = "parentId") int parentId, @Param(value = "catalogId") int catalogId);

    public List<Integer> queryMenusCodes(@Param(value = "menu") Integer menu, @Param(value = "catalog") Integer catalog);

    public List<Integer> getIdByCatlog(
            @Param("catlog") int catlog
    );

    public List<Integer> getSystem(
            @Param("catlog") int catlog
    );

    public Integer getSystemItem(@Param("caption") String caption);
}
