package top.luqichuang.mynovel.source;

import top.luqichuang.mynovel.model.NBaseSourceTest;
import top.luqichuang.mynovel.model.NSource;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/3/4 21:37
 * @ver 1.0
 */
public class K17Test extends NBaseSourceTest {

    @Override
    protected NSource getNSource() {
        return new K17();
    }

    @Override
    public void testRequest() {
//        testSearchRequest();
//        testSearch();
//        testDetailRequest("https://www.17k.com/list/483851.html");
//        testDetail();
//        testContentRequest("https://www.17k.com/chapter/483851/9778552.html");
//        testContentRequest("https://www.17k.com/chapter/3038645/38831717.html");
        testContent();
//        testRankMap();
//        testRankRequest(0);
//        testRank();
    }
}