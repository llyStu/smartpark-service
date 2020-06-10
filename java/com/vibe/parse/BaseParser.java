package com.vibe.parse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;

import com.vibe.scheduledtasks.AssetStoreSqlSession;

public abstract class BaseParser<T extends BaseBean> {
    public void parse(ExcelSheetPO excelSheetPO) throws Exception {
        List<List<String>> excelData = excelSheetPO.getDataList();
        List<T> data = new ArrayList<>();
        for (int i = 0; i < excelData.size(); i++) {
            T t = createBean();
            setId(t);
            data.add(t);
        }

        for (int i = 0; i < excelData.size(); i++) {
            List<String> excelItem = excelData.get(i);
            T item = data.get(i);
            fillData(excelItem, item);
        }

        setParent(excelData, data);

        if (!App.TEST) {
            setSource(excelData, data);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);
        setDatabase(pw);
        pw.println();
        pw.println();
        for (int i = 0; i < data.size(); i++) {
            pw.println(data.get(i).toString());
        }
        pw.flush();
        pw.close();
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306";
        ScriptRunner sr = new ScriptRunner(AssetStoreSqlSession.getSession().getConnection());
        sr.setAutoCommit(true);// 自动提交
        sr.setFullLineDelimiter(false);
        sr.setDelimiter(";");//// 每条命令间的分隔符
        sr.setSendFullScript(false);
        sr.setStopOnError(false);
        sr.runScript(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()))));

    }

    //public void addToRAM(T t){}

    public void setId(T t) {
        t.setId(Id.getUniqueId() + "");
    }

    public void setDatabase(PrintWriter pw) throws IOException {
        pw.print("use db_vibe_monitor;");
    }

    public void setSource(List<List<String>> excelData, List<T> data) {
    }

    public abstract T createBean();

    public abstract String getOutputFileName();

    public abstract void fillData(List<String> excelData, T data);

    public abstract void setParent(List<List<String>> excelData, List<T> data);

}
