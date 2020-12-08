package com.qc.mycomic.source;

import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.test.HtmlUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/12/8 15:24
 * @ver 1.0
 */
public class PuFeiTest {

    Source source = new PuFei();

    @Test
    public void getComicInfoList() {
    }

    @Test
    public void setComicDetail() {
        HtmlUtil.setComicDetailTest(source, "pf/test-detail.html");
    }

    @Test
    public void getImageInfoList() {
    }

    @Test
    public void getRankComicInfoList() {
    }
}