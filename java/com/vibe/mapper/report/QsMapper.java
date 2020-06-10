package com.vibe.mapper.report;

import java.util.List;
import java.util.Map;

public interface QsMapper {
	List<Qs> qs(Map map);
	List<Qs> qs(QsVo vo);
}
