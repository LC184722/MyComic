package com.qc.common.self;

import the.one.base.Interface.ImageSnap;
import top.luqichuang.common.model.Content;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/27 22:35
 * @ver 1.0
 */
public class SnapImageInfo implements ImageSnap {

    public final Content INFO;

    public SnapImageInfo(Content content) {
        this.INFO = content;
    }

    @Override
    public String getImageUrl() {
        return INFO.getUrl();
    }

    @Override
    public String getRefer() {
        return null;
    }

    @Override
    public boolean isVideo() {
        return false;
    }
}
