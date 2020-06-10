package com.vibe.controller.spacemodel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibe.service.spacemodel.TwoDimensionEditorService;
import com.vibe.utils.FormJson;

@Controller
public class TwoDimensionEditorController {

    @Autowired
    private TwoDimensionEditorService tdes;

    @RequestMapping("/twoDimension/upload")
    @ResponseBody
    public synchronized FormJson upload(String name, String josnStr, HttpServletRequest request)
            throws InterruptedException {
        Thread.sleep(2000);
        String filename = tdes.upload(request, josnStr);
        return tdes.insertTwoDimensionMessage(request, name, filename);
    }

    @RequestMapping("/twoDimension/findAllName")
    @ResponseBody
    public List<String> findAllName() {
        return tdes.findAllName();
    }

    @RequestMapping("/twoDimension/findOneFilestr")
    public void findOneFilestr(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam("name") String name) throws IOException {
        String filename = tdes.findOneFilepath(name);
        if ("".equals(filename)) return;
        String path = request.getServletContext().getRealPath("/") + "upload/twoDimensionEditor/";
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(response.getOutputStream(), "utf-8");
        StringBuffer jsonStr = new StringBuffer();
        readToBuffer(jsonStr, path + filename);
        String utf8StringFromGBKString = getUTF8StringFromGBKString(jsonStr.toString());
        outputStreamWriter.write(utf8StringFromGBKString);
        outputStreamWriter.close();
    }

    @RequestMapping("/twoDimension/deleteTwoDimensionEditor")
    @ResponseBody
    public FormJson deleteTwoDimensionEditor(@RequestParam("name") String name, HttpServletRequest request) {
        return tdes.deleteTwoDimensionEditor(request, name);
    }

    private void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }

    public static String getUTF8StringFromGBKString(String gbkStr) {
        try {
            return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InternalError();
        }
    }

    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }

}
