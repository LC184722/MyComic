package com.qc.mycomic.test;

import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qc.mycomic.json.JsonNode;
import com.qc.mycomic.json.JsonStarter;
import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.jsoup.JsoupStarter;
import com.qc.mycomic.model.ChapterInfo;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.model.MyMap;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.source.ManHuaFen;
import com.qc.mycomic.source.MiTui;
import com.qc.mycomic.source.PuFei;
import com.qc.mycomic.source.TengXun;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.DecryptUtil;
import com.qc.mycomic.util.ImageInfoUtil;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.StringUtil;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author LuQiChuang
 * @description
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class HtmlTestUtil implements Source {

    @Override
    public int getSourceId() {
        return Codes.BILI_BILI;
    }

    @Override
    public String getSourceName() {
        return Codes.BILI_BILI_STRING;
    }

    @Override
    public String getIndex() {
        return "https://manga.bilibili.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = "https://manga.bilibili.com/twirp/comic.v1.Comic/Search?device=pc&platform=web";
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("key_word", searchString);
        builder.addFormDataPart("page_num", "1");
        builder.addFormDataPart("page_size", "9");
        return new Request.Builder().addHeader("User-Agent", Codes.USER_AGENT_WEB).url(url).post(builder.build()).build();
    }

    @Override
    public Request getDetailRequest(String detailUrl) {
        String url = "https://manga.bilibili.com/twirp/comic.v1.Comic/ComicDetail?device=pc&platform=web";
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        String id = StringUtil.match("(\\d+)", detailUrl);
        builder.addFormDataPart("comic_id", id);
        return new Request.Builder().addHeader("User-Agent", Codes.USER_AGENT_WEB).url(url).post(builder.build()).build();
    }

    @Override
    public Request getRankRequest(String rankUrl) {
        String[] attrs = rankUrl.split("-");
        String url = String.format("https://manga.bilibili.com/twirp/comic.v1.Comic/%s?device=pc&platform=web", attrs[0]);
        JsonNode node = new JsonNode(attrs[1]);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (String key : node.keySet()) {
            builder.addFormDataPart(key, node.string(key));
        }
        return new Request.Builder().addHeader("User-Agent", Codes.USER_AGENT_WEB).url(url).post(builder.build()).build();
    }

    @Override
    public List<ComicInfo> getComicInfoList(String html) {
        JsonStarter<ComicInfo> starter = new JsonStarter<ComicInfo>() {
            @Override
            public void dealData(JsonNode node) {

            }

            @Override
            public ComicInfo dealDataList(JsonNode node) {
                String title = node.string("org_title");
                String author = node.arrayToString("author_name");
                String updateTime = null;
                String imgUrl = node.string("vertical_cover");
                String detailUrl = "https://manga.bilibili.com/detail/mc" + node.string("id");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startDataList(html, "data", "list");
    }

    private void printJSONObject(JSONObject jsonObject) {
        System.out.println("---start---");
        for (String key : jsonObject.keySet()) {
            System.out.println(key + " --> " + jsonObject.get(key));
        }
        System.out.println("---end---");
    }

    @Override
    public void setComicDetail(ComicInfo comicInfo, String html) {
        comicInfo.setDetailUrl("https://manga.bilibili.com/detail/mc25816");
        String id = StringUtil.match("(\\d+)", comicInfo.getDetailUrl());
        JsonStarter<ChapterInfo> starter = new JsonStarter<ChapterInfo>() {
            @Override
            public void dealData(JsonNode node) {
                String author = node.arrayToString("author_name");
                String intro = node.string("classic_lines");
                String updateStatus = node.string("renewal_time");
                String updateTime = null;
                comicInfo.setDetail(author, updateTime, updateStatus, intro);
            }

            @Override
            public ChapterInfo dealDataList(JsonNode node) {
                String title = node.string("title");
                String chapterUrl = "https://manga.bilibili.com/mc" + id + "/" + node.string("id");
                return new ChapterInfo(title, chapterUrl);
            }
        };
        starter.startData(html, "data");
        comicInfo.initChapterInfoList(starter.startDataList(html, "data", "ep_list"));
    }

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
        return new LinkedList<>();
    }

    @Override
    public MyMap<String, String> getRankMap() {
        MyMap<String, String> map = new MyMap<>();
        map.put("日漫榜", "HomeHot-{\"type\":3}");//https://manga.bilibili.com/twirp/comic.v1.Comic/HomeHot?device=pc&platform=web {"type":3}
        map.put("国漫榜", "HomeHot-{\"type\":4}");//{"type":4}
        map.put("月票榜", "HomeFans-{\"last_week_offset\":0,\"last_month_offset\":0,\"type\":1}");//{"last_week_offset":0,"last_month_offset":0,"type":1}
        map.put("投喂榜", "HomeFans-{\"last_week_offset\":0,\"last_month_offset\":0,\"type\":0}");//{"last_week_offset":0,"last_month_offset":0,"type":0}
        map.put("飙升榜", "HomeHot-{\"type\":2}");//{"type":2}
        map.put("免费榜", "HomeHot-{\"type\":1}");//{"type":1}
        return map;
    }

    @Override
    public List<ComicInfo> getRankComicInfoList(String html) {
        JsonStarter<ComicInfo> starter = new JsonStarter<ComicInfo>() {
            @Override
            public void dealData(JsonNode node) {

            }

            @Override
            public ComicInfo dealDataList(JsonNode node) {
                String title = node.string("title");
                String author = node.arrayToString("author");
                String updateTime = null;
                String imgUrl = node.string("vertical_cover");
                String detailUrl = "https://manga.bilibili.com/detail/mc" + node.string("comic_id");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        List<ComicInfo> list = starter.startDataList(html, "data");
        if (list.isEmpty()) {
            list = starter.startDataList(html, "data", "comics");
        }
        return list;
    }

    public static void main(String[] args) {
        HtmlTestUtil testUtil = new HtmlTestUtil();
//        testUtil.testSearch();
//        testUtil.testDetail();
//        testUtil.testImage();
//        testUtil.testRank();
//        testUtil.test();
        testUtil.testRequest();
    }

    public void test() {
        String url = "https://manga.bilibili.com/twirp/comic.v1.Comic/%s?device=pc&platform=web";
        url = String.format(url, "hot");
        System.out.println("url = " + url);
    }

    private void testRequest() {
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = response.body().string();
                System.out.println("response.body().string() = " + html);
                List<ComicInfo> list = getRankComicInfoList(html);
                System.out.println("list = " + list);
                JsonStarter<String> starter = new JsonStarter<String>() {
                    @Override
                    public void dealData(JsonNode node) {
                    }

                    @Override
                    public String dealDataList(JsonNode node) {
                        return null;
                    }
                };
                starter.startData(html, "data", "index");


            }
        };
        //            https://manga.bilibili.com/twirp/comic.v1.Comic/GetImageIndex?device=pc&platform=web
        //            https://manga.bilibili.com/twirp/comic.v1.Comic/ImageToken?device=pc&platform=web
        String url = "https://manga.bilibili.com/twirp/comic.v1.Comic/GetImageIndex?device=pc&platform=web";
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("ep_id", "307985");
        Request request = new Request.Builder().addHeader("User-Agent", Codes.USER_AGENT_WEB).url(url).post(builder.build()).build();

//        Request request = getRankRequest("HomeFans-{\"last_week_offset\":0,\"last_month_offset\":0,\"type\":0}");

        NetUtil.startLoad(request, callback);
    }

    private void testSearch() {
//        String html = HtmlUtil.getSearchHtml();
        String html = HtmlUtil.getHtmlByFile();
        List<ComicInfo> comicInfoList;
        comicInfoList = getComicInfoList(html);
//        comicInfoList = new PuFei().getComicInfoList(html);
        if (comicInfoList != null) {
            System.out.println("comicInfoList.size() = " + comicInfoList.size());
            for (ComicInfo info : comicInfoList) {
                System.out.println("info.getTitle() = " + info.getTitle());
                System.out.println("info.getAuthor() = " + info.getAuthor());
                System.out.println("info.getUpdateTime() = " + info.getUpdateTime());
                System.out.println("info.getImgUrl() = " + info.getImgUrl());
                System.out.println("info.getDetailUrl() = " + info.getDetailUrl());
                System.out.println();
            }
        } else {
            System.out.println("comicInfoList = " + comicInfoList);
        }
    }

    private void testDetail() {
//        String html = HtmlUtil.getDetailHtml();
        String html = HtmlUtil.getHtmlByFile();
        ComicInfo info = new ComicInfo();
        setComicDetail(info, html);
//        new MiTui().setComicDetail(info, html);

        System.out.println("info.getAuthor() = " + info.getAuthor());
        System.out.println("info.getUpdateTime() = " + info.getUpdateTime());
        System.out.println("info.getUpdateChapter() = " + info.getUpdateChapter());
        System.out.println("info.getUpdateStatus() = " + info.getUpdateStatus());
        System.out.println("info.getIntro() = " + info.getIntro());
        System.out.println("info.getChapterInfoList().size() = " + info.getChapterInfoList().size());
        int size = info.getChapterInfoList().size();
        if (size > 0) {
            int first = 0;
            int last = size - 1;
            System.out.println("info.getChapterInfoList().get(first) = " + info.getChapterInfoList().get(first));
            System.out.println(".......................................");
            System.out.println("info.getChapterInfoList().get(last) = " + info.getChapterInfoList().get(last));
        }
    }

    private void testImage() {
//        String html = HtmlUtil.getImageHtml();
        String html = HtmlUtil.getHtmlByFile();
        List<ImageInfo> imageInfoList = getImageInfoList(html, 100);
//        List<ImageInfo> imageInfoList = new TengXun().getImageInfoList(html, 100);
        System.out.println("imageInfoList.size() = " + imageInfoList.size());
        for (ImageInfo imageInfo : imageInfoList) {
            System.out.println("imageInfo = " + imageInfo.getUrl());
        }
    }

    private void testRank() {
//        String html = HtmlUtil.getSearchHtml();
        String html = HtmlUtil.getHtmlByFile();
        List<ComicInfo> comicInfoList;
        comicInfoList = getRankComicInfoList(html);
//        comicInfoList = new PuFei().getComicInfoList(html);
        System.out.println("comicInfoList.size() = " + comicInfoList.size());
        for (ComicInfo info : comicInfoList) {
            System.out.println("info.getTitle() = " + info.getTitle());
            System.out.println("info.getAuthor() = " + info.getAuthor());
            System.out.println("info.getUpdateTime() = " + info.getUpdateTime());
            System.out.println("info.getImgUrl() = " + info.getImgUrl());
            System.out.println("info.getDetailUrl() = " + info.getDetailUrl());
            System.out.println();
        }
    }
}