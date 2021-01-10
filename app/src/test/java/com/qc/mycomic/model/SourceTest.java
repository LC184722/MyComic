package com.qc.mycomic.model;

import com.qc.mycomic.util.test.HtmlUtil;

import org.junit.Test;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/10 21:44
 * @ver 1.0
 */
public abstract class SourceTest {

    protected abstract Source getSource();

    private String getFileName() {
        return getSource().getSourceName();
    }

    private String formatFileName(String name) {
        return String.format("%s/test-%s.html", getFileName(), name);
    }

    @Test
    public void getComicInfoList() {
        HtmlUtil.getComicInfoListTest(getSource(), formatFileName("search"));
    }

    @Test
    public void setComicDetail() {
        HtmlUtil.setComicDetailTest(getSource(), formatFileName("detail"));
    }

    @Test
    public void getImageInfoList() {
        HtmlUtil.getImageInfoListTest(getSource(), formatFileName("image"));
    }

    @Test
    public void getRankComicInfoList() {
        HtmlUtil.getRankComicInfoListTest(getSource(), formatFileName("rank"));
    }
}