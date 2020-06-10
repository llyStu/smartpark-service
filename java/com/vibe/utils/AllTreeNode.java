package com.vibe.utils;

import java.util.ArrayList;
import java.util.List;


/**
 * 一次性加载整个tree
 *
 * @author FLex3
 */
public class AllTreeNode {
    //id,该节点的id
    private Integer id;
    //树形菜单节点的名称
    private String text;
    //是否有子节点是值为open 否为closed
    private String state;
    //子节点的信息
    private List<AllTreeNode> children;
    //分类
    private Integer kind;
    //节点的状态
    private String name;
    private Integer status;
    private String dataType;
    private Integer catalog;

    private String unit;


    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCatalog() {
        return catalog;
    }

    public void setCatalog(Integer catalog) {
        this.catalog = catalog;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<AllTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<AllTreeNode> children) {
        this.children = children;
    }

    public AllTreeNode() {
        super();
        // TODO Auto-generated constructor stub
    }
    //添加孩子的方法

    public void addChild(AllTreeNode node) {
        if (this.children == null) {
            children = new ArrayList<AllTreeNode>();
            children.add(node);
        } else {
            children.add(node);
        }

    }

}
