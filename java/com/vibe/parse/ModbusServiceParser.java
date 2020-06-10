package com.vibe.parse;

import java.util.List;

public class ModbusServiceParser extends BaseServiceParser<ModbusService> {

    public static final String OUPUT_FILE_NAME = "init_modbus_service.sql";

    public static final int IP_INDEX = 3;
    public static final int PORT_INDEX = 4;
    public static final int UNITID_INDEX = 5;
    public static final int USERTU_INDEX = 6;

    @Override
    public ModbusService createBean() {
        // TODO Auto-generated method stub
        return new ModbusService();
    }

    @Override
    public String getOutputFileName() {
        // TODO Auto-generated method stub
        return OUPUT_FILE_NAME;
    }

    @Override
    public void fillUniqueData(List<String> excelData, ModbusService data) {
        // TODO Auto-generated method stub
        data.setIp(excelData.get(IP_INDEX));
        data.setPort(ExcelUtil.rvZeroAndDot(excelData.get(PORT_INDEX)));
        data.setUnitId(ExcelUtil.rvZeroAndDot(excelData.get(UNITID_INDEX)));
        data.setUseRtu(excelData.get(USERTU_INDEX));
    }

}
