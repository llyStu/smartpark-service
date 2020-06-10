package com.vibe.service.dailycheck;

import java.util.List;

import com.vibe.pojo.CheckRoute;

public interface CheckRouteService {

    public abstract void insert(CheckRoute checkRoute);

    public abstract Integer queryMaxId();

    public abstract List<CheckRoute> queryForList(int orderId);
}
