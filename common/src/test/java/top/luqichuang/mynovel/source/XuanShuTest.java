package top.luqichuang.mynovel.source;

import top.luqichuang.mynovel.model.NBaseSourceTest;
import top.luqichuang.mynovel.model.NSource;

import static org.junit.Assert.*;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/2/16 19:49
 * @ver 1.0
 */
public class XuanShuTest extends NBaseSourceTest {

    @Override
    protected NSource getNSource() {
        return new XuanShu();
    }

    @Override
    public void testRequest() {
//        testSearchRequest();
//        testSearch();
//        testDetailRequest("http://www.iddwx.com/down/197001/42948.html");
//        testDetail();
//        testContentRequest("http://www.iddwx.com/read/42948_1507.html");
        testContent();
//        testRankMap();
//        testRankRequest(0);
//        testRank();
    }
}