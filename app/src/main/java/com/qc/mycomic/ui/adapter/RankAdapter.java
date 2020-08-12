package com.qc.mycomic.ui.adapter;

import com.qc.mycomic.R;
import com.qc.mycomic.model.Comic;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import org.jetbrains.annotations.NotNull;

import the.one.base.adapter.TheBaseQuickAdapter;
import the.one.base.adapter.TheBaseViewHolder;
import the.one.base.util.glide.GlideEngine;

public class RankAdapter extends TheBaseQuickAdapter<Comic> {

    public RankAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull TheBaseViewHolder holder, Comic comic) {
        holder.setText(R.id.tvTitle, comic.getComicInfo().getTitle());
        holder.setText(R.id.tvSource, "");
        holder.setText(R.id.tvAuthor, comic.getComicInfo().getAuthor());
        holder.setText(R.id.tvUpdateTime, comic.getComicInfo().getUpdateTime());
        holder.setText(R.id.tvIndex, String.valueOf(holder.getAdapterPosition() + 1));
        QMUIRadiusImageView qivImg = holder.findView(R.id.qivImg);
        GlideEngine.createGlideEngine().loadImage(getContext(), comic.getComicInfo().getImgUrl(), qivImg);
    }

}
