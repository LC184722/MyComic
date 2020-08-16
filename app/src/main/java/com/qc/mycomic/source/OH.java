package com.qc.mycomic.source;

import com.alibaba.fastjson.JSONObject;
import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.jsoup.JsoupStarter;
import com.qc.mycomic.model.ChapterInfo;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.model.MyMap;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.ComicUtil;
import com.qc.mycomic.util.DecryptUtil;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.StringUtil;

import java.util.List;
import java.util.Locale;

import okhttp3.Request;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/16 23:41
 * @ver 1.0
 */
public class OH implements Source {

    @Override
    public int getSourceId() {
        return Codes.OH;
    }

    @Override
    public String getSourceName() {
        return Codes.OH_STRING;
    }

    @Override
    public String getIndex() {
        return "https://www.ohmanhua.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        searchString = "https://www.ohmanhua.com/search?searchString=" + searchString;
        return NetUtil.getRequest(searchString);
    }

    @Override
    public Request getDetailRequest(String detailUrl) {
        return NetUtil.getRequest(detailUrl);
    }

    @Override
    public Request getRankRequest(String rankUrl) {
        return NetUtil.getRequest(rankUrl);
    }

    @Override
    public List<ComicInfo> getComicInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            public void dealInfo(JsoupNode node) {

            }

            @Override
            public ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("h1 a");
                String author = node.ownText("ul li.fed-col-xs6", 1);
                String updateTime = node.ownText("ul li.fed-col-xs6", 2);
                String imgUrl = node.attr("a", "data-original");
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "dl.fed-deta-info");
    }

    @Override
    public void setComicDetail(ComicInfo comicInfo, String html) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {

            @Override
            public void dealInfo(JsoupNode node) {
                String author = node.ownText("div.fed-part-layout li", 1, "a");
                String intro = node.ownText("p.fed-padding.fed-part-both.fed-text-muted");
                String updateStatus = node.ownText("div.fed-part-layout li", 0, "a");
                String updateTime = node.ownText("div.fed-part-layout li", 2, "a");
                comicInfo.setDetail(author, updateTime, updateStatus, intro);
            }

            @Override
            public ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.title("a");
                String chapterUrl = getIndex() + node.href("a");
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        comicInfo.initChapterInfoList(starter.startElements(html, "ul.fed-part-rows li.fed-col-lg3"));
    }

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
        String[] urls = null;
        String chapterImagesStr = StringUtil.match("C_DATA='(.*?)'", html);
        String result = DecryptUtil.decryptAES(DecryptUtil.decryptBase64(chapterImagesStr), "JRUIFMVJDIWE569j");
        if (result != null) {
            String server = "http://image.mljzmm.com/comic/";
            result = StringUtil.match("(\\{.*?\\})", result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String imgPath = jsonObject.getString("imgpath");
            imgPath = DecryptUtil.getUrlEncodeStr(imgPath);
            int total = jsonObject.getInteger("totalimg");
            urls = new String[total];
            for (int i = 0; i < total; i++) {
                urls[i] = server + imgPath + String.format(Locale.CHINA, "%04d.jpg", i + 1);
            }
        }
        return ComicUtil.getImageInfoList(urls, chapterId);
    }

    @Override
    public MyMap<String, String> getRankMap() {
//        MyMap<String, String> map = new MyMap<>();
//        map.put("日漫榜", "HomeHot-{\"type\":3}");//https://manga.bilibili.com/twirp/comic.v1.Comic/HomeHot?device=pc&platform=web {"type":3}
//        map.put("国漫榜", "HomeHot-{\"type\":4}");//{"type":4}
//        map.put("月票榜", "HomeFans-{\"last_week_offset\":0,\"last_month_offset\":0,\"type\":1}");//{"last_week_offset":0,"last_month_offset":0,"type":1}
//        map.put("投喂榜", "HomeFans-{\"last_week_offset\":0,\"last_month_offset\":0,\"type\":0}");//{"last_week_offset":0,"last_month_offset":0,"type":0}
//        map.put("飙升榜", "HomeHot-{\"type\":2}");//{"type":2}
//        map.put("免费榜", "HomeHot-{\"type\":1}");//{"type":1}
//        return map;
        return null;
    }

    @Override
    public List<ComicInfo> getRankComicInfoList(String html) {
//        JsonStarter<ComicInfo> starter = new JsonStarter<ComicInfo>() {
//            @Override
//            public void dealData(JsonNode node) {
//
//            }
//
//            @Override
//            public ComicInfo dealDataList(JsonNode node, int dataId) {
//                String title = node.string("title");
//                String author = node.arrayToString("author");
//                String updateTime = null;
//                String imgUrl = node.string("vertical_cover");
//                String detailUrl = "https://manga.bilibili.com/detail/mc" + node.string("comic_id");
//                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
//            }
//        };
//        List<ComicInfo> list = starter.startDataList(html, "data");
//        if (list.isEmpty()) {
//            list = starter.startDataList(html, "data", "comics");
//        }
//        return list;
        return null;
    }

}
