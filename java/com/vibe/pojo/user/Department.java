package com.vibe.pojo.user;

import lombok.Data;

/**
 * 部门表
 *
 * @author FLex3
 * idint(11) NOT NULL
 * namevarchar(255) NOT NULL
 * parentint(11) NULL
 * organint(11) NULL
 * abbrvarchar(63) NULL
 * validtinyint(4) NOT NULL
 * sequencesmallint(6) NOT NULL
 * isparenttinyint(4) NOT NULL
 */
@Data
public class Department {
    private Integer id;

    private String name;

    private Integer parent;

    private Integer level;

    private Integer organ;

    private String abbr;

    private Integer valid;

    private Integer sequence;

    public Department() {
        super();
        // TODO Auto-generated constructor stub
    }

}
