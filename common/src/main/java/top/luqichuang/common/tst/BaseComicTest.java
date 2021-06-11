package top.luqichuang.common.tst;

import top.luqichuang.mycomic.model.ComicInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 23:47
 * @ver 1.0
 */
public abstract class BaseComicTest extends BaseSourceTest<ComicInfo> {

    @Override
    protected ComicInfo getInfo() {
        return new ComicInfo();
    }
}
