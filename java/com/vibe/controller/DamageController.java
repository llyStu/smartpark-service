package com.vibe.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.github.pagehelper.PageHelper;
import com.vibe.pojo.Damage;
import com.vibe.service.scene.DamageService;
import com.vibe.util.PathTool;

@Controller
public class DamageController {

    @Autowired
    private DamageService damageService;

    @RequestMapping("/add_damage")
    public String toAddDamage() {
        return "polling/damage/addDamage";
    }

    @RequestMapping("/insert_damage")
    public String insertDamage(Damage damage,
                               @RequestParam(required = false) MultipartFile[] photoFile,
                               HttpServletRequest request) throws IllegalStateException, IOException {
        if (photoFile != null && photoFile[0].getSize() > 0) {
            String path = PathTool.getRelativePath(photoFile, request);
            damage.setPhoto(path);
        }
        damage.setDate(LocalDate.now().toString());
        damageService.insertDamage(damage);
        return "polling/damage/listDamage";
    }

    @RequestMapping("/delete_damage")
    public String deleteDamage(String ids) {
        try {
            if (ids.contains(",")) {
                String[] id_arr = ids.split(",");
                for (String id : id_arr) {
                    damageService.deleteDamage(Integer.parseInt(id));
                }
                return "polling/damage/listDamage";
            } else {
                damageService.deleteDamage(Integer.parseInt(ids));
                return "polling/damage/listDamage";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //响应界面删除请求，不跳转页面
    @RequestMapping("/delete2_damage")
    public String deleteDamage2(String ids) {
        damageService.deleteDamage(Integer.parseInt(ids));
        return "success";
    }

    //点击编辑，传来id，查出Damage，跳转至editDamage.jsp
    @RequestMapping("/edit_damage")
    public String editDamage(int id, HttpServletRequest request) {
        Damage damage = damageService.queryDamage(id);
        String prefixPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/upload/";

        //把文件的属性分开赋值为绝对路径
        damage.setPhoto(prefixPath + damage.getPhoto());
        request.setAttribute("damage", damage);
        return "polling/damage/editDamage";
    }

    @RequestMapping("/update_damage")
    public String updateDamage(Damage damage,
                               @RequestParam(required = false) String delphoto,
                               @RequestParam(required = false) MultipartFile[] photoFile,
                               HttpServletRequest request) throws IllegalStateException, IOException {

        Damage damage2 = damageService.queryDamage(damage.getId());
        String path = PathTool.alterFile(delphoto, photoFile, damage2.getPhoto(), request);
        if (!"".equals(path)) {
            damage.setPhoto(path);
        }
        damageService.updateDamage(damage);
        return "polling/damage/listDamage";
    }

    @RequestMapping("/query_damage")
    public String queryDamage(int id, HttpServletRequest request) {

        Damage damage = damageService.queryDamage(id);
        List<String> path = PathTool.getAbsolutePathList(damage.getPhoto(), request);
        damage.setPhotos(path);
        request.setAttribute("damage", damage);
        return "polling/damage/listDamage";
    }

    @RequestMapping("/android_list_damage")
    @ResponseBody
    public List<Damage> android_listDamage(int pageNum,
                                           int pageCount, HttpServletRequest request) {

        PageHelper.startPage(pageNum, pageCount);
        List<Damage> list = damageService.queryDamageList();
        for (Damage item : list) {
            if (item.getPhoto().contains(",")) {
                List<String> path = PathTool.getAbsolutePathList(item.getPhoto(), request);
                item.setPhotos(path);
                item.setPhoto(null);
            } else {
                String absolutePath = PathTool.getAbsolutePath(item.getPhoto(), request);
                item.setPhoto(absolutePath);
            }
        }
        return list;
    }

    @RequestMapping("/list_damage")
    public String listDamage() {
        return "polling/damage/listDamage";
    }
}




