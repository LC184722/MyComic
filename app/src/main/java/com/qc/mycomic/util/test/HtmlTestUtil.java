package com.qc.mycomic.util.test;

import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.jsoup.JsoupStarter;
import com.qc.mycomic.model.ChapterInfo;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.model.MyMap;
import com.qc.mycomic.source.BaseSource;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.DecryptUtil;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class HtmlTestUtil extends BaseSource {

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
    public List<ComicInfo> getComicInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
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

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
        List<ImageInfo> list = new ArrayList<>();
        String server = "https://img01.eshanyao.com/";
        String chapterImagesEncodeStr = StringUtil.match("var chapterImages = \"(.*?)\";", html);
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
                if (url.startsWith("http:")) {
                    url = "https://dl.manhuachi.com/acqq.php?url=" + url;
                } else if (!url.startsWith("https:")) {
                    url = server + chapterPath + url;
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
        map.put("人气排行", "https://m.manhuafen.com/rank/popularity/?page=1");
        map.put("点击排行", "https://m.manhuafen.com/rank/click/?page=1");
        map.put("订阅排行", "https://m.manhuafen.com/rank/subscribe/?page=1");
        map.put("刚刚更新", "https://m.manhuafen.com/update/?page=1");
        map.put("热血", "https://m.manhuafen.com/list/rexue/click/?page=1");
        map.put("玄幻", "https://m.manhuafen.com/list/maoxian/click/page=1");
        map.put("修真", "https://m.manhuafen.com/list/xiuzhen/click/?page=1");
        map.put("古风", "https://m.manhuafen.com/list/gufeng/click/?page=1");
        map.put("恋爱", "https://m.manhuafen.com/list/lianai/click/?page=1");
        map.put("穿越", "https://m.manhuafen.com/list/chuanyue/click/?page=1");
        map.put("都市", "https://m.manhuafen.com/list/dushi/click/?page=1");
        map.put("霸总", "https://m.manhuafen.com/list/bazong/click/?page=1");
        map.put("悬疑", "https://m.manhuafen.com/list/xuanyi/click/?page=1");
        map.put("搞笑", "https://m.manhuafen.com/list/gaoxiao/click/?page=1");
        map.put("奇幻", "https://m.manhuafen.com/list/qihuan/click/?page=1");
        map.put("总裁", "https://m.manhuafen.com/list/zongcai/click/?page=1");
        map.put("日常", "https://m.manhuafen.com/list/richang/click/?page=1");
        map.put("冒险", "https://m.manhuafen.com/list/maoxian/click/?page=1");
        map.put("科幻", "https://m.manhuafen.com/list/kehuan/click/?page=1");
        map.put("纯爱", "https://m.manhuafen.com/list/chunai/click/?page=1");
        map.put("魔幻", "https://m.manhuafen.com/list/mohuan/click/?page=1");
        map.put("战争", "https://m.manhuafen.com/list/zhanzheng/click/?page=1");
        map.put("蔷薇", "https://m.manhuafen.com/list/qiangwei/click/?page=1");
        map.put("武侠", "https://m.manhuafen.com/list/wuxia/click/?page=1");
        map.put("生活", "https://m.manhuafen.com/list/shenghuo/click/?page=1");
        map.put("动作", "https://m.manhuafen.com/list/dongzuo/click/?page=1");
        map.put("后宫", "https://m.manhuafen.com/list/hougong/click/?page=1");
        map.put("游戏", "https://m.manhuafen.com/list/youxi/click/?page=1");
        map.put("恐怖", "https://m.manhuafen.com/list/kongbu/click/?page=1");
        map.put("漫改", "https://m.manhuafen.com/list/mangai/click/?page=1");
        map.put("真人", "https://m.manhuafen.com/list/zhenren/click/?page=1");
        map.put("校园", "https://m.manhuafen.com/list/xiaoyuan/click/?page=1");
        map.put("剧情", "https://m.manhuafen.com/list/juqing/click/?page=1");
        map.put("灵异", "https://m.manhuafen.com/list/lingyi/click/?page=1");
        map.put("少年", "https://m.manhuafen.com/list/shaonian/click/?page=1");
        map.put("推理", "https://m.manhuafen.com/list/tuili/click/?page=1");
        map.put("怀旧", "https://m.manhuafen.com/list/huaijiu/click/?page=1");
        map.put("情感", "https://m.manhuafen.com/list/qinggan/click/?page=1");
        map.put("偶像", "https://m.manhuafen.com/list/ouxiang/click/?page=1");
        map.put("少女", "https://m.manhuafen.com/list/shaonv/click/?page=1");
        map.put("独家", "https://m.manhuafen.com/list/dujia/click/?page=1");
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
                public ComicInfo dealElement(JsoupNode node, int elementId) {
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
        String key = "KA58ZAQ321oobbG1";
        String iv = "A1B2C3DEF1G321oo";
        return DecryptUtil.decryptAES(code, key, iv);
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
        Request request = getDetailRequest("https://www.ohmanhua.com/show?orderBy=monthlyCount");
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