package com.vibe.parse;

import java.util.List;

public class AtzgbMonitorParser extends BaseMonitorParser<AtzgbMonitor> {

    public static final String OUPUT_FILE_NAME = "init_atzgb_monitor.sql";

    public static final int GATE_ID_INDEX = 11;
    public static final int NODE_ID_INDEX = 12;
    public static final int VALUE_TYPE_INDEX = 13;

    @Override
    public void fillUniqueData(List<String> excelData, AtzgbMonitor data) {
        // TODO Auto-generated method stub
        data.setGateId(ExcelUtil.rvZeroAndDot(excelData.get(GATE_ID_INDEX)));
        data.setNodeId(ExcelUtil.rvZeroAndDot(excelData.get(NODE_ID_INDEX)));
        data.setValueType(ExcelUtil.rvZeroAndDot(excelData.get(VALUE_TYPE_INDEX)));
    }

    @Override
    public List<BaseService> getServices() {
        // TODO Auto-generated method stub
        return App.atzgbServiceParser.getServices();
    }

    @Override
    public BaseMonitorBean createBean() {
        // TODO Auto-generated method stub
        return new AtzgbMonitor();
    }

    @Override
    public String getOutputFileName() {
        // TODO Auto-generated method stub
        return OUPUT_FILE_NAME;
    }

}
