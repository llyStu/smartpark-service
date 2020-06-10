package com.vibe.mapper.ptrl;

import com.vibe.pojo.mounting.ConnMountiong;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PtrlConsequenceMapper {

    ConnMountiong findOneConn(@Param("connname") String connname);

}
