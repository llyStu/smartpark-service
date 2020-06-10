package com.vibe.service.docman;

import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

abstract public class ConvertHandler implements ResourceHandler {
    private static final ConcurrentHashMap<String, Callable<String>> LOCK = new ConcurrentHashMap<>();

    @Override
    public Dispatcher prepare(String path) {
        boolean qs = path.endsWith(".docx.html");
        String qs2 = Charset.defaultCharset().name();
        return (req, resp) -> {

            resp.setCharacterEncoding(qs ? qs2 : "UTF-8");
            req.getRequestDispatcher("WEB-INF/docman-convert/" + path).forward(req, resp);
        };
    }

    public String convert(File file, String type, File rootConvert, String path) throws Exception {
        final String key = file.getPath();
        final CountDownLatch latch = new CountDownLatch(1);
        final String[] ret = {null};
        try {
            Callable<String> future = () -> {
                latch.await();
                return ret[0];
            };
            LOCK.putIfAbsent(key, future);
            Callable<String> lock = LOCK.get(key);
            if (lock != future) return lock.call();

            ret[0] = path = path + ".html";
            File converted = new File(rootConvert, path);

            if (!converted.isFile() || file.lastModified() != converted.lastModified()) {
                converted.getParentFile().mkdirs();
                convertToHtml(file, converted);
                converted.setLastModified(file.lastModified());
            }

            return ret[0];
        } finally {
            latch.countDown();
            if (ret[0] != null) LOCK.remove(key);
        }
    }

    abstract public void convertToHtml(File src, File dest) throws Exception;

}

