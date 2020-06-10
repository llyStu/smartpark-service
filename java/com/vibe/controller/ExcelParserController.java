package com.vibe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.pojo.Response;
import com.vibe.util.ResponseResult;

@Controller
public class ExcelParserController {
    @RequestMapping("/excelParser")
    @ResponseBody
    public Response excelParser(@RequestParam("id") int id) {

        return ResponseResult.getANewResponse(true);
    }
}
