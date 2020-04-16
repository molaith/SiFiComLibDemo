package com.sificomlib.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class SFLoggerWriter {

    public static void write(String message, String logDir, String encoding, boolean append) throws IOException {
        File logparentDir = new File(logDir);
        if (!logparentDir.isDirectory()) {
            throw new IOException(logparentDir.getPath() + " is not a Directory");
        }
        if (!logparentDir.exists()) {
            logparentDir.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SSS");
        String[] date = sdf.format(System.currentTimeMillis()).split(" ");
        String dateFolder = date[0] + File.separator;
        String logFileName = date[1] + ".log";
        File logFile = new File(logDir + File.separator + dateFolder, logFileName);

        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdirs();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(logFile, append);
        fileOutputStream.write(message.getBytes(encoding));
        fileOutputStream.close();
    }
}
