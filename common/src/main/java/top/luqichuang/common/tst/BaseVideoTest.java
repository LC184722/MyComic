package top.luqichuang.common.tst;

import top.luqichuang.myvideo.model.VideoInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/15 14:40
 * @ver 1.0
 */
public abstract class BaseVideoTest extends BaseSourceTest<VideoInfo> {

    @Override
    protected VideoInfo getInfo() {
        return new VideoInfo();
    }
}
