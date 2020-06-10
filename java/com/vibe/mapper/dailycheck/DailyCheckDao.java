package com.vibe.mapper.dailycheck;

import com.vibe.pojo.DailyCheck;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyCheckDao {

    public void insertDailyCheck(DailyCheck dailyCheck);

    public void deleteDailyCheck(int parseInt);

    public DailyCheck queryDailyCheck(int id);

    public List<DailyCheck> queryDailyCheckList();

    public void updateDailyCheck(DailyCheck dailyCheck);

}
