package com.qc.mycomic.adapter;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qc.mycomic.R;
import com.qc.mycomic.model.Comic;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;
import the.one.base.util.glide.GlideEngine;
import the.one.base.util.glide.GlideUtil;
import the.one.base.widge.ScaleImageView;

public class SearchAdapter extends TheBaseQuickAdapter<Comic> {

    public SearchAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, Comic comic) {
        holder.setText(R.id.tvTitle, comic.getComicInfo().getTitle());
        holder.setText(R.id.tvSource, "漫画源数量：" + comic.getSourceSize());
        holder.setText(R.id.tvAuthor, comic.getComicInfo().getAuthor());
        holder.setText(R.id.tvUpdateTime, comic.getComicInfo().getUpdateTime());
        QMUIRadiusImageView qivImg = holder.findView(R.id.qivImg);
        GlideEngine.createGlideEngine().loadImage(getContext(), comic.getComicInfo().getImgUrl(), qivImg);
    }

}
