package top.luqichuang.mycomic.source;

import top.luqichuang.common.model.Source;
import top.luqichuang.common.tst.BaseSourceTest;
import top.luqichuang.mycomic.model.ComicInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 18:56
 * @ver 1.0
 */
public class OHTest extends BaseSourceTest<ComicInfo> {

    @Override
    protected Source<ComicInfo> getSource() {
        return new OH();
    }

    @Override
    protected ComicInfo getInfo() {
        return new ComicInfo();
    }

    @Override
    public void testRequest() {
//        testSearchRequest();
//        testSearch();
//        testDetailRequest("detailUrl");
//        testDetail();
//        testContentRequest("chapterUrl");
//        testContent();
//        testRankMap();
//        testRankRequest();
//        testRank();
        autoTest();
    }
}