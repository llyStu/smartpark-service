package com.vibe.controller.spacemodel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.vibe.pojo.spacemodel.SceneAsset;
import com.vibe.pojo.spacemodel.SpaceModelName;
import com.vibe.service.logAop.MethodLog;
import com.vibe.service.spacemodel.SpaceModelService;
import com.vibe.utils.FormJson;

import static org.apache.poi.hwmf.record.HwmfRecordType.escape;

@Controller
public class SpaceModelController {
    @Autowired
    private SpaceModelService sms;

    @RequestMapping("/spacemodel/findSpaceModelName")
    @ResponseBody
    public List<SpaceModelName> findSpaceModelName(SpaceModelName vo) {
        return sms.findSpaceModelName(vo);
    }

    @RequestMapping("/spacemodel/findSpaceModelFile")
    @ResponseBody
    public List<String> findSpaceModelFile(SpaceModelName vo) {
        return sms.findSpaceModelFile(vo);
    }

    @RequestMapping("/spacemodel/deleteSpaceModelFile")
    @ResponseBody
    public FormJson deleteSpaceModelFile(String[] filename) {
        return sms.deleteSpaceModelFile(filename);
    }

    @RequestMapping("/spacemodel/deleteSceneAsset")
    @ResponseBody
    public FormJson deleteSceneAsset(int[] said) {
        return sms.deleteSceneAsset(said);
    }

    @RequestMapping("/spacemodel/updateSceneAsset")
    @MethodLog(remark = "edit", option = "更新模型位置")
    @ResponseBody
    public FormJson updateSceneAsset(SceneAsset sceneAsset) {
        return sms.updateSceneAsset(sceneAsset);
    }

    @RequestMapping("/spacemodel/findSceneAssetByCatalogAndScene")
    @ResponseBody
    public List<SceneAsset> findSceneAssetByCatalogAndScene(String catalog, String scene) {
        return sms.findSceneAssetByCatalogAndScene(catalog, scene);
    }

    @RequestMapping("/spacemodel/upload")
    @MethodLog(remark = "upload", option = "上传")
    @ResponseBody
    public FormJson upload(@RequestParam CommonsMultipartFile desc, @RequestParam CommonsMultipartFile[] model) throws Exception {
        return sms.upload(desc, model);
    }


    @RequestMapping("/spacemodel/download/{name}")
    public void download(HttpServletRequest req, HttpServletResponse resp, @PathVariable String name) throws IOException {
        //String path = getPath(req);
        String result = "文件不存在";
        if (name != null) {
            String path = URLDecoder.decode(name, "utf-8");
            result = sms.download(path, resp);
        }
        if (result != null) {
            resp.setStatus(500);
            resp.setContentType("text/plain;charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(result);
        }
        resp.flushBuffer();
    }

    /*private final Pattern pathParm = Pattern.compile("/[^/\\\\]+/spacemodel/[^/\\\\]+(/.*)?");

    private String getPath(HttpServletRequest req) throws UnsupportedEncodingException {
        String uri = req.getRequestURI();
        Matcher mat = pathParm.matcher(uri);
        String path = mat.find() ? mat.group(1) : null;

        if (path == null)
            path = "/";
        else
            path = URLDecoder.decode(path, "utf-8");
        return path;
    }*/
    @RequestMapping("/spacemodel/findSceneAsset")
    @ResponseBody
    public List<SceneAsset> findSceneAsset(SceneAsset scene) {
        scene.setPath(URLDecoder.decode(scene.getPath()));
        List<SceneAsset> sceneAssetList = sms.findSceneAsset(scene);
        return sceneAssetList;
    }
}
