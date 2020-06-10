package com.vibe.util;

public class TableDataType {

    public static final String FLOAT = "db_vibe_data.t_sample_float";

    public static final String INT = "db_vibe_data.t_sample_int";

    public static final String BOOLEAN = "db_vibe_data.t_sample_boolean";

    public static String getTableName(String dataType) {
        switch (dataType) {
            case "FLOAT":
                return FLOAT;
            case "INT":
                return INT;
            default:
                return BOOLEAN;
        }

    }

}
