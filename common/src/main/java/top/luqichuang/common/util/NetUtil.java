package top.luqichuang.common.util;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.luqichuang.common.self.CommonCallback;

/**
 * @author LuQiChuang
 * @desc 网络连接工具
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class NetUtil {

    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Mobile Safari/537.36";

    public static final String USER_AGENT_WEB = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36";

    private static final String TAG = "NetUtil";

    private static OkHttpClient okHttpClient = new OkHttpClient();

    /**
     * 根据url，获得默认request
     *
     * @param url url
     * @return Request
     */
    public static Request getRequest(String url) {
        return getRequest(url, getAgent(url));
    }

    /**
     * 设置获得request的userAgent
     *
     * @param url       url
     * @param userAgent userAgent
     * @return Request
     */
    public static Request getRequest(String url, String userAgent) {
        if (url == null) {
            return null;
        } else {
            return new Request.Builder().url(url).addHeader("User-Agent", userAgent).method("GET", null).build();
        }
    }

    /**
     * 通过map设置请求，获得Request
     *
     * @param url url
     * @param map 存放头部信息
     * @return Request
     */
    public static Request getRequestByHeader(String url, Map<String, String> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        if (!map.containsKey("User-Agent")) {
            map.put("User-Agent", getAgent(url));
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

    /**
     * 通过map设置请求，获得Request
     *
     * @param url url
     * @param map 存放param信息
     * @return Request
     */
    public static Request getRequest(String url, Map<String, String> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        StringBuilder builder = new StringBuilder(url + "?");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return getRequest(builder.toString());
    }

    /**
     * 发送post请求
     *
     * @param url  url
     * @param data data
     * @return Request
     */
    public static Request postRequest(String url, String... data) {
        Map<String, String> map = new HashMap<>();
        if (data != null && data.length > 0 && data.length % 2 == 0) {
            for (int i = 0; i < data.length; i = i + 2) {
                map.put(data[i], data[i + 1]);
            }
        }
        return postRequest(url, getAgent(url), map);
    }

    /**
     * 发送post请求
     *
     * @param url         url
     * @param formDataMap formDataMap
     * @return Request
     */
    public static Request postRequest(String url, Map<String, String> formDataMap) {
        return postRequest(url, getAgent(url), formDataMap);
    }

    /**
     * 发送post请求
     *
     * @param url         url
     * @param userAgent   userAgent
     * @param formDataMap formDataMap
     * @return Request
     */
    public static Request postRequest(String url, String userAgent, Map<String, String> formDataMap) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : formDataMap.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        return new Request.Builder().addHeader("User-Agent", userAgent).url(url).post(builder.build()).build();
    }

    public static Request postRequest(String url, Map<String, String> formDataMap, Map<String, String> headerMap) {
        Request.Builder requestBuilder = new Request.Builder();
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : formDataMap.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        return requestBuilder.url(url).post(builder.build()).build();
    }

    /**
     * 使用默认request开始连接网络
     *
     * @param url      url
     * @param callback callback
     * @return void
     */
    public static void startLoad(String url, Callback callback) {
        startLoad(getRequest(url), callback);
    }

    /**
     * 使用特定request开始连接网络
     *
     * @param callback callback
     * @return void
     */
    public static void startLoad(CommonCallback callback) {
        startLoad(callback.getRequest(), callback);
    }

    /**
     * 使用特定request开始连接网络
     *
     * @param request  request
     * @param callback callback
     * @return void
     */
    public static void startLoad(Request request, Callback callback) {
//        System.out.println("request.url() = " + request.url());
        System.out.println("request = " + request);
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    /**
     * 使用Jsoup连接网络
     *
     * @param url url
     * @return String
     */
    public static String startLoadWithJsoup(String url) {
        String html = null;
        try {
            html = Jsoup.connect(url).get().html();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return html;
    }

    private static String getAgent(String url) {
        if (url != null && url.contains("://m.")) {
            return USER_AGENT;
        } else {
            return USER_AGENT_WEB;
        }
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
                //Log.e(TAG, "onFailure: ", e);
            }

            //请求成功执行的方法
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String data = response.body().string();
                //Log.i(TAG, "onResponse: " + response.toString());
//                AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
            }
        });
    }

}
