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
import com.vibe.pojo.Archaeology;
import com.vibe.service.scene.ArchaeologyService;
import com.vibe.util.PathTool;

@Controller
public class ArchaeologyController {

    @Autowired
    private ArchaeologyService archaeologyService;

    @RequestMapping("/add_archaeology")
    public String toAddArchaeology() {
        return "polling/archaeology/addArchaeology";
    }

    @RequestMapping("/insert_archaeology")
    public String insertArchaeology(Archaeology archaeology,
                                    @RequestParam(required = false) MultipartFile[] photoFile,
                                    HttpServletRequest request) throws IllegalStateException, IOException {
        if (photoFile != null && photoFile[0].getSize() > 0) {
            String path = PathTool.getRelativePath(photoFile, request);
            archaeology.setPhoto(path);
        }
        archaeology.setDate(LocalDate.now().toString());
        archaeologyService.insertArchaeology(archaeology);
        return "polling/archaeology/listArchaeology";
    }

    @RequestMapping("/delete_archaeology")
    public String deleteArchaeology(String ids) {
        try {
            if (ids.contains(",")) {
                String[] id_arr = ids.split(",");
                for (String id : id_arr) {
                    archaeologyService.deleteArchaeology(Integer.parseInt(id));
                }
                return "polling/archaeology/listArchaeology";
            } else {
                archaeologyService.deleteArchaeology(Integer.parseInt(ids));
                return "polling/archaeology/listArchaeology";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //响应界面删除请求，不跳转页面
    @RequestMapping("/delete2_archaeology")
    public String deleteArchaeology2(String ids) {

        archaeologyService.deleteArchaeology(Integer.parseInt(ids));
        return null;
    }

    //点击编辑，传来id，查出archaeology，跳转至editArchaeology.jsp
    @RequestMapping("/edit_archaeology")
    public String editArchaeology(int id, HttpServletRequest request) {
        Archaeology archaeology = archaeologyService.queryArchaeology(id);
        String prefixPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/upload/";

        //把文件的属性分开赋值为绝对路径
        archaeology.setPhoto(prefixPath + archaeology.getPhoto());


        request.setAttribute("archaeology", archaeology);
        return "polling/archaeology/editArchaeology";
    }

    @RequestMapping("/update_archaeology")
    public String updateArchaeology(Archaeology archaeology,
                                    @RequestParam(required = false) String delphoto,
                                    @RequestParam(required = false) MultipartFile[] photoFile,
                                    HttpServletRequest request) throws IllegalStateException, IOException {

        Archaeology archaeology2 = archaeologyService.queryArchaeology(archaeology.getId());
        String path = PathTool.alterFile(delphoto, photoFile, archaeology2.getPhoto(), request);
        if (!"".equals(path)) {
            archaeology.setPhoto(path);
        }
        archaeologyService.updateArchaeology(archaeology);
        return "polling/archaeology/listArchaeology";
    }

    @RequestMapping("/query_archaeology")
    public String queryArchaeology(int id, HttpServletRequest request) {

        Archaeology archaeology = archaeologyService.queryArchaeology(id);
        List<String> path = PathTool.getAbsolutePathList(archaeology.getPhoto(), request);
        archaeology.setPhotos(path);
        request.setAttribute("archaeology", archaeology);
        return "polling/archaeology/detailArchaeology";
    }


    @RequestMapping("/android_list_archaeology")
    @ResponseBody
    public List<Archaeology> android_listArchaeology(int pageNum,
                                                     int pageCount, HttpServletRequest request) {

        PageHelper.startPage(pageNum, pageCount);
        List<Archaeology> list = archaeologyService.queryArchaeologyList();
        for (Archaeology item : list) {
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

    @RequestMapping("/list_archaeology")
    public String listArchaeology() {
        return "polling/archaeology/listArchaeology";
    }
}




