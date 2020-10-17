package com.qc.mycomic.source;

import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.StringUtil;
import com.qc.mycomic.util.test.HtmlUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/10/17 14:33
 * @ver 1.0
 */
public class ManHuaTaiTest {

    private Source source = new ManHuaTai();

    @Test
    public void getComicInfoList() {
        HtmlUtil.getComicInfoListTest(source, "mht/mht_search.html");
    }

    @Test
    public void setComicDetail() {
        HtmlUtil.setComicDetailTest(source, "mht/mht_detail.html");
    }

    @Test
    public void getImageInfoList() {
        HtmlUtil.getImageInfoListTest(source, "mht/mht_img.html");
    }

    @Test
    public void getRankComicInfoList() {
        HtmlUtil.getRankComicInfoListTest(source, "mht/mht_rank.html");
    }
}

//https://mhpic.dm300.com
//https://mhpic.jumanhua.com/comic/D/独步逍遥/第1话 被异界痴汉围攻/1.jpg-mht.middle.webp
//http://mhpic.jumanhua.com/comic/D/%E7%8B%AC%E6%AD%A5%E9%80%8D%E9%81%A5/%E7%AC%AC1%E8%AF%9D%20%E8%A2%AB%E5%BC%82%E7%95%8C%E7%97%B4%E6%B1%89%E5%9B%B4%E6%94%BB/1.jpg-mht.middle.webp