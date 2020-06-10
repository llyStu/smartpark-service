package com.vibe.service.global.navigation;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.vibe.utils.TreeNode;

@Service
public class ElevatorList implements NavigationData {
    private final int catalog = 9101;

    @Override
    public List<TreeNode> loadList() {
        List<TreeNode> spaceTree = Navigation.getNavigationService().getAllElectorList(catalog);
        return spaceTree;
    }


}
