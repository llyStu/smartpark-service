package com.vibe.service.energy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.mapper.energy.EnergyDao;
import com.vibe.pojo.KeyValueBean;

@Service
public class EnergyServiceImpl extends BaseEnergyServiceImpl implements EnergyService {
    @Autowired
    EnergyDao energyDao;

    private EnergyServiceTime fenxiangduibiEnergyServiceTime = new EnergyServiceTime() {

        @Override
        public List<?> searchByTime(String timeType, String time) {
            // TODO Auto-generated method stub
            return energyDao.fenxiangduibiTime(timeType, time);
        }

        @Override
        public List<?> searchByStartEndTime(String timeType, String start, String end) {
            // TODO Auto-generated method stub
            return energyDao.fenxiangduibiStartEndTime(timeType, start, end);
        }
    };

    private EnergyServiceTime wangqitongjiEnergyServiceTime = new EnergyServiceTime() {

        @Override
        public List<?> searchByTime(String timeType, String time) {
            // TODO Auto-generated method stub
            return energyDao.fenxiangduibiTime(timeType, time);
        }

        @Override
        public List<?> searchByStartEndTime(String timeType, String start, String end) {
            // TODO Auto-generated method stub
            return energyDao.fenxiangduibiStartEndTime(timeType, start, end);
        }
    };

    @SuppressWarnings("unchecked")
    @Override
    public List<KeyValueBean> fenxiangduibi(String timeType, String time, String start, String end) {
        // TODO Auto-generated method stub
        return (List<KeyValueBean>) doSearchByTimeWork(timeType, time, start, end, fenxiangduibiEnergyServiceTime);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<KeyValueBean> wangqitongji(String timeType, String time, String start, String end) {
        // TODO Auto-generated method stub
        return (List<KeyValueBean>) doSearchByTimeWork(timeType, time, start, end, wangqitongjiEnergyServiceTime);
    }

}
