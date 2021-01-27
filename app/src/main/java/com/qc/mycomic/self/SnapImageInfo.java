package com.qc.mycomic.self;

import the.one.base.Interface.ImageSnap;
import top.luqichuang.common.mycomic.model.ImageInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/27 22:35
 * @ver 1.0
 */
public class SnapImageInfo implements ImageSnap {

    public final ImageInfo INFO;

    public SnapImageInfo(ImageInfo imageInfo) {
        this.INFO = imageInfo;
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
