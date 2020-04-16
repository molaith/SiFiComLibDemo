package com.sificomlib.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SFDBAccessor {
    private SFDBHelper dbHelper = null;
    private String tableName = "";
    private String whereArgs = "";

    public SFDBAccessor(SFDBHelper helper, Class<? extends SFDBColumImpl> constantClass) {
        dbHelper = helper;
        try {
            tableName = constantClass.newInstance().getTableName();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public SFDBAccessor addWhere(String wherearg, Object value) {
        whereArgs = (TextUtils.isEmpty(whereArgs) ? "" : (whereArgs + " AND ")) + wherearg + " ='" + value + "'";
        return this;
    }

    public <C extends SFDBColumImpl> void add(C value) {
        SQLiteDatabase db = dbHelper.openDB();
        ContentValues cvValues = new ContentValues();
        db.beginTransaction();
        try {
            if (value != null) {
                Field[] constantFields = value.getClass().getDeclaredFields();
                for (Field current : constantFields) {
                    Object cvValue = getValue(value, current);
                    if (cvValue != null) {
                        if (cvValue instanceof String) {
                            cvValues.put(current.getName(), (String) cvValue);
                        } else if (cvValue instanceof Integer) {
                            cvValues.put(current.getName(), (Integer) cvValue);
                        } else if (cvValue instanceof Long) {
                            cvValues.put(current.getName(), (Long) cvValue);
                        }
                    }
                }
                db.insert(tableName, null, cvValues);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private Object getValue(Object value, Field field) {
        for (Field current : value.getClass().getDeclaredFields()) {
            if (current.getName().equals(field.getName())) {
                try {
                    return current.get(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public <C extends SFDBColumImpl> void update(String[] args, C value) {
        SQLiteDatabase db = dbHelper.openDB();
        ContentValues cv = new ContentValues();
        db.beginTransaction();
        try {
            for (String currentArg : args) {
                for (Field currentfield : value.getClass().getDeclaredFields()) {
                    if (currentArg.equals(currentfield.getName())) {
                        try {
                            Object fieldValue = currentfield.get(value);
                            if (fieldValue instanceof String) {
                                cv.put(currentfield.getName().toUpperCase(), (String) fieldValue);
                            } else if (fieldValue instanceof Integer) {
                                cv.put(currentfield.getName().toUpperCase(), (Integer) fieldValue);
                            } else if (fieldValue instanceof Long) {
                                cv.put(currentfield.getName().toUpperCase(), (Long) fieldValue);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
            Log.d("molaith", "update where case: " + whereArgs);
            db.update(tableName, cv, whereArgs, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    public <C extends SFDBColumImpl> List<C> query(Class<C> constantClass) {
        SQLiteDatabase db = dbHelper.openDB();
        String sql = "SELECT * FROM " + tableName + whereArgs;
        Log.d("molaith", "query where case: " + sql);
        List<C> listResult = new ArrayList();
        db.beginTransaction();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    try {
                        C object = constantClass.newInstance();
                        setValue(object, cursor);
                        listResult.add(object);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
        }
        return listResult;
    }

    private <C extends SFDBColumImpl> void setValue(C value, Cursor cursor) {
        for (Field constantField : value.getClass().getDeclaredFields()) {
            SFDBColumAnnotation annotation = SFDBUtil.getSFColumAnnoType(constantField);
            if (annotation != null) {
                try {
                    switch (annotation.getValueAnnotation().columType()[0]) {
                        case TEXT:
                            String stringValue = cursor.getString(cursor.getColumnIndex(constantField.getName().toUpperCase()));
                            value.getClass().getDeclaredField(constantField.getName()).set(value, stringValue);
                            break;
                        case INTEGER:
                            int intValue = cursor.getInt(cursor.getColumnIndex(constantField.getName().toUpperCase()));
                            value.getClass().getDeclaredField(constantField.getName()).set(value, intValue);
                            break;
                        case LONG:
                            long longValue = cursor.getLong(cursor.getColumnIndex(constantField.getName().toUpperCase()));
                            value.getClass().getDeclaredField(constantField.getName()).set(value, longValue);
                            break;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int delete(String[] whilecause, Object[] whileargs) {
        if (TextUtils.isEmpty(whereArgs)) {
            return -1;
        }
        SQLiteDatabase db = dbHelper.openDB();
        Log.d("molaith", "delete where case: " + whereArgs);
        db.beginTransaction();
        try {
            return db.delete(tableName, whereArgs, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return -1;
    }

}
