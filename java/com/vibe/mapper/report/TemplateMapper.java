package com.vibe.mapper.report;

import com.vibe.pojo.report.TemplateData;

public interface TemplateMapper {
    void add(TemplateData templateData);

    TemplateData find(String name);
}
