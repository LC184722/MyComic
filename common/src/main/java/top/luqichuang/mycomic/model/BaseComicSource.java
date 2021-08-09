package top.luqichuang.mycomic.model;

import java.util.Map;

import top.luqichuang.common.en.SourceEnum;
import top.luqichuang.common.model.Source;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/10 15:26
 * @ver 1.0
 */
public abstract class BaseComicSource implements Source<ComicInfo> {

    public abstract SourceEnum getSourceEnum();

    @Override
    public final int getSourceId() {
        return getSourceEnum().ID;
    }

    @Override
    public final String getSourceName() {
        return getSourceEnum().NAME;
    }

    @Override
    public Map<String, String> getImageHeaders() {
        return null;
    }
}
