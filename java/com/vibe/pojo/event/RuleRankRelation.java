package com.vibe.pojo.event;

import lombok.Data;

@Data
public class RuleRankRelation {

    private Integer id;
    private Integer assetId;
    private String warnCond;
    private Integer rankId;

}
