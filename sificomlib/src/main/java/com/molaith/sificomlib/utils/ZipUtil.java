package com.molaith.sificomlib.utils;

public class ZipUtil {

    public static void unZipFile(String path,String desPath,IUnZipProgressListener callback){
        
    }

    public interface IUnZipProgressListener{
        void onUnzipProgress(int progress);
        void onUnzipComplete();
        void onError(int code, String msg);
    }
}
