package top.luqichuang.mynovel.source;

import top.luqichuang.mynovel.model.NBaseSourceTest;
import top.luqichuang.mynovel.model.NSource;

import static org.junit.Assert.*;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/2/11 14:17
 * @ver 1.0
 */
public class XinBiQuGeTest extends NBaseSourceTest {

    @Override
    protected NSource getNSource() {
        return new XinBiQuGe();
    }

    @Override
    public void testRequest() {
//        testSearchRequest("洪荒之逍遥小剑仙");
//        autoTest("洪荒之逍遥小剑仙");
//        testDetailRequest("https://www.vbiquge.com/96_96267/");
//        testContentRequest("https://www.vbiquge.com/96_96267/59146.html");
//        testRankMap();
//        testRankRequest(0);
        testContent();
//        autoTest("元尊");
//        autoTest("苟到天下无敌");
    }
}