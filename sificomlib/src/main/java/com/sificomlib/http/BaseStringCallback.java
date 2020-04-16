package com.sificomlib.http;

import com.sificomlib.logger.SFLogger;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class BaseStringCallback implements Callback {
    private static final String TAG = "SIFICOM_HTTP_API";

    public abstract void onFailure(int code, String error);

    public abstract void onResponse(String response);

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String res = response.body().string();
        SFLogger.logD(TAG, "StringResponse: " + res);
        if (response.isSuccessful()) {
            onResponse(res);
        } else {
            onFailure(response.code(), response.message());
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        onFailure(-1, e.getMessage());
    }
}
