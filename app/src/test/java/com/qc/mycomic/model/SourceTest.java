package com.qc.mycomic.model;

import com.qc.mycomic.self.SourceCallback;
import com.qc.mycomic.util.FileUtil;
import com.qc.mycomic.util.MapUtil;
import com.qc.mycomic.util.NetUtil;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/10 21:44
 * @ver 1.0
 */
public abstract class SourceTest {

    public static final String SEARCH = Source.SEARCH;
    public static final String DETAIL = Source.DETAIL;
    public static final String IMAGE = Source.IMAGE;
    public static final String RANK = Source.RANK;

    protected abstract Source getSource();

    private String getFileName() {
        return getSource().getSourceName();
    }

    private String formatFileName(String name) {
        return String.format("%s/test-%s.html", getFileName(), name);
    }

    @Test
    public abstract void testRequest();

    @Test
    public void getComicInfoList() {
        String html = FileUtil.readFile(formatFileName(SEARCH));
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
    public void setComicDetail() {
        String html = FileUtil.readFile(formatFileName(DETAIL));
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
    public void getImageInfoList() {
        String html = FileUtil.readFile(formatFileName(IMAGE));
        List<ImageInfo> imageInfoList = getSource().getImageInfoList(html, 100);
        System.out.println("imageInfoList.size() = " + imageInfoList.size());
        for (ImageInfo imageInfo : imageInfoList) {
            System.out.println("imageInfo = " + imageInfo.getUrl());
        }
    }

    @Test
    public void getRankMap() {
        Map<String, String> map = getSource().getRankMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "\t-->\t" + entry.getValue());
        }
    }

    @Test
    public void getRankComicInfoList() {
        String html = FileUtil.readFile(formatFileName(RANK));
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
        Source source = getSource();

        Request searchRequest = source.getSearchRequest(searchString);
        testRequest(searchRequest, SEARCH);
        String search = FileUtil.readFile(formatFileName(SEARCH));
        List<ComicInfo> searchList = source.getComicInfoList(search);
        System.out.println("searchList.size() = " + searchList.size());
        Assert.assertFalse("未搜索到漫画", searchList.isEmpty());

        ComicInfo comicInfo = searchList.get(0);
        System.out.println("comicInfo.getTitle() = " + comicInfo.getTitle());
        String detailUrl = comicInfo.getDetailUrl();
        Request detailRequest = source.getDetailRequest(detailUrl);
        testRequest(detailRequest, DETAIL);
        String detail = FileUtil.readFile(formatFileName(DETAIL));
        source.setComicDetail(comicInfo, detail);
        System.out.println("chapterList.size() = " + comicInfo.getChapterInfoList().size());
        Assert.assertFalse("未搜索到漫画章节", comicInfo.getChapterInfoList().isEmpty());

        ChapterInfo chapterInfo = comicInfo.getChapterInfoList().get(0);
        String imageUrl = chapterInfo.getChapterUrl();
        Request imageRequest = source.getImageRequest(imageUrl);
        testRequest(imageRequest, IMAGE);
        String image = FileUtil.readFile(formatFileName(IMAGE));
        List<ImageInfo> imageInfoList = source.getImageInfoList(image, 100);
        System.out.println("imageList.size() = " + imageInfoList.size());
        Assert.assertFalse("未搜索到漫画图片", imageInfoList.isEmpty());

        for (ImageInfo imageInfo : imageInfoList) {
            System.out.println("imageInfo = " + imageInfo.getImageUrl());
        }
    }

    protected final void testSearchRequest() {
        testSearchRequest("我的");
    }

    protected final void testSearchRequest(String searchString) {
        testRequest(getSource().getSearchRequest(searchString), SEARCH);
        getComicInfoList();
    }

    protected final void testDetailRequest(String url) {
        testRequest(getSource().getDetailRequest(url), DETAIL);
        setComicDetail();
    }

    protected final void testImageRequest(String url) {
        testRequest(getSource().getImageRequest(url), IMAGE);
        getImageInfoList();
    }

    protected final void testRankRequest(String url) {
        testRequest(getSource().getRankRequest(url), RANK);
        getRankComicInfoList();
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

    protected final void testRequest(Request request, String fileName) {
        boolean[] flag = new boolean[1];
        NetUtil.startLoad(request, new SourceCallback(request, getSource(), fileName) {
            @Override
            public void onFailure(String errorMsg) {
                System.err.println(errorMsg);
                flag[0] = true;
            }

            @Override
            public void onResponse(String html) {
                FileUtil.writeFile(html, formatFileName(fileName));
                flag[0] = true;
            }
        });
        int num = 0;
        while (!flag[0] && num++ < 10) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}