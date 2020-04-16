package com.sificomlib.logger;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class SFLoggerReader {
    private static final long maxFileReadSize = 5 * 1024 * 1024;

    public static String read(File logFile) {
        return read(logFile, null);
    }

    public static String read(File logFile, IReadFileListener listener) {
        FileInputStream fileInputStream = null;
        try {
            if (logFile.length() > maxFileReadSize) {
                throw new IOException("(" + logFile.length() + ") file is too larg, max is " + maxFileReadSize);
            }
            fileInputStream = new FileInputStream(logFile);
            long fileLength = logFile.length();
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            char[] buffer = new char[4096];
            StringBuilder stringBuilder = new StringBuilder();
            int n = 0;
            int readedLength = 0;
            for (boolean var5 = false; -1 != (n = inputStreamReader.read(buffer)); readedLength += (long) n) {
                String partial = new String(buffer, 0, n);
                stringBuilder.append(partial);
                if (listener != null) {
                    listener.onReadingProgress(partial, (int) (readedLength * 100 / fileLength));
                }
            }
            if (listener != null) {
                listener.onReadComplete(stringBuilder.toString(), readedLength);
            }
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void readAndShow(File logFile, AlertDialog.Builder dialog) {
        String content = read(logFile);
        if (dialog != null) {
            dialog.setTitle(logFile.getName());
            dialog.setMessage(content);
            dialog.setCancelable(false);
            dialog.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog.create().show();
        }
    }


}
