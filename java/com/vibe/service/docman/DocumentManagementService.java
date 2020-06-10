package com.vibe.service.docman;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.docman.DocumentManagementMapper;
import com.vibe.pojo.docman.DocumentBackup;
import com.vibe.pojo.docman.DocumentBackupVo;
import com.vibe.pojo.docman.DocumentDir;
import com.vibe.pojo.docman.DocumentDirVo;
import com.vibe.service.docman.DefaultResourceHandler.Cause;
import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;
import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;
import com.vibe.utils.Page;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Service
public class DocumentManagementService {
	@Autowired
	private DocumentManagementMapper dmm;
	@Autowired
	private Environment environment;

	public FormJson upload(Integer did, CommonsMultipartFile[] files) {
		File parent = new File(rootDoc, did.toString());
		if (!parent.isDirectory()) {
			return FormJsonBulider.fail("档案不存在");
		}
		if (files == null || files.length == 0) {
			return FormJsonBulider.success().withCause("没有文件上传");
		}
		ArrayList<String> faild = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for (CommonsMultipartFile file : files) {
			String filename = file.getOriginalFilename();
			try {
				File f = new File(parent, filename);
				if (f.exists()) {
					faild.add(filename + "/文件夹已存在");
					continue;
				}
				file.transferTo(f);
				sb.append(filename).append('/');
			} catch (Exception e) {
				faild.add(filename + "/" + e.getMessage());
				continue;
			}
		}
		dmm.addDocumentFile(did, sb.toString());
		if (faild.size() == 0) {
			return FormJsonBulider.success();
		}
		return FormJsonBulider.fail("保存文件失败: " + faild.toString());
	}

	public FormJson backup(int did, int uid, String desc) {
		File parent = new File(rootDoc, Integer.toString(did));
		if (!parent.exists()) {
			return FormJsonBulider.fail("档案不存在");
		}
		File bkRoot = this.getbackup();
		if (!bkRoot.isDirectory() && !bkRoot.mkdirs()) {
			return FormJsonBulider.fail("文件夹创建错误");
		}

		DocumentBackupVo vo = new DocumentBackupVo();
		vo.setId(did);
		List<DocumentBackup> findbackup = dmm.findbackup(vo);
		if (findbackup != null && findbackup.size() > 0) {
			return FormJsonBulider.fail("文件已备份，已取消操作");
		}

		DocumentBackup bak = new DocumentBackup();
		bak.setId(did);
		bak.setBuid(uid);
		bak.setBdesc(desc);
		int id = dmm.backup(bak);
		if (id != 1)
			return FormJsonBulider.fail("失败:" + id);
		id = bak.getBid();
		File file = new File(bkRoot, id + ".zip");
		try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
			this.zip(parent, out);
			return FormJsonBulider.success().withCause(Integer.toString(id));
		} catch (Exception e) {
			e.printStackTrace();
			return FormJsonBulider.fail(e.getMessage());
		}
	}

	private void zip(File parent, OutputStream out) throws IOException {
		try (ZipOutputStream zip = new ZipOutputStream(out)) {
			File[] docs = parent.listFiles();
			if (docs != null && docs.length != 0) {
				for (File file : docs) {
					if (file.isDirectory())
						continue;
					zip.putNextEntry(new ZipEntry(file.getName()));
					try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
						IOUtils.copy(in, zip);
					}
				}
			}
			zip.flush();
		}
	}

	public FormJson delete(int did) {
		if (did < 15) {
			return FormJsonBulider.fail("系统预留类型，不能删除");
		}
		File parent = new File(rootDoc, did + "");
		if (dmm.deleteDocumentDir(did, false) != 1)
			FormJsonBulider.fail("");
		try {
			FileUtils.deleteDirectory(parent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return FormJsonBulider.success();
	}

	public Page<DocumentDir> findDir(DocumentDirVo vo, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<DocumentDir> list = dmm.findDocumentDir(vo);
		PageInfo<DocumentDir> page = new PageInfo<>(list);

		Page<DocumentDir> result = new Page<>();
		result.setRows(list);
		result.setPage(page.getPageNum());
		result.setSize(page.getPageSize());
		result.setTotal((int) page.getTotal());
		return result;
	}

	public Page<DocumentBackup> findbackup(DocumentBackupVo vo, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<DocumentBackup> list = dmm.findbackup(vo);
		PageInfo<DocumentBackup> page = new PageInfo<>(list);

		Page<DocumentBackup> result = new Page<>();
		result.setRows(list);
		result.setPage(page.getPageNum());
		result.setSize(page.getPageSize());
		result.setTotal((int) page.getTotal());
		return result;
	}

	public FormJson mkdir(DocumentDir dir) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		dir.setModifiedString(sdf.format(dir.getModified()));
		int id = dmm.saveDocumentDir(dir);
		if (id != 1) {
			return FormJsonBulider.fail("创建失败:" + id);
		}
		id = dir.getId();
		File file = new File(rootDoc, Integer.toString(id));
		file.delete();
		if (!dir.getVisible())
			file.mkdirs();
		return FormJsonBulider.success().withCause(Integer.toString(id));
	}

	public FormJson updatedir(DocumentDir dir) {
		if (dir.getId() < 15) {
			return FormJsonBulider.fail("系统预留类型，不能修改");
		}
		int num = dmm.updatedir(dir);
		return num == 1 ? FormJsonBulider.success() : FormJsonBulider.fail("更新条数： " + num);
	}

	public List<DocumentDir> tree(Integer id) {
		List<DocumentDir> dirs = dmm.findAllDocumentDir();
		if (dirs.size() == 0)
			return dirs;

		List<DocumentDir> ret = new ArrayList<>();

		Map<Integer, DocumentDir> map = new HashMap<>();
		int i = 0;
		DocumentDir it = dirs.get(i), old;
		while (i < dirs.size()) {
			ArrayList<DocumentDir> list = new ArrayList<>();
			int pid = it.getPid();
			do {
				old = map.put(it.getId(), it);
				if (old != null)
					it.setNodes(old.getNodes());
				(pid == 0 ? ret : list).add(it);
			} while (++i < dirs.size() && (it = dirs.get(i)).getPid() == pid);

			map.putIfAbsent(pid, new DocumentDir());
			map.get(pid).setNodes(list);
		}
		return id == null || id.equals(0) ? ret : Arrays.asList(map.get(id));
	}

	public Dispatcher getRequestDispatcher(String path, boolean direct) {
		path = path.trim();
		if (StringUtils.isEmpty(path) || path.charAt(0) != '/' || path.contains("/../")) {
			return defaultResourceHandler.prepare(Cause.NotFound);
		}
		File file = new File(rootDoc, path);
		if (!file.isFile()) {
			return defaultResourceHandler.prepare(Cause.NotFound);
		}

		String filename = file.getName();
		int idx = filename.lastIndexOf(".");
		if (idx == -1 || idx == filename.length() - 1) {
			return defaultResourceHandler.prepare(Cause.Unsupported);
		}
		String post = filename.substring(idx + 1).toLowerCase();
		ResourceHandler handler = resourceHandlers.get(post);
		if (handler == null)
			return defaultResourceHandler.prepare(Cause.Unsupported);

		if (handler instanceof ConvertHandler) {
			direct = false;
			try {
				path = ConvertHandler.class.cast(handler).convert(file, post, rootDoc, path);
			} catch (Exception e) {
				e.printStackTrace();
				return defaultResourceHandler.prepare(e.getMessage());
			}
		}
		return direct ? directResourceHandler.prepare(path) : handler.prepare(path);
	}
	public ResponseModel<String> getFileIsExists(String path) {
		path = path.trim();
		if (StringUtils.isEmpty(path) || path.charAt(0) != '/' || path.contains("/../")) {
			return ResponseModel.failure("文件为空，无法预览").code(ResultCode.ERROR);
		}

		File file = new File(rootDoc, path);
		if (!file.isFile()) {
			return ResponseModel.failure("文件不存在，无法预览").code(ResultCode.ERROR);
		}

		String filename = file.getName();
		int idx = filename.lastIndexOf(".");
		if (idx == -1 || idx == filename.length() - 1) {
			return ResponseModel.failure("不支持的文件类型，无法预览").code(ResultCode.ERROR);
		}
		String post = filename.substring(idx + 1).toLowerCase();
		ResourceHandler handler = resourceHandlers.get(post);
		if (handler == null)
			return ResponseModel.failure("不支持的文件类型，无法预览").code(ResultCode.ERROR);
		//2020年2月26日16:24:39 修改word预览
		if (handler instanceof ConvertHandler) {
//            direct = false;
			try {
				path = ConvertHandler.class.cast(handler).convert(file, post, rootDoc, path);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseModel.failure(e.getMessage()).code(ResultCode.ERROR);
//                return defaultResourceHandler.prepare(e.getMessage());
			}
		}
		/*String outFileName = UUID.randomUUID().toString() + ".pdf";
				if ("doc".equals(post) || "docx".equals(post)) {
					// String filepath = "xxxx.docx";
					String outDic =  rootDoc + File.separator + "pdf" + File.separator;

					File dicFile = new File(outDic);
					if (!dicFile.exists()) {
						dicFile.mkdir();
					}
					InputStream source = null;
					OutputStream target = null;
					try {
				source = new FileInputStream(rootDoc + path);
				target = new FileOutputStream(outDic + outFileName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Map<String, String> params = new HashMap<String, String>();
			PdfOptions options = PdfOptions.create();
			try {
				wordConverterToPdf(source, target, options, params, post);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return ResponseModel.success("/image/pdf/"+outFileName).code(ResultCode.SUCCESS);*/
		return ResponseModel.success("/image"+path).code(ResultCode.SUCCESS);
//        return direct ? directResourceHandler.prepare(path) : handler.prepare(path);
	}


	public final DefaultResourceHandler defaultResourceHandler = new DefaultResourceHandler();
	private final ResourceHandler directResourceHandler = path -> {
		return (req, resp) -> {
			req.getRequestDispatcher("/WEB-INF/docman-root/" + path).forward(req, resp);
		};
	};
	private final Map<String, ResourceHandler> resourceHandlers = new HashMap<>();
	{
		BaseHandler imgHandler = () -> "show-img";
		resourceHandlers.put("png", imgHandler);
		resourceHandlers.put("jpg", imgHandler);
		resourceHandlers.put("jpeg", imgHandler);
		resourceHandlers.put("gif", imgHandler);

		BaseHandler pdfHandler = () -> "show-pdf";
		resourceHandlers.put("pdf", pdfHandler);

		resourceHandlers.put("docx", new DocxHandler());
		resourceHandlers.put("doc", new DocHandler());
		resourceHandlers.put("xlsx", new XlsxHandler());
		resourceHandlers.put("xls", new XlsHandler());
	}

	@SuppressWarnings("unused")
	private File rootDoc, rootConvert, rootBackup,rootUpload;

	@Autowired
	private void setRoot(ServletContext sc) {



		rootDoc = new File(environment.getProperty("imgStoreCatalogue"));
		rootConvert = new File(sc.getRealPath("/WEB-INF/docman-convert/"));
		rootBackup = new File(sc.getRealPath("/WEB-INF/docman-backup/"));
		rootUpload = new File(sc.getRealPath("/upload/"));
		/*rootDoc = new File("D:/WEB-INF/docman-root/");
		rootConvert = new File("D:/WEB-INF/docman-convert/");
		rootBackup = new File("D:/WEB-INF/docman-backup/");
		rootUpload = new File("D:/upload/");*/
	}

	@PostConstruct
	private void init() throws Exception {
		if (rootDoc == null || (!rootDoc.isDirectory() && !rootDoc.mkdirs())) {
			throw new Exception("创建文档管理根目录错误！！");
		}
		if (rootConvert == null || (!rootConvert.isDirectory() && !rootConvert.mkdirs())) {
			throw new Exception("创建文档管理根目录错误！！");
		}
	}

	public String download(String path, HttpServletResponse resp) throws IOException {
		if (path == null || path.length() <= 1 || path.charAt(0) != '/' || path.indexOf('/', 1) == -1
				|| path.contains("/../")) {
			return "文件名错误";
		}
		File file = new File(rootDoc, path);
		if (!file.exists()) {
			return "文件不存在";
		}

		resp.reset();
		resp.setContentType("application/octet-stream;charset=UTF-8");

		if (file.isDirectory()) {
			String filename = file.getName();
			try {
				int id = Integer.parseInt(filename);
				DocumentDirVo dir = new DocumentDirVo();
				dir.setId(id);
				List<DocumentDir> findDocumentDir = dmm.findDocumentDir(dir);
				filename = findDocumentDir.get(0).getNumber() +"-"+ findDocumentDir.get(0).getName();
			} catch (Exception e) {
				e.printStackTrace();
			}

			resp.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(filename, "UTF-8") + ".zip\"");
			this.zip(file, resp.getOutputStream());
		} else {
			resp.addHeader("Content-Length", "" + file.length());
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(file.getName(), "UTF-8") + "\"");
			try (InputStream in = new FileInputStream(file)) {
				IOUtils.copy(in, resp.getOutputStream());
			}
		}
		return null;
	}

	public File getbackup() {
		String backupPath = dmm.getBackupPath();
		if (backupPath == null) backupPath = "a/";
		File path = new File(backupPath);
		if (path.isAbsolute()) return path;
		return new File(rootBackup, path.getPath());
	}

	public FormJson setbackup(String path) {
		path = path.trim().replaceAll("\\\\", "/");
		System.out.println("setbackup:"+path);
		if (path.startsWith("../") || path.endsWith("/..") || path.contains("/../") || path.equals("..")) {
			return FormJsonBulider.fail("目录名非法");
		}
		File file = new File(path);
		if (file.isAbsolute() && file.exists()) {
			return FormJsonBulider.fail("路径已存在");
		}
		int result = dmm.setBackupPath(path);
		if (result != 1 && dmm.saveBackupPath(path) != 1) {
			return FormJsonBulider.fail("更新目录失败:"+ result);
		}
		return FormJsonBulider.success();
	}

	public FormJson delCategory(Integer cid) {
		if (cid < 15) {
			return FormJsonBulider.fail("系统预留类型，不能删除");
		}
		if (dmm.deleteDocumentDir(cid, true) != 1) {
			return FormJsonBulider.fail("失败");
		}
		int ret = dmm.delParent(cid);
		return FormJsonBulider.success().withCause(ret +"");
	}

	@SuppressWarnings("resource")
	public FormJson restore(int bid) {
		DocumentBackupVo vo = new DocumentBackupVo();
		vo.setBid(bid);
		List<DocumentBackup> findbackup = dmm.findbackup(vo);
		if (findbackup == null || findbackup.size() == 0) {
			return FormJsonBulider.fail("备份不存在");
		}
		DocumentBackup backup = findbackup.get(0);

		File dirRestore;
		try {
			dirRestore = new File(rootDoc, backup.getId() +".restore");
			FileUtils.deleteDirectory(dirRestore);
			dirRestore.mkdir();

			File src = new File(getbackup(), bid +".zip");
			if (!src.exists()) {
				return FormJsonBulider.fail("备份不存在，或许是因为已经更改了备份路径");
			}
			ZipFile zipFile = new ZipFile(src);
			Enumeration<? extends ZipEntry> iter = zipFile.entries();
			if (iter != null) {
				while (iter.hasMoreElements()) {
					ZipEntry entry = iter.nextElement();
					if (entry.isDirectory()) continue;
					try (OutputStream out = new FileOutputStream(new File(dirRestore, entry.getName()));
						 InputStream in = zipFile.getInputStream(entry)) {
						IOUtils.copy(in, out);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return FormJsonBulider.fail("恢复文件出错: "+ e.getMessage());
		}

		dmm.deleteDocumentDir(backup.getId(), backup.getVisible());
		if (dmm.restore(backup) != 1) {
			return FormJsonBulider.fail("恢复数据出错");
		}

		try {
			File dir = new File(rootDoc, backup.getId().toString());
			FileUtils.deleteDirectory(dir);
			dirRestore.renameTo(dir);
		} catch (IOException e) {
			e.printStackTrace();
			return FormJsonBulider.fail("恢复文件出错：" + e.getMessage());
		}
		return FormJsonBulider.success();
	}
}
