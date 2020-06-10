package com.vibe.parse;

public class AtzgbMonitor extends BaseMonitorBean {

    private String gateId;
    private String nodeId;
    private String valueType;


    public String getGateId() {
        return gateId;
    }

    public void setGateId(String gateId) {
        this.gateId = gateId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    @Override
    public String getAiType() {
        // TODO Auto-generated method stub
        return App.ATZGB_AI_TYPE;
    }

    @Override
    public String getAoType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDiType() {
        // TODO Auto-generated method stub
        return App.ATZGB_DI_TYPE;
    }

    @Override
    public String getDoType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        if (App.TEST) {
            return super.toString();
        } else {
            return super.toString() + "insert into t_asset_prop(asset, name, value) values (" + getId()
                    + ", \"GateId\", \"" + getGateId()
                    + "\");\ninsert into t_asset_prop(asset, name, value) values (" + getId() + ", \"NodeId\", \""
                    + getNodeId() + "\");\ninsert into t_asset_prop(asset, name, value) values (" + getId() + ", \"ValueType\", \""
                    + getValueType() + "\");";
        }
    }
}
