package com.qc.mycomic.source;

import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.test.HtmlUtil;

import org.junit.Test;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/10/11 20:21
 * @ver 1.0
 */
public class ManHuaFenTest {

    private Source source = new ManHuaFen();

    @Test
    public void getComicInfoList() {
    }

    @Test
    public void setComicDetail() {
    }

    @Test
    public void getImageInfoList() {
        HtmlUtil.getImageInfoListTest(source, "mhf/mhf-test.html");
    }
}