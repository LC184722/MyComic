package com.qc.mycomic.source;

import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.test.HtmlUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/12/8 15:28
 * @ver 1.0
 */
public class TengXunTest {

    private Source source = new TengXun();

    @Test
    public void getComicInfoList() {
        HtmlUtil.getComicInfoListTest(source, "tx/test-search.html");
    }

    @Test
    public void setComicDetail() {
        HtmlUtil.setComicDetailTest(source, "tx/test-detail.html");
    }

    @Test
    public void getImageInfoList() {
        HtmlUtil.getImageInfoListTest(source, "tx/test-img.html");
    }

    @Test
    public void getRankComicInfoList() {
        HtmlUtil.getRankComicInfoListTest(source, "tx/test-rank.html");
    }
}