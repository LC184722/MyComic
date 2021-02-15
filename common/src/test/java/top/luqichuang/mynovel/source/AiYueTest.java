package top.luqichuang.mynovel.source;

import top.luqichuang.mynovel.model.NBaseSourceTest;
import top.luqichuang.mynovel.model.NSource;

import static org.junit.Assert.*;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/2/15 19:35
 * @ver 1.0
 */
public class AiYueTest extends NBaseSourceTest {

    @Override
    protected NSource getNSource() {
        return new AiYue();
    }

    @Override
    public void testRequest() {
//        testSearchRequest();
//        testSearch();
//        testDetailRequest("http://www.hybooks.cn/447699wdjszcwhq/");
//        testDetail();
//        testContentRequest("http://www.hybooks.cn/447699wdjszcwhq/345412928.html");
        testContent();
//        testRankMap();
//        testRankRequest(0);
//        testRank();
    }
}