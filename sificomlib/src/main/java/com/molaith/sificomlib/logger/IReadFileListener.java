package com.molaith.sificomlib.logger;

public interface IReadFileListener {
    void onReadingProgress(String content, int percent);
    void onReadComplete(String content, long length);
}
