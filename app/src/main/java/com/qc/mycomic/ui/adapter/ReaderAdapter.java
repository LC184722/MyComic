package com.qc.mycomic.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qc.mycomic.R;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.other.MyHeaders;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.ImgUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;
import the.one.base.util.glide.GlideUtil;

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
        ImageView imageView = holder.findView(R.id.imageView);
        if (imageView != null) {
            ImgUtil.loadReaderImg(getContext(), imageInfo, imageView);
        }
        //Log.i(TAG, "convert: position = " + holder.getAdapterPosition() + ", status = " + imageInfo.getStatus() + ", count = " + (++count));
    }

    public void clearMap() {
        ImgUtil.clearMap();
    }

}
