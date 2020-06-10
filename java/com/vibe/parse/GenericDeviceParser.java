package com.vibe.parse;

import java.util.List;

public class GenericDeviceParser extends BaseDeviceParser<GenericDevice> {

    public static final String OUPUT_FILE_NAME = "init_generic_device.sql";


    @Override
    public GenericDevice createBean() {
        // TODO Auto-generated method stub
        return new GenericDevice();
    }

    @Override
    public String getOutputFileName() {
        // TODO Auto-generated method stub
        return OUPUT_FILE_NAME;
    }

    @Override
    public void fillUniqueData(List<String> excelData, GenericDevice data) {
        // TODO Auto-generated method stub

    }

}
