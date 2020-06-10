package com.vibe.controller.global;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.service.global.navigation.Navigation;

@Controller
public class NavigationController {
    @Autowired
    private Navigation navigation;

    @RequestMapping("/navigation/list")
    public @ResponseBody
    Map<String, Object> loadNavigationList(@RequestParam(defaultValue = "5") Integer menuId) {
        Map<String, Object> map = new HashMap<String, Object>();
        navigation.loadNavigation(menuId);
        map.put("relation", navigation.getRelationMap());
        map.put("name", navigation.getNameMap());
        map.put("value", navigation.getValueMap());
        return map;
    }

}
