package com.vibe.service.ptrl;


import com.vibe.pojo.ptrl.PtrlRecord;
import com.vibe.utils.Page;

public interface PtrlConsequenceService {


    Page<PtrlRecord> patrollingRecord(int pageNum, int pageSize);


}
