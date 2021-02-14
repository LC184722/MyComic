package top.luqichuang.mycomic.source;

import top.luqichuang.mycomic.model.BaseSourceTest;
import top.luqichuang.mycomic.model.Source;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/13 20:39
 * @ver 1.0
 */
public class BiliBiliTest extends BaseSourceTest {

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