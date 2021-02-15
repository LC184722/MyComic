package top.luqichuang.mycomic.model;

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
 * @desc 漫画源测试类，继承本类完成对某漫画源测试
 * @date 2021/1/10 21:44
 * @ver 1.0
 */
public abstract class BaseSourceTest {

    public static final String SEARCH = Source.SEARCH;
    public static final String DETAIL = Source.DETAIL;
    public static final String IMAGE = Source.IMAGE;
    public static final String RANK = Source.RANK;

    protected abstract Source getSource();

    private String getFileName() {
        return getSource().getSourceName();
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
        List<ComicInfo> comicInfoList;
        comicInfoList = getSource().getComicInfoList(html);
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
            System.out.println("comicInfoList = null");
        }
    }

    @Test
    public void testDetail() {
        String html = FileUtil.readFile(formatFileName(DETAIL));
        System.out.println("html.length() = " + html.length());
        ComicInfo info = new ComicInfo();
        getSource().setComicDetail(info, html);
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
    public void testImage() {
        String html = FileUtil.readFile(formatFileName(IMAGE));
        System.out.println("html.length() = " + html.length());
        List<ImageInfo> imageInfoList = getSource().getImageInfoList(html, 100);
        System.out.println("imageInfoList.size() = " + imageInfoList.size());
        for (ImageInfo imageInfo : imageInfoList) {
            System.out.println("imageInfo = " + imageInfo.getUrl());
        }
    }

    @Test
    public void testRankMap() {
        Map<String, String> map = getSource().getRankMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "\t-->\t" + entry.getValue());
        }
    }

    @Test
    public void testRank() {
        String html = FileUtil.readFile(formatFileName(RANK));
        System.out.println("html.length() = " + html.length());
        List<ComicInfo> comicInfoList = getSource().getRankComicInfoList(html);
        System.out.println("comicInfoList.size() = " + comicInfoList.size());
        int i = 0;
        for (ComicInfo info : comicInfoList) {
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
        Source source = getSource();

        Request searchRequest = source.getSearchRequest(searchString);
        String search = testRequest(searchRequest, SEARCH);
        FileUtil.writeFile(search, formatFileName(searchString, SEARCH));
        List<ComicInfo> searchList = source.getComicInfoList(search);
        System.out.println("searchList.size() = " + searchList.size());
        Assert.assertFalse("未搜索到漫画", searchList.isEmpty());

        ComicInfo comicInfo = searchList.get(index);
        System.out.println("comicInfo.getTitle() = " + comicInfo.getTitle());
        String detailUrl = comicInfo.getDetailUrl();
        Request detailRequest = source.getDetailRequest(detailUrl);
        String detail = testRequest(detailRequest, DETAIL);
        FileUtil.writeFile(detail, formatFileName(comicInfo.getTitle(), DETAIL));
        source.setComicDetail(comicInfo, detail);
        System.out.println("chapterList.size() = " + comicInfo.getChapterInfoList().size());
        Assert.assertFalse("未搜索到漫画章节", comicInfo.getChapterInfoList().isEmpty());

        ChapterInfo chapterInfo = comicInfo.getChapterInfoList().get(0);
        String imageUrl = chapterInfo.getChapterUrl();
        Request imageRequest = source.getImageRequest(imageUrl);
        String image = testRequest(imageRequest, IMAGE);
        FileUtil.writeFile(image, formatFileName(comicInfo.getTitle(), IMAGE));
        List<ImageInfo> imageInfoList = source.getImageInfoList(image, 100);
        System.out.println("imageList.size() = " + imageInfoList.size());
        Assert.assertFalse("未搜索到漫画图片", imageInfoList.isEmpty());

        for (ImageInfo imageInfo : imageInfoList) {
            System.out.println("imageInfo = " + imageInfo.getUrl());
        }
    }

    protected final void testSearchRequest() {
        testSearchRequest("我的");
    }

    protected final void testSearchRequest(String searchString) {
        testRequest(getSource().getSearchRequest(searchString), SEARCH);
        testSearch();
    }

    protected final void testDetailRequest(String url) {
        testRequest(getSource().getDetailRequest(url), DETAIL);
        testDetail();
    }

    protected final void testImageRequest(String url) {
        testRequest(getSource().getImageRequest(url), IMAGE);
        testImage();
    }

    protected final void testRankRequest(String url) {
        testRequest(getSource().getRankRequest(url), RANK);
        testRank();
    }

    protected final void testRankRequest(int index) {
        Map<String, String> map = getSource().getRankMap();
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
        NetUtil.startLoad(request, new SourceCallback(request, getSource(), fileName) {
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