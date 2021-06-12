package top.luqichuang.mycomic.source;

import top.luqichuang.common.model.Source;
import top.luqichuang.common.tst.BaseComicTest;
import top.luqichuang.mycomic.model.ComicInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 18:56
 * @ver 1.0
 */
public class OHTest extends BaseComicTest {

    @Override
    protected Source<ComicInfo> getSource() {
        return new OH();
    }

    @Override
    public void testRequest() {
        autoTest();
    }
}