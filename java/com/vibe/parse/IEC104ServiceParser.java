package com.vibe.parse;

import java.util.List;

public class IEC104ServiceParser extends BaseServiceParser<IEC104Service> {

    public static final String OUPUT_FILE_NAME = "init_iec104_service.sql";

    public static final int IP_INDEX = 3;
    public static final int PORT_INDEX = 4;
    public static final int COMMON_ADDR_STR_INDEX = 5;

    @Override
    public void fillUniqueData(List<String> excelData, IEC104Service data) {
        // TODO Auto-generated method stub
        data.setIp(ExcelUtil.rvZeroAndDot(excelData.get(IP_INDEX)));
        data.setPort(ExcelUtil.rvZeroAndDot(excelData.get(PORT_INDEX)));
        data.setCommonAddrStr(ExcelUtil.rvZeroAndDot(excelData.get(COMMON_ADDR_STR_INDEX)));
    }

    @Override
    public BaseService createBean() {
        // TODO Auto-generated method stub
        return new IEC104Service();
    }

    @Override
    public String getOutputFileName() {
        // TODO Auto-generated method stub
        return OUPUT_FILE_NAME;
    }

}
