package com.vibe.mapper.dailycheck;

import com.vibe.pojo.CheckRoute;

import java.util.List;

public interface CheckRouteDao {

    public abstract void insert(CheckRoute checkRoute);

    public abstract Integer queryMaxId();

    public abstract List<CheckRoute> queryForList(int orderId);
}
