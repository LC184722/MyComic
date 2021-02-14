package com.qc.mycomic.ui.adapter;

import android.widget.RelativeLayout;

import com.qc.mycomic.R;
import com.qc.common.util.ImgUtil;

import org.jetbrains.annotations.NotNull;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;
import top.luqichuang.mycomic.model.ImageInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class ReaderAdapter extends TheBaseQuickAdapter<ImageInfo> {

    private int count;

    public ReaderAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, ImageInfo imageInfo) {
        RelativeLayout layout = holder.findView(R.id.imageRelativeLayout);
        ImgUtil.loadImage(getContext(), imageInfo.getUrl(), layout);
    }

    public void clearMap() {
        ImgUtil.clearMap();
    }

}
