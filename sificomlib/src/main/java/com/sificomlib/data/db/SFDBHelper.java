package com.sificomlib.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SFDBHelper extends SQLiteOpenHelper {
    private static SFDBHelper mInstance = null;
    public static final String DB_NAME = "sificom.db";
    public static int DB_VERSION = 1;
    private boolean mainTmpDirSet = false;//是否设置db临时目录
    private Context context;
    private String lastDB = "";
    private SQLiteDatabase dataBase = null;
    private SFDBAccessor accessor = null;
    private List tableList = new ArrayList<>();

    private SFDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    private SFDBHelper(String dbName, Context context) {
        super(context, dbName, null, DB_VERSION);
        this.context = context;
    }

    public static SFDBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SFDBHelper(context);
        }
        return mInstance;
    }

    public <C extends SFDBColumImpl> void addTable(C dbConstant) {
        tableList.add(dbConstant);
    }

    public SFDBAccessor getAccessor(Class<? extends SFDBColumImpl> constantClass) {
        accessor = new SFDBAccessor(this, constantClass);
        return accessor;
    }

    public void init() {
        if (dataBase == null) {
            openDB();
        }
        createCollectionTable(dataBase);
    }

    public SQLiteDatabase openDB() {
        return openDB("data/data/" + context.getPackageName() + "/databases/" + DB_NAME);
    }

    public void closeDB() {
        if (dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

    public SQLiteDatabase openDB(String dbPath) {
        if (dataBase == null) {
            try {
                File file = new File(dbPath);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                dataBase = SQLiteDatabase.openOrCreateDatabase(dbPath, null, new DefaultDatabaseErrorHandler());
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            while (dataBase.inTransaction()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataBase;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createCollectionTable(db);
    }

    private void createCollectionTable(SQLiteDatabase db) {
        for (Object constant : tableList) {
            if (constant instanceof SFDBColumImpl) {
                ((SFDBColumImpl) constant).createTable(db);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase dataBase, int ver1, int ver2) {

    }

    public boolean isTableExist(String dbName, String tableName) {
        SQLiteDatabase db = openDB(dbName);
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master where type='table'", null);
        boolean result = false;
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("name")).equals(tableName)) {
                    result = true;
                    break;
                }
            }
            cursor.close();
        }
        db.close();
        return result;
    }

}
