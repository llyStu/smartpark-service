package com.vibe.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegixCut {
    public static List<Integer> stringCutToList(String text, String regex) {
        List<Integer> list = new ArrayList<Integer>();
        String[] split = text.split(regex);
        for (String str : split) {
            list.add(Integer.parseInt(str));
        }
        return list;
    }

    public static List<String> regixCutToList(String text) {

        List<String> list = new ArrayList<String>();
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
        Matcher m = p.matcher(text);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }

    public static List<Integer> regixCutStringToList(String text, String symbol) {

        List<Integer> list = new ArrayList<Integer>();
        if (text != null && text != "") {
            String[] split = text.split(symbol);
            for (String str : split) {
                list.add(Integer.parseInt(str));
            }
        }
        return list;
    }

    public static List<Integer> CutStringToList(String text, String symbol) {

        List<Integer> list = new ArrayList<Integer>();
        if (text != null && text != "") {
            String[] split = text.split(symbol);
            for (String str : split) {
                boolean isNum = str.matches("[1-9][0-9]+");
                if (isNum) {
                    list.add(Integer.parseInt(str));
                }
            }
        }
        return list;
    }

    public static void main(String[] args) {
        String sss = "3,14";
        String dd = ",";
        List<Integer> regixCutToList = stringCutToList(sss, dd);
        for (Integer string : regixCutToList) {
            System.out.println(string);
        }

    }
}
