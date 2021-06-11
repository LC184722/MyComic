package top.luqichuang.mynovel.model;

import top.luqichuang.common.en.NSourceEnum;
import top.luqichuang.common.model.Source;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/10 16:53
 * @ver 1.0
 */
public abstract class BaseNovelSource implements Source<NovelInfo> {

    public abstract NSourceEnum getNSourceEnum();

    @Override
    public int getSourceId() {
        return getNSourceEnum().ID;
    }

    @Override
    public String getSourceName() {
        return getNSourceEnum().NAME;
    }

}
