package top.luqichuang.mynovel.source;

import top.luqichuang.mynovel.model.NBaseSourceTest;
import top.luqichuang.mynovel.model.NSource;

import static org.junit.Assert.*;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/2/14 20:07
 * @ver 1.0
 */
public class QuanXiaoShuoTest extends NBaseSourceTest {

    @Override
    protected NSource getNSource() {
        return new QuanXiaoShuo();
    }

    @Override
    public void testRequest() {
//        testSearchRequest();
//        testSearch();
//        testDetailRequest("https://qxs.la/29868/");
//        testDetail();
//        testContentRequest("https://qxs.la/29868/2759888/");
//        testContent();
//        testRankMap();
//        testRankRequest(1);
//        testRank();
    }
}