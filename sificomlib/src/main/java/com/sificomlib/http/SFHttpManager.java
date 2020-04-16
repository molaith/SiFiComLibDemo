package com.sificomlib.http;

import android.text.TextUtils;

import com.sificomlib.logger.SFLogger;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by molaith on 2017/8/31.
 */

public class SFHttpManager {
    private static final String TAG = "SIFICOM_HTTP_MANAGER";
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    public static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
    public static final MediaType PCM = MediaType.parse("audio/x-raw,+layout=(string)interleaved,+rate=(int)16000,+format=(string)S16LE,+channels=(int)1");
    public static final MediaType WAV = MediaType.parse("audio/wav");
    public static final MediaType STREAM = MediaType.parse("application/octet-stream");

    private long TIMEOUT = 10;
    private String url = "";
    private LinkedHashMap<String, String> headers;
    private LinkedHashMap<String, String> params;
    private LinkedHashMap<String, String> forms;
    private byte[] pcmData = null;
    private String content = "";
    private boolean isEncode = false;
    private MediaType mediaType;
    private boolean isPostFile = false;
    private File[] fileDatas = null;
    private String method = "";

    public SFHttpManager url(String baseUrl) {
        url = baseUrl;
        return this;
    }

    public SFHttpManager setMediaType(MediaType type) {
        mediaType = type;
        return this;
    }

    public SFHttpManager addParam(String key, String value) {
        if (params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, value);
        return this;
    }

    public SFHttpManager addParams(HashMap<String, String> params) {
        if (params!=null){
            this.params = new LinkedHashMap<>();
            this.params.putAll(params);
        }
        return this;
    }

    public SFHttpManager get() {
        method = "GET";
        return this;
    }

    public SFHttpManager get(HashMap<String, String> params) {
        method = "GET";
        addParams(params);
        return this;
    }

    public SFHttpManager post(HashMap<String, String> params) {
        mediaType = FORM;
        method = "POST";
        addForms(params);
        return this;
    }

    public SFHttpManager postJsonString(String jsonString) {
        mediaType = JSON;
        method = "POST";
        content = jsonString;
        return this;
    }

    public SFHttpManager postPCM(byte[] data) {
        pcmData = data;
        mediaType = PCM;
        method = "POST";
        return this;
    }

    public SFHttpManager postFile(MediaType type, File file) {
        fileDatas = new File[]{file};
        isPostFile = true;
        mediaType = PCM;
        method = "POST";
        return this;
    }

    public SFHttpManager postFiles(MediaType type, File[] files) {
        fileDatas = files;
        isPostFile = true;
        mediaType = PCM;
        method = "POST";
        return this;
    }

    public SFHttpManager put(MediaType type) {
        mediaType = type;
        method = "PUT";
        return this;
    }

    public SFHttpManager delete() {
        method = "DELETE";
        return this;
    }

    public SFHttpManager addForm(String key, String value) {
        if (forms == null) {
            forms = new LinkedHashMap<>();
        }
        forms.put(key, value);
        return this;
    }

    public SFHttpManager addForms(HashMap<String, String> formParams) {
        if (formParams != null) {
            if (forms == null) {
                forms = new LinkedHashMap<>();
            }
            forms.putAll(formParams);
        }
        return this;
    }

    private void addParams() {
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        for (String key : params.keySet()) {
            if (!TextUtils.isEmpty(params.get(key))) {
                sb.append(key + "=" + params.get(key) + "&");
            }
        }
        String paramString = sb.substring(0, sb.length() - 1);
        url = url + paramString;
    }

    public SFHttpManager addHeader(String key, String value) {
        if (headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, value);
        return this;
    }

    public SFHttpManager addHeaders(HashMap<String, String> headParam) {
        if (headParam != null) {
            if (headers == null) {
                headers = new LinkedHashMap<>();
            }
            headers.putAll(headParam);
        }
        return this;
    }

    public Call buildUnVerifiedHttpsCall() {
        if (params != null) {
            addParams();
        }
        Request.Builder builder;
//        if (isEncode){
//            builder=new Request.Builder().url(getEncodeUrl(url));
//        }else {
        builder = new Request.Builder().url(url);
//        }

        if (headers != null) {
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                if (!TextUtils.isEmpty(value)) {
                    builder.header(key, headers.get(key));
                }
            }
        }

        RequestBody body = null;
        if (forms != null) {
            FormBody.Builder formBuilder = new FormBody.Builder();
            for (String key : forms.keySet()) {
                SFLogger.logD(TAG, "FORM key: " + key + ";value:" + forms.get(key));
                if (!TextUtils.isEmpty(forms.get(key))) {
                    formBuilder.add(key, forms.get(key));
                }
            }
            body = formBuilder.build();
        } else {
//            if (!TextUtils.isEmpty(content)) {
                body = RequestBody.create(mediaType, content);
//            }
        }

        if (isPostFile) {
            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (int i = 0; i < fileDatas.length; i++) {
                File current = fileDatas[i];
                body = RequestBody.create(mediaType, current);
                requestBody.addFormDataPart("file" + i, current.getName(), body);
            }
            body = requestBody.build();
        }

        switch (method) {
            case "POST":
                builder.post(body);
                break;
            case "GET":
                builder.get();
                break;
            case "DELETE":
                builder.delete();
                break;
            case "PUT":
                builder.put(body);
                break;

        }

        Request request = builder.build();
        SFLogger.logD(TAG, "request: " + request.toString() + "\n header: " + request.headers().toString());
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();
        return client.newCall(request);
    }

    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    private class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
