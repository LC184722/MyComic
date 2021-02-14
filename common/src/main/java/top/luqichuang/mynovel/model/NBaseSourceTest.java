package top.luqichuang.mynovel.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.self.SourceCallback;
import top.luqichuang.common.util.FileUtil;
import top.luqichuang.common.util.MapUtil;
import top.luqichuang.common.util.NetUtil;

/**
 * @author LuQiChuang
 * @desc 小说源测试类，继承本类完成对某小说源测试
 * @date 2021/1/10 21:44
 * @ver 1.0
 */
public abstract class NBaseSourceTest {

    public static final String SEARCH = NSource.SEARCH;
    public static final String DETAIL = NSource.DETAIL;
    public static final String CONTENT = NSource.CONTENT;
    public static final String RANK = NSource.RANK;

    protected abstract NSource getNSource();

    private String getFileName() {
        return getNSource().getNSourceName();
    }

    private String formatFileName(String tag) {
        return String.format("%s/test-%s.html", getFileName(), tag);
    }

    private String formatFileName(String name, String tag) {
        return String.format("%s/%s-%s.html", getFileName(), name, tag);
    }

    @Test
    public abstract void testRequest();

    @Test
    public void testSearch() {
        String html = FileUtil.readFile(formatFileName(SEARCH));
        System.out.println("html.length() = " + html.length());
        List<NovelInfo> novelInfoList;
        novelInfoList = getNSource().getNovelInfoList(html);
        if (novelInfoList != null) {
            System.out.println("novelInfoList.size() = " + novelInfoList.size());
            for (NovelInfo info : novelInfoList) {
                System.out.println("info.getTitle() = " + info.getTitle());
                System.out.println("info.getAuthor() = " + info.getAuthor());
                System.out.println("info.getUpdateTime() = " + info.getUpdateTime());
                System.out.println("info.getImgUrl() = " + info.getImgUrl());
                System.out.println("info.getDetailUrl() = " + info.getDetailUrl());
                System.out.println();
            }
        } else {
            System.out.println("novelInfoList = null");
        }
    }

    @Test
    public void testDetail() {
        String html = FileUtil.readFile(formatFileName(DETAIL));
        NovelInfo info = new NovelInfo();
        getNSource().setNovelDetail(info, html);
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
        ContentInfo contentInfo = getNSource().getContentInfo(html, 100);
        System.out.println("======================");
        System.out.println(contentInfo.getContent());
    }

    @Test
    public void testRankMap() {
        Map<String, String> map = getNSource().getRankMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "\t-->\t" + entry.getValue());
        }
    }

    @Test
    public void testRank() {
        String html = FileUtil.readFile(formatFileName(RANK));
        System.out.println("html.length() = " + html.length());
        List<NovelInfo> novelInfoList = getNSource().getRankNovelInfoList(html);
        System.out.println("novelInfoList.size() = " + novelInfoList.size());
        int i = 0;
        for (NovelInfo info : novelInfoList) {
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
        NSource source = getNSource();

        Request searchRequest = source.getSearchRequest(searchString);
        String search = testRequest(searchRequest, SEARCH);
        List<NovelInfo> searchList = source.getNovelInfoList(search);
        System.out.println("searchList.size() = " + searchList.size());
        Assert.assertFalse("未搜索到小说", searchList.isEmpty());

        NovelInfo novelInfo = searchList.get(index);
        System.out.println("novelInfo.getTitle() = " + novelInfo.getTitle());
        String detailUrl = novelInfo.getDetailUrl();
        Request detailRequest = source.getDetailRequest(detailUrl);
        String detail = testRequest(detailRequest, DETAIL);
        source.setNovelDetail(novelInfo, detail);
        System.out.println("chapterList.size() = " + novelInfo.getChapterInfoList().size());
        Assert.assertFalse("未搜索到小说章节", novelInfo.getChapterInfoList().isEmpty());

        ChapterInfo chapterInfo = novelInfo.getChapterInfoList().get(0);
        String imageUrl = chapterInfo.getChapterUrl();
        Request imageRequest = source.getContentRequest(imageUrl);
        String image = testRequest(imageRequest, CONTENT);
        ContentInfo contentInfo = source.getContentInfo(image, 100);
        Assert.assertNotNull("未搜索到小说内容", contentInfo);
        System.out.println("contentInfo = " + contentInfo.getContent());
    }

    protected final void testSearchRequest() {
        testSearchRequest("我的");
    }

    protected final void testSearchRequest(String searchString) {
        testRequest(getNSource().getSearchRequest(searchString), SEARCH);
        testSearch();
    }

    protected final void testDetailRequest(String url) {
        testRequest(getNSource().getDetailRequest(url), DETAIL);
        testDetail();
    }

    protected final void testContentRequest(String url) {
        testRequest(getNSource().getContentRequest(url), CONTENT);
        testContent();
    }

    protected final void testRankRequest(String url) {
        testRequest(getNSource().getRankRequest(url), RANK);
        testRank();
    }

    protected final void testRankRequest(int index) {
        Map<String, String> map = getNSource().getRankMap();
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
        String[] strings = {""};
        boolean[] flag = new boolean[1];
        NetUtil.startLoad(request, new SourceCallback(request, getNSource(), fileName) {
            @Override
            public void onFailure(String errorMsg) {
                System.err.println(errorMsg);
                flag[0] = true;
            }

            @Override
            public void onResponse(String html) {
                strings[0] = html;
                FileUtil.writeFile(html, formatFileName(fileName));
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