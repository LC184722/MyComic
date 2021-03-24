package top.luqichuang.mycomic.source;

import top.luqichuang.mycomic.model.BaseSourceTest;
import top.luqichuang.mycomic.model.Source;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/3/23 23:21
 * @ver 1.0
 */
public class BLTest extends BaseSourceTest {

    @Override
    protected Source getSource() {
        return new BL();
    }

    @Override
    public void testRequest() {
//        testSearchRequest("大管家");
//        testSearch();
//        testDetailRequest("https://www.kanbl.com/manhua/4248.html");
//        testDetail();
//        testImageRequest("https://www.kanbl.com/chapter/425135.html");
//        testImage();
//        testRankMap();
//        testRankRequest(0);
//        testRank();
    }

    @Override
    public void testImage() {
        super.testImage();
    }
}