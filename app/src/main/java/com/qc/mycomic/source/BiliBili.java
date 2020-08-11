package com.qc.mycomic.source;

import android.util.Log;

import com.qc.mycomic.json.JsonNode;
import com.qc.mycomic.json.JsonStarter;
import com.qc.mycomic.model.ChapterInfo;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.model.MyMap;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.StringUtil;

import java.util.LinkedList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.Request;

public class BiliBili implements Source {
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
        String[] attrs = rankUrl.split("#");
        System.out.println("attrs[0] = " + attrs[0]);
        System.out.println("attrs[1] = " + attrs[1]);
        String url = String.format("https://manga.bilibili.com/twirp/comic.v1.Comic/%s?device=pc&platform=web", attrs[0]);
        JsonNode node = new JsonNode(attrs[1]);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (String key : node.keySet()) {
            System.out.println("key = " + key);
            System.out.println("node = " + node.string(key));
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
        //https://manga.bilibili.com/twirp/comic.v1.Comic/HomeHot?device=pc&platform=web {"type":3}
        map.put("日漫榜", "HomeHot#{\"type\":3}");
        map.put("国漫榜", "HomeHot#{\"type\":4}");//{"type":4}
        map.put("月票榜", "HomeFans#{\"last_week_offset\":0,\"last_month_offset\":0,\"type\":1}");//{"last_week_offset":0,"last_month_offset":0,"type":1}
        map.put("投喂榜", "HomeFans#{\"last_week_offset\":0,\"last_month_offset\":0,\"type\":0}");//{"last_week_offset":0,"last_month_offset":0,"type":0}
        map.put("飙升榜", "HomeHot#{\"type\":2}");//{"type":2}
        map.put("免费榜", "HomeHot#{\"type\":1}");//{"type":1}
        //https://manga.bilibili.com/twirp/comic.v1.Comic/ClassPage?device=pc&platform=web {"style_id":1028,"area_id":-1,"is_finish":-1,"order":0,"page_num":1,"page_size":18,"is_free":-1}
        map.put("免费", "ClassPage#{\"style_id\":-1,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":1}");
        map.put("付费", "ClassPage#{\"style_id\":-1,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":2}");
        map.put("等就免费", "ClassPage#{\"style_id\":-1,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":3}");
        map.put("连载", "ClassPage#{\"style_id\":-1,\"area_id\":-1,\"is_finish\":0,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("完结", "ClassPage#{\"style_id\":-1,\"area_id\":-1,\"is_finish\":1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("正能量", "ClassPage#{\"style_id\":1028,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("冒险", "ClassPage#{\"style_id\":1013,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("热血", "ClassPage#{\"style_id\":999,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("搞笑", "ClassPage#{\"style_id\":994,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("恋爱", "ClassPage#{\"style_id\":995,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("少女", "ClassPage#{\"style_id\":1026,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("日常", "ClassPage#{\"style_id\":1020,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("校园", "ClassPage#{\"style_id\":1001,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("运动", "ClassPage#{\"style_id\":1010,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("治愈", "ClassPage#{\"style_id\":1007,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("古风", "ClassPage#{\"style_id\":997,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("玄幻", "ClassPage#{\"style_id\":1016,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("奇幻", "ClassPage#{\"style_id\":998,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("后宫", "ClassPage#{\"style_id\":1017,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("惊奇", "ClassPage#{\"style_id\":996,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("悬疑", "ClassPage#{\"style_id\":1023,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("都市", "ClassPage#{\"style_id\":1002,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("总裁", "ClassPage#{\"style_id\":1004,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
        map.put("剧情", "ClassPage#{\"style_id\":1030,\"area_id\":-1,\"is_finish\":-1,\"order\":0,\"page_num\":1,\"page_size\":200,\"is_free\":-1}");
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
                String id = node.string("comic_id");
                if (id == null) {
                    id = node.string("season_id");
                }
                String detailUrl = "https://manga.bilibili.com/detail/mc" + id;
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        List<ComicInfo> list = starter.startDataList(html, "data");
        if (list.isEmpty()) {
            list = starter.startDataList(html, "data", "comics");
        }
        return list;
    }
}
