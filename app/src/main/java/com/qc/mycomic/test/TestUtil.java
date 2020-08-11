package com.qc.mycomic.test;

import com.qc.mycomic.json.JsonNode;
import com.qc.mycomic.json.JsonStarter;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.StringUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class TestUtil {

    public static void main(String[] args) {
//        Callback callback = new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String html = response.body().string();
//                JsonNode node = new JsonNode(html, "data", "index");
//                String pics = node.string("pics");
//                Callback callback = new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        String html = response.body().string();
//                        System.out.println("response.body().string() = " + html);
//                    }
//                };
//                String url = "https://manga.bilibili.com/twirp/comic.v1.Comic/ImageToken?device=pc&platform=web";
//                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//                builder.addFormDataPart("urls", pics);
//                Request request = new Request.Builder().addHeader("User-Agent", Codes.USER_AGENT_WEB).url(url).post(builder.build()).build();
//                NetUtil.startLoad(request, callback);
//            }
//        };
//        String url = "https://manga.bilibili.com/twirp/comic.v1.Comic/GetImageIndex?device=pc&platform=web";
//        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        builder.addFormDataPart("ep_id", "307985");
//        Request request = new Request.Builder().addHeader("User-Agent", Codes.USER_AGENT_WEB).url(url).post(builder.build()).build();
//        NetUtil.startLoad(request, callback);

        String s = "https://manga.bilibili.com/mc25816/481442";
        int index = s.lastIndexOf('/');
        String id = StringUtil.match("(\\d+)", s.substring(index));
        System.out.println("id = " + id);

    }
}

