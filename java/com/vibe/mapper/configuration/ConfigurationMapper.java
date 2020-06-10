package com.vibe.mapper.configuration;

import org.apache.ibatis.annotations.Param;

public interface ConfigurationMapper {
    int save(@Param("key") String key, @Param("value") String value);

    int delete(@Param("key") String key);

    int update(@Param("key") String key, @Param("value") String value);

    String get(@Param("key") String key);
}
