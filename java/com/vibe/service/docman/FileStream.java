package com.vibe.service.docman;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Service;

@Service
public class FileStream {
    private static final MimetypesFileTypeMap mineType = new MimetypesFileTypeMap();

    public void pipe(File file, HttpServletResponse resp) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = resp.getOutputStream()) {
            resp.setContentLengthLong(file.length());
            resp.setContentType("video/x-flv");

            IOUtils.copy(fis, os);
        }
    }
}
