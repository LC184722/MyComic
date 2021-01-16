package com.qc.mycomic.source;

import com.qc.mycomic.model.Source;
import com.qc.mycomic.model.SourceTest;

import static org.junit.Assert.*;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/13 20:39
 * @ver 1.0
 */
public class BiliBiliTest extends SourceTest {

    @Override
    protected Source getSource() {
        return new BiliBili();
    }

    @Override
    public void testRequest() {
//        testSearchRequest();
//        testDetailRequest("https://manga.bilibili.com/detail/mc25949");
        testImageRequest("https://manga.bilibili.com/mc25949/288851");
    }
}