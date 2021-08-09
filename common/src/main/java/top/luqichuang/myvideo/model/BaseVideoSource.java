package top.luqichuang.myvideo.model;

import java.util.Map;

import top.luqichuang.common.en.VSourceEnum;
import top.luqichuang.common.model.Source;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/15 14:24
 * @ver 1.0
 */
public abstract class BaseVideoSource implements Source<VideoInfo> {

    public abstract VSourceEnum getVSourceEnum();

    @Override
    public final int getSourceId() {
        return getVSourceEnum().ID;
    }

    @Override
    public final String getSourceName() {
        return getVSourceEnum().NAME;
    }

    @Override
    public Map<String, String> getImageHeaders() {
        return null;
    }
}
