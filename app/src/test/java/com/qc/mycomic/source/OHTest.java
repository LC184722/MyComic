package com.qc.mycomic.source;

import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.test.HtmlUtil;

import org.junit.Test;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/10/6 20:13
 * @ver 1.0
 */
public class OHTest {

    private Source source = new OH();

    @Test
    public void getComicInfoList() {
    }

    @Test
    public void setComicDetail() {
    }

    @Test
    public void getImageInfoList() {
        HtmlUtil.getImageInfoListTest(source, "oh/oh-test.html");
    }

    @Test
    public void getRankComicInfoList() {
    }
}