package com.vibe.parse;

public class ModbusMonitor extends BaseMonitorBean {

    private String registerAddr;
    private String dataType;

    public String getRegisterAddr() {
        return registerAddr;
    }

    public void setRegisterAddr(String registerAddr) {
        this.registerAddr = registerAddr;
    }


    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        if (App.TEST) {
            return super.toString();
        } else {
            return super.toString() + "insert into t_asset_prop(asset, name, value) values (" + getId()
                    + ", \"RegisterAddr\", \"" + getRegisterAddr() + "\");";
        }

    }

    @Override
    public String getNotTestMonitorType() {
        // TODO Auto-generated method stub
        return getDataType();
    }

    @Override
    public String getAiType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAoType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDiType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDoType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }


}
