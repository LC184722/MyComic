package top.luqichuang.common.tst;

import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 23:48
 * @ver 1.0
 */
public abstract class BaseNovelTest extends BaseSourceTest<NovelInfo> {

    @Override
    protected NovelInfo getInfo() {
        return new NovelInfo();
    }
}
