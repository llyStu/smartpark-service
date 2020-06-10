package com.vibe.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;


@EqualsAndHashCode(callSuper = true)
@Data
public class EnergyModule extends CommonMonitorDataVo {

    private String rankType;

    private String rank;

    private Integer floorId;

    private Integer page;

    private Integer size;

    private String catalogs;

}