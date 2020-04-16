package com.sificomlib.logger;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SFLoggerManager {

    public Map<Date, File[]> getLogs(String logDir) {
        Map<Date, File[]> mappedLogs = new HashMap<>();
        File logRootDir = new File(logDir);
        if (logRootDir.exists() && logRootDir.isDirectory()) {
            for (File current : logRootDir.listFiles()) {
                if (checkLogFolder(current)) {
                    String[] datesplits = current.getName().split("-");
                    try {
                        Calendar.getInstance().set(Integer.parseInt(datesplits[0]), Integer.parseInt(datesplits[1]), Integer.parseInt(datesplits[2]));
                        mappedLogs.put(Calendar.getInstance().getTime(), current.listFiles());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return mappedLogs;
    }

    private boolean checkLogFolder(File dateFolder) {
        if (dateFolder.isDirectory()) {
            String folderName = dateFolder.getName();
            if (folderName.contains("-")) {
                String[] datesplits = folderName.split("-");
                if (datesplits.length == 3) {
                    return true;
                }
            }
        }
        return false;
    }

}
