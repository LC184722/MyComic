package com.qc.mycomic.util;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetUtil {

    private static final String TAG = "NetUtil";

    private static OkHttpClient okHttpClient = new OkHttpClient();

    public static Request getRequest(String url) {
        if (url.contains(SourceUtil.getSource(Codes.TENG_XUN).getIndex())) {
            return new Request.Builder().url(url).addHeader("User-Agent", Codes.USER_AGENT_WEB).method("GET", null).build();
        } else {
            return new Request.Builder().url(url).addHeader("User-Agent", Codes.USER_AGENT).method("GET", null).build();
        }
    }

    public static Request getRequest(String url, Map<String, String> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        if (!map.containsKey("User-Agent")) {
            map.put("User-Agent", Codes.USER_AGENT);
        }
        Headers.Builder builder = new Headers.Builder();
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (value != null) {
                builder.add(key, value);
            }
        }
        Headers headers = builder.build();
        return new Request.Builder().url(url).headers(headers).method("GET", null).build();
    }

    public static void startLoad(Request request, Callback callback) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public static void startLoad(String url, Callback callback) {
        Call call = okHttpClient.newCall(getRequest(url));
        call.enqueue(callback);
    }

    private void demo(String url) {
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建Request对象，设置一个url地址,设置请求方式。
        Request request = new Request.Builder().url(url).method("GET", null).build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.请求加入调度，重写回调方法
        call.enqueue(new Callback() {
            //请求失败执行的方法
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            //请求成功执行的方法
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String data = response.body().string();
                Log.i(TAG, "onResponse: " + response.toString());
                AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }

}
