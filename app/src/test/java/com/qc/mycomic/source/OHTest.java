package com.qc.mycomic.source;

import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.test.HtmlUtil;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

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
        String html = HtmlUtil.getHtmlByFile("oh-test.html");
        List<ImageInfo> imageInfoList = source.getImageInfoList(html, 100);
        System.out.println("imageInfoList.size() = " + imageInfoList.size());
        for (ImageInfo imageInfo : imageInfoList) {
            System.out.println("imageInfo = " + imageInfo.getUrl());
        }
    }

    @Test
    public void getRankComicInfoList() {
    }
}