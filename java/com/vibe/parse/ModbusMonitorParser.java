package com.vibe.parse;

import java.util.List;

public class ModbusMonitorParser extends BaseMonitorParser<ModbusMonitor> {

    public static final String OUPUT_FILE_NAME = "init_modbus_monitor.sql";

    public static final int REGISTERADDR_TYPE_INDEX = 11;
    public static final int DATATYPE_TYPE_INDEX = 12;


    @Override
    public ModbusMonitor createBean() {
        // TODO Auto-generated method stub
        return new ModbusMonitor();
    }

    @Override
    public String getOutputFileName() {
        // TODO Auto-generated method stub
        return OUPUT_FILE_NAME;
    }

    @Override
    public void fillUniqueData(List<String> excelData, ModbusMonitor data) {
        // TODO Auto-generated method stub
        data.setRegisterAddr(ExcelUtil.rvZeroAndDot(excelData.get(REGISTERADDR_TYPE_INDEX)));
        data.setDataType(excelData.get(DATATYPE_TYPE_INDEX));
    }

    @Override
    public List<BaseService> getServices() {
        // TODO Auto-generated method stub
        return App.modbusServiceParser.getServices();
    }

}
