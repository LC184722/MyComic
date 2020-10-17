package com.qc.mycomic.source;

import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.jsoup.JsoupStarter;
import com.qc.mycomic.model.ChapterInfo;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.model.MyMap;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.ComicUtil;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.StringUtil;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/10/17 14:31
 * @ver 1.0
 */
public class ManHuaTai extends BaseSource {

    @Override
    public int getSourceId() {
        return Codes.MAN_HUA_TAI;
    }

    @Override
    public String getSourceName() {
        return Codes.MAN_HUA_TAI_STRING;
    }

    @Override
    public String getIndex() {
        return "https://m.manhuatai.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = "https://m.manhuatai.com/sort/all.html?cache=false&search_key=";
        return NetUtil.getRequest(url + searchString);
    }

    @Override
    public List<ComicInfo> getComicInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            public ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("p.title");
                String author = null;
                String updateTime = null;
                String imgUrl = "https:" + node.attr("img", "data-src");
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "li.comic-item");
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
                String author = node.attr("div.thumbnail img", "alt");
                String intro = node.ownText("p#js_desc_content");
                String updateStatus;
                String updateTime;
                try {
                    String text = node.ownText("p.comic-update-status");
                    String[] texts = text.split(" ");
                    updateStatus = texts[0];
                    updateTime = texts[1];
                } catch (Exception e) {
                    updateStatus = null;
                    updateTime = null;
                }
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
        comicInfo.initChapterInfoList(starter.startElements(html, "ul#js_chapter_list li"));
    }

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
        String[] urls;
        try {
            String currentChapter = StringUtil.match("current_chapter:\\{(.*?)\\}", html);
            String endNumStr = StringUtil.match("end_num:(\\d+)", currentChapter);
            int endNum = Integer.parseInt(endNumStr);
            String url = StringUtil.match("rule:\"(.*?)\"", currentChapter);
            String chapterDomain = StringUtil.match("chapter_domain:\"(.*?)\"", currentChapter);
            String server = "https://mhpic." + chapterDomain;
            String suffix = "-mht.middle.webp";
            urls = new String[endNum];
            for (int i = 0; i < urls.length; i++) {
                String mid_url = url.replace("$$", String.valueOf(i + 1));
                urls[i] = server + mid_url + suffix;
            }
        } catch (Exception e) {
            urls = null;
            e.printStackTrace();
        }
        return ComicUtil.getImageInfoList(urls, chapterId);
    }

    @Override
    public MyMap<String, String> getRankMap() {
        MyMap<String, String> map = new MyMap<>();
        map.put("综合榜", "https://m.manhuatai.com/top/all.html");
        map.put("自制榜", "https://m.manhuatai.com/top/self.html");
        map.put("少年榜", "https://m.manhuatai.com/top/boy.html");
        map.put("少女榜", "https://m.manhuatai.com/top/girl.html");
        map.put("新作榜", "https://m.manhuatai.com/top/new.html");
        map.put("黑马榜", "https://m.manhuatai.com/top/dark.html");
        map.put("免费榜", "https://m.manhuatai.com/top/free.html");
        map.put("付费榜", "https://m.manhuatai.com/top/charge.html");
        map.put("完结榜", "https://m.manhuatai.com/top/finish.html");
        map.put("连载榜", "https://m.manhuatai.com/top/serialize.html");
        return map;
    }

    @Override
    public List<ComicInfo> getRankComicInfoList(String html) {
        List<ComicInfo> list = new ArrayList<>();
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            public void dealInfo(JsoupNode node) {
                node.addElement("div.comic-item.top-1");
                node.addElement("div.comic-item.top-2");
                node.addElement("div.comic-item.top-3");
                for (Element element : node.getElements()) {
                    node.init(element);
                    String title = node.ownText("div.comic-detail a");
                    String author = null;
                    String updateTime = null;
                    String imgUrl = "https:" + node.attr("div.comic-images img", "data-src");
                    String detailUrl = getIndex() + node.href("div.comic-images a");
                    ComicInfo comicInfo = new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
                    list.add(comicInfo);
                }
            }

            @Override
            public ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("div.comic-detail a");
                String author = node.ownText("div.comic-author");
                String updateTime = null;
                String imgUrl = null;
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        starter.startInfo(html);
        list.addAll(starter.startElements(html, "ul.comic-list li.list"));
        return list;
//        if (!list.isEmpty()) {
//            return list;
//        } else {
//            return new JsoupStarter<ComicInfo>() {
//                @Override
//                public ComicInfo dealElement(JsoupNode node, int elementId) {
//                    String title = node.title("h3.ret-works-title a");
//                    String author = node.title("p.ret-works-author");
//                    String updateTime = null;
//                    String imgUrl = node.attr("img", "data-original");
//                    String detailUrl = getIndex() + node.href("a");
//                    return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
//                }
//            }.startElements(html, "ul.clearfix li");
//        }
    }

}
