package com.qc.mycomic.ui.adapter;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.qc.mycomic.R;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.util.ImgUtil;
import com.qmuiteam.qmui.widget.QMUIProgressBar;

import org.jetbrains.annotations.NotNull;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;

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
        //Log.i(TAG, "convert: " + getData());
//        ImageView imageView = holder.findView(R.id.imageView);
//        QMUIProgressBar progressBar = holder.findView(R.id.progressBar);
//        ImgUtil.loadImage(getContext(), imageInfo.getUrl(), imageView, progressBar);
        //Log.i(TAG, "convert: position = " + holder.getAdapterPosition() + ", status = " + imageInfo.getStatus() + ", count = " + (++count));
        RelativeLayout layout = holder.findView(R.id.imageRelativeLayout);
        ImgUtil.loadImage(getContext(), imageInfo.getUrl(), layout);
    }

    public void clearMap() {
        ImgUtil.clearMap();
    }

}
