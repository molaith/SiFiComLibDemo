package com.sificomlib.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by molaith on 2017/4/5.
 */

public class DataPreferences {
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferenceEditor;
    private final String split = "sificom_siplit";

    public DataPreferences(Context context) {
        preferences = context.getSharedPreferences("sificom.SharedPreferences",
                Context.MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }

    public boolean containKey(String key) {
        return preferences.contains(key);
    }

    public void deleteValue(String key) {
        preferenceEditor.remove(key);
        preferenceEditor.commit();
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        preferenceEditor.putBoolean(key, value);
        preferenceEditor.commit();
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public void putString(String key, String value) {
        preferenceEditor.putString(key, value);
        preferenceEditor.commit();
    }

    public long getLong(String key) {
        return preferences.getLong(key, 0);
    }

    public void putLong(String key, long value) {
        preferenceEditor.putLong(key, value);
        preferenceEditor.commit();
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultvalue) {
        return preferences.getInt(key, defaultvalue);
    }

    public void putInt(String key, int value) {
        preferenceEditor.putInt(key, value);
        preferenceEditor.commit();
    }

    public List<String> getStringList(String key) {
        List<String> stringList = new ArrayList<>();
        String stringValue = preferences.getString(key, "");
        if (!TextUtils.isEmpty(stringValue)) {
            for (String current : stringValue.split(split)) {
                stringList.add(current);
            }
        }
        return stringList;
    }

    public void addStringList(String key, String value) {
        List<String> list = getStringList(key);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(value);
        putStringList(key, list);
    }

    public void putStringList(String key, List<String> value) {
        if (value != null) {
            if (value.size() > 0) {
                String stringValue = "";
                for (String current : value) {
                    stringValue = TextUtils.isEmpty(stringValue) ? current : stringValue + split + current;
                }
                preferenceEditor.putString(key, stringValue);
            } else {
                preferenceEditor.remove(key);
            }
            preferenceEditor.commit();
        }
    }
}
