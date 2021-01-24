package com.qc.mycomic.source;

import com.qc.mycomic.en.SourceEnum;
import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.jsoup.JsoupStarter;
import com.qc.mycomic.model.ChapterInfo;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.util.ComicUtil;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.StringUtil;

import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/24 19:48
 * @ver 1.0
 */
public class BL extends BaseSource {
    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.BL;
    }

    @Override
    public String getIndex() {
        return "https://www.kanbl.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = "https://www.kanbl.com/search?keyword=" + searchString;
        return NetUtil.getRequest(url);
    }

    @Override
    public Request buildRequest(String requestUrl, String html, String tag) {
        if (IMAGE.equals(tag) && requestUrl != null && !requestUrl.contains("?page=")) {
            JsoupNode node = new JsoupNode(html);
            node.init(node.getElements("select.selectpage option").last());
            String value = node.attr("option", "value");
            return NetUtil.getRequest(requestUrl + "?page=" + value);
        }
        return super.buildRequest(requestUrl, html, tag);
    }

    @Override
    public List<ComicInfo> getComicInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.title("h3.title a");
                String author = null;
                String updateTime = node.ownText("span.chapter");
                String imgUrl = node.attr("img", "data-src");
                String detailUrl = getIndex() + node.href("h3.title a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div#comicListBox li");
    }

    @Override
    public void setComicDetail(ComicInfo comicInfo, String html) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected boolean isDESC() {
                return false;
            }

            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("h1.title");
                String imgUrl = node.attr("div.comic-cover img", "data-src");
                String author = node.ownText("span.update-time");
                String intro = node.ownText("div.desc-con");
                String updateStatus = null;
                String updateTime = node.ownText("p.update");
                try {
                    author = author.substring(author.indexOf('：') + 1).trim();
                    updateTime = updateTime.substring(updateTime.indexOf(':') + 1).trim();
                } catch (Exception ignored) {
                }
                comicInfo.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a");
                String chapterUrl = getIndex() + node.href("a");
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        comicInfo.initChapterInfoList(starter.startElements(html, "ul#chapterList li"));
    }

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
        JsoupNode node = new JsoupNode(html);
        node.init(node.getElements("div.comiclist img").last());
        String url = node.src("img");
        int size = 0;
        try {
            size = Integer.parseInt(StringUtil.match("/(\\d+)\\.", url));
        } catch (NumberFormatException ignored) {
        }
        String baseUrl = url.replaceAll("/\\d+\\.", "/%04d\\.");
        String[] urls = new String[size];
        for (int i = 0; i < size; i++) {
            urls[i] = String.format(baseUrl, i + 1);
        }
        return ComicUtil.getImageInfoList(urls, chapterId);
    }

    @Override
    public Map<String, String> getRankMap() {
        return null;
    }

    @Override
    public List<ComicInfo> getRankComicInfoList(String html) {
        return null;
    }
}
