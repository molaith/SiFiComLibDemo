package com.sificomlib.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {

    public static void unZipFile(String path, String desPath, IUnZipProgressListener listener) {
        try {
            ZipFile zipFile = new ZipFile(path);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entry.isDirectory()) {
                    releaseCildDir(desPath, zipFile, entry);
                } else {
                    releaseChildFile(desPath, zipFile, entry);
                }
            }
            listener.onUnzipComplete();
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onError(-1, e.getLocalizedMessage());
            }
        }
    }

    private static void releaseCildDir(String despath, ZipFile zip, ZipEntry entry) {
        File childDir = new File(despath + File.separator + entry.getName());
        if (!childDir.exists()) {
            childDir.mkdirs();
        }
    }

    private static void releaseChildFile(String despath, ZipFile zip, ZipEntry entry) throws IOException {
        File file = new File(despath, entry.getName());
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(zip.getInputStream(entry));
            out = new BufferedOutputStream(new FileOutputStream(file));
            byte buffer[] = new byte[240];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public interface IUnZipProgressListener {
        void onUnzipProgress(int progress);

        void onUnzipComplete();

        void onError(int code, String msg);
    }
}
