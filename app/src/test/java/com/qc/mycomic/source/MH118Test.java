package com.qc.mycomic.source;

import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.test.HtmlUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/11/26 20:29
 * @ver 1.0
 */
public class MH118Test {

    private Source source = new MH118();

    @Test
    public void getComicInfoList() {
        HtmlUtil.getComicInfoListTest(source, "118/test-search.html");
    }

    @Test
    public void setComicDetail() {
        HtmlUtil.setComicDetailTest(source, "118/test-detail.html");
    }

    @Test
    public void getImageInfoList() {
        HtmlUtil.getImageInfoListTest(source, "118/test-img.html");
    }

    @Test
    public void getRankComicInfoList() {
        HtmlUtil.getRankComicInfoListTest(source, "118/test-rank.html");
    }
}