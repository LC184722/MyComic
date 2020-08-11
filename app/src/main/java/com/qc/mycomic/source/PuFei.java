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
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.StringUtil;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class PuFei implements Source {

    @Override
    public int getSourceId() {
        return Codes.PU_FEI;
    }

    @Override
    public String getSourceName() {
        return Codes.PU_FEI_STRING;
    }

    @Override
    public String getIndex() {
        return "http://m.pufei8.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        searchString = "http://m.pufei8.com/e/search/?searchget=1&tbname=mh&show=title,player,playadmin,bieming,pinyin,playadmin&tempid=4&keyboard=" + StringUtil.getGBKDecodedStr(searchString);
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
                String title = node.ownText("h3");
                String author = node.ownText("dl dd");
                String updateTime = node.text("dl", 3, "dd");
                String imgUrl = node.attr("div.thumb img", "data-src");
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "ul#detail li");
    }

    @Override
    public void setComicDetail(ComicInfo comicInfo, String html) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            public void dealInfo(JsoupNode node) {
                String author = node.ownText("div.book-detail dl:eq(3) dd");
                String intro = node.ownText("div#bookIntro p");
                String updateStatus = node.ownText("div.thumb i");
                String updateTime = node.ownText("div.book-detail dl:eq(2) dd");
                comicInfo.setDetail(author, updateTime, updateStatus, intro);
            }

            @Override
            public ChapterInfo dealElement(JsoupNode node) {
                String title = node.title("a");
                String chapterUrl = getIndex() + node.href("a");
                return new ChapterInfo(title, chapterUrl);
            }
        };
        starter.startInfo(html);
        comicInfo.initChapterInfoList(starter.startElements(html, "div#chapterList2 li"));
    }

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
        List<ImageInfo> list = new LinkedList<>();
        String encodeStr = StringUtil.match("cp=\"(.*?)\"", html);
        if (encodeStr != null) {
            String[] urls = decodeStr(encodeStr);
            String prevUrl = "http://res.img.youzipi.net/";
            int i = 0;
            for (String url : urls) {
                ImageInfo imageInfo = new ImageInfo(chapterId, i++, urls.length, prevUrl + url);
                list.add(imageInfo);
            }
        }
        return list;
    }

    @Override
    public MyMap<String, String> getRankMap() {
        MyMap<String, String> map = new MyMap<>();
        map.put("风云榜", "http://m.pufei8.com/manhua/paihang.html");
        map.put("刚刚更新", "http://m.pufei8.com/manhua/update.html");
        map.put("少年热血", "http://m.pufei8.com/shaonianrexue/");
        map.put("少女爱情", "http://m.pufei8.com/shaonvaiqing/");
        map.put("武侠格斗", "http://m.pufei8.com/wuxiagedou/");
        map.put("科幻魔幻", "http://m.pufei8.com/wuxiagedou/");
        map.put("竞技体育", "http://m.pufei8.com/jingjitiyu/");
        map.put("搞笑喜剧", "http://m.pufei8.com/gaoxiaoxiju/");
        map.put("耽美BL", "http://m.pufei8.com/danmeirensheng/");
        map.put("侦探推理", "http://m.pufei8.com/zhentantuili/");
        map.put("恐怖灵异", "http://m.pufei8.com/kongbulingyi/");
        return map;
    }

    @Override
    public List<ComicInfo> getRankComicInfoList(String html) {
        return getComicInfoList(html);
    }

    private String[] decodeStr(String encodeStr) {
//        String decodeStr = StringUtil.base64Decode(encodeStr);
//        try {
//            Context ctx = Context.enter();
//            ctx.setOptimizationLevel(-1);
//            Scriptable scope = ctx.initStandardObjects();
//            Object object = ctx.evaluateString(scope, decodeStr, null, 0, null);
//            String result = Context.toString(object);
//            return result.split(",");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String code = DecryptUtil.decryptBase64(encodeStr);
        String result = DecryptUtil.exeJsCode(code);
        if (result != null) {
            return result.split(",");
        } else {
            return new String[0];
        }
    }

}