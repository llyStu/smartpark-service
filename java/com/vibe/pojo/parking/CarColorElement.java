package com.vibe.pojo.parking;

import com.vibe.util.StringUtils;

/**
 * 分类名称
 * ClassName: CarColorElement
 * Package:com.vibe.pojo.parking
 * Description:
 *
 * @Date:2020/5/8 16:01
 * @Author: hyd132@136.com
 */
public enum CarColorElement {

    WHITE("0","白色"),
    BLACK("1","黑色"),
    RED("2","红色"),
    YELLOW("3","黄色"),
    GRAY("4","银灰色"),
    BLUE("5","蓝色"),
    GREEN("6","黄色"),
    ORANGE("7","橙色"),
    PURPLE("8","紫色"),
    CYAN("9","青色"),
    COLOUR("10","粉色"),
    BROWN("11","棕色"),
    UNKNOWN("99","未知的"),
    OTHER("100","其它");



    String  key;
    String value;

    CarColorElement(String key,String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey(){
        return this.key;
    }

    public String getValue(){
        return this.value;
    }

    public static String getCarColorByNum(String key) {
        for (CarColorElement enums : CarColorElement.values()) {
            if (StringUtils.equals(key,enums.key)) {
                return enums.getValue();
            }
        }
        return "未知的";
    }

    /*public static void main(String[] args) {
        System.out.println(getCarColorByNum("1"));
    }*/
}
