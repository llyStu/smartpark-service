package com.vibe.service.backup;

import java.io.File;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vibe.mapper.docman.DocumentManagementMapper;
import com.vibe.util.PathTool;
import com.vibe.utils.Back;
import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;

@Service
public class BackupServiceImpl implements BackupService {
	
	@Autowired
	private DocumentManagementMapper dmm;
	private final String DOC_ROOT = "/docman-root/";
	private final String UPLOAD = "/upload/";
	private final String DOC_CONVERT = "/docman-convert/";
	private final String WEB_INF = "/WEB-INF";
	private String defultBackupPath ;
	private ServletContext sc;
	private List<BackupBean> files = new ArrayList<BackupBean>();
	@Autowired
	private void initBackupFileList(ServletContext sc){
		this.sc = sc;
	//	defultBackupPath = sc.getRealPath("/WEB-INF/backup/");
		defultBackupPath = "D://backup/";
		String[] paths = { WEB_INF+DOC_ROOT, WEB_INF+DOC_CONVERT,UPLOAD};
		for (String path : paths) {
			BackupBean backupBean = new BackupBean();
			backupBean.setFile(new File(sc.getRealPath(path)));
			String[] split = path.split("/");
			int length = split.length;
			backupBean.setFileName(split[length-1]);
			files.add(backupBean);
		}
	}
	@Override
	public FormJson backupFiles(List<BackupBean> dirs, String backupPath) {
			for (BackupBean srcFiles : dirs) {
				File srcFile = srcFiles.getFile();
				if (!srcFile.exists()||srcFile.list() == null) {
					return FormJsonBulider.fail("不存在要备份的文件");
				}
				File bkRoot = new File(backupPath+"/file");
				if (!bkRoot.isDirectory() && !bkRoot.mkdirs()) {
					return FormJsonBulider.fail("文件夹创建错误");
				}
				try  {  
					ZipCompress zc = new ZipCompress(bkRoot, srcFiles.getFileName()+ ".zip");     
					zc.compress(srcFile.getAbsolutePath()); 
				} catch (Exception e) {
					e.printStackTrace();
					return FormJsonBulider.fail(e.getMessage());
				}
			}
			return FormJsonBulider.success().withCause("file");
	}
	
	@Value("${jdbc.username}") 
	private String username;
	@Value("${jdbc.password}") 
	private String password;
	@Value("${ip}") 
	private String ip;
	@Value("${port}") 
	private String port;
	@Override
	public FormJson backupData(List<String> dataBase, String descPath) {
		try {
			for (String database : dataBase) {
			String sqlPath = descPath+"/sql/"+database+".sql";
			System.out.println(descPath);
			Back.dbBackup(username, password, ip, port,database,
					sqlPath);
			}
			return FormJsonBulider.success().withCause("sql");
		} catch (Exception e) {
			return FormJsonBulider.fail(e.getMessage());
		}

	}
	@Override
	public FormJson recoverData(File[] srcFile) {
		try {
			for (File sql : srcFile) {
				String srcName = PathTool.getFileName(sql.getName());
				String path = sql.getPath();
				String replace = path.replace("\\", "/");
				if(srcName == null){
					return FormJsonBulider.fail("没有sql文件");
				}
				Back.dbRecover(username, password, ip, port, srcName, replace);
			}
			return FormJsonBulider.success().withCause("sql");
		} catch (Exception e) {
			e.printStackTrace();
			return FormJsonBulider.fail(e.getMessage());
		}

	}
	public static void main(String[] args) {
	
		String localTime = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
		
		System.out.println(localTime);
	}
	@Override
	public FormJson backupAll() {
		//id 备份时间    人  路径    备注   路径/时间/文件  数据库 / 备份文件和数据库
		String localTime = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
		String backupPath =defultBackupPath+"/"+localTime;
		FormJson backupFiles = backupFiles(files,backupPath);
		FormJson backupData = backupData(Arrays.asList("db_vibe_basic","db_vibe_data","db_vibe_monitor"), backupPath);
		if(backupFiles.isSuccess() && backupData.isSuccess()){
			return FormJsonBulider.success().withCause("total");
		}else if(!backupFiles.isSuccess()){
			return backupFiles;
		}else{
			return backupData;
		}
		//最新时间  
	}
	//恢复备份
	@Override
	public FormJson recoverAll() {
		//获取时间最大文件夹
		try {
			File maxfile = getMaxtimeFile(defultBackupPath);
			File[] childList =maxfile.listFiles();
			for (File child : childList) {
				if("file".equals(child.getName())){
					File[] files = child.listFiles();
					for (File srcfile : files) {
						String name= srcfile.getName();
						String srcName = PathTool.getFileName(name);
						if(srcName != null){
							if("upload".equals(srcName)){
								 recoverFile(srcfile, sc.getRealPath("/"));
							}else{
								 recoverFile(srcfile, sc.getRealPath(WEB_INF+"/"));
							}
						}
					}
				}
				if("sql".equals(child.getName())){
					return  recoverData(child.listFiles());
				}
			}	
			return FormJsonBulider.fail("不存在备份的文件");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return FormJsonBulider.fail("恢复文件失败");
		}
	}
	private File getMaxtimeFile(String path) throws ParseException {
		File file = new File(path);
		String[] timelist = file.list();
		long maxtime = 0;
		String fileName = "";
		if(timelist != null &&  timelist.length>0){
			for (String time : timelist) {
				long timeLong = new SimpleDateFormat("yyyyMMdd-HHmmss").parse(time).getTime();
				if(timeLong > maxtime){
					fileName = time;
					maxtime = timeLong;
				}
			}
		}
		 file = new File(path+"/"+fileName);
		return file;
	}
	
	public void recoverFile(File src, String dirRecover) {
		ZipUncompress zipUncompress = new  ZipUncompress();
		zipUncompress.unZiFiles(src, dirRecover);
	}
	public DocumentManagementMapper getDmm() {
		return dmm;
	}
	public void setDmm(DocumentManagementMapper dmm) {
		this.dmm = dmm;
	}
}
