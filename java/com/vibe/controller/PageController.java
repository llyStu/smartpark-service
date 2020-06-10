package com.vibe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
    //跳jsp下的页面通用
    @RequestMapping("/topage/{page}")
    public String topage(@PathVariable String page) {

        return page;
    }

    //跳到user下的通用页面
    @RequestMapping("/topage/{page1}/{page2}/{page3}")
    public String toUserPage(@PathVariable String page1, @PathVariable String page2, @PathVariable String page3, @RequestParam(value = "id", required = false) Integer parentId, Model model) {
        String page = page1 + "/" + page2 + "/" + page3;
        if (parentId != null) {
            model.addAttribute("parentId", parentId);
        }
        return page;
    }

    //跳到两层页面
    @RequestMapping("/topage/{page4}/{page5}")
    public String toAssetPage(@PathVariable String page4, @PathVariable String page5, String value, @RequestParam(value = "id", required = false) Integer parentId, String typeName, Model model) {
        String page = page4 + "/" + page5;
        if (value != null) {
            model.addAttribute("kind", value);
        }
        if (parentId != null) {
            model.addAttribute("parentId", parentId);
        }
        if (typeName != null) {
            model.addAttribute("typeName", typeName);
        }
        return page;
    }
}
