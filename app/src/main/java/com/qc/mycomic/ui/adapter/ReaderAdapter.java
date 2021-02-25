package com.qc.mycomic.ui.adapter;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qc.common.self.ImageConfig;
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

    public ReaderAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, ImageInfo imageInfo) {
        RelativeLayout layout = holder.findView(R.id.imageRelativeLayout);
        ImageConfig config = ImgUtil.getDefaultConfig(getContext(), imageInfo.getUrl(), layout);
        config.setErrorBitmap(ImgUtil.drawableToBitmap(getDrawable(R.drawable.ic_image_error_24)));
        config.setScaleType(ImageView.ScaleType.CENTER);
        ImgUtil.loadImage(getContext(), config);
    }

}
