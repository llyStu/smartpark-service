package com.vibe.parse;

import java.util.List;

public class OpcMonitorParser extends BaseMonitorParser<OpcMonitor> {

    public static final String OUPUT_FILE_NAME = "init_opc_monitor.sql";

    public static final int ITEMTAG_TYPE_INDEX = 11;


    @Override
    public BacnetMonitor createBean() {
        // TODO Auto-generated method stub
        return new BacnetMonitor();
    }

    @Override
    public String getOutputFileName() {
        // TODO Auto-generated method stub
        return OUPUT_FILE_NAME;
    }

    @Override
    public void fillUniqueData(List<String> excelData, OpcMonitor data) {
        // TODO Auto-generated method stub
        data.setItemTag(excelData.get(ITEMTAG_TYPE_INDEX));
    }

    @Override
    public List<BaseService> getServices() {
        // TODO Auto-generated method stub
        return App.opcServiceParser.getServices();
    }

}
