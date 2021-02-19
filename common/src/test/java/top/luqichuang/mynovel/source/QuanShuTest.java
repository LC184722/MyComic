package top.luqichuang.mynovel.source;

import top.luqichuang.mynovel.model.NBaseSourceTest;
import top.luqichuang.mynovel.model.NSource;

import static org.junit.Assert.*;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/2/12 20:04
 * @ver 1.0
 */
public class QuanShuTest extends NBaseSourceTest {

    @Override
    protected NSource getNSource() {
        return new QuanShu();
    }

    @Override
    public void testRequest() {
//        testSearch();
//        testDetailRequest("http://www.quanshuwang.com/book_162527.html");
        testDetail();
//        testContentRequest("http://www.quanshuwang.com/book/162/162527/51465244.html");
//        testContent();
//        testRankMap();
//        testRankRequest(1);
//        testRank();
    }
}