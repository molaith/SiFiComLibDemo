package com.sificomlib.data.db;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.sificomlib.logger.SFLogger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class SFDBColumImpl implements BaseColumns {
    private final String TAG = "SIFI_DB";

    public abstract String getTableName();

    public void createTable(SQLiteDatabase db) {
        if (db != null) {
            String columCreations = " (" + crateFiledColums() + ");";
            String sql = "CREATE TABLE IF NOT EXISTS " + getTableName() + columCreations;
            SFLogger.logD(TAG, "createTable_sql: " + sql);
            db.execSQL(sql);
        }
    }

    private String crateFiledColums() {
        Field[] members = getClass().getDeclaredFields();
        if (members != null && members.length > 0) {
            List<String> items = new ArrayList<>();
            String primaryKey = "";
            for (Field current : members) {
                String type = "";
                SFDBColumAnnotation annotation = SFDBUtil.getSFColumAnnoType(current);
                boolean isPrimaryKey = annotation.isPrimaryKey();
                if (annotation != null) {
                    switch (annotation.getValueAnnotation().columType()[0]) {
                        case TEXT:
                            type = " TEXT";
                            if (isPrimaryKey) {
                                if (!TextUtils.isEmpty(primaryKey)) {
                                    throw new RuntimeException("already dinfine primary key" + primaryKey);
                                }
                                primaryKey = current.getName();
                                type = " TEXT PRIMARY KEY";
                            }
                            break;
                        case LONG:
                            type = " LONG";
                            if (isPrimaryKey) {
                                if (!TextUtils.isEmpty(primaryKey)) {
                                    throw new RuntimeException("already dinfine primary key" + primaryKey);
                                }
                                primaryKey = current.getName();
                                type = " LONG PRIMARY KEY";
                            }
                            break;
                        case INTEGER:
                            type = " INTEGER";
                            if (isPrimaryKey) {
                                if (!TextUtils.isEmpty(primaryKey)) {
                                    throw new RuntimeException("already dinfine primary key" + primaryKey);
                                }
                                primaryKey = current.getName();
                                type = " INTEGER PRIMARY KEY";
                            }
                            break;
                    }
                }
                if (!TextUtils.isEmpty(type)) {
                    items.add(current.getName().toUpperCase() + type);
                }
            }
            if (items.size() > 0) {
                String result = "";
                for (int i = 0; i < items.size(); i++) {
                    if (i == items.size() - 1) {
                        result = result + items.get(i);
                    } else {
                        result = result + items.get(i) + ",";
                    }
                }
                return result;
            }
        }
        return "";
    }
}
