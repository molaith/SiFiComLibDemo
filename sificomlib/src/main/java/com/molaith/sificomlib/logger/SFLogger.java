package com.molaith.sificomlib.logger;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class SFLogger {
    public static boolean isShowLog = true;
    public static String logDirPath = "";
    public static boolean isWriteLog = false;
    private static final String TAG = SFLogger.class.getName();

    public static void logV(String tag, String message) {
        if (isShowLog) {
            logout(Log.VERBOSE, tag, formattMessage(message));
        }
        writeLog(tag, message);
    }

    public static void logV(String message) {
        logV(TAG, message);
    }

    public static void logI(String tag, String message) {
        if (isShowLog) {
            logout(Log.INFO, tag, formattMessage(message));
        }
        writeLog(tag, message);
    }

    public static void logI(String message) {
        logI(TAG, message);
    }

    public static void logD(String tag, String message) {
        if (isShowLog) {
            logout(Log.DEBUG, tag, formattMessage(message));
        }
        writeLog(tag, message);
    }

    public static void logD(String message) {
        logD(TAG, message);
    }

    public static void logW(String tag, String message) {
        if (isShowLog) {
            logout(Log.WARN, tag, formattMessage(message));
        }
        writeLog(tag, message);
    }

    public static void logW(String message) {
        logW(TAG, message);
    }

    public static void logE(String tag, String message) {
        if (isShowLog) {
            logout(Log.ERROR, tag, formattMessage(message));
        }
        writeLog(tag, message);
    }

    public static void logE(String message) {
        logE(TAG, message);
    }

    private static void logout(int property, String tag, String[] messages) {
        for (String current : messages) {
            Log.println(property, tag, current);
        }
    }

    private static String[] formattMessage(String message) {
        if (message.length() > 4000) {
            int count = (message.length() / 4) + 1;
            String[] logs = new String[count];
            for (int i = 0; i < count; i++) {
                int start = i * 4000;
                int end = start + 4000;
                logs[i] = message.substring(start, end >= message.length() ? message.length() : end);
            }
            return logs;
        } else {
            return new String[]{message};
        }
    }

    private static void writeLog(String tag, String message) {
        if (isWriteLog) {
            try {
                String writeLogDir = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+ "SiFiLogger";
                if (!TextUtils.isEmpty(logDirPath)) {
                    writeLogDir = logDirPath;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
                String content = sdf.format(System.currentTimeMillis()) + " [" + tag + "] " + message;
                SFLoggerWriter.write(content, writeLogDir, "UTF-8", true);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
