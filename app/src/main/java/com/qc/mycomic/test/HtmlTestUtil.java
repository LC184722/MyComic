package com.qc.mycomic.test;

import android.widget.ListView;

import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.jsoup.JsoupStarter;
import com.qc.mycomic.model.ChapterInfo;
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
import com.qc.mycomic.util.StringUtil;

import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlTestUtil implements Source {

    @Override
    public int getSourceId() {
        return Codes.TENG_XUN;
    }

    @Override
    public String getSourceName() {
        return Codes.TENG_XUN_STRING;
    }

    @Override
    public String getIndex() {
        return "https://ac.qq.com";
    }

    @Override
    public String getSearchUrl(String searchString) {
        return "https://ac.qq.com/Comic/searchList?search=" + searchString;
    }

    @Override
    public List<ComicInfo> getComicInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            public void dealInfo(JsoupNode node) {

            }

            @Override
            public ComicInfo dealElement(JsoupNode node) {
                String title = node.title("a");
                String author = null;
                String updateTime = null;
                String imgUrl = node.attr("img", "data-original");
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "ul.mod_book_list li");
    }

    @Override
    public void setComicDetail(ComicInfo comicInfo, String html) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {

            @Override
            public boolean isDESC() {
                return false;
            }

            @Override
            public void dealInfo(JsoupNode node) {
                String author = node.ownText("div.works-intro span.first em");
                String intro = node.ownText("div.works-intro p.works-intro-short");
                String updateStatus = node.ownText("div.works-intro label.works-intro-status");
                String updateTime = node.ownText("span.ui-pl10");
                comicInfo.setDetail(author, updateTime, updateStatus, intro);
            }

            @Override
            public ChapterInfo dealElement(JsoupNode node) {
                String title = node.ownText("a");
                String chapterUrl = getIndex() + node.href("a");
                return new ChapterInfo(title, chapterUrl);
            }
        };
        starter.startInfo(html);
        comicInfo.initChapterInfoList(starter.startElements(html, "ol.chapter-page-all span"));
    }

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
        String raw = StringUtil.match("DATA.*=.*'(.*?)',", html);
        String nonce = StringUtil.match("window\\[.*?\\] *=(.*?);", html);
        if (nonce != null) {
            String[] docs = nonce.split("\\(\\)");
            for (String doc : docs) {
                String tmp = StringUtil.match("(\\(.*document.*\\)\\.toString)", doc);
                if (tmp == null) {
                    tmp = StringUtil.match("(\\(.*window.*\\)\\.toString)", doc);
                }
                if (tmp != null) {
                    nonce = nonce.replace(tmp + "()", "0");
                }
            }
            nonce = DecryptUtil.exeJsCode(nonce);
        }
        String data = DecryptUtil.exeJsFunction(getJsCode(), "decode", raw, nonce);
        System.out.println("data = " + data);
        String[] urls = null;
        if (data != null) {
            data = DecryptUtil.decryptBase64(data);
            data = data.replaceAll("\\\\", "");
            urls = StringUtil.matchArray("pid(.*?)\"url\":\"(.*?)\"", data, 2);
        }
        return ImageInfoUtil.getImageInfoList(urls, chapterId);
    }

    private String getJsCode() {
        return "function decode(T, N) {\n" +
                "\tvar len, locate, str;\n" +
                "\tT = T.split('');\n" +
                "\tN = N.match(/\\d+[a-zA-Z]+/g);\n" +
                "\tlen = N.length;\n" +
                "\twhile (len--) {\n" +
                "\t\tlocate = parseInt(N[len]) & 255;\n" +
                "\t\tstr = N[len].replace(/\\d+/g, '');\n" +
                "\t\tT.splice(locate, str.length)\n" +
                "\t}\n" +
                "\tT = T.join('');\n" +
                "\treturn T;\n" +
                "}";
    }

    @Override
    public MyMap<String, String> getRankMap() {
        MyMap<String, String> map = new MyMap<>();
        return map;
    }

    @Override
    public List<ComicInfo> getRankComicInfoList(String html) {
        List<ComicInfo> list = new LinkedList<>();
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            public void dealInfo(JsoupNode node) {

            }

            @Override
            public ComicInfo dealElement(JsoupNode node) {
                String title = node.ownText("h3.ret-works-title a");
                String author = node.title("p.ret-works-author");
                String updateTime = null;
                String imgUrl = node.attr("img", "data-original");
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "ul.clearfix li");
    }

    public static void main(String[] args) {
        HtmlTestUtil testUtil = new HtmlTestUtil();
//        testUtil.testSearch();
//        testUtil.testDetail();
        testUtil.testImage();
//        testUtil.testRank();
//        testUtil.test();
    }

    public void test() {
        String key = "313733395a4151353433323162624731";
        String iv = "41424344454631473334343332316262";
        System.out.println("key = " + key.length());
        System.out.println("iv = " + iv.length());
    }

    private void testSearch() {
//        String html = HtmlUtil.getSearchHtml();
        String html = HtmlUtil.getHtmlByFile();
        List<ComicInfo> comicInfoList;
        comicInfoList = getComicInfoList(html);
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