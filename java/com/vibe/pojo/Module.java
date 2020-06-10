package com.vibe.pojo;

import java.io.Serializable;


/**
 * 模块表
 *
 * @author FLex3
 */
public class Module implements Serializable {

    private Integer id;            //模块id
    private Integer parent;        //模块的父id
    private String name;        //模块名称
    private String caption;        //说明
    private Integer sequence;    //排序，默认1
    private Integer isparent;    //0是叶子节点，1是父节点，默认是0

    public Module() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getIsparent() {
        return isparent;
    }

    public void setIsparent(Integer isparent) {
        this.isparent = isparent;
    }


}
