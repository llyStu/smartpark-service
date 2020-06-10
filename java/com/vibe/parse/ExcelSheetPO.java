package com.vibe.parse;

import java.util.List;

public class ExcelSheetPO {

    /**
     * sheet的名称
     */
    private String sheetName;



    /**
     * 数据集合
     */
    private List<List<String>> dataList;



    public List<List<String>> getDataList() {
        return dataList;
    }

    public void setDataList(List<List<String>> dataList) {
        this.dataList = dataList;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }



}