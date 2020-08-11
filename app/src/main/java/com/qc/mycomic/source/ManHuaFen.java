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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class ManHuaFen implements Source {

    @Override
    public int getSourceId() {
        return Codes.MAN_HUA_FEN;
    }

    @Override
    public String getSourceName() {
        return Codes.MAN_HUA_FEN_STRING;
    }

    @Override
    public String getIndex() {
        return "https://m.manhuafen.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        searchString = "https://m.manhuafen.com/search/?keywords=" + searchString;
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
            public ChapterInfo dealElement(JsoupNode node) {
                String title = node.ownText("span");
                String chapterUrl = node.href("a");
                if (chapterUrl.contains("html")) {
                    if (!chapterUrl.startsWith("http")) {
                        chapterUrl = getIndex() + chapterUrl;
                    }
                    return new ChapterInfo(title, chapterUrl);
                } else {
                    return null;
                }
            }
        };
        starter.startInfo(html);
        comicInfo.initChapterInfoList(starter.startElements(html, "ul#chapter-list-1 li"));
    }

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
        List<ImageInfo> list = new LinkedList<>();
        String server = "https://img01.eshanyao.com/";
        String chapterImagesEncodeStr = StringUtil.match("var chapterImages = \"(.*?)\";", html);
        System.out.println("chapterImagesEncodeStr = " + chapterImagesEncodeStr);
        //var chapterPath = "images/comic/259/517692/";
        String chapterPath = StringUtil.match("var chapterPath = \"(.*?)\";", html);
        String chapterImagesStr = decrypt(chapterImagesEncodeStr);
        if (chapterImagesStr != null) {
            chapterImagesStr = chapterImagesStr.replaceAll("\\\\", "");
            String[] urls = StringUtil.matchArray("\"(.*?)\"", chapterImagesStr);
            int i = 0;
//            int length = -1;
            for (String url : urls) {
//                if (length == -1) {
//                    length = url.length();
//                } else if (Math.abs(length - url.length()) > 4) {
//                    continue;
//                }
                if (!url.startsWith("http")) {
                    url = server + chapterPath + url;
                } else {
                    url = "https://img01.eshanyao.com/showImage.php?url=" + url;
                }
                ImageInfo imageInfo = new ImageInfo(chapterId, i++, urls.length, url);
                list.add(imageInfo);
            }
        }
        return list;
    }

    @Override
    public MyMap<String, String> getRankMap() {
        MyMap<String, String> map = new MyMap<>();
        map.put("人气排行", "https://m.manhuafen.com/rank/popularity/");
        map.put("点击排行", "https://m.manhuafen.com/rank/click/");
        map.put("订阅排行", "https://m.manhuafen.com/rank/subscribe/");
        map.put("刚刚更新", "https://m.manhuafen.com/update/");
        map.put("热血", "https://m.manhuafen.com/list/rexue/");
        map.put("玄幻", "https://m.manhuafen.com/list/xuanhuan/");
        map.put("修真", "https://m.manhuafen.com/list/xiuzhen/");
        map.put("古风", "https://m.manhuafen.com/list/gufeng/");
        map.put("恋爱", "https://m.manhuafen.com/list/lianai/");
        map.put("穿越", "https://m.manhuafen.com/list/chuanyue/");
        map.put("都市", "https://m.manhuafen.com/list/dushi/");
        map.put("霸总", "https://m.manhuafen.com/list/bazong/");
        map.put("悬疑", "https://m.manhuafen.com/list/xuanyi/");
        map.put("搞笑", "https://m.manhuafen.com/list/gaoxiao/");
        map.put("奇幻", "https://m.manhuafen.com/list/qihuan/");
        map.put("总裁", "https://m.manhuafen.com/list/zongcai/");
        map.put("日常", "https://m.manhuafen.com/list/richang/");
        map.put("冒险", "https://m.manhuafen.com/list/maoxian/");
        map.put("科幻", "https://m.manhuafen.com/list/kehuan/");
        map.put("纯爱", "https://m.manhuafen.com/list/chunai/");
        map.put("魔幻", "https://m.manhuafen.com/list/mohuan/");
        map.put("战争", "https://m.manhuafen.com/list/zhanzheng/");
        map.put("蔷薇", "https://m.manhuafen.com/list/qiangwei/");
        map.put("武侠", "https://m.manhuafen.com/list/wuxia/");
        map.put("生活", "https://m.manhuafen.com/list/shenghuo/");
        map.put("动作", "https://m.manhuafen.com/list/dongzuo/");
        map.put("后宫", "https://m.manhuafen.com/list/hougong/");
        map.put("游戏", "https://m.manhuafen.com/list/youxi/");
        map.put("恐怖", "https://m.manhuafen.com/list/kongbu/");
        map.put("漫改", "https://m.manhuafen.com/list/mangai/");
        map.put("真人", "https://m.manhuafen.com/list/zhenren/");
        map.put("校园", "https://m.manhuafen.com/list/xiaoyuan/");
        map.put("剧情", "https://m.manhuafen.com/list/juqing/");
        map.put("灵异", "https://m.manhuafen.com/list/lingyi/");
        map.put("少年", "https://m.manhuafen.com/list/shaonian/");
        map.put("推理", "https://m.manhuafen.com/list/tuili/");
        map.put("怀旧", "https://m.manhuafen.com/list/huaijiu/");
        map.put("情感", "https://m.manhuafen.com/list/qinggan/");
        map.put("偶像", "https://m.manhuafen.com/list/ouxiang/");
        map.put("少女", "https://m.manhuafen.com/list/shaonv/");
        map.put("独家", "https://m.manhuafen.com/list/dujia/");
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
                public ComicInfo dealElement(JsoupNode node) {
                    String title = node.ownText("a.txtA");
                    String author = node.ownText("span.info a");
                    String updateTime = null;
                    String imgUrl = node.src("img");
                    String detailUrl = node.href("a.txtA");
                    if (author != null) {
                        author = "更新至：" + author;
                    }
                    return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
                }
            };
            return starter.startElements(html, "ul#comic-items li");
        }
    }

    private String decrypt(String code) {
        String key = "1739ZAQ54321bbG1";
        String iv = "ABCDEF1G344321bb";
        return DecryptUtil.decryptAES(code, key, iv);
    }

}
