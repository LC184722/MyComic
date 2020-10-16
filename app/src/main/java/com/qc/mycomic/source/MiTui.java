package com.qc.mycomic.source;

import android.util.Log;

import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.jsoup.JsoupStarter;
import com.qc.mycomic.model.ChapterInfo;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.model.MyMap;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.StringUtil;

import java.util.LinkedList;
import java.util.List;

import okhttp3.Request;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class MiTui extends BaseSource {

    @Override
    public int getSourceId() {
        return Codes.MI_TUI;
    }

    @Override
    public String getSourceName() {
        return Codes.MI_TUI_STRING;
    }

    @Override
    public String getIndex() {
        return "https://www.imitui.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        searchString = "https://m.imitui.com/search/?keywords=" + searchString;
        return NetUtil.getRequest(searchString);
    }

    @Override
    public List<ComicInfo> getComicInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            public void dealInfo(JsoupNode node) {

            }

            @Override
            public ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a.title");
                String author = node.ownText("p.txtItme");
                String updateTime = node.ownText("span.date");
                String imgUrl = node.src("img");
                String detailUrl = node.href("a.title");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.itemBox");
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
                String author = node.ownText("p.txtItme");
                String intro = node.ownText("p#full-des", "p#simple-des");
                String updateStatus = node.ownText("p.txtItme:eq(2) :eq(3)");
                String updateTime = node.ownText("p.txtItme span.date");
                try {
                    intro = intro.substring(intro.indexOf(':') + 1);
                } catch (Exception ignored) {
                }
                comicInfo.setDetail(author, updateTime, updateStatus, intro);
            }

            @Override
            public ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("span");
                String chapterUrl = node.href("a");
                if (chapterUrl.contains("html")) {
                    if (!chapterUrl.startsWith("http")) {
                        chapterUrl = getIndex() + chapterUrl;
                    } else {
                        chapterUrl = chapterUrl.replace("//m.", "//www.");
                    }
                    return new ChapterInfo(elementId, title, chapterUrl);
                } else {
                    return null;
                }
            }
        };
        starter.startInfo(html);
        comicInfo.initChapterInfoList(starter.startElements(html, "ul#chapter-list-1 li"));
    }

    private static String[] servers = {
            "https://res0818.imitui.com",
            "https://imgimtmaa.1a3.net",
            "https://imgimtmi.1a3.net",
            "https://res.imitui.com",
    };

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
        List<ImageInfo> list = new LinkedList<>();
        String chapterImagesStr = StringUtil.match("chapterImages = \\[(.*?)\\]", html);
        String chapterPath = StringUtil.match("var chapterPath = \"(.*?)\";", html);
        if (chapterImagesStr != null) {
            String[] urls = chapterImagesStr.split(",");
            String server = Codes.miTuiServer != null ? Codes.miTuiServer : servers[0];
            int i = 0;
            for (String url : urls) {
                url = url.replace("\"", "").replace("\\", "");
                if (!url.startsWith("/")) {
                    url = "/" + chapterPath + url;
                }
                ImageInfo imageInfo = new ImageInfo(chapterId, i++, urls.length, server + url);
                list.add(imageInfo);
            }
        }
        return list;
    }

    @Override
    public MyMap<String, String> getRankMap() {
        MyMap<String, String> map = new MyMap<>();
        map.put("人气排行", "https://m.imitui.com/rank/popularity/?page=1");
        map.put("点击排行", "https://m.imitui.com/rank/click/?page=1");
        map.put("订阅排行", "https://m.imitui.com/rank/subscribe/?page=1");
        map.put("刚刚更新", "https://m.imitui.com/update/?page=1");
        map.put("热血", "https://m.imitui.com/list/rexue/click/?page=1");
        map.put("玄幻", "https://m.imitui.com/list/xuanhuan/click/?page=1");
        map.put("修真", "https://m.imitui.com/list/xiuzhen/click/?page=1");
        map.put("古风", "https://m.imitui.com/list/gufeng/click/?page=1");
        map.put("恋爱", "https://m.imitui.com/list/lianai/click/?page=1");
        map.put("穿越", "https://m.imitui.com/list/chuanyue/click/?page=1");
        map.put("都市", "https://m.imitui.com/list/dushi/click/?page=1");
        map.put("霸总", "https://m.imitui.com/list/bazong/click/?page=1");
        map.put("悬疑", "https://m.imitui.com/list/xuanyi/click/?page=1");
        map.put("搞笑", "https://m.imitui.com/list/gaoxiao/click/?page=1");
        map.put("奇幻", "https://m.imitui.com/list/qihuan/click/?page=1");
        map.put("总裁", "https://m.imitui.com/list/zongcai/click/?page=1");
        map.put("日常", "https://m.imitui.com/list/richang/click/?page=1");
        map.put("冒险", "https://m.imitui.com/list/maoxian/click/?page=1");
        map.put("科幻", "https://m.imitui.com/list/kehuan/click/?page=1");
        map.put("纯爱", "https://m.imitui.com/list/chunai/click/?page=1");
        map.put("魔幻", "https://m.imitui.com/list/mohuan/click/?page=1");
        map.put("战争", "https://m.imitui.com/list/zhanzheng/click/?page=1");
        map.put("蔷薇", "https://m.imitui.com/list/qiangwei/click/?page=1");
        map.put("武侠", "https://m.imitui.com/list/wuxia/click/?page=1");
        map.put("生活", "https://m.imitui.com/list/shenghuo/click/?page=1");
        map.put("动作", "https://m.imitui.com/list/dongzuo/click/?page=1");
        map.put("后宫", "https://m.imitui.com/list/hougong/click/?page=1");
        map.put("游戏", "https://m.imitui.com/list/youxi/click/?page=1");
        map.put("恐怖", "https://m.imitui.com/list/kongbu/click/?page=1");
        map.put("漫改", "https://m.imitui.com/list/mangai/click/?page=1");
        map.put("真人", "https://m.imitui.com/list/zhenren/click/?page=1");
        map.put("校园", "https://m.imitui.com/list/xiaoyuan/click/?page=1");
        map.put("剧情", "https://m.imitui.com/list/juqing/click/?page=1");
        map.put("灵异", "https://m.imitui.com/list/lingyi/click/?page=1");
        map.put("少年", "https://m.imitui.com/list/shaonian/click/?page=1");
        map.put("推理", "https://m.imitui.com/list/tuili/click/?page=1");
        map.put("怀旧", "https://m.imitui.com/list/huaijiu/click/?page=1");
        map.put("情感", "https://m.imitui.com/list/qinggan/click/?page=1");
        map.put("偶像", "https://m.imitui.com/list/ouxiang/click/?page=1");
        map.put("少女", "https://m.imitui.com/list/shaonv/click/?page=1");
        map.put("独家", "https://m.imitui.com/list/dujia/click/?page=1");
        return map;
    }

    @Override
    public List<ComicInfo> getRankComicInfoList(String html) {
        List<ComicInfo> list = getComicInfoList(html);
        if (list.size() > 0) {
            return list;
        } else {
            JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
                @Override
                public void dealInfo(JsoupNode node) {

                }

                @Override
                public ComicInfo dealElement(JsoupNode node, int elementId) {
                    String title = node.ownText("a.txtA");
                    String author = node.ownText("span.info");
                    String updateTime = null;
                    String imgUrl = node.src("img");
                    String detailUrl = node.href("a.txtA");
                    return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
                }
            };
            return starter.startElements(html, "ul#comic-items li");
        }
    }

}