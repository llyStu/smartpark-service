package com.vibe.controller.docman;

import com.vibe.pojo.docman.DocumentBackup;
import com.vibe.pojo.docman.DocumentBackupVo;
import com.vibe.pojo.docman.DocumentDir;
import com.vibe.pojo.docman.DocumentDirVo;
import com.vibe.pojo.user.User;
import com.vibe.service.docman.DocumentManagementService;
import com.vibe.util.StringUtils;
import com.vibe.util.constant.ResponseModel;
import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;
import com.vibe.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class DocumentManagementController {
	@Autowired
	private DocumentManagementService dms;

	@RequestMapping("/docman/restore")
	@ResponseBody
	public FormJson restore(int bid) {
		return dms.restore(bid);
	}

	@RequestMapping("/docman/upload")
	@ResponseBody
	public FormJson upload(@RequestParam Integer did, @RequestParam CommonsMultipartFile[] files) {
		return dms.upload(did, files);
	}

	@RequestMapping("/docman/setbackuppath")
	@ResponseBody
	public FormJson setbackuppath(@RequestParam String path) {
		return dms.setbackup(path);
	}

	@RequestMapping("/docman/getbackuppath")
	@ResponseBody
	public String filebackup() {
		return dms.getbackup().getPath();
	}

	@RequestMapping("/docman/deldir")
	@ResponseBody
	public FormJson delete(Integer did) {
		return dms.delete(did);
	}

	@RequestMapping("/docman/delCategory")
	@ResponseBody
	public FormJson delCategory(Integer cid) {
		return dms.delCategory(cid);
	}

	@RequestMapping("/docman/find")
	@ResponseBody
	public Page<DocumentDir> find(DocumentDirVo vo) {
		if (vo.getPageNum() == null)
			vo.setPageNum(1);
		if (vo.getPageSize() == null)
			vo.setPageSize(10);
		return dms.findDir(vo, vo.getPageNum(), vo.getPageSize());
	}

	@RequestMapping("/docman/findbackup")
	@ResponseBody
	public Page<DocumentBackup> findbackup(DocumentBackupVo vo) {
		if (vo.getPageNum() == null)
			vo.setPageNum(1);
		if (vo.getPageSize() == null)
			vo.setPageSize(10);
		return dms.findbackup(vo, vo.getPageNum(), vo.getPageSize());
	}

	@RequestMapping("/docman/backup")
	@ResponseBody
	public FormJson backup(int did, String desc, HttpSession session) {
		User user = (User) session.getAttribute("loginUser");

		return dms.backup(did, user.getId(), desc);
	}

	@RequestMapping("/docman/createCategory")
	@ResponseBody
	public FormJson createCategory(String name, Integer pid, HttpSession session) {
		User user = (User) session.getAttribute("loginUser");
		DocumentDir dir = new DocumentDir();
		dir.setName(name);
		dir.setPid(pid == null ? 0 : pid);
		dir.setUid(user.getId());
		dir.setModified(new Date());
		dir.setVisible(true);

		return dms.mkdir(dir);
	}

	@RequestMapping("/docman/mkdir")
	@ResponseBody
	public FormJson mkdir(DocumentDir dir, HttpSession session) {
		User user = (User) session.getAttribute("loginUser");
		dir.setUid(user.getId());
		dir.setVisible(false);
		return dms.mkdir(dir);
	}

	public static class MkdirAndUploadVo {
		public DocumentDir getDir() {
			return dir;
		}
		public void setDir(DocumentDir dir) {
			this.dir = dir;
		}
		public CommonsMultipartFile[] getFiles() {
			return files;
		}
		public void setFiles(CommonsMultipartFile[] files) {
			this.files = files;
		}
		DocumentDir dir;
		CommonsMultipartFile[] files;
	}

	@RequestMapping("/docman/mkdirAndUpload")
	@ResponseBody
	public FormJson mkdirAndUpload(MkdirAndUploadVo vo, HttpSession session) {
		FormJson mkdir = this.mkdir(vo.dir, session);
		if (!mkdir.isSuccess()) {
			return mkdir;
		}
		int did = Integer.parseInt(mkdir.getMessage());
		return this.upload(did, vo.files);
	}

	@RequestMapping("/docman/updatedir")
	@ResponseBody
	public FormJson updatedir(DocumentDir dir) {
		if (dir.getId() == null)
			return FormJsonBulider.fail("需要id");
		return dms.updatedir(dir);
	}

	@RequestMapping("/docman/tree")
	@ResponseBody
	public List<DocumentDir> tree(Integer id) {
		return dms.tree(id);
	}

	@RequestMapping("/docman/show/**")
	@ResponseBody
	public ResponseModel<String> show(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		try {
			//System.out.println(req);
			String path = getPath(req);
			System.out.println("/docman/show/**"+path);
//			return path;
			ResponseModel<String> responseModel= dms.getFileIsExists(path);
			return responseModel;
//			dms.getRequestDispatcher(path, false).forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
//			dms.defaultResourceHandler.prepare(e.getMessage()).forward(req, resp);
		}
		return null;
	}

	@RequestMapping("/docman/download/**")
	@ResponseBody
	public void download(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String path = getPath(req);
		String result = dms.download(path, resp);
		if (result != null) {
			resp.setContentType("text/plain;charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(result);
		}
		resp.flushBuffer();
	}

	@RequestMapping("/docman/resource/**")
	public void getResource(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String path = getPath(req);
		dms.getRequestDispatcher(path, true).forward(req, resp);
	}

	private final Pattern pathParm = Pattern.compile("/[^/\\\\]+/docman/[^/\\\\]+(/.*)?");

	private String getPath(HttpServletRequest req) throws UnsupportedEncodingException {
		String uri = req.getRequestURI();
//		Matcher mat = pathParm.matcher(uri);
//		String path = mat.find() ? mat.group(1) : null;
		//2020年2月28日18:32:14 liujingeng修改
		String path = null;
		if (uri != null && uri.contains("/docman")) {
			char[] array = uri.toCharArray();
			int index = 2;
			for (int i = array.length - 1; i > -1; i--) {
				if (array[i] == '/' && --index == 0) {
					path = uri.substring(i);
				}
			}
		}

		if (path == null)
			path = "/";
		else
			path = URLDecoder.decode(path, "utf-8");
		return path;
	}
}
