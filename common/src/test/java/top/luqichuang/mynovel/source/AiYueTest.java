package top.luqichuang.mynovel.source;

import top.luqichuang.common.model.Source;
import top.luqichuang.common.tst.BaseSourceTest;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 18:58
 * @ver 1.0
 */
public class AiYueTest extends BaseSourceTest<NovelInfo> {

    @Override
    protected Source<NovelInfo> getSource() {
        return new AiYue();
    }

    @Override
    protected NovelInfo getInfo() {
        return new NovelInfo();
    }

    @Override
    public void testRequest() {
//        testSearchRequest();
//        testSearch();
//        testDetailRequest("http://www.hybooks.cn/447742wdwxlsj/");
//        testDetail();
//        testContentRequest("http://www.hybooks.cn/447742wdwxlsj/345433443.html");
//        testContent();
//        testRankMap();
//        testRankRequest();
//        testRank();
//        autoTest();
    }
}