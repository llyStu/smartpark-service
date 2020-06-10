package com.vibe.controller.ptrl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.ptrl.PtrlRecord;
import com.vibe.service.ptrl.PtrlConsequenceService;
import com.vibe.utils.Page;

@Controller
public class PtrlConsequenceController {

    @Autowired
    private PtrlConsequenceService pcs;

    @RequestMapping("/ptrl/patrollingRecord")
    @ResponseBody
    public Page<PtrlRecord> patrollingRecord(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int rows) {
        return pcs.patrollingRecord(page, rows);
    }

    @RequestMapping("/Electronic_Patrol")
    public String ElectronicPatro() {
        return "polling/ElectronicPatrol/ElectronicPatrol";
    }

}
