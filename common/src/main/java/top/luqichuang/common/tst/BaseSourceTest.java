package top.luqichuang.common.tst;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.model.EntityInfo;
import top.luqichuang.common.model.Source;
import top.luqichuang.common.self.CommonCallback;
import top.luqichuang.common.util.FileUtil;
import top.luqichuang.common.util.MapUtil;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.mycomic.model.ComicInfo;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 18:19
 * @ver 1.0
 */
public abstract class BaseSourceTest<T extends EntityInfo> {

    public static final String SEARCH = Source.SEARCH;
    public static final String DETAIL = Source.DETAIL;
    public static final String CONTENT = Source.CONTENT;
    public static final String RANK = Source.RANK;
    private Map<String, Object> dataMap;
    private final Source<T> source = getSource();
    private final T info = getInfo();

    protected abstract Source<T> getSource();

    protected abstract T getInfo();

    private String getFileName() {
        return source.getSourceName();
    }

    private String formatFileName(String tag) {
        return String.format("%s/test-%s.html", getFileName(), tag);
    }

    private String formatFileName(String name, String tag) {
        return String.format("%s/%s-%s.html", getFileName(), name, tag);
    }

    @Test
    public abstract void testRequest();

    private void testRequestDefault() {
//        testSearchRequest();
//        testSearch();
//        testDetailRequest("detailUrl");
//        testDetail();
//        testContentRequest("chapterUrl");
//        testContent();
//        testRankMap();
//        testRankRequest();
//        testRank();
//        autoTest();
    }

    @Test
    public void testSearch() {
        String html = FileUtil.readFile(formatFileName(SEARCH));
        System.out.println("html.length() = " + html.length());
        List<T> infoList = source.getInfoList(html);
        if (infoList != null) {
            System.out.println("infoList.size() = " + infoList.size());
            for (T info : infoList) {
                System.out.println("info.getTitle() = " + info.getTitle());
                System.out.println("info.getAuthor() = " + info.getAuthor());
                System.out.println("info.getUpdateTime() = " + info.getUpdateTime());
                System.out.println("info.getImgUrl() = " + info.getImgUrl());
                System.out.println("info.getDetailUrl() = " + info.getDetailUrl());
                System.out.println();
            }
        } else {
            System.out.println("comicInfoList = null");
        }
    }

    @Test
    public void testDetail() {
        String html = FileUtil.readFile(formatFileName(DETAIL));
        System.out.println("html.length() = " + html.length());
        source.setInfoDetail(info, html, dataMap);
        System.out.println("info.getTitle() = " + info.getTitle());
        System.out.println("info.getImgUrl() = " + info.getImgUrl());
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
            System.out.println("last  chapter = " + info.getChapterInfoList().get(first));
            System.out.println(".......................................");
            System.out.println("first chapter = " + info.getChapterInfoList().get(last));
        }
    }

    @Test
    public void testContent() {
        String html = FileUtil.readFile(formatFileName(CONTENT));
        System.out.println("html.length() = " + html.length());
        List<Content> contentList = source.getContentList(html, 100, dataMap);
        System.out.println("contentList.size() = " + contentList.size());
        if (info instanceof ComicInfo) {
            for (Content content : contentList) {
                System.out.println("content.getUrl() = " + content.getUrl());
            }
        } else if (info instanceof NovelInfo) {
            System.out.println("=== start ===");
            System.out.println("========|");
            for (Content content : contentList) {
                System.out.println(content.getContent());
                System.out.println("=============");
            }
            System.out.println("===  end  ===");
        }
    }

    @Test
    public void testRankMap() {
        Map<String, String> map = source.getRankMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "\t-->\t" + entry.getValue());
        }
    }

    @Test
    public void testRank() {
        String html = FileUtil.readFile(formatFileName(RANK));
        System.out.println("html.length() = " + html.length());
        List<T> comicInfoList = source.getRankInfoList(html);
        System.out.println("comicInfoList.size() = " + comicInfoList.size());
        int i = 0;
        for (T info : comicInfoList) {
            System.out.println("--------------------  " + (++i) + "  ----------------------");
            System.out.println("info.getTitle() = " + info.getTitle());
            System.out.println("info.getAuthor() = " + info.getAuthor());
            System.out.println("info.getUpdateTime() = " + info.getUpdateTime());
            System.out.println("info.getImgUrl() = " + info.getImgUrl());
            System.out.println("info.getDetailUrl() = " + info.getDetailUrl());
            System.out.println("----------------------------------------------");
            System.out.println();
        }
    }

    protected final void autoTest() {
        autoTest("我的");
    }

    protected final void autoTest(String searchString) {
        autoTest(searchString, 0);
    }

    protected final void autoTest(String searchString, int index) {
        Request searchRequest = source.getSearchRequest(searchString);
        String search = testRequest(searchRequest, SEARCH);
        FileUtil.writeFile(search, formatFileName(searchString, SEARCH));
        List<T> searchList = source.getInfoList(search);
        System.out.println("searchList.size() = " + searchList.size());
        Assert.assertFalse("未搜索到相关信息", searchList.isEmpty());

        T info = searchList.get(index);
        System.out.println("info.getTitle() = " + info.getTitle());
        String detailUrl = info.getDetailUrl();
        Request detailRequest = source.getDetailRequest(detailUrl);
        String detail = testRequest(detailRequest, DETAIL);
        FileUtil.writeFile(detail, formatFileName(info.getTitle(), DETAIL));
        source.setInfoDetail(info, detail, dataMap);
        System.out.println("chapterList.size() = " + info.getChapterInfoList().size());
        Assert.assertFalse("未搜索到章节信息", info.getChapterInfoList().isEmpty());

        ChapterInfo chapterInfo = info.getChapterInfoList().get(0);
        String chapterUrl = chapterInfo.getChapterUrl();
        Request contentRequest = source.getContentRequest(chapterUrl);
        String image = testRequest(contentRequest, CONTENT);
        FileUtil.writeFile(image, formatFileName(info.getTitle(), CONTENT));
        List<Content> contentList = source.getContentList(image, 100, dataMap);
        System.out.println("contentList.size() = " + contentList.size());
        Assert.assertFalse("未搜索到阅读页信息", contentList.isEmpty());
        if (info instanceof ComicInfo) {
            for (Content content : contentList) {
                System.out.println("content.getUrl() = " + content.getUrl());
            }
        } else if (info instanceof NovelInfo) {
            System.out.println("=== start ===");
            System.out.println("========|");
            for (Content content : contentList) {
                System.out.println(content.getContent());
                System.out.println("=============");
            }
            System.out.println("===  end  ===");
        }

    }

    protected final void testSearchRequest() {
        testSearchRequest("我的");
    }

    protected final void testSearchRequest(String searchString) {
        testRequest(source.getSearchRequest(searchString), SEARCH);
        testSearch();
    }

    protected final void testDetailRequest(String url) {
        testRequest(source.getDetailRequest(url), DETAIL);
        testDetail();
    }

    protected final void testContentRequest(String url) {
        testRequest(source.getContentRequest(url), CONTENT);
        testContent();
    }

    protected final void testRankRequest(String url) {
        testRequest(source.getRankRequest(url), RANK);
        testRank();
    }

    protected final void testRankRequest() {
        testRankRequest(0);
    }

    protected final void testRankRequest(int index) {
        Map<String, String> map = source.getRankMap();
        int size = map.size();
        if (size > 0) {
            if (index < 0) {
                index = 0;
            }
            if (index >= size) {
                index = size - 1;
            }
            String url = MapUtil.getValueByIndex(map, index);
            testRankRequest(url);
        } else {
            System.out.println("map.size() == 0");
        }
    }

    protected final String testRequest(Request request, String fileName) {
        String[] strings = new String[1];
        boolean[] flag = new boolean[1];
        NetUtil.startLoad(request, new CommonCallback(request, source, fileName) {
            @Override
            public void onFailure(String errorMsg) {
                System.err.println(errorMsg);
                flag[0] = true;
            }

            @Override
            public void onResponse(String html, Map<String, Object> map) {
                strings[0] = html;
                FileUtil.writeFile(html, formatFileName(fileName));
                dataMap = map;
                flag[0] = true;
            }
        });
        int num = 0;
        while (!flag[0]) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (++num > 20) {
                System.out.println("请求时间超过20s...");
                break;
            }
        }
        return strings[0];
    }

}
