package com.vibe.mapper.task;

import com.vibe.pojo.SingleProp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SinglePropDao {
    //query prop list by site id
    public abstract List<SingleProp> queryForList(@Param("taskId") int taskId,
                                                  @Param("catalog") int catalog);

    //insert a record into table t_single_prop
    public abstract void insert(SingleProp singleProp);

    //insert a record into table t_task_prop
    public abstract void insertIntoIntervalTable(@Param("taskId") int taskId,
                                                 @Param("propId") int propId);

    public abstract int queryMaxId();
}
