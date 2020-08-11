package com.qc.mycomic.source;

import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.jsoup.JsoupStarter;
import com.qc.mycomic.model.ChapterInfo;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.model.MyMap;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.DecryptUtil;
import com.qc.mycomic.util.ImageInfoUtil;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.StringUtil;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Request;

public class TengXun implements Source {

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
    public Request getSearchRequest(String searchString) {
        searchString = "https://ac.qq.com/Comic/searchList?search=" + searchString;
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
        String[] urls = null;
        if (data != null) {
            data = DecryptUtil.decryptBase64(data);
            if (data != null) {
                data = data.replaceAll("\\\\", "");
                urls = StringUtil.matchArray("pid(.*?)\"url\":\"(.*?)\"", data, 2);
            }
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
        long time = new Date().getTime();
        map.put("飙升榜", "https://m.ac.qq.com/rank/index?t=" + time + "&type=rise&page=1&pageSize=10&style=items");
        map.put("畅销榜", "https://m.ac.qq.com/rank/index?t=" + time + "&type=pay&page=1&pageSize=10&style=items");
        map.put("新作榜", "https://m.ac.qq.com/rank/index?t=" + time + "&type=new&page=1&pageSize=10&style=items");
        map.put("真香榜", "https://m.ac.qq.com/rank/index?t=" + time + "&type=hot&page=1&pageSize=10&style=items");
        map.put("免费", "https://ac.qq.com/Comic/all/search/hot/vip/1/page/1");
        map.put("付费", "https://ac.qq.com/Comic/all/search/hot/vip/2/page/1");
        map.put("条漫", "https://m.ac.qq.com/category/listAll/type/tm/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("独家", "https://m.ac.qq.com/category/listAll/type/dj/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("完结", "https://m.ac.qq.com/category/listAll/type/wj/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("日漫", "https://m.ac.qq.com/category/listAll/type/rm/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("恋爱", "https://m.ac.qq.com/category/listAll/type/na/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("玄幻", "https://m.ac.qq.com/category/listAll/type/xh/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("热血", "https://m.ac.qq.com/category/listAll/type/rx/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("悬疑", "https://m.ac.qq.com/category/listAll/type/xy/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("少女", "https://m.ac.qq.com/category/listAll/type/sv/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("韩漫", "https://m.ac.qq.com/category/listAll/type/hm/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("科幻", "https://m.ac.qq.com/category/listAll/type/kh/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("逗比", "https://m.ac.qq.com/category/listAll/type/db/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("校园", "https://m.ac.qq.com/category/listAll/type/qcxy/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("都市", "https://m.ac.qq.com/category/listAll/type/ds/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("治愈", "https://m.ac.qq.com/category/listAll/type/zy/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("恐怖", "https://m.ac.qq.com/category/listAll/type/kb/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("妖怪", "https://m.ac.qq.com/category/listAll/type/yg/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        return map;
    }

    @Override
    public List<ComicInfo> getRankComicInfoList(String html) {
        List<ComicInfo> list = new LinkedList<>();
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            public void dealInfo(JsoupNode node) {
                node.addElement("div.top3-box-item1");
                node.addElement("div.top3-box-item2");
                node.addElement("div.top3-box-item3");
                for (Element element : node.getElements()) {
                    node.init(element);
                    String title = node.ownText("strong.comic-title");
                    String author = node.ownText("small.comic-update");
                    String updateTime = null;
                    String imgUrl = node.src("img");
                    String detailUrl = getIndex() + node.href("a");
                    ComicInfo comicInfo = new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
                    list.add(comicInfo);
                }
            }

            @Override
            public ComicInfo dealElement(JsoupNode node) {
                String title = node.ownText("strong.comic-title");
                String author = node.ownText("small.comic-update");
                String updateTime = null;
                String imgUrl = node.src("img");
                String detailUrl = getIndex() + node.href("a");
                try {
                    if (detailUrl.contains("comic/index")) {
                        detailUrl = detailUrl.replace("comic/index", "Comic/comicInfo");
                    }
                } catch (Exception ignored) {
                }
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        starter.startInfo(html);
        list.addAll(starter.startElements(html, "li.comic-item"));
        if (!list.isEmpty()) {
            return list;
        } else {
            return new JsoupStarter<ComicInfo>() {
                @Override
                public void dealInfo(JsoupNode node) {

                }

                @Override
                public ComicInfo dealElement(JsoupNode node) {
                    String title = node.title("h3.ret-works-title a");
                    String author = node.title("p.ret-works-author");
                    String updateTime = null;
                    String imgUrl = node.attr("img", "data-original");
                    String detailUrl = getIndex() + node.href("a");
                    return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
                }
            }.startElements(html, "ul.clearfix li");
        }
    }
}
