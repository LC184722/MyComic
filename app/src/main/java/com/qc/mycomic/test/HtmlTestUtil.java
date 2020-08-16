package com.qc.mycomic.test;

import com.alibaba.fastjson.JSONObject;
import com.qc.mycomic.json.JsonNode;
import com.qc.mycomic.json.JsonStarter;
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

import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class HtmlTestUtil implements Source {

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

    public static void main(String[] args) {
        HtmlTestUtil testUtil = new HtmlTestUtil();
//        testUtil.testSearch();
//        testUtil.testDetail();
        testUtil.testImage();
//        testUtil.testRank();
//        testUtil.test();
//        testUtil.testRequest();
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
                HtmlUtil.writeFile(html);
            }
        };
//        String url = "https://manga.bilibili.com/twirp/comic.v1.Comic/GetImageIndex?device=pc&platform=web";
//        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        builder.addFormDataPart("ep_id", "307985");
//        Request request = new Request.Builder().addHeader("User-Agent", Codes.USER_AGENT_WEB).url(url).post(builder.build()).build();
//        Request request = getSearchRequest("放开那个女巫");
        Request request = getDetailRequest("https://www.ohmanhua.com/12394/1/180.html");
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