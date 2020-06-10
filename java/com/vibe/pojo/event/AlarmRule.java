package com.vibe.pojo.event;

import lombok.Data;

@Data
public class AlarmRule {

    private Integer monitorId;
    private String caption;
    private Integer monitorCatalog;
    private Integer parent;
    private String warnCond;
    private String relationId;
    private String singlewarn;//拆分后的报警条件
    private EventRank eventRank;
    private String notify;//通知方式 预留
    private Integer monitorState;//录入类型

    private String system;
    private Integer deviceCatalogId;
    private String deviceCatalogName;
    private String monitorCatalogName;
    private String deviceCaption;
}
