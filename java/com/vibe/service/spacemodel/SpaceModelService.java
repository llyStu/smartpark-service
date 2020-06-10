package com.vibe.service.spacemodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.javassist.bytecode.stackmap.BasicBlock.Catch;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.vibe.mapper.classification.SelectOptionDao;
import com.vibe.mapper.spacemodel.SpaceModelMapper;
import com.vibe.pojo.CommonSelectOption;
import com.vibe.pojo.spacemodel.SceneAsset;
import com.vibe.pojo.spacemodel.SpaceModelName;
import com.vibe.util.RegixCut;
import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;

@Service
public class SpaceModelService {
    private File dirRoot;

    public String download(String path, HttpServletResponse resp) throws FileNotFoundException, IOException {
        if (path == null || path.length() <= 1) {
            return "文件名错误";
        }
        File file = new File(dirRoot, path);
        if (!file.isFile()) {
            return "文件不存在";
        }

        resp.reset();
        resp.setContentType("application/octet-stream;charset=UTF-8");
        resp.addHeader("Content-Length", "" + file.length());
        resp.setHeader("Content-Disposition",
                "attachment; filename=\"" + URLEncoder.encode(file.getName(), "UTF-8") + "\"");
        try {
            InputStream in = new FileInputStream(file);
            IOUtils.copy(in, resp.getOutputStream());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "下载错误";
        }


    }


    public List<SpaceModelName> findSpaceModelName(SpaceModelName vo) {
        List<Integer> cas = vo.getCas();
        if (cas != null && cas.size() > 0) {
            Integer[] array = cas.stream()
                    .map(v -> this.getAllCode(v))
                    .flatMap(v -> v.stream())
                    .toArray(v -> new Integer[v]);
            vo.setCas(Arrays.asList(array));
        }
        return smm.findSpaceModelName(vo);
    }


    public List<String> findSpaceModelFile(SpaceModelName vo) {
        List<Integer> cas = vo.getCas();
        if (cas != null && cas.size() > 0) {
            Integer[] array = cas.stream()
                    .map(v -> this.getAllCode(v))
                    .flatMap(v -> v.stream())
                    .toArray(v -> new Integer[v]);
            vo.setCas(Arrays.asList(array));
        }
        return smm.findSpaceModelFile(vo);
    }

    @Autowired
    ApplicationContext ac;

    public synchronized FormJson upload(CommonsMultipartFile desc, CommonsMultipartFile[] models) throws Exception {
        SimpleEntry<Set<String>, List<SpaceModelName>> parsedDesc = null;
        try (InputStream in = desc.getInputStream()) {
            parsedDesc = parseDesc(in);
        }
        Set<String> nameSet = parsedDesc.getKey();
        Set<String> manifest = new HashSet<>();
        ArrayList<String> absence = new ArrayList<>();
        ArrayList<String> exists = new ArrayList<>();
        HashSet<String> names = new HashSet<>();


        for (CommonsMultipartFile it : models) {
            String filename = new File(it.getOriginalFilename()).getName();
            if (filename.endsWith(".manifest")) {
                if (!manifest.remove(filename.substring(0, filename.length() - ".manifest".length())))
                    manifest.add(filename);
                continue;
            }
            if (!nameSet.remove(filename)) {
                absence.add(filename);
                continue;
            }
            if (!manifest.remove(filename + ".manifest")) {
                manifest.add(filename);
            }
            if (new File(dirRoot, filename).exists()) {
                exists.add(filename);
            }
            names.add(filename);
        }
        StringBuilder sb = new StringBuilder();
        if (!absence.isEmpty()) {
            sb.append("模型文件 [" + String.join("、", absence) + "] 缺失对应的描述信息，");
        }
        String[] array = manifest.stream().filter(v -> !v.endsWith(".manifest")).toArray(v -> new String[v]);
        if (array.length != 0) {
            sb.append("模型文件 [" + String.join("、", Arrays.asList(array)) + "] 缺少对应的描述文件，");
        }
        if (!exists.isEmpty()) {
            sb.append("模型文件 [" + String.join("、", exists) + "] 已存在，");
        }
        if (sb.length() != 0) {
            return FormJsonBulider.fail(sb.append("请重新上传").toString());
        }

        String sql = "insert into db_vibe_basic.t_spacemodelname(filename, name, nickname, catalog) values(?, ?, ?, ?)";
        DataSource ds = ac.getBean(DataSource.class);
        int[] executeResult = null;
        try (Connection conn = ds.getConnection()) {
            boolean autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
                for (SpaceModelName it : parsedDesc.getValue()) {
                    if (!names.contains(it.getFilename())) continue;
                    pStmt.setString(1, it.getFilename());
                    pStmt.setString(2, it.getName());
                    pStmt.setString(3, it.getNickname());
                    pStmt.setInt(4, it.getCatalog());
                    pStmt.addBatch();
                }
                executeResult = pStmt.executeBatch();
            }
            conn.commit();
            conn.setAutoCommit(autoCommit);
        }
        for (CommonsMultipartFile it : models) {
            it.transferTo(new File(dirRoot, it.getOriginalFilename()));
        }
        array = manifest.stream().filter(v -> v.endsWith(".manifest")).toArray(v -> new String[v]);
        if (array.length != 0) {
            sb.append("描述文件 [" + String.join("、", Arrays.asList(array)) + "] 多余，");
        }
        if (!nameSet.isEmpty()) {
            sb.append("描述信息 [" + String.join("、", nameSet) + "] 多余，");
        }
        sb.append(Arrays.stream(executeResult).sum() + "/" + executeResult.length);
        return FormJsonBulider.success().withCause(sb.toString());
    }

    private static SimpleEntry<Set<String>, List<SpaceModelName>> parseDesc(InputStream in) throws Exception {
        try (Workbook wb = WorkbookFactory.create(in)) {
            Sheet sheet = wb.getSheetAt(0);
            String filename = null;
            int catalog = 0;
            HashSet<String> filenames = new HashSet<>();
            ArrayList<SpaceModelName> modelnames = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
                Row row = sheet.getRow(i);
                String field1;
                if (row == null || row.getCell(1) == null
                        || (field1 = row.getCell(1).toString()) == null
                        || field1.trim().length() == 0) {
                    continue;
                }
                String field2;
                if (row.getCell(2) == null
                        || (field2 = row.getCell(2).toString()) == null
                        || field2.trim().length() == 0) {
                    filename = field1;
                    filenames.add(filename);
                    continue;
                }
                if (row.getCell(3) != null) {
                    Cell cell = row.getCell(3);
                    if (CellType.NUMERIC.equals(cell.getCellTypeEnum())) {
                        catalog = (int) Math.round(cell.getNumericCellValue());
                    } else {
                        catalog = (int) Math.round(Double.parseDouble(cell.toString()));
                    }
                }
                SpaceModelName it = new SpaceModelName();
                modelnames.add(it);
                it.setFilename(filename);
                it.setName(field1);
                it.setNickname(field2);
                it.setCatalog(catalog);
            }
            return new SimpleEntry<>(filenames, modelnames);
        }
    }

    @Autowired
    private void setRoot(ApplicationContext ac) throws IOException {
        dirRoot = ac.getResource("/spacemodel-login/").getFile();
        if (!dirRoot.exists()) dirRoot.mkdirs();
    }

    @Autowired
    private SpaceModelMapper smm;

    public List<SceneAsset> findSceneAssetByCatalogAndScene(String catalogs, String scene) {
        if (catalogs == null) {
            SceneAsset sceneAsset = new SceneAsset();
            sceneAsset.setSceneId(scene);
            return smm.findSceneAssetByScene(sceneAsset);
        } else {
            List<Integer> list = RegixCut.stringCutToList(catalogs, ",");
            return smm.findSceneAssetByCatalogAndScene(list, scene);
        }
    }


    @Autowired
    private SelectOptionDao sod;

    private List<Integer> getAllCode(int catalog) {
        ArrayList<Integer> ret = new ArrayList<>();
        ret.add(catalog);
        for (int i = 0; i < ret.size(); ++i) {
            List<CommonSelectOption> children = sod.querySelectOptionList(ret.get(i), 2001);
            if (children != null && !children.isEmpty())
                ret.addAll(Arrays.asList(children.stream().map(v -> v.getId()).toArray(v -> new Integer[v])));
        }
        return ret;
    }

    public FormJson insertSceneAsset(SceneAsset sceneAsset) {
        if (sceneAsset == null || sceneAsset.getAssetId() == null || sceneAsset.getSceneId() == null) {
            return FormJsonBulider.fail(null);
        }
        if (smm.insertSceneAsset(sceneAsset) != 1) {
            return FormJsonBulider.fail(null);
        }
        return FormJsonBulider.success();
    }

    public FormJson updateSceneAsset(SceneAsset sceneAsset) {
        if (sceneAsset == null || sceneAsset.getSaid() == null) {
            return insertSceneAsset(sceneAsset);
        }
        if (smm.updateSceneAsset(sceneAsset) != 1) {
            return FormJsonBulider.fail(null);
        }
        return FormJsonBulider.success().withCause("" + sceneAsset.getSaid());
    }


    public FormJson deleteSpaceModelFile(String[] filenames) {
        if (filenames == null || filenames.length == 0) return FormJsonBulider.success().withCause("0");

        int ret = smm.deleteSpaceModelFile(filenames);
        for (String filename : filenames) {
            new File(dirRoot, filename).delete();
            new File(dirRoot, filename + ".manifest").delete();
        }
        return FormJsonBulider.success().withCause(ret + "");
    }

    public FormJson deleteSceneAsset(int[] saids) {
        if (saids == null || saids.length == 0) return FormJsonBulider.success().withCause("0");
        return FormJsonBulider.success().withCause("" + smm.deleteSceneAsset(saids));
    }


    public List<SceneAsset> findSceneAsset(SceneAsset scene) {
        // TODO Auto-generated method stub
        return smm.findSceneAssetByScene(scene);
    }

}
