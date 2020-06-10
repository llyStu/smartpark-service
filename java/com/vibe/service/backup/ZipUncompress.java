package com.vibe.service.backup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 适用于目录下，中文名称是UTF-8编码的文件
 *
 * @author Game
 */
public class ZipUncompress {
    public void unZiFiles(File zipFile, String descDir) {
        File pathFile = new File(descDir);

        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = null;
        InputStream in = null;
        OutputStream out = null;

        try {
            zip = new ZipFile(zipFile);
            Enumeration<?> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                in = zip.getInputStream(entry);

                String outPath = (descDir + "/" + zipEntryName).replace("\\*", "/");
                //判断路径是否存在，不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经创建,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                out = new FileOutputStream(outPath);

                byte[] buf = new byte[4 * 1024];
                int len;
                while ((len = in.read(buf)) >= 0) {
                    out.write(buf, 0, len);
                }
                out.flush();
                in.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (zip != null) {
                    zip.close();
                }
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ZipUncompress zipUncompress = new ZipUncompress();
        File zipFile = new File("E:/resource/resource.zip");
        zipUncompress.unZiFiles(zipFile, "E:/resource");
    }
}
