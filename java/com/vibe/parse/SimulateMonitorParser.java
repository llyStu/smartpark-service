package com.vibe.parse;

import java.util.List;

public class SimulateMonitorParser extends BaseMonitorParser<SimulateMonitor> {

    public static final String OUPUT_FILE_NAME = "init_test_monitor.sql";

    @Override
    public void fillUniqueData(List<String> excelData, SimulateMonitor data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSource(List<List<String>> excelData, List<BaseMonitorBean> data) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<BaseService> getServices() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SimulateMonitor createBean() {
        // TODO Auto-generated method stub
        return new SimulateMonitor();
    }

    @Override
    public String getOutputFileName() {
        // TODO Auto-generated method stub
        return OUPUT_FILE_NAME;
    }

}
